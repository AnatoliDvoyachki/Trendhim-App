package com.example.asus.trendhimapp.shoppingCartActivity;

import com.example.asus.trendhimapp.wishlistPage.WishlistProduct;

public class ShoppingCartProduct extends WishlistProduct {

    private int quantity;

    private String productKey, userEmail;

    public ShoppingCartProduct() {}

    public ShoppingCartProduct(String productKey, String userEmail, int quantity) {
        this.productKey = productKey;
        this.userEmail = userEmail;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductKey() {
        return productKey;
    }

    public String getUserEmail() {
        return userEmail;
    }

    @Override
    public String toString() {
        return "ShoppingCartProduct{" +
                "quantity=" + quantity +
                ", productKey='" + productKey + '\'' +
                ", userEmail='" + userEmail + '\'' +
                '}';
    }

}
