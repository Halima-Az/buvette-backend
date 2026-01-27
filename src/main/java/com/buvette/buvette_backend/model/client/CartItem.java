package com.buvette.buvette_backend.model.client;

import org.springframework.data.annotation.Transient;

public class CartItem {
    private String itemId;
    private int quantity;
    @Transient // MongoDB ne le sauvegarde pas
    private String itemName;
    @Transient
    private double itemPrice;

    public double getItemPrice() {
        return this.itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemName() {
        return this.itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public CartItem() {}

    public CartItem(String itemId, int quantity) {
        this.itemId = itemId;
        this.quantity = quantity;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
