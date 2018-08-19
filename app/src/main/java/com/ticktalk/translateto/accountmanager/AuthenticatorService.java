package com.ticktalk.translateto.accountmanager;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;


public class AuthenticatorService extends Service {

    // Instance field that stores the authenticator object
    // Notice, this is the same Authenticator class we defined earlier
    private CustomAuthenticator mAuthenticator;

    @Override
    public void onCreate(){
        // Create a new authenticator object
        mAuthenticator = new CustomAuthenticator(this);
    }

    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
