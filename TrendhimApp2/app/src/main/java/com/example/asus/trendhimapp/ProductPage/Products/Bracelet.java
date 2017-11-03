package com.example.asus.trendhimapp.ProductPage.Products;

public class Bracelet extends Product {

    // No-argument constructor for the Firebase implementation
    public Bracelet() {
        super();
    }

    public Bracelet(int productId, String bannerPictureUrl, String leftPictureUrl,
                    String rightPictureUrl, String brandPictureUrl, String brand, double price) {
        super(productId, bannerPictureUrl, leftPictureUrl, rightPictureUrl, brandPictureUrl, brand, price);
    }

}
