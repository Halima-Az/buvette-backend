package com.buvette.buvette_backend.controller;

import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.buvette.buvette_backend.model.User;
import com.buvette.buvette_backend.services.UserService;

import org.springframework.web.bind.annotation.GetMapping;



@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
    private UserService service;
    public UserController(UserService service){
        this.service=service;
    }
    @GetMapping("/user")
    public ResponseEntity<User> findUser(Authentication auth) {
        User user= service.findByEmail(auth.getName());
        return ResponseEntity.ok(user);
    }
    
}
