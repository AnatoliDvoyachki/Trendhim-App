package com.example.asus.trendhimapp.mainActivities.recentProducts;

public class RecentProduct {

    private String email, category, key, visit;

    public RecentProduct(){}

    RecentProduct(String key, String email, String category, String visit) {
        this.email = email;
        this.category = category;
        this.key = key;
        this.visit = visit;
    }

    public String getEmail() {
        return email;
    }

    String getCategory() {
        return category;
    }

    public String getKey() {
        return key;
    }

    public String getVisit(){
        return visit;
    }
}
