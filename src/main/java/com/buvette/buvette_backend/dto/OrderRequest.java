package com.buvette.buvette_backend.dto;

import java.util.List;

public class OrderRequest {

    public static class Item {
        private String itemId;
        private int quantity;

        // getters & setters
        public String getItemId() { return itemId; }
        public void setItemId(String itemId) { this.itemId = itemId; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }

    private List<Item> items;

    // getters & setters
    public List<Item> getItems() { return items; }
    public void setItems(List<Item> items) { this.items = items; }
}
