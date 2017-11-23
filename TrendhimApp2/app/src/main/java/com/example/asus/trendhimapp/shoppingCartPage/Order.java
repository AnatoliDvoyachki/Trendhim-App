package com.example.asus.trendhimapp.shoppingCartPage;

public class Order {

    private String productKey, quantity;

    public Order(){} // No-argument constructor for the Firebase queries

    public Order(String productKey, String quantity){
        this.productKey = productKey;
        this.quantity = quantity;
    }

    public String getProductKey() {
        return productKey;
    }

    public String getQuantity() {
        return quantity;
    }

}
