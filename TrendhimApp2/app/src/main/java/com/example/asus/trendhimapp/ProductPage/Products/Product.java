package com.example.asus.trendhimapp.ProductPage.Products;

public abstract class Product {
    private int productId;
    private String bannerPictureUrl, leftPictureUrl, rightPictureUrl, brandPictureUrl, brand;
    private double price;

    public Product() {}

    public Product(int productId, String bannerPictureUrl, String leftPictureUrl,
                   String rightPictureUrl, String brandPictureUrl, String brand, double price) {
        setProductId(productId);
        setBannerPictureUrl(bannerPictureUrl);
        setLeftPictureUrl(leftPictureUrl);
        setRightPictureUrl(rightPictureUrl);
        setBrandPictureUrl(brandPictureUrl);
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

    public String getBrandPictureUrl() {
        return brandPictureUrl;
    }

    public void setBrandPictureUrl(String brandPictureUrl) {
        this.brandPictureUrl = brandPictureUrl;
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
