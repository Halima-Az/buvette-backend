package com.buvette.buvette_backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.buvette.buvette_backend.model.User;
import com.buvette.buvette_backend.services.UserAuthService;

import jakarta.validation.Valid;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
@Validated
public class AuthController {

    @Autowired
    private UserAuthService service;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> {
                errors.put(error.getField(), error.getDefaultMessage());
            });
            return ResponseEntity.badRequest().body(errors);
        }
        service.save(user);
        return ResponseEntity.ok("Utilisateur créé !");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        if (service.authenticate(user.getEmail(), user.getPassword())) {
            return ResponseEntity.ok("Login success");
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }

}