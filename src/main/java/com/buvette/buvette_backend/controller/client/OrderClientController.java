package com.buvette.buvette_backend.controller.client;

import com.buvette.buvette_backend.dto.OrderRequest;
import com.buvette.buvette_backend.model.shared.Order;
import com.buvette.buvette_backend.repository.shared.UserRepository;
import com.buvette.buvette_backend.services.shared.OrderService;
import com.buvette.buvette_backend.services.shared.JwtService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
public class OrderClientController {

    private final OrderService orderService;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public OrderClientController(OrderService orderService, UserRepository userRepository, JwtService jwtService) {
        this.orderService = orderService;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @PostMapping("/orders")
    public Order createOrder(@RequestBody OrderRequest orderRequest,
                             @RequestHeader("Authorization") String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing token");
        }

        String token = authHeader.substring(7);

        // Extract email from JWT
        String email = jwtService.extractEmail(token);

        // Fetch user once
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create order with userId and username
        return orderService.createOrder(user.getId(), user.getUsername(), orderRequest);
    }
}
