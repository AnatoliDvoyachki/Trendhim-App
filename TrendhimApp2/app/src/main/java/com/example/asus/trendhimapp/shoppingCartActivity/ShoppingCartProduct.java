package com.example.asus.trendhimapp.shoppingCartActivity;

import com.example.asus.trendhimapp.wishlistPage.WishlistProduct;

public class ShoppingCartProduct extends WishlistProduct {
    private int quantity;
    private String productName, bannerImageUrl;

    public ShoppingCartProduct() {}

    public ShoppingCartProduct(String productKey, String entityName, String userEmail) {
        super(productKey, entityName, userEmail);
        setQuantity(0);
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return super.getUserEmail() + " " + super.getProductKey();
    }
}
