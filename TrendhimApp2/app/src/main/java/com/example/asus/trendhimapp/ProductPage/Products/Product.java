package com.example.asus.trendhimapp.ProductPage.Products;

public class Product {

    private String bannerPictureUrl, leftPictureUrl, rightPictureUrl, brand, productName;
    private int productId, price;

    // No-argument constructor for the Firebase implementation
    public Product() {}

    public Product(int productId, String productName, String bannerPictureUrl, String leftPictureUrl,
                   String rightPictureUrl, String brand, int price) {

        this.productName = productName;
        this.brand = brand;
        this.bannerPictureUrl = bannerPictureUrl;
        this.productId = productId;
        this.leftPictureUrl = leftPictureUrl;
        this.rightPictureUrl = rightPictureUrl;
        this.price = price;
    }

    public int getProductId() {
        return productId;
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