package com.example.asus.trendhimapp.CategoryPage;

import java.util.ArrayList;

class CategoryPage {

    private String name;
    private int price;
    private String brand;


    private CategoryPage(String name, int price, String brand) {
        this.name = name;
        this.price = price;
        this.brand = brand;
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


    static ArrayList<CategoryPage> createCategoryList(int numCategories) {
        ArrayList<CategoryPage> categories = new ArrayList<>();

        for (int i = 1; i <= numCategories; i++) {
            categories.add(new CategoryPage("Watch " + i, 100 * i, "Brand" + i));
        }

        return categories;
    }
}
