package com.ticktalk.translateto.usergestion.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.text.TextUtils;

import com.ticktalk.translateto.MainActivity;
import com.ticktalk.translateto.sessionpref.SessionPrefs;
import com.ticktalk.translateto.usergestion.signup.SignUpActivity;
import com.ticktalk.translateto.utils.Utils;
import com.ticktalk.translateto.R;
import com.ticktalk.translateto.webservicesvolley.pojo.LoginPojo;
import com.ticktalk.translateto.webservicesvolley.sendpojo.SendLogin;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v4.provider.FontsContractCompat.FontRequestCallback.RESULT_OK;
import static com.ticktalk.translateto.spinner.ArraySpinner.countryNames;
import static com.ticktalk.translateto.utils.Utils.hideSoftKeyboard;

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
import com.ticktalk.translateto.utils.Constant;
import com.ticktalk.translateto.webservicesvolley.DefaultExclusionStrategy;

import static com.ticktalk.translateto.utils.Utils.isConnection;

/**
 * Created by Indogroup02 on 02/02/2018.
 */

public class LoginFragment extends Fragment implements
        Response.ErrorListener,
        Response.Listener<String>
{

    public static final String TAG = "LOGIN_FRAGMENT";
    private static final int REQUEST_SIGNUP = 0;

    private View view;
    private AppCompatActivity activity;

    private String password, email, session;
    private String premium = "false";
    private SendLogin sendLogin;
    private LoginPojo model;


    @BindView(R.id.input_email)
    EditText emailText;

    @BindView(R.id.input_password)
    EditText passwordText;

    @BindView(R.id.input_password_layout)
    TextInputLayout passwordTextLayout;

    @BindView(R.id.btn_login)
    Button loginButton;

    @BindView(R.id.forgot_pass)
    TextView forgotPassword;

    @BindView(R.id.link_signup)
    TextView signupLink;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (AppCompatActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);

        if(SessionPrefs.get(activity).isLoggedIn()){
            LoginActivity.startLoginActivity(activity);
        }

        forgotPassword.setOnClickListener(v -> dialogForgot());

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        signupLink.setOnClickListener(v -> {
            Intent intent = new Intent(activity, SignUpActivity.class);
            startActivityForResult(intent, REQUEST_SIGNUP);
        });

        emailText.setOnFocusChangeListener((view, hasFocus) -> {
            if(!hasFocus){
                hideSoftKeyboard(view);
            }
        });


        passwordText.setOnFocusChangeListener((view, hasFocus) -> {
            if(!hasFocus){
                hideSoftKeyboard(view);
            }
        });

        return view;
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);

        //Creo la ventana de progreso
        final ProgressDialog progressDialog = new ProgressDialog(activity,
                R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        email = emailText.getText().toString();
        password = passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.
        // Envio peticion a tranlateto.com
        sendText();

        //Lanzo un manejador con dos segundos de retardo para esperar la respuesta de Volley
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        ConnectivityManager conMgr = (ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);
                        // TODO Fuerzo a esperar de volley
//                        while(!isCallbackDone && interner){//
//                        }

                        // En la llamada, completa ya sea onLoginSuccess o onLoginFailed
                        if(model!=null && isConnection(conMgr)) {
                            if (model.getFindEnd().equals("200")) {
                                onLoginSuccess();
                                progressDialog.cancel();
                            } else {
                                onLoginFailed();
                                progressDialog.cancel();
                            }
                        } else{
                            onLoginFailedConnection();
                            progressDialog.cancel();
                        }
                    }

                }, 2500);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                // TODO: Implement successful signup logic here
//                De manera predeterminada, solo terminamos la Actividad e iniciamos sesión automáticamente
                activity.finish();
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if(SessionPrefs.get(activity).isLoggedIn()){
            LoginActivity.startLoginActivity(activity);
        }
    }

    public void onLoginSuccess() {
        loginButton.setEnabled(true);
        activity.finish();
    }

    public void onLoginFailed() {
        Toast.makeText(activity, "Login failed", Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
    }

    public void onLoginFailedConnection(){
        Toast.makeText(activity, "Login failed, Conection problem !!", Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("Enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 16) {
            passwordText.setError("Between 6 and 16 alphanumeric characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }

    private void dialogForgot(){
        ForgotPassDialogFragment.startCustomDialog(activity);
    }


    /**
     *
     * Request WebServices Volley methods
     *
     * */
    private void sendText(){

        // Creo la variable con el texto a enviar
        if(TextUtils.isEmpty(emailText.getText().toString()) && TextUtils.isEmpty(emailText.getText().toString())){
            Toast.makeText(activity, "Insert username and password, please.", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(emailText.getText().toString())){
            Toast.makeText(activity, "Insert username, please.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(passwordText.getText().toString())){
            Toast.makeText(activity, "Insert password, please.", Toast.LENGTH_SHORT).show();
        } else{
            email = emailText.getText().toString();
            password = passwordText.getText().toString();
        }

//        Log.d("loginActivity1", email+"");
//        Log.d("loginActivity2", password+"");
//        Log.d("loginActivity3", session+"");

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            sendLogin = new SendLogin(Constant.url, Constant.accessLogin,
                    null, null, email, password, Constant.userAgent, "English",
                    "true", null);

            Request<?> request = sendLogin.getRequest(this, this);
            App.getInstance().addToRequestQueue(request, App.TAG);
        }


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
//        isCallbackDone = true;
    }

    //Recibimos un tipo string y deberemos convertirlo a objeto
    @Override
    public void onResponse(String response) {

        GsonBuilder builder = new GsonBuilder();
        builder.setExclusionStrategies(new DefaultExclusionStrategy());
        Gson gson = builder.create();

        Log.d("loginActivity4", response+"");
        model = gson.fromJson(response, LoginPojo.class);

        //La web
        String received = model.getFindEnd();
        Log.d("loginActivity5", received);

        //Recojo el lenguajeNativo
        String ln = model.getLanguage();
//        String ln2 = model.getLenguajeNativo();
        Log.d("loginActivity6", ln.toString()+"");
//        Log.d("loginActivity7", ln2.toString()+"");


        if(received!=null) {

            if (model.getFindEnd().equals("200")) {
                //Guardo la respuesta del login del servidor
                SessionPrefs.get(activity).saveSessionLogin(model);

                //comparo el nombre en el array y guardo la pos en preference
                for(int i = 0; i < countryNames.length; i++){
                    if(countryNames[i].equals(ln)){
                        SessionPrefs.get(activity).saveSessionnativeLanguage(i);
                    }
                }
//                Log.d("loginActivity7", SessionPrefs.get(this).getPrefNativeLanguage()+"");
                // Tb lanzar a pantalla de bienvenido
                Toast.makeText(activity, "Login success", Toast.LENGTH_SHORT).show();
                Utils.launchActivity(MainActivity.class, activity);

            } else if (model.getFindEnd().equals("209")) {
                Toast.makeText(activity, "incorrect user or email!", Toast.LENGTH_SHORT).show();

            } else if (model.getFindEnd().equals("206") ) {
                Toast.makeText(activity, "Inactive User!", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(activity, received + "Internal Error!", Toast.LENGTH_SHORT).show();
            }
        }

    }




}
