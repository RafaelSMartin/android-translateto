package com.ticktalk.moreapplib;

import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by indogroup on 9/7/17.
 */

public class MoreAppInitializer {

    public static void init(Context context)
    {
        Fresco.initialize(context);
    }
}
