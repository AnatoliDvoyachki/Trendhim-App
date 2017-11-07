package com.example.asus.trendhimapp.util;

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
    public static final String TABLE_NAME_WEEKLY_LOOK = "weekly_look";
    public static final String TABLE_NAME_RECENT_PRODUCTS = "recent_products";

    // Attribute names for Product objects
    public static final String KEY_PRODUCT_NAME = "productName";
    public static final String KEY_PRICE = "price";
    public static final String KEY_BANNER_PIC_URL = "bannerPictureUrl";
    public static final String KEY_LEFT_PIC_URL = "leftPictureUrl";
    public static final String KEY_RIGHT_PIC_URL = "rightPictureUrl";
    public static final String KEY_BRAND_NAME = "brand";
    public static final String KEY_PRODUCT_KEY = "productKey";

    public static final String WATCH_PREFIX = "watch";
    public static final String ALL_NUMBERS_REGEX = "[^A-Za-z]";

    //Attribute names for Weekly Look objects
    public static final String KEY_LOOK_MAIN_PICTURE_URL = "mainPictureUrl";
    public static final String KEY_LOOK_SECOND_PICTURE_URL = "secondPictureUrl";
    public static final String KEY_LOOK_THIRD_PICTURE_URL = "thirdPictureUrl";
    public static final String KEY_LOOK_FOURTH_PICTURE_URL = "fourthPictureUrl";
    public static final String KEY_LOOK_FIFTH_PICTURE_URL = "fifthPictureUrl";
    public static final String KEY_LOOK_PHRASE = "phrase";
    public static final String KEY_LOOK_KEY = "lookKey";

}
