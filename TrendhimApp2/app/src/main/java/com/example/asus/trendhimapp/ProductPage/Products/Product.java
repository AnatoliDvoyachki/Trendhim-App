package com.example.asus.trendhimapp.ProductPage.Products;

public class Product {
    private int productId;
    private String bannerPictureUrl, leftPictureUrl, rightPictureUrl, brand;
    private double price;

    // No-argument constructor for the Firebase implementation
    public Product() {}

    public Product(int productId, String bannerPictureUrl, String leftPictureUrl,
                   String rightPictureUrl, String brand, double price) {
        setProductId(productId);
        setBannerPictureUrl(bannerPictureUrl);
        setLeftPictureUrl(leftPictureUrl);
        setRightPictureUrl(rightPictureUrl);
        setBrand(brand);
        setPrice(price);
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getBannerPictureUrl() {
        return bannerPictureUrl;
    }

    public void setBannerPictureUrl(String bannerPictureUrl) {
        this.bannerPictureUrl = bannerPictureUrl;
    }

    public String getLeftPictureUrl() {
        return leftPictureUrl;
    }

    public void setLeftPictureUrl(String leftPictureUrl) {
        this.leftPictureUrl = leftPictureUrl;
    }

    public String getRightPictureUrl() {
        return rightPictureUrl;
    }

    public void setRightPictureUrl(String rightPictureUrl) {
        this.rightPictureUrl = rightPictureUrl;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}
