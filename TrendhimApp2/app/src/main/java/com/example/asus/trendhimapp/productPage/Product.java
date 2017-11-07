package com.example.asus.trendhimapp.productPage;

public class Product {

    private String bannerPictureUrl, leftPictureUrl, rightPictureUrl, brand, productName, productKey;
    private int price;

    // No-argument constructor for the Firebase implementation
    public Product() {}

    public Product(String key, String productName, String bannerPictureUrl, String leftPictureUrl,
                   String rightPictureUrl, String brand, int price) {

        this.productName = productName;
        this.brand = brand;
        this.bannerPictureUrl = bannerPictureUrl;
        this.productKey = key;
        this.leftPictureUrl = leftPictureUrl;
        this.rightPictureUrl = rightPictureUrl;
        this.price = price;
    }

    public String getProductKey() {
        return productKey;
    }

    public  String getProductName(){
        return productName;
    }

    public String getBannerPictureUrl() {
        return bannerPictureUrl;
    }

    public String getLeftPictureUrl() {
        return leftPictureUrl;
    }

    public String getRightPictureUrl() {
        return rightPictureUrl;
    }

    public String getBrand() {
        return brand;
    }

    public int getPrice() {
        return price;
    }

}