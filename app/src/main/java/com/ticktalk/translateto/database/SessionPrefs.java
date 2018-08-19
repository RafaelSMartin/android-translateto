package com.ticktalk.translateto.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.ticktalk.translateto.remote.model.LoginPojo;

/**
 * Created by Rafael S. Martin
 */

public class SessionPrefs {

    private static SessionPrefs INSTANCE;

    public static final String PREF_EMAIL = "PREF_EMAIL";
    public static final String PREF_FIRST_NAME = "PREF_FIRST_NAME";
    public static final String PREF_LAST_NAME = "PREF_LAST_NAME";
    public static final String PREF_PASS = "PREF_PASS";
    public static final String PREF_SESSION = "PREF_TOKEN";
    public static final String PREF_NATIVE_LANGUAGE = "NATIVE_LANGUAGE";

    private String email, password, fName, lName;
    private int nLanguage;

    private final SharedPreferences mPrefsSession, mPrefsEmail, mPrefsPass, mPrefFirstName, mPrefLastName, mPrefNativeLanguage;
    private boolean mIsLoggedIn = false;

    public static SessionPrefs get(Context context){
        if (INSTANCE == null){
            INSTANCE = new SessionPrefs(context);
        }
        return INSTANCE;
    }

    private SessionPrefs(Context context){
        mPrefsSession = context.getApplicationContext().getSharedPreferences(PREF_EMAIL, Context.MODE_PRIVATE);
        mPrefFirstName = context.getApplicationContext().getSharedPreferences(PREF_FIRST_NAME, Context.MODE_PRIVATE);
        mPrefLastName = context.getApplicationContext().getSharedPreferences(PREF_LAST_NAME, Context.MODE_PRIVATE);
        mPrefsEmail = context.getApplicationContext().getSharedPreferences(PREF_EMAIL, Context.MODE_PRIVATE);
        mPrefsPass = context.getApplicationContext().getSharedPreferences(PREF_PASS, Context.MODE_PRIVATE);
        mPrefNativeLanguage = context.getApplicationContext().getSharedPreferences(PREF_NATIVE_LANGUAGE, Context.MODE_PRIVATE);

        mIsLoggedIn = !TextUtils.isEmpty(mPrefsSession.getString(PREF_EMAIL, null));
        fName = mPrefFirstName.getString(PREF_FIRST_NAME, "?");
        lName = mPrefLastName.getString(PREF_LAST_NAME, "?");
        email = mPrefsEmail.getString(PREF_EMAIL, "?");
        password = mPrefsPass.getString(PREF_PASS, "?");
        nLanguage = mPrefNativeLanguage.getInt(PREF_NATIVE_LANGUAGE, 0);
    }

    public void saveSessionLogin(LoginPojo login){
        if(login != null){
            SharedPreferences.Editor editorSession = mPrefsSession.edit();
            editorSession.putString(PREF_SESSION, login.getSesion());

            SharedPreferences.Editor editorFName = mPrefFirstName.edit();
            editorFName.putString(PREF_FIRST_NAME, login.getFirstName());

            SharedPreferences.Editor editorLName = mPrefLastName.edit();
            editorLName.putString(PREF_LAST_NAME, login.getLastName());

            SharedPreferences.Editor editorEmail = mPrefsEmail.edit();
            editorEmail.putString(PREF_EMAIL, login.getEmail());

            SharedPreferences.Editor editorPass = mPrefsPass.edit();
            editorPass.putString(PREF_PASS, login.getPass());


            editorSession.apply();
            editorFName.apply();
            editorLName.apply();
            editorEmail.apply();
            editorPass.apply();

            mIsLoggedIn = true;

            this.fName = login.getFirstName();
            this.lName = login.getLastName();
            this.email = login.getEmail();
            this.password = login.getPass();
        }
    }

    public void saveSessionnativeLanguage (int posNativeLanguage){

        SharedPreferences.Editor editorNativeLanguage = mPrefNativeLanguage.edit();
        editorNativeLanguage.putInt(PREF_NATIVE_LANGUAGE, posNativeLanguage);

        editorNativeLanguage.apply();

        this.nLanguage = posNativeLanguage;

    }


    public void logOut(){
        mIsLoggedIn = false;
        SharedPreferences.Editor editorSession = mPrefsSession.edit();
        editorSession.putString(PREF_SESSION, null);

        SharedPreferences.Editor editorFName = mPrefFirstName.edit();
        editorFName.putString(PREF_FIRST_NAME, null);

        SharedPreferences.Editor editorLName = mPrefLastName.edit();
        editorLName.putString(PREF_LAST_NAME, null);

        SharedPreferences.Editor editorEmail = mPrefsEmail.edit();
        editorEmail.putString(PREF_EMAIL, null);

        SharedPreferences.Editor editorPass = mPrefsPass.edit();
        editorPass.putString(PREF_PASS, null);

        SharedPreferences.Editor editorNativeLanguage = mPrefNativeLanguage.edit();
        editorNativeLanguage.putInt(PREF_NATIVE_LANGUAGE, 0);

        nLanguage = 0;

        editorSession.apply();
        editorFName.apply();
        editorLName.apply();
        editorEmail.apply();
        editorPass.apply();
        editorNativeLanguage.apply();

    }
    public boolean isLoggedIn(){
        return mIsLoggedIn;
    }

    public String getPrefEmail(){
        return email;
    }

    public String getPefPass(){
        return password;
    }

    public String getPrefFname() {
        return fName;
    }

    public String getPrefLname() {
        return lName;
    }

    public int getPrefNativeLanguage(){ return nLanguage; }
}
