package com.ticktalk.translateto.webservicesvolley;

import com.android.volley.DefaultRetryPolicy;

/**
 *Nos permite definir un tiempo de espera en ms y una cantidad de intentos si la conexion es lenta
 *
 */

public class LongTimeoutAndTryRetryPolicy extends DefaultRetryPolicy {

    public static final int TIMEOUT_MS = 2000;
    public static final int RETRIES_PHONE_ISP = 3;

    public LongTimeoutAndTryRetryPolicy(int retries) {
        super(TIMEOUT_MS, retries, DEFAULT_BACKOFF_MULT);
    }
}