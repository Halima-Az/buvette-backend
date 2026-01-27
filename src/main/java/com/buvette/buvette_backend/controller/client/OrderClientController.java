package com.buvette.buvette_backend.controller.client;

import com.buvette.buvette_backend.dto.OrderRequest;
import com.buvette.buvette_backend.model.client.User;
import com.buvette.buvette_backend.model.shared.Order;
import com.buvette.buvette_backend.repository.shared.UserRepository;
import com.buvette.buvette_backend.services.shared.OrderService;
import com.buvette.buvette_backend.services.shared.JwtService;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client/orders")
@PreAuthorize("hasRole('CLIENT')")
@CrossOrigin(origins = "http://localhost:5173")
public class OrderClientController {

    private final OrderService orderService;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public OrderClientController(OrderService orderService,
            UserRepository userRepository,
            JwtService jwtService) {
        this.orderService = orderService;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @PostMapping
    public Order createOrder(@RequestBody OrderRequest orderRequest,
            @RequestHeader("Authorization") String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing token");
        }

        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return orderService.createOrder(user, orderRequest);
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
    public Order updateOrder(@PathVariable String orderId, @RequestBody Order order) throws Exception {
        return orderService.updateOrder(orderId, order);
    }

    // Cancel order
    @PatchMapping("/{orderId}/cancel")
    public Order cancelOrder(@PathVariable String orderId) throws Exception {
        return orderService.cancelOrder(orderId);
    }

}
