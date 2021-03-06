package com.example.asus.trendhimapp.mainActivities.recentProducts;

public class RecentProduct {

    private String email, category, key;

    public RecentProduct(){} // No-argument constructor for the Firebase queries

    public RecentProduct(String key, String email, String category) {
        this.email = email;
        this.category = category;
        this.key = key;
    }

    public String getEmail() {
        return email;
    }

    public String getCategory() {
        return category;
    }

    public String getKey() {
        return key;
    }
    
}
