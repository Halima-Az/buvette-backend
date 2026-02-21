package com.buvette.buvette_backend.controller.admin;

import com.buvette.buvette_backend.dto.UserAdminDTO;
import com.buvette.buvette_backend.model.client.User;
import com.buvette.buvette_backend.services.shared.JwtService;
import com.buvette.buvette_backend.services.shared.UserService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
public class AdminUserController {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    // Get all users
    @GetMapping
    public List<UserAdminDTO> getAllUsers(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String email = jwtService.extractEmail(token);

            return userService.getAllUsers(email);
        }
        
        return userService.getAllUsers();
    }

    // Block user
    @PutMapping("/{id}/block")
    public ResponseEntity<User> blockUser(@PathVariable String id) {
        return ResponseEntity.ok(userService.blockUser(id));
    }

    // Unblock user
    @PutMapping("/{id}/unblock")
    public ResponseEntity<User> unblockUser(@PathVariable String id) {
        return ResponseEntity.ok(userService.unblockUser(id));
    }

    // Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // Add user
    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.addUser(user));
    }
}
