package com.example.asus.trendhimapp.shoppingCartActivity;

import com.example.asus.trendhimapp.wishlistPage.WishlistProduct;

public class ShoppingCartProduct extends WishlistProduct {

    private String productKey, userEmail;

    public ShoppingCartProduct() {}

    public ShoppingCartProduct(String productKey, String userEmail) {
        this.productKey = productKey;
        this.userEmail = userEmail;
    }

    public String getProductKey() {
        return productKey;
    }

    public String getUserEmail() {
        return userEmail;
    }

}
