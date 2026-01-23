package com.buvette.buvette_backend.services.shared;

import com.buvette.buvette_backend.dto.OrderRequest;
import com.buvette.buvette_backend.enumAttribute.Status;
import com.buvette.buvette_backend.model.client.CartItem;
import com.buvette.buvette_backend.model.client.MenuItem;
import com.buvette.buvette_backend.model.shared.Order;
import com.buvette.buvette_backend.repository.client.MenuItemRepository;
import com.buvette.buvette_backend.repository.shared.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;

    public OrderService(OrderRepository orderRepository, MenuItemRepository menuItemRepository) {
        this.orderRepository = orderRepository;
        this.menuItemRepository = menuItemRepository;
    }

    public Order createOrder(String userId, String username, OrderRequest orderRequest) {
        // 1️⃣ Convert OrderRequest items to CartItems
        List<CartItem> cartItems = orderRequest.getItems().stream()
                .map(i -> new CartItem(i.getItemId(), i.getQuantity()))
                .collect(Collectors.toList());

        // 2️⃣ Calculate total using MenuItem repository
        double total = cartItems.stream()
                .mapToDouble(ci -> {
                    MenuItem menuItem = menuItemRepository.findById(ci.getItemId())
                            .orElseThrow(() -> new RuntimeException("Item not found: " + ci.getItemId()));
                    return menuItem.getPrice() * ci.getQuantity();
                })
                .sum();

        // 3️⃣ Create and save order
        Order order = new Order();
        order.setUserId(userId);
        order.setUsername(username);
        order.setItems(cartItems);
        order.setTotal(total);

        return orderRepository.save(order);
    }

    public List<Order> getOrders() {

        List<Order> orders = orderRepository.findAll();

        for (Order order : orders) {
            for (CartItem cartItem : order.getItems()) {

                MenuItem item = menuItemRepository.findById(cartItem.getItemId())
                        .orElse(null);

                if (item != null) {
                    cartItem.setItemName(item.getName());
                }
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
