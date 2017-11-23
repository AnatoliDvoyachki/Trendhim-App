package com.example.asus.trendhimapp.wishlistPage;

public class WishlistProduct {
    private String productKey, entityName, userEmail;

    public WishlistProduct() {} // No-argument constructor for the Firebase queries

    public WishlistProduct(String productKey, String entityName, String userEmail) {
        this.productKey = productKey;
        this.entityName = entityName;
        this.userEmail = userEmail;
    }

    public String getProductKey() {
        return productKey;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getUserEmail() {
        return userEmail;
    }

}
