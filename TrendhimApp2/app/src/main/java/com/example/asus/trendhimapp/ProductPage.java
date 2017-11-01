package com.example.asus.trendhimapp;

public class ProductPage {

    private String name;
    private int price;
    private String description;
    private String brand;


    public  ProductPage(String name, int price, String description, String brand) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getBrand() {
        return brand;
    }

    public String getDescription() {
        return description;
    }


}
