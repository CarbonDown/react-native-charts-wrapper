package com.github.wuxudong.rncharts.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.AsyncTask;

import com.github.wuxudong.rncharts.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DrawableUtils {
    public static Drawable drawableFromUrl(Context context, String url, final int width, final int height) {

        int nWidth = (int)(width * 0.7);
        int nHeight = (int)(height * 0.7);

        if (BuildConfig.BUILD_TYPE == "debug") {
            try {
                return new DrawableLoadingAsyncTask().execute(url, Integer.toString(nWidth), Integer.toString(nHeight)).get();
            } catch (Exception e) {
                // draw dummy drawable when execution fail
                e.printStackTrace();
                return new ShapeDrawable();
            }
        } else {
            int resourceId = context.getResources().getIdentifier(url, "drawable", context.getPackageName());

            Bitmap image = BitmapFactory.decodeResource(context.getResources(), resourceId);
            return new BitmapDrawable(Resources.getSystem(), Bitmap.createScaledBitmap(image, nWidth, nHeight, true));
        }



    }

    static class DrawableLoadingAsyncTask extends AsyncTask<String, Void, Drawable> {
        @Override
        protected Drawable doInBackground(String... strings) {
            try {
                Bitmap x;

                int width = Integer.parseInt(strings[1]);
                int height = Integer.parseInt(strings[2]);

                HttpURLConnection connection = (HttpURLConnection) new URL(strings[0]).openConnection();
                connection.connect();
                InputStream input = connection.getInputStream();

                x = BitmapFactory.decodeStream(input);

                return new BitmapDrawable(Resources.getSystem(), Bitmap.createScaledBitmap(x, width, height, true));

            } catch(IOException e) {
                e.printStackTrace();
                // draw dummy drawable when connection fail
                return new ShapeDrawable();
            }
        }
    };
}
