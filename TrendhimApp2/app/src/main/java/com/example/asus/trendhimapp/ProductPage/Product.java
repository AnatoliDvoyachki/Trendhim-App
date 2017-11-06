package com.example.asus.trendhimapp.ProductPage;

public class Product {

    private String bannerPictureUrl, leftPictureUrl, rightPictureUrl, brand, productName, key;
    private int price;

    // No-argument constructor for the Firebase implementation
    public Product() {}

    public Product(String key, String productName, String bannerPictureUrl, String leftPictureUrl,
                   String rightPictureUrl, String brand, int price) {

        this.productName = productName;
        this.brand = brand;
        this.bannerPictureUrl = bannerPictureUrl;
        this.key = key;
        this.leftPictureUrl = leftPictureUrl;
        this.rightPictureUrl = rightPictureUrl;
        this.price = price;
    }

    public String getKey() {
        return key;
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

    @Override
    public String toString() {
        return "Product{" +
                "bannerPictureUrl='" + bannerPictureUrl + '\'' +
                ", leftPictureUrl='" + leftPictureUrl + '\'' +
                ", rightPictureUrl='" + rightPictureUrl + '\'' +
                ", brand='" + brand + '\'' +
                ", productName='" + productName + '\'' +
                ", key='" + key + '\'' +
                ", price=" + price +
                '}';
    }
}