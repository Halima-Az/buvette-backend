package com.buvette.buvette_backend.controller.shared;

import org.springframework.security.core.Authentication;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.buvette.buvette_backend.dto.ChangePasswordRequest;
import com.buvette.buvette_backend.model.client.User;
import com.buvette.buvette_backend.services.shared.JwtService;
import com.buvette.buvette_backend.services.shared.UserService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
    private UserService service;
    private JwtService jwtService;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/user")
    public ResponseEntity<User> findUser(Authentication auth) {
        User user = service.findByEmail(auth.getName());
        return ResponseEntity.ok(user);
    }

    // recuperer user connecte
    @GetMapping("/me")
    public User me(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        String token = auth.replace("Bearer ", "");

        String email = jwtService.extractEmail(token);
        User user = service.findByEmail(email);

        return new User(
                user.getEmail(),
                user.getRole());
    }

    // change password
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Authentication authentication) {
        User user = service.findByEmail(authentication.getName());

        if (!service.updateUserPassword(
                user,
                request.getOldPassword(),
                request.getNewPassword())) {
            return ResponseEntity
                    .badRequest()
                    .body("Ancien mot de passe incorrect");
        }

        return ResponseEntity.ok(
                Map.of("lastPasswordChange", user.getLastPasswordChange()));
    }




}
