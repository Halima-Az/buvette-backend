package com.buvette.buvette_backend.controller.shared;

import java.util.Map;

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

            // Récupérer l'utilisateur réel depuis la DB
            User realUser = userService.findByEmail(user.getEmail());
            String token = jwtService.generateToken(realUser.getEmail());

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "role", realUser.getRole() // <-- utilise le rôle réel
            ));
        }

        return ResponseEntity.status(401).body("Invalid credentials");
    }

}
