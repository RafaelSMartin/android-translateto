package com.ticktalk.translateto.usergestion.login;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.StackingBehavior;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ticktalk.translateto.App;
import com.ticktalk.translateto.utils.Constant;
import com.ticktalk.translateto.webservicesvolley.DefaultExclusionStrategy;
import com.ticktalk.translateto.webservicesvolley.pojo.LoginPojo;
import com.ticktalk.translateto.R;
import com.ticktalk.translateto.webservicesvolley.sendpojo.SendLogin;

public class ForgotPassDialogFragment extends DialogFragment implements
        Response.ErrorListener,
        Response.Listener<String>
{

    private String userAgent = "AndroidApi";

    private String firstName="";
    private String lastName="";
    private String email;
    private String password="";
    private String confirmPass="";

    private SendLogin sendLogin;
    private Context context;


    public static void startCustomDialog(Activity startingActivity)
    {
        ForgotPassDialogFragment customDialog = new ForgotPassDialogFragment();
        customDialog.show(startingActivity.getFragmentManager(), "ForgotPassDialogFragment");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        context = getActivity();

             MaterialDialog forgotMaterialDialog = new MaterialDialog.Builder(getActivity())
                    .title(R.string.input)
                    .content(R.string.input_content_custominvalidation)
                    .stackingBehavior(StackingBehavior.ADAPTIVE)
                    .inputType(
                            InputType.TYPE_CLASS_TEXT
                                    | InputType.TYPE_TEXT_VARIATION_PERSON_NAME
                                    | InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                    .positiveText(R.string.submit)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            if (isEmailValid(email)){
                                sendText();
                            } else{

                            }
                        }
                    })
                    .alwaysCallInputCallback() // this forces the callback to be invoked with every input change
                    .inputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                    .input(R.string.input_hint, 0, new MaterialDialog.InputCallback() {
                        @Override
                        public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                            if (input.toString().equalsIgnoreCase("")) {
                                dialog.setContent(R.string.input_content_custominvalidation);
                                dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
                                email = input.toString();
                                if (isEmailValid(email)){
                                    dialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);
                                } else{
                                    dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
                                }
                            } else {
                                dialog.setContent(R.string.input_content_custominvalidation);
                                dialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);
                                email = input.toString();
                                if (isEmailValid(email)){
                                    dialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);
                                } else{
                                    dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
                                }
                            }
                        }
                    })
                    .negativeText(R.string.cancel)
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            Toast.makeText(getActivity(), "onNegative", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .cancelable(false)
                    .build();
            return forgotMaterialDialog;

    }

    //Se envian los datos al WebService
    private void sendText(){
        sendLogin = new SendLogin(Constant.url,
                Constant.accessForgotPass,null, null, email, null,
                userAgent, "es",
                "false",null);

        Log.d("forgotPassword", email);
        Request<?> request = sendLogin.getRequest(this, this);
        App.getInstance().addToRequestQueue(request, App.TAG);
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("ErrorResponse", error.getMessage()+"");

    }

    @Override
    public void onResponse(String response) {
        GsonBuilder builder = new GsonBuilder();
        builder.setExclusionStrategies(new DefaultExclusionStrategy());
        Gson gson = builder.create();

        Log.d("forgotPassword", response+"");
        LoginPojo model = gson.fromJson(response, LoginPojo.class);

        //La web
        String received = model.getFindEnd();
        Log.d("forgotPassword", received+"");

        if(received != null){
            if(model.getFindEnd().equals("209")){
                Log.d("forgotPassword", "Email No encontrado o pass");
            } else if(model.getFindEnd().equals("200")){
                Log.d("forgotPassword", "Email encontrado y enviado");
                // TODO Tb lanzar a pantalla mensaje de password enviado al email
                Toast.makeText(context, R.string.toast_send_pass, Toast.LENGTH_SHORT).show();
            }
        }


    }

    private boolean isEmailValid(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }


}
