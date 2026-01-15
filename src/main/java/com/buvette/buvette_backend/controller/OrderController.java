package com.buvette.buvette_backend.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.buvette.buvette_backend.model.CartItem;
import com.buvette.buvette_backend.model.MenuItem;
import com.buvette.buvette_backend.model.User;
import com.buvette.buvette_backend.model.server.Order;
import com.buvette.buvette_backend.repository.MenuItemRepository;
import com.buvette.buvette_backend.services.JwtService;
import com.buvette.buvette_backend.services.OrderService;
import com.buvette.buvette_backend.services.UserService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {

    private final OrderService orderService;
    private final JwtService jwtService;
    private final UserService service;
    private final MenuItemRepository menuItemRepository;

    public OrderController(OrderService orderService,
            JwtService jwtService,
            UserService userService, MenuItemRepository menuItemRepository) {

        this.orderService = orderService;
        this.jwtService = jwtService;
        this.service = userService;
        this.menuItemRepository = menuItemRepository;
    }

    @PostMapping("/order/add")
    public ResponseEntity<?> addOrder(@RequestBody Order orderFromClient, HttpServletRequest request) {
        try {
            // 🔹 Vérifier le header Authorization
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("Authorization header missing or invalid");
            }

            // 🔹 Extraire le token
            String token = authHeader.substring(7);
            // 🔹 Extraire les claims du token
            Claims claims = jwtService.extractClaims(token);

            String userId = claims.getSubject();

            // 🔹 Récupérer l'utilisateur
            User user = service.findByEmail(userId);
            if (user == null) {
                return ResponseEntity.status(404).body("User not found");
            }
            System.out.println("Utilisateur trouvé: " + user.getUsername() + " " + user.getFname());

            // calculer le total
            double total = 0;
            for (CartItem cart : orderFromClient.getItems()) {
                MenuItem menuItem = menuItemRepository
                        .findById(cart.getItemId())
                        .orElseThrow(() -> new RuntimeException("MenuItem not found: "
                                + cart.getItemId()));
                total += cart.getQuantity() * menuItem.getPrice();

            }
            // 🔹 Construire et sauvegarder la commande
            Order order = new Order();
            order.setServerId(orderFromClient.getServerId());
            order.setItems(orderFromClient.getItems());
            order.setUserId(user.getId());
            order.setUsername(user.getUsername());
            order.setUserPrenom(user.getFname());
            order.setTotal(total);

            System.out.println("Order prêt à être sauvegardé: " + order);
            orderService.save(order);
            System.out.println("Order sauvegardé avec succès !");

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            // 🔹 Log complet de l'erreur
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
        }
    }

    @GetMapping("/order/my-last")
    public ResponseEntity<?> getMyLastOrder(HttpServletRequest request) {

        System.out.println("\n \n i'm here \n \n" );
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }
        System.out.println("\n \n "+authHeader+"\n \n" );

        String token = authHeader.substring(7);
        System.out.println("\n \n "+token+"\n \n" );
        Claims claims = jwtService.extractClaims(token);
        String email = claims.getSubject();
        System.out.println("\n \n "+email+"\n \n" );
        User user = service.findByEmail(email);
         System.out.println("\n \n"+ user);
        String userId = user.getId();
        System.out.println("\n \n "+userId+"\n \n" );
        Optional<Order> optionalOrder = orderService.getLastOrderByUser(userId);
        System.out.println("\n \n "+optionalOrder+"\n \n" );

        if (optionalOrder.isEmpty()) {
            Map<String, Object> emptyResp = new HashMap<>();
            emptyResp.put("status", null);
            return ResponseEntity.ok(emptyResp);
        }

        Order order = optionalOrder.get();
        System.out.println("\n \n "+order+"\n \n" );

        Map<String, Object> response = new HashMap<>();
        response.put("status", order.getStatus());
        response.put("orderId", order.getId());
        response.put("createdAt", order.getCreatedAt());

        return ResponseEntity.ok(response);

    }

}
