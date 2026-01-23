package com.buvette.buvette_backend.dto;

import java.time.LocalDateTime;

public class OrderDTO {

    private String orderId;
    private String itemId;

    private String itemName;
    private int quantity;
    private double total;
    private LocalDateTime time;

      // constructor
    public OrderDTO(String orderId, String itemId, String itemName, int quantity, double total , LocalDateTime time) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.total=total;
        this.time=time;
    }
    
    public String getOrderId() {
        return this.orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getItemId() {
        return this.itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
    public double getTotal() {
        return this.total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public LocalDateTime getTime() {
        return this.time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

 

    public String getItemName() {
        return this.itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

  

  
}
