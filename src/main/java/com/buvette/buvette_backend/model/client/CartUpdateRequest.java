package com.buvette.buvette_backend.model.client;

public class CartUpdateRequest {

    private String itemId;
    private int quantity;

    public String getItemId() { return itemId; }
    public int getQuantity() { return quantity; }
}
