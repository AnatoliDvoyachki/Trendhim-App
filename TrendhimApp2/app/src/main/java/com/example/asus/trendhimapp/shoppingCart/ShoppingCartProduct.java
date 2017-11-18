package com.example.asus.trendhimapp.shoppingCart;

import com.example.asus.trendhimapp.wishlist.WishlistProduct;

public class ShoppingCartProduct extends WishlistProduct {

    private String productKey, userEmail;
    private String quantity;

    public ShoppingCartProduct() {}

    public ShoppingCartProduct(String productKey, String userEmail, String quantity) {
        this.productKey = productKey;
        this.userEmail = userEmail;
        this.quantity = quantity;
    }

    public String getProductKey() {
        return productKey;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getQuantity() {
        return quantity;
    }

}
