package com.buvette.buvette_backend.model.worker;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.buvette.buvette_backend.enumAttribute.RequestOrderStatus;

@Document(collection = "requests_workers")
public class RequestRegister {
@Id    
private String id;
private String firstName;
private String lastName;
private String email;
private RequestOrderStatus status; // PENDING, APPROVED, REJECTED
private LocalDateTime createdAt;

    public String getId() {
        return this.id;
    }

    public RequestRegister( String firstName, String lastName, String email, RequestOrderStatus status, LocalDateTime createdAt) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.status = status;
        this.createdAt = createdAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RequestOrderStatus getStatus() {
        return this.status;
    }

    public void setStatus(RequestOrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
