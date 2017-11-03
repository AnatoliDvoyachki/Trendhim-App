package com.example.asus.trendhimapp.ProductPage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

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
public class DownloadThread extends AsyncTask<String, Integer, Bitmap> {
    private ImageView imageView;

    public DownloadThread(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        return downloadImage(params[0]);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        imageView.setImageBitmap(bitmap); // Once the download is finished, show the image to the user
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
            bitmap = BitmapFactory.decodeStream(inFromInternet);
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
