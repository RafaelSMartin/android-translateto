package com.ticktalk.translateto.accountmanager;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;

import com.ticktalk.translateto.usergestion.login._LoginForm;
import com.ticktalk.translateto.R;


public class CustomAuthenticator extends AbstractAccountAuthenticator{

    private Context context;
    private String YOUR_ACCOUNT_TYPE = "com.ticktalk.translateto";

    public CustomAuthenticator(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse accountAuthenticatorResponse, String s) {
        throw new UnsupportedOperationException();
    }


    @Override
    public Bundle addAccount (AccountAuthenticatorResponse response,
                              String accountType, String authTokenType,
                              String[] requiredFeatures, Bundle options) throws NetworkErrorException  {

        AccountManager am = AccountManager.get(context);
        if (ActivityCompat.checkSelfPermission(context,
                android.Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            // Checking to see if you can have a look at accounts present on the device.
            Log.d("Authenticator", "GET_ACCOUNTS not present.");
        }

        if (UserAccountUtil.getAccount(context) != null) {
            // This means there's an account present already. If you don't want to support multiple accounts, keep this.
            // This is how you report an error occurred.
            final Bundle result = new Bundle();

            result.putInt(AccountManager.KEY_ERROR_CODE, 400);
            result.putString(AccountManager.KEY_ERROR_MESSAGE, context.getResources().getString(R.string.one_account_allowed));

            return result;
        }


        final Intent intent = new Intent(context, _LoginForm.class);

        // This key can be anything. Try to use your domain/package
        intent.putExtra(YOUR_ACCOUNT_TYPE, accountType);

        // This key can be anything too. It's just a way of identifying the token's type (used when there are multiple permissions)
        intent.putExtra("full_access", authTokenType);

        // This key can be anything too. Used for your reference. Can skip it too.
        intent.putExtra("is_adding_new_account", true);

        // Copy this exactly from the line below.
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);

        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);

        return bundle;

    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, Bundle bundle) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response,
                               Account account, String authTokenType, Bundle bundle) throws NetworkErrorException {

        AccountManager am = AccountManager.get(context);

        String authToken = am.peekAuthToken(account, authTokenType);

        if(TextUtils.isEmpty(authToken)){
//            authToken = HTTPNetwork.login(account.name, am.getPassword(account));
        }

        if(!TextUtils.isEmpty(authToken)){
            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
            return result;
        }

        // If you reach here, person needs to login again. or sign up

        // If we get here, then we couldn't access the user's password - so we
        // need to re-prompt them for their credentials. We do that by creating
        // an intent to display our AuthenticatorActivity which is the AccountsActivity in my case.
        final Intent intent = new Intent(context, _LoginForm.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        intent.putExtra(YOUR_ACCOUNT_TYPE, account.type);
        intent.putExtra("full_access", authTokenType);

        Bundle retBundle = new Bundle();
        retBundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return retBundle;
    }

    @Override
    public String getAuthTokenLabel(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String s, Bundle bundle) throws NetworkErrorException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String[] strings) throws NetworkErrorException {
        throw new UnsupportedOperationException();
    }

    // Handle a user logging out here.
    @Override
    public Bundle getAccountRemovalAllowed(AccountAuthenticatorResponse response, Account account) {
        try {
            return super.getAccountRemovalAllowed(response, account);
        } catch (NetworkErrorException e) {
            return null;
        }
    }


}
