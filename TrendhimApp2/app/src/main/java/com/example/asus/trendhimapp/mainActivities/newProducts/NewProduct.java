package com.example.asus.trendhimapp.mainActivities.newProducts;

public class NewProduct {

    private String category, key;

    public NewProduct() {} // No-argument constructor for the Firebase queries

    public NewProduct(String key, String category) {
        this.category = category;
        this.key = key;
    }

    public String getCategory() {
        return category;
    }

    public String getKey() {
        return key;
    }

}
