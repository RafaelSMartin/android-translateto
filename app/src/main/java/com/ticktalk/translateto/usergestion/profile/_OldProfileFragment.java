package com.ticktalk.translateto.usergestion.profile;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;


import com.danielstone.materialaboutlibrary.MaterialAboutFragment;
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.items.MaterialAboutItemOnClickAction;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;
import com.ticktalk.translateto.sessionpref.SessionPrefs;
import com.ticktalk.translateto.webservicesvolley.pojo.LoginPojo;
import com.ticktalk.translateto.R;

import static com.ticktalk.helper.Helper.openUrl;
/**
 * Created by Indogroup02 on 04/12/2017.
 */

public class _OldProfileFragment extends MaterialAboutFragment
{


    public static final String TAG = "PROFILE_FRAGMENT";
    private Context myContext;
    private boolean isLogged;
    private LoginPojo model;
    private String fname, lname, email, nLanguage, oldPass, newPass, confirmPass;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createCardList();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        createCardList();
        getView().setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));

    }

    @Override
    protected MaterialAboutList getMaterialAboutList(Context context) {

        myContext = context;

        isLogged = SessionPrefs.get(myContext).isLoggedIn();

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if(isLogged){
            fname = SessionPrefs.get(myContext).getPrefFname();
            lname = SessionPrefs.get(myContext).getPrefLname();
            email = SessionPrefs.get(myContext).getPrefEmail();
            oldPass = SessionPrefs.get(myContext).getPefPass();
        } else{
            fname = getResources().getString(R.string.first_name);
            lname = getResources().getString(R.string.last_name);
            email = getResources().getString(R.string.email);
            oldPass = getResources().getString(R.string.old_password);
        }

        int titleColor;
        try {
            titleColor = ContextCompat.getColor(getActivity(), R.color.colorPrimary);
        }
        catch(NullPointerException e) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                titleColor = getContext().getColor(R.color.colorPrimary);
            }
            else {
                titleColor = getResources().getColor(R.color.colorPrimary);
            }
        }

        MaterialAboutCard.Builder generalCard = new MaterialAboutCard.Builder();
        generalCard.title("PERSONAL INFO");
        generalCard.titleColor(titleColor);

        generalCard.addItem(new MaterialAboutActionItem.Builder()
                .text(fname)
                .icon(R.drawable.profile_name)
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {
                        CustomDialog.startCustomDialog(getActivity(), "Change First Name", getString(R.string.custom_input) + " " , fname, 1);
//                        openLink(getActivity());

                        updateProfile();

                    }
                })
                .build());

        generalCard.addItem(new MaterialAboutActionItem.Builder()
                .text(lname)
                .icon(R.drawable.profile_name)
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {
                        CustomDialog.startCustomDialog(getActivity(), "Change Last Name", getString(R.string.custom_input) + " " , lname, 2);
//                        CustomDialog customDialog = new CustomDialog();
//                        customDialog.setTargetFragment(customDialog, DIALOG_FRAGMENT);
//                        customDialog.show(getActivity().getFragmentManager(), "CustomDialog");


                    }
                })
                .build());

        generalCard.addItem(new MaterialAboutActionItem.Builder()
                .text(email)
                .icon(R.drawable.profile_email)
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {
                        Toast.makeText(myContext, "No disponible cambio de email", Toast.LENGTH_SHORT).show();
                    }
                })
                .build());

        MaterialAboutCard.Builder nativeCard = new MaterialAboutCard.Builder();
        nativeCard.title("NATIVE LANGUAGE");
        nativeCard.titleColor(titleColor);

        nativeCard.addItem(new MaterialAboutActionItem.Builder()
                .text(R.string.native_language)
                .icon(R.drawable.profile_native_language)
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {

                    }
                })
                .build());

        MaterialAboutCard.Builder passwordCard = new MaterialAboutCard.Builder();
        passwordCard.title("CHANGE PASSWORD");
        passwordCard.titleColor(titleColor);

        passwordCard.addItem(new MaterialAboutActionItem.Builder()
                .text(oldPass)
                .icon(R.drawable.profile_password)
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {

                    }
                })
                .build());

        passwordCard.addItem(new MaterialAboutActionItem.Builder()
                .text(R.string.new_password)
                .icon(R.drawable.profile_password)
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {

                    }
                })
                .build());

        passwordCard.addItem(new MaterialAboutActionItem.Builder()
                .text(R.string.confirm_password)
                .icon(R.drawable.profile_password)
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {

                    }
                })
                .build());


        return new MaterialAboutList(
                generalCard.build(),
                nativeCard.build(),
                passwordCard.build()
        );
    }

    public void openLink(Activity activity){
        String url = activity.getString(R.string.url_profile);
        String websiteName = activity.getString(R.string.profile);
        openUrl(activity, websiteName, url);
    }

    public final void updateProfile(){
        createCardList();
        if(isLogged){
            fname = SessionPrefs.get(myContext).getPrefFname();
            lname = SessionPrefs.get(myContext).getPrefLname();
            email = SessionPrefs.get(myContext).getPrefEmail();
            oldPass = SessionPrefs.get(myContext).getPefPass();
        } else{
            fname = getResources().getString(R.string.first_name);
            lname = getResources().getString(R.string.last_name);
            email = getResources().getString(R.string.email);
            oldPass = getResources().getString(R.string.old_password);
        }
        createCardList();
    }





}