package com.example.asus.trendhimapp.ProductPage;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.example.asus.trendhimapp.ProductPage.Products.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Anatoli on 02-Nov-17.
 */

/**
 * Thread, used to handle the downloads from the GoogleFirebase storage service.
 **/
public class DownloadThread extends AsyncTask<Void, Void, Bitmap> {
    private ImageView imageView;
    private String pictureUrl;

    /***
     *  Constructs the Thread.
     *  @param pictureUrl
     *         url reference to the picture
     *  @param imageView
     *          the view to which the picture will be assigned after execution
     **/
    public DownloadThread(String pictureUrl, ImageView imageView) {
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
        BitmapFactory.savePicture(pictureUrl, bitmap);// Once the download is finished, cache the image
        imageView.setImageBitmap(bitmap); // Then show it to the user
    }

    /*
     * Used to download an image, referenced by the url passed as a param
     * @param urlString
     *      the url of the image
     * @return
     *      a bitmap of the image
     */
    private Bitmap downloadImage(String urlString) {
        HttpURLConnection con = null;
        InputStream inFromInternet = null;
        Bitmap bitmap = null;
        try {
            URL url = new URL(urlString);
            con = (HttpURLConnection) url.openConnection();
            con.getContentLength();
            inFromInternet = con.getInputStream();
            bitmap = android.graphics.BitmapFactory.decodeStream(inFromInternet);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            con.disconnect();
            try {
                inFromInternet.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return bitmap;
    }
}
