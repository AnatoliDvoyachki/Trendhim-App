package com.example.asus.trendhimapp.util;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class, used for obtaining Bitmap objects. Implemented using the Flyweight Design Pattern.
 **/
public final class BitmapFlyweight {

    private BitmapFlyweight() {}

    private static final int MAX_CACHE_CAPACITY = 120;
    private static final Map<String, Bitmap> PICTURE_CACHE = new HashMap<>();

    /**
     * Used to get a picture for the ImageViews.
     *
     * @param url       the url of the picture - KEY
     * @param imageView the ImageView to which the picture will be assigned
     **/
    public static void getPicture(String url, ImageView imageView) {
        Bitmap bitmap = PICTURE_CACHE.get(url);
        if (bitmap == null) {
            new DownloadTask(url, imageView).execute(); // If the picture is not stored in the memory, download it
        } else {
            imageView.setImageBitmap(bitmap); // If it is stored in the memory, reuse it
        }
    }

    /**
     * Used to store the picture into the Flyweight.
     *
     * @param url    the url of the picture - KEY
     * @param bitmap the bitmap of the picture - VALUE
     **/
    public static void cachePicture(String url, Bitmap bitmap) {
        // If the HashMap contains over 120 entries, it is cleared to free up the memory
        if (PICTURE_CACHE.size() >= MAX_CACHE_CAPACITY) {
            PICTURE_CACHE.clear();
        }
        PICTURE_CACHE.put(url, bitmap);
    }

}
