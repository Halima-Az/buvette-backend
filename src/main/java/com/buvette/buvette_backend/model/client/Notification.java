package com.buvette.buvette_backend.model.client;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.buvette.buvette_backend.enumAttribute.Types;

import java.time.LocalDateTime;

public class Notification {
    
    private String id;
    private String userId;
    private String title;
    private String message;
    private Types type;
    private String orderId;
    private boolean read = false;
    private LocalDateTime createdAt;
    
    // Constructor
    public Notification() {
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public Types getType() { return type; }
    public void setType(Types type) { this.type = type; }
    
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    
    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    
}