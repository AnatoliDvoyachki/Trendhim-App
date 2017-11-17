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
    public static final String TABLE_NAME_USER_CREDENTIALS = "user_credentials";
    public static final String TABLE_NAME_RECOMMENDED_PRODUCTS = "recommended_products";

    // Attribute name for the Product object
    public static final String KEY_PRODUCT_NAME = "productName";
    public static final String KEY_PRICE = "price";
    public static final String KEY_BANNER_PIC_URL = "bannerPictureUrl";
    public static final String KEY_LEFT_PIC_URL = "leftPictureUrl";
    public static final String KEY_RIGHT_PIC_URL = "rightPictureUrl";
    public static final String KEY_BRAND_NAME = "brand";
    public static final String KEY_PRODUCT_KEY = "productKey";

    // Keys for passing objects from one intent to another
    public static final String KEY_EMAIL = "email";
    public static final String KEY_VISIT_TIME = "visit";
    public static final String KEY_USER_EMAIL = "userEmail";
    public static final String KEY_ORDER = "order";
    public static final String KEY_ATTR_KEY = "key";
    public static final String KEY_PRODUCTS = "products";
    public static final String KEY_LOOK_KEY = "lookKey";
    public static final String KEY_CATEGORY = "category";

    // Shipping cost
    public static final int SINGLE_ITEM_SHIPPING_COST = 5;

    // Regex
    public static final String WATCH_PREFIX = "watch";
    public static final String ALL_DIGITS_REGEX = "\\d+";
    public static final String BEARD_CARE_PREFIX = "beard";

    //Credentials
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_ZIPCODE = "zipcode";
    public static final String KEY_CITY = "city";
    public static final String KEY_NAME = "name";

}
