package com.example.asus.trendhimapp.ProductPage;

public class Product {

    private int productId, bannerPictureId, leftPictureId, rightPictureId, brandPictureId; // byte[] or Bitmaps
    private String productType, brand;
    private double price;

    // No-argument constructor for the Firebase implementation
    public Product() {}

    public Product(int productId, String productType, int bannerPictureId, int leftPictureId,
                   int rightPictureId, int brandPictureId, String brand, double price) {
        setProductId(productId);
        setBannerPictureId(bannerPictureId);
        setLeftPictureId(leftPictureId);
        setRightPictureId(rightPictureId);
        setBrandPictureId(brandPictureId);
        setProductType(productType);
        setBrand(brand);
        setPrice(price);
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getBannerPictureId() {
        return bannerPictureId;
    }

    public void setBannerPictureId(int bannerPictureId) {
        this.bannerPictureId = bannerPictureId;
    }

    public int getLeftPictureId() {
        return leftPictureId;
    }

    public void setLeftPictureId(int leftPictureId) {
        this.leftPictureId = leftPictureId;
    }

    public int getRightPictureId() {
        return rightPictureId;
    }

    public void setRightPictureId(int rightPictureId) {
        this.rightPictureId = rightPictureId;
    }

    public int getBrandPictureId() {
        return brandPictureId;
    }

    public void setBrandPictureId(int brandPictureId) {
        this.brandPictureId = brandPictureId;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
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

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", bannerPictureId=" + bannerPictureId +
                ", leftPictureId=" + leftPictureId +
                ", rightPictureId=" + rightPictureId +
                ", brandPictureId=" + brandPictureId +
                ", productType='" + productType + '\'' +
                ", brand='" + brand + '\'' +
                ", price=" + price +
                "}\n";
    }
}
