package com.example.asus.trendhimapp.CategoryPage;

public class CategoryProduct {

    private String name, brand, bannerPictureURL, productKey;
    private int price;

    public CategoryProduct(String name, int price, String brand, String bannerPictureURL, String productKey) {
        this.name = name;
        this.price = price;
        this.brand = brand;
        this.bannerPictureURL = bannerPictureURL;
        this.productKey = productKey;
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

    public String getBannerPictureURL(){
        return bannerPictureURL;
    }

    public String getProductKey() {
        return productKey;
    }

    @Override
    public String toString() {
        return "CategoryProduct{" +
                "name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", bannerPictureURL='" + bannerPictureURL + '\'' +
                ", productKey='" + productKey + '\'' +
                ", price=" + price +
                '}';
    }
}
