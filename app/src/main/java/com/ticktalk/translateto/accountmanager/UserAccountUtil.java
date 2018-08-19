package com.ticktalk.translateto.accountmanager;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by Rafael S. Martin
 */

public class UserAccountUtil {



    public static Account getAccount(Context context) {
        if (ActivityCompat.checkSelfPermission(context,
                android.Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "GET_ACCOUNTS not present.");
        }

        AccountManager accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccountsByType(AccountConstants.ACCOUNT_TYPE);
        if (accounts.length > 0) {
            return accounts[0];
        } else {
            return null;
        }
    }

}
