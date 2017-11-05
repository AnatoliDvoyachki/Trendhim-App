package com.example.asus.trendhimapp.ProductPage;

public class WishlistProduct {
    private String email, entity, key;

    public WishlistProduct(String email, String entity, String key) {
        this.email = email;
        this.entity = entity;
        this.key = key;
    }

    public String getEmail() {
        return email;
    }

    public String getEntity() {
        return entity;
    }

    public String getKey() {
        return key;
    }
}