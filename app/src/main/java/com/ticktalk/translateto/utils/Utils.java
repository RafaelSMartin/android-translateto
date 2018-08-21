package com.ticktalk.translateto.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.ticktalk.translateto.fragments.ForgotPassDialogFragment;
import com.ticktalk.translateto.database.SessionPrefs;
import com.ticktalk.translateto.R;

import java.util.Locale;
import java.util.StringTokenizer;

import github.nisrulz.easydeviceinfo.base.EasyAppMod;
import github.nisrulz.easydeviceinfo.base.EasyDeviceMod;

/**
 * Created by Rafael S. Martin
 */

public class Utils extends AppCompatActivity {

    public static void launchActivity(Class clase, Activity activity){
        Intent i = new Intent(activity, clase);
        activity.startActivity(i);
    }

    public static void launchActivityNewTask(Class clase, Activity activity){
        Intent i = new Intent(activity, clase);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(i);
    }

    public static void dialogForgot(Activity activity){
        ForgotPassDialogFragment forgotPassDialogFragment = new ForgotPassDialogFragment();
        forgotPassDialogFragment.show(activity.getFragmentManager(), "ForgotPassDialogFragment");
    }

    public static boolean isEmailValid(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    public static void launchActivityLoggedIn(Class clase, Activity activity){
        Log.d("SessionPrefs", SessionPrefs.get(activity).isLoggedIn()+"" );

        if(SessionPrefs.get(activity).isLoggedIn()){
            activity.startActivity(new Intent(activity, clase)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
            activity.finish();
            return;
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        if (activity != null) {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (activity.getCurrentFocus() != null && inputManager != null) {
                inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
                inputManager.hideSoftInputFromInputMethod(activity.getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    public static void hideSoftKeyboard(View view) {
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager != null) {
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public static void hideSoftKeyboardOutEditText(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static void setClipboard(Context context, String text) {
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
            Log.d("MainSendCopy", clip.toString()+"");
        }
    }

    public static void launchShare(View view, String text, Activity activity){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        //String shareBody = "This is a  wonderful app for learning English";
//        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "FusionApp In Use And Test");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
        activity.startActivity(Intent.createChooser(sharingIntent, "Share translateTo"));
        return;
    }

    public static void openUrl(Activity activity, String websiteName, String url){
        Intent browserIntent = new Intent(Intent.createChooser(new Intent("android.intent.action.VIEW", Uri.parse(url)), websiteName));
        activity.startActivity(browserIntent);
    }

    public static void launchTranslateToWebsite(Activity activity) {
        String url = activity.getString(R.string.website_link);
        String websiteName = activity.getString(R.string.translate_to);
        openUrl(activity, websiteName, url);
    }

    public static void launchPrivacyPolicy(Activity activity) {
        String url = activity.getString(R.string.privacy_policy_url);
        String websiteName = activity.getString(R.string.privacy_policy);
        openUrl(activity, websiteName, url);
    }

    public static void launchContactUs(Activity activity, String appName) {
        String message = "";
        EasyAppMod easyAppMod = new EasyAppMod(activity);
        EasyDeviceMod easyDeviceMod = new EasyDeviceMod(activity);
        message = message + "************* Please don't remove this information ****************";
        message = message + "\nDevice information";
        message = message + "\n\n" + easyAppMod.getAppName() + ", Version: " + easyAppMod.getAppVersion();
        message = message + "\nDevice: " + easyDeviceMod.getDevice();
        message = message + "\nManufacturer: " + easyDeviceMod.getManufacturer();
        message = message + "\nOS Version: " + easyDeviceMod.getOSVersion();
        message = message + "\nOS Codename: " + easyDeviceMod.getOSCodename();
        message = message + "\n*****************************\n\n";
        Intent emailIntent = new Intent("android.intent.action.SENDTO", Uri.parse("mailto:" + activity.getString(R.string.contact_us_link)));
        emailIntent.putExtra("android.intent.extra.SUBJECT", appName);
        emailIntent.putExtra("android.intent.extra.TEXT", message);
        activity.startActivity(Intent.createChooser(emailIntent, "Send email"));
    }


    public static boolean isConnection(ConnectivityManager conMgr){

        if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ) {
            // notify user you are online
            return true;
        }
        else {
            // notify user you are not online
            return false;
        }
    }

    public static String buscarCountryCodeUpper(String toLanguage){

        String[] isoCountryCodes = Locale.getISOLanguages();
        String countryName = "US";

        for (String countryCode : isoCountryCodes) {
            Locale locale = new Locale(toLanguage, countryCode);
            countryName = locale.getDisplayName();
            Log.d("COUNTRY", countryName);

        }
        return  countryName;
    }


    public static void playRotateSwapAnimation(Context context, ImageView iv)
    {
        Animation swap = AnimationUtils.loadAnimation(context, R.anim.rotate_swap_button);
        iv.startAnimation(swap);
    }

    public static void clearText(EditText texto) {
        texto.getText().clear();
    }

    public static String clearFirtsAndLast(String datosArray){

        datosArray = datosArray.substring(1);
        datosArray = datosArray.substring(0, datosArray.length() - 1);

        return datosArray;
    }

    public static Boolean isPar(int numero) {
        Boolean par;
        if (numero % 2 == 0) {
           par = true;
        } else {
            par = false;
        }
        return par;
    }

    public static int contardorPalabras(String contador){
        //Cuento las palabras y si es una quito espacios finales y setto unaPalabra a true
        StringTokenizer cantidadNombres = new StringTokenizer(contador, " ");
        int contadorNombres = 0;
        while (cantidadNombres.hasMoreTokens()) {
            contadorNombres++;
            cantidadNombres.nextToken();
        }
        Log.d("ContadorDePalabras", String.valueOf(contadorNombres));
        return  contadorNombres;
    }


}
