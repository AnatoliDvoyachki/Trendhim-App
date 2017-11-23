package com.example.asus.trendhimapp.settings.order;

public class UserOrder {

    private String date, address, grand_total, key;

    public UserOrder(){}; //empty constructor for the firebase queries

    public UserOrder(String date, String address, String grand_total, String key){
        this.date = date;
        this.address = address;
        this.grand_total = grand_total;
        this.key = key;
    }

    public String getDate() {
        return date;
    }

    public String getAddress() {
        return address;
    }

    public String getGrandTotal() {
        return grand_total;
    }

    public String getKey() {
        return key;
    }
}
