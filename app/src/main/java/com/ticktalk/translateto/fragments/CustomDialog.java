package com.ticktalk.translateto.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.StackingBehavior;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ticktalk.translateto.App;
import com.ticktalk.translateto.database.SessionPrefs;
import com.ticktalk.translateto.utils.Constant;
import com.ticktalk.translateto.remote.DefaultExclusionStrategy;
import com.ticktalk.translateto.remote.model.LoginPojo;
import com.ticktalk.translateto.R;
import com.ticktalk.translateto.remote.sendmodel.SendLogin;

import static com.ticktalk.translateto.utils.ArraySpinner.countryNames;

/**
 * Created by Rafael S. Martin
 */


public class CustomDialog extends DialogFragment implements
        Response.ErrorListener,
        Response.Listener<String>
{

    private String userAgent = "AndroidApi";

    private String fName;
    private String lName;
    private String email;
    private String pass, oldPass;
    private String confirmPass;
    private String text = "";
    private String textProfile;
    private String access;

    private static int modo;

    private SendLogin sendLogin;
    private Context context;

    private static String customTitleDialog, customTextDialog;


    public static void startCustomDialog(Activity startingActivity, String customTitle, String customText, String textProfile, int mode)
    {
        CustomDialog customDialog = new CustomDialog();
        customTitleDialog = customTitle;
        customTextDialog = customText+textProfile;
        modo = mode;
        customDialog.show(startingActivity.getFragmentManager(), "CustomDialog");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        context = getActivity();

        MaterialDialog customMaterialDialog = new MaterialDialog.Builder(getActivity())
                .title(customTitleDialog)
                .content(customTextDialog)
                .stackingBehavior(StackingBehavior.ADAPTIVE)
                .inputType(
                        InputType.TYPE_CLASS_TEXT
                                | InputType.TYPE_TEXT_VARIATION_PERSON_NAME
                                | InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .positiveText(R.string.custom_submit)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        sendText();
                    }
                })
                .alwaysCallInputCallback() // this forces the callback to be invoked with every input change
                .inputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME)
                .input(R.string.custom_input_hint, 0, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        if (input.toString().equalsIgnoreCase("")) {
                            dialog.setContent(customTextDialog);
                            dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
                            text = input.toString();
                            if (text.length()>3){
                                dialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);
                            } else{
                                dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
                            }
                        } else {
                            dialog.setContent(customTextDialog);
                            dialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);
                            text = input.toString();
                            if (text.length()>3){
                                dialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);
                            } else{
                                dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
                            }
                        }
                    }
                })
                .negativeText(R.string.custom_cancel)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Toast.makeText(getActivity(), "onNegative", Toast.LENGTH_SHORT).show();
                    }
                })
                .cancelable(false)
                .build();
        return customMaterialDialog;

    }

    //Se envian los datos al WebService
    private void sendText(){

        if (modo == 1){
            fName = text;
            lName = SessionPrefs.get(getActivity()).getPrefLname();
            pass = SessionPrefs.get(getActivity()).getPefPass();
            access = Constant.accessProfile;

        }else if(modo == 2){
            fName = SessionPrefs.get(getActivity()).getPrefFname();
            lName = text;
            pass = SessionPrefs.get(getActivity()).getPefPass();
            access = Constant.accessProfile;

        } else if (modo == 3){
            fName = SessionPrefs.get(getActivity()).getPrefFname();
            lName = SessionPrefs.get(getActivity()).getPrefLname();
            pass = text;
            access = Constant.accessChangePass;
        }
        email = SessionPrefs.get(getActivity()).getPrefEmail();
        oldPass = SessionPrefs.get(getActivity()).getPefPass();

        int posLenguajeNativo = SessionPrefs.get(getActivity()).getPrefNativeLanguage();
        String lenguajeNativo = countryNames[posLenguajeNativo];

        sendLogin = new SendLogin(Constant.url,
                access, fName, lName, email, oldPass,
                userAgent, lenguajeNativo,
                pass,null);
        Log.d("customDialog", Constant.accessProfile);
        Log.d("customDialogfName", fName);
        Log.d("customDialoglName", lName);
        Log.d("customDialogEmail", email);
        Log.d("customDialogPass", pass);
        Log.d("customDialogOldPass", oldPass);
        Log.d("customDialogText", text);
        Log.d("customDialogLNativo", lenguajeNativo);

        Request<?> request = sendLogin.getRequest(this, this);
        App.getInstance().addToRequestQueue(request, App.TAG);
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        if (error instanceof AuthFailureError){
            Log.e("ErrorResponse", "Error credentials" + error.getMessage());
        } else if(error instanceof NetworkError){
            Log.e("ErrorResponse", "Error network" + error.getMessage());
        } else if (error instanceof NoConnectionError){
            Log.e("ErrorResponse", "Error no conection" + error.getMessage());
        } else if (error instanceof ParseError){
            Log.e("ErrorResponse", "Error no process the response" + error.getMessage());
        } else if (error instanceof ServerError){
            Log.e("ErrorResponse", "Error form server" + error.getMessage());
        } else if (error instanceof TimeoutError){
            Log.e( "ErrorResponse", "Error out of time" + error.getMessage());
        }

    }

    @Override
    public void onResponse(String response) {

        GsonBuilder builder = new GsonBuilder();
        builder.setExclusionStrategies(new DefaultExclusionStrategy());
        Gson gson = builder.create();

        Log.d("customDialogResponse", response+"");

        LoginPojo model = gson.fromJson(response, LoginPojo.class);

        //La web
        String received = model.getFindEnd();
        Log.d("customDialogReceive", received+"");
//        Toast.makeText(getActivity(), received, Toast.LENGTH_SHORT).show();


        if(received != null){
            if(model.getFindEnd().equals("209")){
                Log.d("customDialog", "Email No encontrado o pass");
            } else if(model.getFindEnd().equals("200")){
                //Guardo la respuesta del login del servidor
                SessionPrefs.get(getActivity()).saveSessionLogin(model);
                Log.d("customDialog", "cambiado");
                // TODO Tb lanzar a pantalla mensaje de cambio realizado con exito
                Toast.makeText(context, "Cambio realizado con exito", Toast.LENGTH_SHORT).show();

                this.mListener.onCompleteCustomDialog(text, modo);

            } else if(model.getFindEnd().equals("206")){
                Log.d("customDialog", "user inactivo");
            } else if(model.getFindEnd().equals("301")){
                if(fName.equals(textProfile)){
                    Toast.makeText(context, "Cambio realizado con exito", Toast.LENGTH_SHORT).show();
                }else if(lName.equals(textProfile)){
                    Toast.makeText(context, "Cambio realizado con exito", Toast.LENGTH_SHORT).show();
                }else{
                    Log.d("customDialog", "Error interno, prueba mas adelante.");
                }
            }
        }


    }


    public  interface OnCompleteListener {
         void onCompleteCustomDialog(String text, int modo);
    }

    private OnCompleteListener mListener;

    // make sure the Activity implemented it
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnCompleteListener)activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }

}
