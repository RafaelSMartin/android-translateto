package com.ticktalk.translateto;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.support.v4.util.LruCache;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.ticktalk.translateto.database.DatabaseManager;


public class App extends MultiDexApplication {


//    /**
//     * Billing
//     *
//     **/
//    private Billing mBilling;
//    public Billing getBilling(){ return mBilling; }

    /**
     * Log or request TAG
     */
    public static final String TAG = "TranslateTo";

    /**
     * Image cache size.
     */
    public static final int IMAGE_CACHE_SIZE = 10;

    /**
     * Global request queue for Volley
     */
    private RequestQueue mRequestQueue;

    /**
     *
     */
    private ImageLoader mImageLoader;

    /**
     * A singleton instance of the application class for easy access in other places
     */
    private static App sInstance;


    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    @Override
    public void onCreate() {
        super.onCreate();

        // initialize the singleton
        sInstance = this;

        DatabaseManager.init(this);
        Fresco.initialize(this);

//        mBilling = new Billing(this, new Billing.DefaultConfiguration() {
//            @NonNull
//            @Override
//            public String getPublicKey() {
//                return getString(R.string.base_64_key);
//            }
//        });
    }

    /**
     * @return ApplicationController singleton instance
     */
    public static synchronized App getInstance() {
        return sInstance;
    }

    /**
     * @return The Volley Request queue, the queue will be created if it is null
     */
    public RequestQueue getRequestQueue() {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    /**
     * @return The Volley image loader, if null will be created.
     */
    public ImageLoader getImageLoader(){
        if (mImageLoader == null){
            mImageLoader = new ImageLoader(getRequestQueue(), new ImageLoader.ImageCache() {
                private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(IMAGE_CACHE_SIZE);

                public void putBitmap(String url, Bitmap bitmap) {
                    mCache.put(url, bitmap);
                }

                public Bitmap getBitmap(String url) {
                    return mCache.get(url);
                }
            });
        }
        return mImageLoader;
    }

    /**
     * Adds the specified request to the global queue, if tag is specified
     * then it is used else Default TAG is used.
     *
     * @param req
     * @param tag
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

        VolleyLog.d("Adding request to queue: %s", req.getUrl());

        getRequestQueue().add(req);
    }

    /**
     * Adds the specified request to the global queue using the Default TAG.
     *
     * @param req
     */
    public <T> void addToRequestQueue(Request<T> req) {
        // set the default tag if tag is empty
        req.setTag(TAG);

        getRequestQueue().add(req);
    }

    /**
     * Cancels all pending requests by the specified TAG, it is important
     * to specify a TAG so that the pending/ongoing requests can be cancelled.
     *
     * @param tag
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }






}
