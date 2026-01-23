package com.buvette.buvette_backend.services.shared;

import com.buvette.buvette_backend.dto.OrderRequest;
import com.buvette.buvette_backend.enumAttribute.Status;
import com.buvette.buvette_backend.model.client.CartItem;
import com.buvette.buvette_backend.model.client.MenuItem;
import com.buvette.buvette_backend.model.client.User;
import com.buvette.buvette_backend.model.shared.Order;
import com.buvette.buvette_backend.repository.client.MenuItemRepository;
import com.buvette.buvette_backend.repository.shared.OrderRepository;
import com.buvette.buvette_backend.repository.shared.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository,
                        MenuItemRepository menuItemRepository,
                        UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.menuItemRepository = menuItemRepository;
        this.userRepository = userRepository;
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

        Order savedOrder = orderRepository.save(order);

        // 4️⃣ Clear user cart AFTER order success
        user.getCart().clear();
        userRepository.save(user);

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

        order.setStatus(newStatus);
        return orderRepository.save(order);
    }
}

