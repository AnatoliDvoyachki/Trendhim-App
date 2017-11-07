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

    // Attribute name for the Product object
    public static final String KEY_PRODUCT_NAME = "productName";
    public static final String KEY_PRICE = "price";
    public static final String KEY_BANNER_PIC_URL = "bannerPictureUrl";
    public static final String KEY_LEFT_PIC_URL = "leftPictureUrl";
    public static final String KEY_RIGHT_PIC_URL = "rightPictureUrl";
    public static final String KEY_BRAND_NAME = "brand";
    public static final String KEY_PRODUCT_KEY = "productKey";

    public static final String WATCH_PREFIX = "watch";
    public static final String ALL_NUMBERS_REGEX = "\\d";
}
