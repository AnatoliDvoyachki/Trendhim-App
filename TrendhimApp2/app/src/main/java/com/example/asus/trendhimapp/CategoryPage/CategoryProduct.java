package com.example.asus.trendhimapp.CategoryPage;

public class CategoryProduct {

    private String name, brand, bannerPictureURL, key;
    private int price;

    public CategoryProduct(String name, int price, String brand, String bannerPictureURL, String key) {
        this.name = name;
        this.price = price;
        this.brand = brand;
        this.bannerPictureURL = bannerPictureURL;
        this.key = key;
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

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return "CategoryProduct{" +
                "name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", bannerPictureURL='" + bannerPictureURL + '\'' +
                ", key='" + key + '\'' +
                ", price=" + price +
                '}';
    }
}
