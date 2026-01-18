package com.buvette.buvette_backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.buvette.buvette_backend.enumAttribute.ItemCategory;

@Document(collection = "menu_items")
public class MenuItem {

    @Id
    private String id;       // MongoDB _id maps here
    private String name;
    private double price;
    private double rating;
    private ItemCategory itemCategory;
    private String image;

    
    public MenuItem() {} // default constructor is required

    // getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    public ItemCategory getCategorie() {
        return itemCategory;
    }
    public void setCategorie(ItemCategory categorie) {
        this.itemCategory = categorie;
    }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
}
