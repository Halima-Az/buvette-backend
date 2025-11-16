package com.buvette.buvette_backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.*;

@Document(collection = "users")
public class User {
    @Id
    private String id;
    @NotBlank(message="username required")
    private String username;
    @Email(message="invalide email")
        @NotBlank(message="email required")
    private String email;
    @NotBlank(message="password required")
    @Size(min=8,message="passort must at least 8 characters")
    private String password;
    private String role = "CLIENT"; // par défaut

    public User() {}

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

}
