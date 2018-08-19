package com.ticktalk.translateto.usergestion.profile;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ticktalk.translateto.favorite.FavoriteActivity;
import com.ticktalk.translateto.history.HistoryActivity;
import com.ticktalk.translateto.sessionpref.SessionPrefs;
import com.ticktalk.translateto.setting.SettingActivity;
import com.ticktalk.translateto.spinner.CustomAdapter;
import com.ticktalk.translateto.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ticktalk.translateto.spinner.ArraySpinner.countryCodes;
import static com.ticktalk.translateto.spinner.ArraySpinner.countryNames;
import static com.ticktalk.translateto.spinner.ArraySpinner.flags;

/**
 * Created by Rafael S. Martin
 */

public class _OldDoneProfileActivity extends AppCompatActivity implements
        View.OnClickListener,
        AdapterView.OnItemSelectedListener,
        Toolbar.OnMenuItemClickListener,
        CustomDialog.OnCompleteListener
{

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.layout_profile_first_name)
    LinearLayout layoutFirstName;

    @BindView(R.id.profile_first_name)
    TextView edFirstName;

    @BindView(R.id.layout_profile_last_name)
    LinearLayout layoutLastName;

    @BindView(R.id.profile_last_name)
    TextView edLastName;

    @BindView(R.id.layout_profile_email)
    LinearLayout layoutEmail;

    @BindView(R.id.profile_email)
    TextView edEmail;

    @BindView(R.id.layout_profile_native_language)
    RelativeLayout layoutNativeLanguage;

    @BindView(R.id.native_language_spinner)
    Spinner spinnerNativeLanguage;

    @BindView(R.id.layout_profile_old_password)
    LinearLayout layoutOldPassword;

    @BindView(R.id.profile_old_password)
    TextView edOldPassword;

    private String firstName, lastName, email, oldPass;
    private Context myContext;
    private boolean isLogged;
    private boolean isSpinnerSelected = false;
    int posCurrentSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);

        myContext = getApplicationContext();

        isLogged = SessionPrefs.get(myContext).isLoggedIn();

        if(isLogged){
            firstName = SessionPrefs.get(myContext).getPrefFname();
            lastName = SessionPrefs.get(myContext).getPrefLname();
            email = SessionPrefs.get(myContext).getPrefEmail();
            oldPass = SessionPrefs.get(myContext).getPefPass();
        } else{
            firstName = getResources().getString(R.string.first_name);
            lastName = getResources().getString(R.string.last_name);
            email = getResources().getString(R.string.email);
            oldPass = getResources().getString(R.string.old_password);
        }

        edFirstName.setText(firstName);
        edLastName.setText(lastName);
        edEmail.setText(email);

        initToolbar();
        initListeners();

    }

    public void initToolbar(){
        toolbar.inflateMenu(R.menu.menu_toolbar);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        toolbar.setTitle("ProfileActivity");
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onBackPressed();
            }
        });
    }

    public void initListeners(){
        layoutFirstName.setOnClickListener(this);
        layoutLastName.setOnClickListener(this);
        layoutEmail.setOnClickListener(this);
        layoutNativeLanguage.setOnClickListener(this);

        //initspinner
        CustomAdapter customAdapter=new CustomAdapter(getApplicationContext(), flags, countryNames, countryCodes, true);
        spinnerNativeLanguage.setAdapter(customAdapter);

        if (SessionPrefs.get(myContext).getPrefNativeLanguage() != 0){
            spinnerNativeLanguage.setSelection(SessionPrefs.get(myContext).getPrefNativeLanguage());
        } else{
            spinnerNativeLanguage.setSelection(0);
        }
        posCurrentSelection = SessionPrefs.get(myContext).getPrefNativeLanguage();

        //initListener spinner
        spinnerNativeLanguage.setOnItemSelectedListener(this);
        if (SessionPrefs.get(myContext).getPrefNativeLanguage() != 0){
            spinnerNativeLanguage.setSelection(SessionPrefs.get(myContext).getPrefNativeLanguage());
        } else{
            spinnerNativeLanguage.setSelection(0);

        }
        layoutOldPassword.setOnClickListener(this);

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                SettingActivity.startSettingActivity(this);
                break;

            case R.id.history:
                HistoryActivity.startHistoryActivity(this);
                break;

            case R.id.favorite:
                FavoriteActivity.startFavoriteActivity(this);
                break;

        }
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_profile_first_name:
                CustomDialog.startCustomDialog(this, "Change First Name", getString(R.string.custom_input) + " " , firstName, 1);
                break;

            case R.id.layout_profile_last_name:
                CustomDialog.startCustomDialog(this, "Change Last Name", getString(R.string.custom_input) + " " , lastName, 2);
                break;

            case R.id.layout_profile_old_password:
                CustomDialog.startCustomDialog(this, "Change old Password", getString(R.string.custom_input) + " " , oldPass, 3);
                break;

            default:
                break;
        }
    }


    /**
     *
     * Spinner
     *
     * **/
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // Para saber si he cambiado el spinner
        if (posCurrentSelection != pos){
            isSpinnerSelected = true;
        }
        posCurrentSelection = pos;

        String posNativeLanguage = spinnerNativeLanguage.getSelectedItemPosition()+""; // es igual a pos
        Log.d("spinnerProfile1", pos+"");
        Log.d("spinnerProfile2", posNativeLanguage);

        //Recojo el string con la posicion de los spinners
        String sendSpinnerNativeLanguage = countryNames[spinnerNativeLanguage.getSelectedItemPosition()];
        Log.d("spinnerProfile3", sendSpinnerNativeLanguage);

        //Guardo la pos del spinner
        SessionPrefs.get(myContext).saveSessionnativeLanguage(spinnerNativeLanguage.getSelectedItemPosition());

        if (SessionPrefs.get(myContext).getPrefNativeLanguage() != 0){
            spinnerNativeLanguage.setSelection(spinnerNativeLanguage.getSelectedItemPosition());
            Log.d("spinnerProfile4", spinnerNativeLanguage.getSelectedItemPosition()+"");
        } else{
            spinnerNativeLanguage.setSelection(0);
        }

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        if(isSpinnerSelected){
            //Enviar a server
            Toast.makeText(this, "save native language", Toast.LENGTH_SHORT).show();
        }
        isSpinnerSelected = false;

    }


    /**
     * Interface del Listener de CustomDialog
     *
     **/

    @Override
    public void onCompleteCustomDialog(String text, int modo) {
//        Toast.makeText(myContext, "onCompleteDialog" + text, Toast.LENGTH_SHORT).show();
        if (modo == 1){
            firstName = text;
            edFirstName.setText(text);
        } else if(modo == 2){
            lastName = text;
            edLastName.setText(text);
        } else if (modo == 3){
            oldPass = text;
            edOldPassword.setText(text);
        }
    }


}
