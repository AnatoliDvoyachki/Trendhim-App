package com.example.asus.trendhimapp.ProductPage.Products;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.asus.trendhimapp.ProductPage.DownloadThread;

import java.util.HashMap;

/**
 * Utility class, used for obtaining Bitmap objects. Implemented using the Flyweight Design Pattern.
 **/
public final class BitmapFactory {
    private BitmapFactory() {}

    private static String TAG = "FLYWEIGHT:";

    private static HashMap<String, Bitmap> PICTURE_CACHE = new HashMap<>();

    /**
     * Used to get a picture for the ImageViews.
     *
     * @param url       the url of the picture - KEY
     * @param imageView the ImageView to which the picture will be assigned
     **/
    public static void getPicture(String url, ImageView imageView) {
        Bitmap bitmap = PICTURE_CACHE.get(url);
        if (bitmap == null) {
            new DownloadThread(url, imageView).execute(); // If the picture is not stored locally, download it
            Log.d(TAG, "Downloading");
        } else {
            imageView.setImageBitmap(bitmap); // If it is stored locally, reuse it
            Log.d(TAG, "Reused " + bitmap.toString());
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
        Log.d(TAG, "Saved " + bitmap.toString());
    }

}
