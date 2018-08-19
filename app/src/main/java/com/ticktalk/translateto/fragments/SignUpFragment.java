package com.ticktalk.translateto.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.ticktalk.translateto.activities.MainActivity;
import com.ticktalk.translateto.database.SessionPrefs;
import com.ticktalk.translateto.adapters.CustomAdapter;
import com.ticktalk.translateto.activities.LoginActivity;
import com.ticktalk.translateto.utils.Constant;
import com.ticktalk.translateto.utils.Utils;
import com.ticktalk.translateto.remote.DefaultExclusionStrategy;
import com.ticktalk.translateto.R;
import com.ticktalk.translateto.remote.model.LoginPojo;
import com.ticktalk.translateto.remote.sendmodel.SendLogin;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ticktalk.translateto.utils.ArraySpinner.countryCodes;
import static com.ticktalk.translateto.utils.ArraySpinner.countryNames;
import static com.ticktalk.translateto.utils.ArraySpinner.flags;
import static com.ticktalk.translateto.utils.Utils.isConnection;

/**
 * Created by Rafael S. Martin
 */

public class SignUpFragment extends Fragment implements
        Response.ErrorListener,
        Response.Listener<String>,
        View.OnClickListener,
        AdapterView.OnItemSelectedListener
{

    public static final String TAG = "SIGNUP_FRAGMENT";
    private View view;
    private AppCompatActivity activity;

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

    private String firstName, lastName, email, pass, confirmPass, ultimoLogin;
    private SendLogin sendLogin;
    private LoginPojo model;

    private Date currentDate;
    private boolean isSpinnerSelected = false;
    int posCurrentSelection;


    @Override
    public void onAttach (Context context){
        super.onAttach(context);
        this.activity = (AppCompatActivity) context;
    }

    @Override
    public void onDetach(){
        if(isSpinnerSelected){
//            //Enviar a server
//            Toast.makeText(activity, "save native language", Toast.LENGTH_SHORT).show();
        }
        isSpinnerSelected = false;

        super.onDetach();
        activity = null;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentDate = Calendar.getInstance().getTime();
        ultimoLogin = currentDate.toString();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_signup, container, false);
        ButterKnife.bind(this, view);

        initSpinner();
        initListeners();

        return view;
    }

    private void initSpinner(){
        //initspinner
        CustomAdapter customAdapter=new CustomAdapter(activity, flags, countryNames, countryCodes, true);
        spinnerNativeLanguage.setAdapter(customAdapter);

        if (SessionPrefs.get(activity).getPrefNativeLanguage() != 0){
            spinnerNativeLanguage.setSelection(SessionPrefs.get(activity).getPrefNativeLanguage());
        } else{
            spinnerNativeLanguage.setSelection(0);
        }

        posCurrentSelection = SessionPrefs.get(activity).getPrefNativeLanguage();

        //initListener spinner
        spinnerNativeLanguage.setOnItemSelectedListener(this);

        if (SessionPrefs.get(activity).getPrefNativeLanguage() != 0){
            spinnerNativeLanguage.setSelection(SessionPrefs.get(activity).getPrefNativeLanguage());
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
//                Utils.launchActivity(LoginForm.class, activity);
                LoginActivity.startLoginActivity(activity);
                break;

            default:
                break;
        }
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
        final ProgressDialog progressDialog = new ProgressDialog(activity,
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
                        ConnectivityManager conMgr = (ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);

                        // En la llamada, completa ya sea onLoginSuccess o onLoginFailed
                        if(model!=null && isConnection(conMgr)) {
                            if (model.getFindEnd().equals("200")) {
                                onLoginSuccess();
                                progressDialog.dismiss();
                            } else {
                                onLoginFailed();
                                progressDialog.cancel();
                            }
                        }else{
                            onLoginFailedConnection();
                            progressDialog.cancel();
                        }
                    }
                }, 2500);
    }

    public void onLoginSuccess() {
        signUp.setEnabled(true);
        activity.finish();
    }

    public void onLoginFailed() {
        Toast.makeText(activity, "Login failed", Toast.LENGTH_LONG).show();
        signUp.setEnabled(true);
    }

    public void onLoginFailedConnection(){
        Toast.makeText(activity, "Login failed, Conection problem !!", Toast.LENGTH_LONG).show();
        signUp.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        if(TextUtils.isEmpty(firstName) && TextUtils.isEmpty(lastName) && TextUtils.isEmpty(email)
                && TextUtils.isEmpty(pass) && TextUtils.isEmpty(confirmPass) ){
            Toast.makeText(activity, "Rellena todos los campos", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        else if(firstName.length()<4){
            Toast.makeText(activity, R.string.form_fname, Toast.LENGTH_SHORT).show();
            valid = false;
        }
        else if(lastName.length()<4){
            Toast.makeText(activity, R.string.form_lname, Toast.LENGTH_SHORT).show();
            valid = false;
        }
        else if(!Utils.isEmailValid(email)){
            Toast.makeText(activity, R.string.form_email, Toast.LENGTH_SHORT).show();
            valid = false;
        }
        else if(pass.length()<6){
            Toast.makeText(activity, R.string.form_pass, Toast.LENGTH_SHORT).show();
            valid = false;
        }
        else if(!pass.equals(confirmPass)){
            Toast.makeText(activity, R.string.form_confirm_pass, Toast.LENGTH_SHORT).show();
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
                "true", "premiun");

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

        Log.d("SignActivity2", response+"");
        model = gson.fromJson(response, LoginPojo.class);

        //La web
        String received = model.getFindEnd();
        Log.d("SignActivity3", received+"");

        if(received!=null){
            if(received.equals("200")){
                //Guardo la respuesta del login del servidor
                SessionPrefs.get(activity).saveSessionLogin(model);
                SessionPrefs.get(activity).saveSessionnativeLanguage(spinnerNativeLanguage.getSelectedItemPosition());
                // Tb lanzar a pantalla de bienvenido
                Utils.launchActivity(MainActivity.class, activity);
                Toast.makeText(activity, "Signin success", Toast.LENGTH_SHORT).show();
            }else if(received.equals("209")){
                Toast.makeText(activity, "Signin failed, repeated email or pass", Toast.LENGTH_SHORT).show();
            } else if (received.equals("206")){
                Toast.makeText(activity, "user inactive ", Toast.LENGTH_SHORT).show();
            } else if(received.equals("215")){
                Toast.makeText(activity, "user repetido ", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(activity, response+ " error interno", Toast.LENGTH_SHORT).show();

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
        SessionPrefs.get(activity).saveSessionnativeLanguage(spinnerNativeLanguage.getSelectedItemPosition());

        if (SessionPrefs.get(activity).getPrefNativeLanguage() != 0){
            spinnerNativeLanguage.setSelection(spinnerNativeLanguage.getSelectedItemPosition());
            Log.d("spinnerProfile4", spinnerNativeLanguage.getSelectedItemPosition()+"");
        } else{
            spinnerNativeLanguage.setSelection(0);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


}
