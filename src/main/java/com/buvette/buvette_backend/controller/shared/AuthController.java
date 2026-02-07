package com.buvette.buvette_backend.controller.shared;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.buvette.buvette_backend.model.client.User;
import com.buvette.buvette_backend.services.shared.JwtService;
import com.buvette.buvette_backend.services.shared.UserAuthService;
import com.buvette.buvette_backend.services.shared.UserService;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
@Validated
public class AuthController {

    private final UserAuthService service;
    private final JwtService jwtService;
    private final UserService userService;

    public AuthController(UserAuthService service, JwtService jwtService, UserService userService) {
        this.service = service;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
        service.save(user);
        return ResponseEntity.ok("Utilisateur créé !");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        if (service.authenticate(user.getEmail(), user.getPassword())) {

            String token = jwtService.generateToken(user.getEmail());
            String role = service.getRoleByEmail(user.getEmail());
            User curentUser = userService.findByEmail(user.getEmail());
            String userId = curentUser.getId();
            System.out.println(role + "--------------------------------------------------------");

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "role", role,
                    "userId", userId));
        }

        return ResponseEntity.status(401).body("Invalid credentials");
    }

    // forget password traitement
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {

        System.out.println("\n hi \n");
        String identifier = request.get("identifier");
        System.out.println("\n hi \n" + identifier);
        User user = userService.getUserByEmail(identifier);

        if (user == null) {
            return ResponseEntity
                    .status(400) // Bad Request
                    .body(Map.of("message", "Email not found !"));
        }
        
        String token = UUID.randomUUID().toString();

        user.setResetPasswordToken(token);
        user.setResetPasswordExpiry(LocalDateTime.now().plusMinutes(10));

        service.save(user);

        return ResponseEntity.ok(Map.of(
                "resetToken", token,
                "expiresIn", "10 minutes"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {

        String token = request.get("token");
        String newPassword = request.get("password");
        userService.resetPassword(token, newPassword);

        return ResponseEntity.ok("Password updated successfully");
    }
}
