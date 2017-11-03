package com.example.asus.trendhimapp.CategoryPage;

class CategoryPage {

    private String name, brand, bannerPictureURL;
    private int price;

    CategoryPage(String name, int price, String brand, String bannerPictureURL) {
        this.name = name;
        this.price = price;
        this.brand = brand;
        this.bannerPictureURL = bannerPictureURL;
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

}
