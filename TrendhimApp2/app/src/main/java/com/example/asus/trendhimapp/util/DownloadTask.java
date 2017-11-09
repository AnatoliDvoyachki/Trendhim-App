package com.example.asus.trendhimapp.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Used to handle the downloads from the GoogleFirebase storage service.
 **/
public class DownloadTask extends AsyncTask<Void, Void, Bitmap> {
    private ImageView imageView;
    private String pictureUrl;

    /**
     * Constructs the Thread.
     *
     * @param pictureUrl url reference to the picture
     * @param imageView  the view to which the picture will be assigned after execution
     */
    public DownloadTask(String pictureUrl, ImageView imageView) {
        this.pictureUrl = pictureUrl;
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        return downloadImage(pictureUrl);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        BitmapFlyweight.cachePicture(pictureUrl, bitmap);// Once the download is finished, cache the image
        imageView.setImageBitmap(bitmap); // Then show it to the user
    }

    /**
     * Used to download an image, referenced by an url
     *
     * @param urlString the url of the image
     * @return a bitmap of the image
     */
    private Bitmap downloadImage(String urlString) {
        HttpURLConnection con = null;
        InputStream inFromInternet = null;
        Bitmap bitmap = null;
        try {
            URL url = new URL(urlString);
            con = (HttpURLConnection) url.openConnection();
            if (con != null) inFromInternet = con.getInputStream();
            if (inFromInternet != null) bitmap = BitmapFactory.decodeStream(inFromInternet);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) con.disconnect();
                if (inFromInternet != null) inFromInternet.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return bitmap;
    }
}
