package com.example.asus.trendhimapp.util;

/**
 * Utility class, used for storing constants that are used throughout the project
 **/
public final class Constants {
    private Constants() {}

    // Entity names in Google Firebase
    public static final String TABLE_NAME_BAGS = "bags";
    public static final String TABLE_NAME_BEARD_CARE = "beard_care";
    public static final String TABLE_NAME_BOW_TIES = "bow_ties";
    public static final String TABLE_NAME_BRACELETS = "bracelets";
    public static final String TABLE_NAME_NECKLACES = "necklaces";
    public static final String TABLE_NAME_TIES = "ties";
    public static final String TABLE_NAME_WATCHES = "watches";
    public static final String TABLE_NAME_WISHLIST = "wishlist";
    public static final String TABLE_NAME_SHOPPING_CART = "shopping_cart";
    public static final String TABLE_NAME_WEEKLY_LOOK = "weekly_look";
    public static final String TABLE_NAME_RECENT_PRODUCTS = "recent_products";
    public static final String TABLE_NAME_USER_CREDENIALS = "user_credentials";

    // Attribute name for the Product object
    public static final String KEY_PRODUCT_NAME = "productName";
    public static final String KEY_PRICE = "price";
    public static final String KEY_BANNER_PIC_URL = "bannerPictureUrl";
    public static final String KEY_LEFT_PIC_URL = "leftPictureUrl";
    public static final String KEY_RIGHT_PIC_URL = "rightPictureUrl";
    public static final String KEY_BRAND_NAME = "brand";
    public static final String KEY_PRODUCT_KEY = "productKey";


    //Attribute names for Weekly Look objects
    public static final String KEY_LOOK_KEY = "lookKey";


    // Regex for the add to wishlist functionality
    public static final String WATCH_REGEX = "watch";
    public static final String ALL_DIGITS_REGEX = "\\d+";
}
