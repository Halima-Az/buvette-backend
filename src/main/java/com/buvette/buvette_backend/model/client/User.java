package com.buvette.buvette_backend.model.client;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.validation.constraints.*;

@Document(collection = "users")
public class User {
    @Id
    private String id;
    @NotBlank(message = "username required")
    private String username;
    @Email(message = "invalide email")
    @NotBlank(message = "Email required")
    private String email;
    @NotBlank(message = "password required")
    @Size(min = 8, message = "passort must at least 8 characters")
    private String password;
    private String role = "ROLE_CLIENT"; // par défaut
    @NotNull(message = "You must accept the terms & conditions")
    private Boolean agreeTerms;
    @NotBlank(message = "First name required")
    private String fname;

    @NotBlank(message = "Last name required")
    private String lname;
    private String dob;

    private List<CartItem> cart = new ArrayList<>();

    @Field("notifs")
    private List<Notification> notifications = new ArrayList<>();

    private LocalDateTime lastPasswordChange;

    public Boolean isAgreeTerms() {
        return this.agreeTerms;
    }

    public LocalDateTime getLastPasswordChange() {
        return this.lastPasswordChange;
    }

    public void setLastPasswordChange(LocalDateTime lastPasswordChange) {
        this.lastPasswordChange = lastPasswordChange;
    }

    public User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
     public User(String email, String role) {
        this.email = email;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getAgreeTerms() {
        return agreeTerms;
    }

    public void setAgreeTerms(Boolean agreeTerms) {
        this.agreeTerms = agreeTerms;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public List<CartItem> getCart() {
        return cart;
    }

    public void setCart(List<CartItem> cart) {
        this.cart = cart;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }
    
    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

}
