package com.ticktalk.translateto.usergestion.signup;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ticktalk.translateto.App;
import com.ticktalk.translateto.MainActivity;
import com.ticktalk.translateto.favorite.FavoriteActivity;
import com.ticktalk.translateto.history.HistoryActivity;
import com.ticktalk.translateto.usergestion.login.LoginActivity;
import com.ticktalk.translateto.utils.Constant;
import com.ticktalk.translateto.utils.Utils;
import com.ticktalk.translateto.sessionpref.SessionPrefs;
import com.ticktalk.translateto.setting.SettingActivity;
import com.ticktalk.translateto.spinner.CustomAdapter;
import com.ticktalk.translateto.webservicesvolley.DefaultExclusionStrategy;
import com.ticktalk.translateto.webservicesvolley.pojo.LoginPojo;
import com.ticktalk.translateto.R;
import com.ticktalk.translateto.webservicesvolley.sendpojo.SendLogin;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ticktalk.translateto.spinner.ArraySpinner.countryCodes;
import static com.ticktalk.translateto.spinner.ArraySpinner.countryNames;
import static com.ticktalk.translateto.spinner.ArraySpinner.flags;

/**
 * Created by Indogroup02 on 20/11/2017.
 */

public class _SignUpActivity extends AppCompatActivity implements
        Response.ErrorListener,
        Response.Listener<String>,
        View.OnClickListener,
        AdapterView.OnItemSelectedListener,
        Toolbar.OnMenuItemClickListener
{

        @BindView(R.id.toolbar)
        Toolbar toolbar;

        @BindView(R.id.ed_first_name)
        EditText edFirstName;

        @BindView(R.id.ed_last_name)
        EditText edLastName;

        @BindView(R.id.ed_email)
        EditText edEmail;

        @BindView(R.id.native_language_spinner)
        Spinner spinnerNativeLanguage;

        @BindView(R.id.ed_password)
        EditText edPassword;

        @BindView(R.id.ed_confirm_password)
        EditText edConfirmPassword;

        @BindView(R.id.button_sign_up)
        Button signUp;

        @BindView(R.id.link_login)
        TextView linkLogin;

        private static final String TAG = "SignUpActivity";

        private String firstName, lastName, email, pass, confirmPass, ultimoLogin;
        private SendLogin sendLogin;
        private Context myContext;
        private LoginPojo model;

        private Date currentDate;
        private boolean isSpinnerSelected = false;
        int posCurrentSelection;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ButterKnife.bind(this);

        myContext = getApplicationContext();

//        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
//        if(Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress()) != null)
//            ipAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());

        currentDate = Calendar.getInstance().getTime();
        ultimoLogin = currentDate.toString();

        initToolbar();
        initSpinner();
        initListeners();

    }


    private void initToolbar(){
        toolbar.inflateMenu(R.menu.menu_toolbar);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        toolbar.setTitle("SignUp");
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onBackPressed();
            }
        });
    }

    private void initSpinner(){
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
    }

    private void initListeners(){
        signUp.setOnClickListener(this);
        linkLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_sign_up:
                signUp();
                break;

            case R.id.link_login:
//                Utils.launchActivity(LoginForm.class, this);
                LoginActivity.startLoginActivity(this);
                break;

            default:
                break;
        }
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

    public void signUp() {
        Log.d(TAG, "Login");

        firstName = edFirstName.getText().toString();
        lastName = edLastName.getText().toString();
        email = edEmail.getText().toString();
        pass = edPassword.getText().toString();
        confirmPass = edConfirmPassword.getText().toString();

        if (!validate()) {
            onLoginFailed();
            return;
        }

        signUp.setEnabled(false);

        //Creo la ventana de progreso
        final ProgressDialog progressDialog = new ProgressDialog(_SignUpActivity.this,
                R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();



        // TODO: Implement your own authentication logic here.
        // Envio peticion a tranlateto.com
        sendText();

        //Lanzo un manejador con dos segundos de retardo para esperar la respuesta de Volley
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // En la llamada, completa ya sea onLoginSuccess o onLoginFailed
                        if(model.getFindEnd().equals("200")){
                            onLoginSuccess();
                            progressDialog.dismiss();
                        }else{
                            onLoginFailed();
                            progressDialog.cancel();
                        }
                    }
                }, 2000);
    }

        public void onLoginSuccess() {
            signUp.setEnabled(true);
            finish();
        }

        public void onLoginFailed() {
            Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
            signUp.setEnabled(true);
        }

        public boolean validate() {
            boolean valid = true;

            if(TextUtils.isEmpty(firstName) && TextUtils.isEmpty(lastName) && TextUtils.isEmpty(email)
                    && TextUtils.isEmpty(pass) && TextUtils.isEmpty(confirmPass) ){
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();
                valid = false;
            }
            else if(firstName.length()<4){
                Toast.makeText(this, R.string.form_fname, Toast.LENGTH_SHORT).show();
                valid = false;
            }
            else if(lastName.length()<4){
                Toast.makeText(this, R.string.form_lname, Toast.LENGTH_SHORT).show();
                valid = false;
            }
            else if(!Utils.isEmailValid(email)){
                Toast.makeText(this, R.string.form_email, Toast.LENGTH_SHORT).show();
                valid = false;
            }
            else if(pass.length()<6){
                Toast.makeText(this, R.string.form_pass, Toast.LENGTH_SHORT).show();
                valid = false;
            }
            else if(!pass.equals(confirmPass)){
                Toast.makeText(this, R.string.form_confirm_pass, Toast.LENGTH_SHORT).show();
                valid = false;
            }

            return valid;
        }


        /**
         *
         * Request WebServices Volley methods
         *
         * */

        private void sendText(){

            Log.d("SignActivity", firstName+"");
            Log.d("SignActivity", lastName+"");
            Log.d("SignActivity", email+"");
            Log.d("SignActivity", pass+"");
            Log.d("SignActivity", ultimoLogin+"");
            Log.d("SignActivity", countryNames[spinnerNativeLanguage.getSelectedItemPosition()]+"");


            sendLogin = new SendLogin(Constant.url, Constant.accessSignUp,
                    firstName, lastName, email, pass, Constant.userAgent,
                    countryNames[spinnerNativeLanguage.getSelectedItemPosition()],
                    "true_o_false", "premiun");

            Request<?> request = sendLogin.getRequest(this, this);
            App.getInstance().addToRequestQueue(request, App.TAG);

            Toast.makeText(this, "Registrando", Toast.LENGTH_SHORT).show();

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

            Log.d("SignActivity2", response+"");
            model = gson.fromJson(response, LoginPojo.class);

            //La web
            String received = model.getFindEnd();
            Log.d("SignActivity3", received+"");


            if(received!=null){
                if(received.equals("200")){
                    //Guardo la respuesta del login del servidor
                    SessionPrefs.get(this).saveSessionLogin(model);
                    SessionPrefs.get(this).saveSessionnativeLanguage(spinnerNativeLanguage.getSelectedItemPosition());
                    // Tb lanzar a pantalla de bienvenido
                    Utils.launchActivity(MainActivity.class, _SignUpActivity.this);
                    Toast.makeText(myContext, "Signin success", Toast.LENGTH_SHORT).show();
                }else if(received.equals("209")){
                    Toast.makeText(myContext, "Signin failed, repeated email or pass", Toast.LENGTH_SHORT).show();
                } else if (received.equals("206")){
                    Toast.makeText(myContext, "user inactive ", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(myContext, response+ " error interno", Toast.LENGTH_SHORT).show();

                }
            }


        }

        /**
         *
         * Spinner
         *
         * **/

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            // An item was selected. You can retrieve the selected item using
//         parent.getItemAtPosition(pos);

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

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

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

}
