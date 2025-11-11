package com.buvette.buvette_backend.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.buvette.buvette_backend.model.User;
import com.buvette.buvette_backend.services.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService service;

    @PostMapping("/register")
    public void addUser(@RequestBody User user) {
        service.engister(user);
    }
}
