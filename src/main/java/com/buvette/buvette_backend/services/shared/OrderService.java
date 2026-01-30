package com.buvette.buvette_backend.services.shared;

import com.buvette.buvette_backend.config.OrderCodeGenerator;
import com.buvette.buvette_backend.dto.OrderRequest;
import com.buvette.buvette_backend.dto.OrderRequest.Item;
import com.buvette.buvette_backend.enumAttribute.Status;
import com.buvette.buvette_backend.model.client.CartItem;
import com.buvette.buvette_backend.model.client.MenuItem;
import com.buvette.buvette_backend.model.client.Notification;
import com.buvette.buvette_backend.model.client.User;
import com.buvette.buvette_backend.enumAttribute.Types;
import com.buvette.buvette_backend.model.shared.Order;
import com.buvette.buvette_backend.repository.client.MenuItemRepository;
import com.buvette.buvette_backend.repository.shared.OrderRepository;
import com.buvette.buvette_backend.repository.shared.UserRepository;
import com.buvette.buvette_backend.services.client.NotificationService;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;


    public OrderService(OrderRepository orderRepository,
                        MenuItemRepository menuItemRepository,
                        UserRepository userRepository,
                        NotificationService notificationService,
                        SimpMessagingTemplate messagingTemplate) {
        this.orderRepository = orderRepository;
        this.menuItemRepository = menuItemRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        this.messagingTemplate = messagingTemplate;
    }

    @Transactional
    public Order createOrder(User user, OrderRequest orderRequest) {

        // 1️⃣ Convert request items → CartItems
        List<CartItem> cartItems = orderRequest.getItems().stream()
                .map(i -> new CartItem(i.getItemId(), i.getQuantity()))
                .collect(Collectors.toList());

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cannot create order with empty cart");
        }

        // 2️⃣ Calculate total (server-side authority)
        double total = cartItems.stream()
                .mapToDouble(ci -> {
                    MenuItem menuItem = menuItemRepository.findById(ci.getItemId())
                            .orElseThrow(() ->
                                    new RuntimeException("Item not found: " + ci.getItemId()));
                    return menuItem.getPrice() * ci.getQuantity();
                })
                .sum();

  
        // 3️⃣ Create order
        Order order = new Order();
        order.setUserId(user.getId());
        order.setUsername(user.getUsername());
        order.setItems(cartItems);
        order.setTotal(total);
        order.setStatus(Status.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        order.setOrderCode(OrderCodeGenerator.generate());

        Order savedOrder = orderRepository.save(order);

        // 4️⃣ Clear user cart AFTER order success
        user.getCart().clear();
        userRepository.save(user);

        Notification notf = new Notification();
        notf.setId(UUID.randomUUID().toString());
        notf.setUserId(user.getId());
        notf.setCreatedAt(LocalDateTime.now());
        notf.setMessage("Your order is created ,Waiting to be confirmed for preparing (Order #"+order.getOrderCode()+")");
        notf.setOrderId(order.getId());
        notf.setType(Types.ORDER_PENDING);

        notificationService.createNotification(user.getId(), notf );

        // Notifier les workers (TEMPS RÉEL)
        messagingTemplate.convertAndSend(
                "/topic/notifications/"+notf.getUserId(),
                notf);

        return savedOrder;
    }

    // Worker/Admin
    public List<Order> getOrders() {
        List<Order> orders = orderRepository.findAll();

        // Enrich item names (acceptable for now)
        for (Order order : orders) {
            for (CartItem cartItem : order.getItems()) {
                menuItemRepository.findById(cartItem.getItemId())
                        .ifPresent(item -> cartItem.setItemName(item.getName()));
            }
        }
        return orders;
    }

    public Order updateStatus(String orderId, Status newStatus) {

        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getStatus().canTransitionTo(newStatus)) {
            throw new IllegalStateException(
                "Invalid status transition: " +
                order.getStatus() + " → " + newStatus
            );
        }

        order.setStatus(newStatus);

        switch (newStatus) {
            case PREPARING -> order.setStartedAt(LocalDateTime.now());
            case READY -> order.setReadyTime(LocalDateTime.now());
            case DELIVERED -> order.setDeliveredAt(LocalDateTime.now());
            case CANCELLED -> order.setCancelledAt(LocalDateTime.now());
            default -> throw new IllegalArgumentException("Unexpected value: " + newStatus);
        }

        Order savedOrder = orderRepository.save(order);

        // 🔔 Notification
        Types type = Types.fromStatus(newStatus);

        Notification notification = new Notification();
        notification.setId(UUID.randomUUID().toString());
        notification.setUserId(savedOrder.getUserId());
        notification.setOrderId(savedOrder.getId());
        notification.setType(type);
        notification.setTitle(type.getTitle());
        notification.setMessage(type.buildMessage(savedOrder.getOrderCode()));

        notificationService.createNotification(savedOrder.getUserId(), notification);

        // Notifier les workers (TEMPS RÉEL)
        messagingTemplate.convertAndSend(
                "/topic/notifications/"+notification.getUserId(),
                notification);

        return savedOrder;
    }

    public Order getOrderById(String id){
        Order order =orderRepository.findById(id).orElse(null);
         for (CartItem item : order.getItems()){
            MenuItem product = menuItemRepository.findById(item.getItemId()).orElse(null);
                item.setItemName(product.getName());
                item.setItemPrice(product.getPrice());
            }
          {
            return order;

          }
        
    }

    // list of orders 
    public List<Order>getOrdersByUser(String id){
        return orderRepository.findByUserId(id);
    }

    // Update an order (items/quantities)
    public Order updateOrder(String orderId, Order updatedOrder) throws Exception {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new Exception("Order not found"));

        if (existingOrder.getStatus() != Status.PENDING) {
            throw new Exception("Only pending orders can be updated");
        }

        // Update items
        existingOrder.setItems(updatedOrder.getItems());

        // Recalculate total
        double total = existingOrder.getItems().stream()
                .mapToDouble(i -> i.getQuantity() * i.getItemPrice())
                .sum();
        existingOrder.setTotal(total);

        // Auto-cancel if no items left
        if (existingOrder.getItems().isEmpty()) {
            existingOrder.setStatus(Status.CANCELLED);
            existingOrder.setCancelledAt(LocalDateTime.now());
        }

        return orderRepository.save(existingOrder);
    }

    // Cancel an order
    public Order cancelOrder(String orderId) throws Exception {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new Exception("Order not found"));

        if (existingOrder.getStatus() != Status.PENDING) {
            throw new Exception("Only pending orders can be cancelled");
        }

        existingOrder.setStatus(Status.CANCELLED);
        existingOrder.setCancelledAt(LocalDateTime.now());
        return orderRepository.save(existingOrder);
    }

}

