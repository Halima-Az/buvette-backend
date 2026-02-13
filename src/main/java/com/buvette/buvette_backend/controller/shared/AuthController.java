package com.buvette.buvette_backend.controller.shared;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.buvette.buvette_backend.model.client.User;
import com.buvette.buvette_backend.services.shared.EmailService;
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
    private final EmailService emailService;

    public AuthController(UserAuthService service, JwtService jwtService,
            UserService userService, EmailService emailService) {
        this.service = service;
        this.jwtService = jwtService;
        this.userService = userService;
        this.emailService = emailService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
        service.sendVerificationEmail(user);
        return ResponseEntity.ok("Utilisateur créé !");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {

        String result = service.authenticate(user.getEmail(), user.getPassword());

        if (result.equals("SUCCESS")) {

            String token = jwtService.generateToken(user.getEmail());
            String role = service.getRoleByEmail(user.getEmail());
            User currentUser = userService.findByEmail(user.getEmail());

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "role", role,
                    "userId", currentUser.getId()));
        }

        if (result.equals("EMAIL_NOT_VERIFIED")) {
            return ResponseEntity.status(403).body("Please verify your email first.");
        }

        if (result.equals("EMAIL_NOT_FOUND")) {
            return ResponseEntity.status(404).body("Email not found.");
        }

        return ResponseEntity.status(401).body("Incorrect password.");
    }

    // forget password traitement
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String identifier = request.get("identifier");
        User user = userService.getUserByEmail(identifier);

        if (!service.forgotPassword(identifier, user)) {
            return ResponseEntity
                    .status(400) // Bad Request
                    .body(Map.of("message", "Email not found !"));
        }

        return ResponseEntity.ok(Map.of(
                "success", "Reset password email sent. Please check your inbox."));

    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {

        String token = request.get("token");
        String newPassword = request.get("password");
        userService.resetPassword(token, newPassword);

        return ResponseEntity.ok("Password updated successfully");
    }

    // verify user email
    @GetMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        boolean verifyUserEmail = service.verifyUserEmail(token);

        if (!verifyUserEmail) {
            return ResponseEntity.badRequest().body("Invalid token");
        }

        return ResponseEntity.ok("Email verified successfully");
    }
}
