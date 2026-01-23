package com.buvette.buvette_backend.controller.client;
import com.buvette.buvette_backend.dto.OrderRequest;
import com.buvette.buvette_backend.model.client.User;
import com.buvette.buvette_backend.model.shared.Order;
import com.buvette.buvette_backend.repository.shared.UserRepository;
import com.buvette.buvette_backend.services.shared.OrderService;
import com.buvette.buvette_backend.services.shared.JwtService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
@PreAuthorize("hasRole('CLIENT')")
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

    @PostMapping("/orders")
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
}
