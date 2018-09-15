package com.ticktalk.translateto.fragments;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.danielstone.materialaboutlibrary.MaterialAboutFragment;
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.items.MaterialAboutItemOnClickAction;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ticktalk.helper.Constant;
import com.ticktalk.helper.Helper;
import com.ticktalk.translateto.App;
import com.ticktalk.translateto.activities.MainActivity;
import com.ticktalk.translateto.activities.MoreAppActivity;
import com.ticktalk.translateto.activities.LoginActivity;
import com.ticktalk.translateto.activities.ProfileActivity;
import com.ticktalk.translateto.utils.Utils;
import com.ticktalk.translateto.activities.HumanTranslationActivity;
import com.ticktalk.translateto.database.SessionPrefs;
import com.ticktalk.translateto.remote.DefaultExclusionStrategy;
import com.ticktalk.translateto.remote.model.LoginPojo;
import com.ticktalk.translateto.R;
import com.ticktalk.translateto.remote.sendmodel.SendLogin;


/**
 * Created by Rafael S. Martin
 */

public class SettingFragment extends MaterialAboutFragment implements Response.ErrorListener,
        Response.Listener<String> {

    public static final String TAG = "SETTING_FRAGMENT";

    private Context myContext;
    private boolean isLogged;
    private SendLogin sendLogin;
    private LoginPojo model;

    @Override
    protected MaterialAboutList getMaterialAboutList(Context context) {

        myContext = context;
        isLogged = SessionPrefs.get(myContext).isLoggedIn();

        int titleColor = ContextCompat.getColor(myContext, R.color.colorPrimary);

        MaterialAboutCard.Builder generalCard = new MaterialAboutCard.Builder();
        generalCard.title("General");
        generalCard.titleColor(titleColor);

        generalCard.addItem(new MaterialAboutActionItem.Builder()
                .text(R.string.human_translation)
                .icon(R.drawable.icon_human_translation_blue)
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {
                        if(isLogged){
                            Utils.launchActivity(HumanTranslationActivity.class, getActivity());
                        } else{
                            LoginActivity.startLoginActivity(getActivity());
                        }
                    }
                })
                .build());

        if(isLogged){
            generalCard.addItem(new MaterialAboutActionItem.Builder()
                    .text(R.string.profile)
                    .icon(R.drawable.icon_profile)
                    .setOnClickAction(new MaterialAboutItemOnClickAction() {
                        @Override
                        public void onClick() {
                            ProfileActivity.startProfileActivity(getActivity());
                        }
                    })
                    .build());
            generalCard.addItem(new MaterialAboutActionItem.Builder()
                    .text(R.string.log_out)
                    .icon(R.drawable.icon_logout)
                    .setOnClickAction(new MaterialAboutItemOnClickAction() {
                        @Override
                        public void onClick() {
                            sendLogOut();
                        }
                    })
                    .build());
        } else{
            generalCard.addItem(new MaterialAboutActionItem.Builder()
                    .text(R.string.login)
                    .icon(R.drawable.icon_login)
                    .setOnClickAction(new MaterialAboutItemOnClickAction() {
                        @Override
                        public void onClick() {
                            LoginActivity.startLoginActivity(getActivity());
                        }
                    })
                    .build());
        }


        MaterialAboutCard.Builder moreAppCard = new MaterialAboutCard.Builder();
        moreAppCard.title(R.string.more_app);
        moreAppCard.titleColor(titleColor);

        moreAppCard.addItem(new MaterialAboutActionItem.Builder()
                .text(R.string.camera_translator)
                .icon(R.drawable.icon_camera_translator)
                .subText(R.string.camera_translator_sub_text)
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {
                        Helper.launchAppOrPlayStore(getActivity(), Constant.PackageName.CAMERA_TRANSLATOR);
                    }
                })
                .build());

        moreAppCard.addItem(new MaterialAboutActionItem.Builder()
                .text(R.string.translate_voice)
                .icon(R.drawable.voice_icon)
                .subText(R.string.translate_voice_sub_text)
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {
                        Helper.launchAppOrPlayStore(getActivity(), Constant.PackageName.VOICE_TRANSLATOR_FREE);
                    }
                })
                .build());

        moreAppCard.addItem(new MaterialAboutActionItem.Builder()
                .text(R.string.multiple_translator)
                .icon(R.drawable.icon_multi_language)
                .subText(R.string.multiple_translator_sub_text)
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {
                        Helper.launchAppOrPlayStore(getActivity(), Constant.PackageName.MULTI_TRANSLATOR);
                    }
                })
                .build());

        moreAppCard.addItem(new MaterialAboutActionItem.Builder()
                .text(R.string.dictionary_app_name)
                .icon(R.drawable.icon_dictionary_with_background)
                .subText(R.string.dictionary_sub_text)
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {
                        Helper.launchAppOrPlayStore(getActivity(), Constant.PackageName.DICTIONARY);
                    }
                })
                .build());

        moreAppCard.addItem(new MaterialAboutActionItem.Builder()
                .text(R.string.see_more_app)
                .icon(R.drawable.icon_play_store)
                .subText(R.string.see_more_app_sub_text)
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {
                        MoreAppActivity.start(getActivity());
                    }
                })
                .build());

        MaterialAboutCard.Builder aboutCard = new MaterialAboutCard.Builder();
        aboutCard.title(R.string.about);
        aboutCard.titleColor(titleColor);

        aboutCard.addItem(new MaterialAboutActionItem.Builder()
                .text(R.string.website)
                .subText(R.string.website_link)
                .icon(R.drawable.logo)
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {
                        Utils.launchTranslateToWebsite(getActivity());
                    }
                })
                .build());

        aboutCard.addItem(new MaterialAboutActionItem.Builder()
                .text(R.string.contact_us)
                .subText(R.string.contact_us_sub_text)
                .icon(R.drawable.ic_email_orange_48dp)
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {
                        Utils.launchContactUs(getActivity(), getString(R.string.app_name));
                    }
                })
                .build());

        aboutCard.addItem(new MaterialAboutActionItem.Builder()
                .text(R.string.privacy_policy)
                .icon(R.drawable.ic_security_green_48dp)
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {
                        Utils.launchPrivacyPolicy(getActivity());
                    }
                })
                .build());

        return new MaterialAboutList(
                generalCard.build(),
                moreAppCard.build(),
                aboutCard.build()
        );
    }


    private void sendLogOut(){
        String email = SessionPrefs.get(myContext).getPrefEmail();
        String password = SessionPrefs.get(myContext).getPefPass();

        Log.d("LogOutEmail", email);
        Log.d("LogOutPass", password);

        sendLogin = new SendLogin(com.ticktalk.translateto.utils.Constant.url,
                com.ticktalk.translateto.utils.Constant.accessLogOut,
                null, null, email, password,
                com.ticktalk.translateto.utils.Constant.userAgent, null,
                "false", null);

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
            Log.e("ErrorResponse", "Error from server" + error.getMessage());
        } else if (error instanceof TimeoutError){
            Log.e( "ErrorResponse", "Error out of time" + error.getMessage());
        }
    }

    @Override
    public void onResponse (String response){
        GsonBuilder builder = new GsonBuilder();
        builder.setExclusionStrategies(new DefaultExclusionStrategy());
        Gson gson = builder.create();

        Log.d("LogOut1", response+"");
        model = gson.fromJson(response, LoginPojo.class);

        String received = model.getFindEnd();
        Log.d("LogOut2", received+"");

        if(received != null){
            if(model.getFindEnd().equals("200")){

                boolean mLoggedIn = SessionPrefs.get(myContext).isLoggedIn();
                Log.d("LogOut3", mLoggedIn+"" );

                SessionPrefs.get(myContext).logOut();
                mLoggedIn = SessionPrefs.get(myContext).isLoggedIn();
                Log.d("LogOut4", mLoggedIn+"" );

                Toast.makeText(myContext, "Log out success", Toast.LENGTH_SHORT).show();
                Utils.launchActivity(MainActivity.class, getActivity());
            }
            else if(model.getFindEnd().equals("201")){
                //Apaño hasta que el servidor funcione correctamente
                SessionPrefs.get(myContext).logOut();
                boolean mLoggedIn = SessionPrefs.get(myContext).isLoggedIn();
                Log.d("LogOut4", mLoggedIn+"" );

                Toast.makeText(myContext, "Log out success", Toast.LENGTH_SHORT).show();
                Utils.launchActivity(MainActivity.class, getActivity());
            }
            else{
                Toast.makeText(myContext, "Error interno", Toast.LENGTH_SHORT).show();
            }
        }

    }


}
