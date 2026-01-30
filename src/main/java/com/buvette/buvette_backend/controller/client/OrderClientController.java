package com.buvette.buvette_backend.controller.client;

import com.buvette.buvette_backend.dto.OrderRequest;
import com.buvette.buvette_backend.model.client.CartItem;
import com.buvette.buvette_backend.model.client.MenuItem;
import com.buvette.buvette_backend.model.client.User;
import com.buvette.buvette_backend.model.shared.Order;
import com.buvette.buvette_backend.repository.client.MenuItemRepository;
import com.buvette.buvette_backend.repository.shared.UserRepository;
import com.buvette.buvette_backend.services.shared.OrderService;
import com.buvette.buvette_backend.services.client.MenuItemService;
import com.buvette.buvette_backend.services.shared.JwtService;

import java.security.Principal;
import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client/orders")
@PreAuthorize("hasRole('CLIENT')")
@CrossOrigin(origins = "http://localhost:5173")
public class OrderClientController {

    private final SimpMessagingTemplate messagingTemplate;
    private final OrderService orderService;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final MenuItemService menuItemService;

    public OrderClientController(OrderService orderService,
            UserRepository userRepository,
            JwtService jwtService,
            SimpMessagingTemplate messagingTemplate, MenuItemService menuItemService) {
        this.orderService = orderService;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.messagingTemplate = messagingTemplate;
        this.menuItemService = menuItemService;
    }

    @PostMapping
    public Order createOrder(@RequestBody OrderRequest orderRequest,
            @RequestHeader("Authorization") String authHeader, Principal principal) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing token");
        }

        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = orderService.createOrder(user, orderRequest);
        for (CartItem item : order.getItems()) {
            MenuItem i = menuItemService.getItemById(item.getItemId());
            item.setItemName(i.getName());
        }

        // Notifier les workers (TEMPS RÉEL)
        messagingTemplate.convertAndSend(
                "/topic/orders",
                order);

        return order;
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable String id) {
        Order order = orderService.getOrderById(id);
        return order;

    }

    @GetMapping
    public List<Order> getOrders(@RequestHeader("Authorization") String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing token");
        }

        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return orderService.getOrdersByUser(user.getId());
    }

    // Update order (change quantities, remove items)
    @PutMapping("/{orderId}")
    public Order updateOrder(@PathVariable String orderId, @RequestBody Order order,
            Principal principal) throws Exception {
        Order o = orderService.updateOrder(orderId, order);
        for (CartItem item : o.getItems()) {
            MenuItem i = menuItemService.getItemById(item.getItemId());
            item.setItemName(i.getName());
        }
        // Notifier les workers (TEMPS RÉEL)
        messagingTemplate.convertAndSend(
                "/topic/orders",
                o);
        return o;
    }

    // Cancel order
    @PatchMapping("/{orderId}/cancel")
    public Order cancelOrder(@PathVariable String orderId) throws Exception {
        return orderService.cancelOrder(orderId);
    }

}
