package com.example.asus.trendhimapp.Util;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.util.HashMap;

/**
 * Utility class, used for obtaining Bitmap objects. Implemented using the Flyweight Design Pattern.
 **/
public final class BitmapFlyweight {
    private BitmapFlyweight() {}

    private final static HashMap<String, Bitmap> PICTURE_CACHE = new HashMap<>();

    /**
     * Used to get a picture for the ImageViews.
     *
     * @param url       the url of the picture - KEY
     * @param imageView the ImageView to which the picture will be assigned
     **/
    public static void getPicture(String url, ImageView imageView) {
        Bitmap bitmap = PICTURE_CACHE.get(url);
        if (bitmap == null) {
            new DownloadTask(url, imageView).execute(); // If the picture is not stored locally, download it
        } else {
            imageView.setImageBitmap(bitmap); // If it is stored locally, reuse it
        }
    }

    /**
     * Used to store the picture into the Flyweight.
     *
     * @param url    the url of the picture - KEY
     * @param bitmap the bitmap of the picture - VALUE
     **/
    public static void savePicture(String url, Bitmap bitmap) {
        PICTURE_CACHE.put(url, bitmap);
    }

}