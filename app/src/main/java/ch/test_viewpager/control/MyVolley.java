package ch.test_viewpager.control;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import ch.test_viewpager.view.MainActivity;

/**
 * Created by ChaeHyun on 2016. 9. 13..
 */
public class MyVolley {
    private static MyVolley one;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(20);

    private MyVolley(Context context) {
        requestQueue = Volley.newRequestQueue(context);

        /*
        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
        */
    }

    public static MyVolley getInstance(Context context) {
        if(one == null)
            one = new MyVolley(context);

        return one;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

}