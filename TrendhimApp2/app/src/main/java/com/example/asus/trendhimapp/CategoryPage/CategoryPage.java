package com.example.asus.trendhimapp.CategoryPage;

class CategoryPage {

    private String name, brand, bannerPictureURL;
    private int price, productId;

    CategoryPage(String name, int price, String brand, String bannerPictureURL, int productId) {
        this.name = name;
        this.price = price;
        this.brand = brand;
        this.bannerPictureURL = bannerPictureURL;
        this.productId = productId;
    }

    String getName() {
        return name;
    }

    int getPrice() {
        return price;
    }

    String getBrand() {
        return brand;
    }

    String getBannerPictureURL(){
        return bannerPictureURL;
    }

    int getProductId() {
        return productId;
    }

}
