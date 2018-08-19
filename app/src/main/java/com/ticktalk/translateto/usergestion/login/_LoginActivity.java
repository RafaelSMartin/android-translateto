package com.ticktalk.translateto.usergestion.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ticktalk.translateto.App;
import com.ticktalk.translateto.MainActivity;
import com.ticktalk.translateto.usergestion.signup._SignUpActivity;
import com.ticktalk.translateto.utils.Constant;
import com.ticktalk.translateto.utils.Utils;
import com.ticktalk.translateto.sessionpref.SessionPrefs;
import com.ticktalk.translateto.humantranslation.HumanTranslationActivity;
import com.ticktalk.translateto.setting.SettingActivity;
import com.ticktalk.translateto.webservicesvolley.DefaultExclusionStrategy;
import com.ticktalk.translateto.webservicesvolley.pojo.LoginPojo;
import com.ticktalk.translateto.R;
import com.ticktalk.translateto.webservicesvolley.sendpojo.SendLogin;

import butterknife.BindView;
import butterknife.ButterKnife;


public class _LoginActivity extends AppCompatActivity implements
        Response.ErrorListener,
        Response.Listener<String>,
        View.OnClickListener,
        Toolbar.OnMenuItemClickListener
    {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.login)
    Button logIn;

    @BindView(R.id.forgot_pass)
    TextView forgotPass;

    @BindView(R.id.sign_up)
    Button signUp;

    @BindView(R.id.ed_email_id)
    EditText user;

    @BindView(R.id.ed_pass)
    EditText pass;

    private String password, email;
    private String access = "Login";
    private String userAgent = "AndroidApi";
    private SendLogin sendLogin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._activity_login);

        ButterKnife.bind(this);

        //TODO dont forgot remove
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          Utils.launchActivity(HumanTranslationActivity.class, _LoginActivity.this);
            }
        });

        initToolbar();
        initListeners();

        user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(user.getText())){
                    email = user.getText().toString();
                }
                if(email.length() != 0 && Utils.isEmailValid(email)){
                    logIn.setEnabled(true);
                } else{
                    logIn.setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });



    }

    private void initToolbar(){
        toolbar.inflateMenu(R.menu.menu_toolbar);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onBackPressed();
            }
        });
    }

    private void initListeners(){
        logIn.setOnClickListener(this);
        logIn.setEnabled(false);


        signUp.setOnClickListener(this);
        forgotPass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                sendText();
                break;

            case R.id.forgot_pass:
                dialogForgot();
                break;

            case R.id.sign_up:
                Utils.launchActivity(_SignUpActivity.class, _LoginActivity.this);
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

        }
        return true;
    }



        /**
         *
         * Request WebServices Volley methods
         *
         * */
    //Se envian los datos al WebService
    private void sendText(){
        // Creo la variable con el texto a enviar
        if(TextUtils.isEmpty(user.getText().toString()) && TextUtils.isEmpty(pass.getText().toString())){
            Toast.makeText(this, "Introduce nombre de usuario y password", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(user.getText().toString())){
            Toast.makeText(this, "Introduce nombre de usuario", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pass.getText().toString())){
            Toast.makeText(this, "Introduce password", Toast.LENGTH_SHORT).show();
        } else{
            email = user.getText().toString();
            password = pass.getText().toString();
        }

        Log.d("loginActivity", email+"");
        Log.d("loginActivity", password+"");

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            sendLogin = new SendLogin(Constant.url, access,
                    null, null, email, password, userAgent, "lenguajeNativo",
                    "true o false",null);
            // Hace la peticion al WebService con dos parametros que estan definidos en forma de Listeners
            // onResponse y onErrorResponse
            Request<?> request = sendLogin.getRequest(this, this);
            // Instancio el singleton pasandole la peticion y/o alguna etiqueta
            App.getInstance().addToRequestQueue(request, App.TAG);
        }


    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("ErrorResponse", error.getMessage()+"");
    }

    //Recibimos un tipo string y deberemos convertirlo a objeto
    @Override
    public void onResponse(String response) {
        //Creacion del GSON builder
        GsonBuilder builder = new GsonBuilder();
        //Convertir texto a JSON y viceversa
        builder.setExclusionStrategies(new DefaultExclusionStrategy());
        //Creacion de objeto gson con builder
        Gson gson = builder.create();

        Log.d("loginActivity", response+"");
        // Recogemos la respuesta del WebService
        LoginPojo model = gson.fromJson(response, LoginPojo.class);

        //Guardo la respuesta del login del servidor
        SessionPrefs.get(this).saveSessionLogin(model);

        //La web
        String received = model.getFindEnd();
        Log.d("loginActivity", received);


        if(received!=null) {
            if (model.getFindEnd().equals("Si_login")) {
                // Tb lanzar a pantalla de bienvenido
                Toast.makeText(this, "LoginPojo success", Toast.LENGTH_SHORT).show();
                Utils.launchActivity(MainActivity.class, _LoginActivity.this);

            } else if (model.getFindEmail().equals("7") || model.getFindEmail().equals("5")) {
                Toast.makeText(this, "email incorrecto", Toast.LENGTH_SHORT).show();

            } else if (model.getFindPass().equals("7") || model.getFindPass().equals("5")) {
                Toast.makeText(this, "pass incorrecto", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, received, Toast.LENGTH_SHORT).show();
            }
        }

    }

    /**
     *
     * LIFE CICLE
     *
     **/

//    @Override
//    protected void onResume(){
//        super.onResume();
////        launchActivityLoggedIn(MainActivity.class);
//        Toast.makeText(this, "onResume LoginActivity", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public  void onBackPressed(){
//        finish();
//        super.onBackPressed();
//        finish();
//    }



    /**
     *
     * Other utils
     *
     * */

    private void dialogForgot(){
            ForgotPassDialogFragment forgotPassDialogFragment = new ForgotPassDialogFragment();
            forgotPassDialogFragment.show(getFragmentManager(), "ForgotPassDialogFragment");
        }



}




