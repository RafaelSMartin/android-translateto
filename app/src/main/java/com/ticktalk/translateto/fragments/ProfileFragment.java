package com.ticktalk.translateto.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.ticktalk.translateto.database.SessionPrefs;
import com.ticktalk.translateto.adapters.CustomAdapter;
import com.ticktalk.translateto.utils.Constant;
import com.ticktalk.translateto.remote.DefaultExclusionStrategy;
import com.ticktalk.translateto.R;
import com.ticktalk.translateto.remote.model.LoginPojo;
import com.ticktalk.translateto.remote.sendmodel.SendLogin;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ticktalk.translateto.utils.ArraySpinner.countryCodes;
import static com.ticktalk.translateto.utils.ArraySpinner.countryNames;
import static com.ticktalk.translateto.utils.ArraySpinner.flags;
import static com.ticktalk.translateto.utils.Constant.userAgent;

public class ProfileFragment extends Fragment  implements
        View.OnClickListener,
        AdapterView.OnItemSelectedListener,
        CustomDialog.OnCompleteListener,
        Response.ErrorListener,
        Response.Listener<String>
{

    public static final String TAG = "PROFILE_FRAGMENT";
    private View view;
    private AppCompatActivity activity;


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

    private String firstName, lastName;
    private String fName;
    private String lName;
    private String email;
    private String pass, oldPass;
    private String confirmPass;
    private String text = "";
    private String textProfile;
    private String access;
    private boolean isLogged;
    private boolean isSpinnerSelected = false;
    int posCurrentSelection;

    @Override
    public void onAttach (Context context){
        super.onAttach(context);
        this.activity = (AppCompatActivity) context;
    }

    @Override
    public void onDetach(){
        super.onDetach();
        activity = null;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        isLogged = SessionPrefs.get(activity).isLoggedIn();

        if(isLogged){
            firstName = SessionPrefs.get(activity).getPrefFname();
            lastName = SessionPrefs.get(activity).getPrefLname();
            email = SessionPrefs.get(activity).getPrefEmail();
            oldPass = SessionPrefs.get(activity).getPefPass();
        } else{
            firstName = getResources().getString(R.string.first_name);
            lastName = getResources().getString(R.string.last_name);
            email = getResources().getString(R.string.email);
            oldPass = getResources().getString(R.string.old_password);
        }

        edFirstName.setText(firstName);
        edLastName.setText(lastName);
        edEmail.setText(email);

        initListeners();
        initSpinner();

        return view;
    }

    public void initListeners(){
        layoutFirstName.setOnClickListener(this);
        layoutLastName.setOnClickListener(this);
        layoutEmail.setOnClickListener(this);
        layoutNativeLanguage.setOnClickListener(this);
        layoutOldPassword.setOnClickListener(this);
    }

    public void initSpinner(){
        //initspinner
        CustomAdapter customAdapter=new CustomAdapter(activity, flags,
                countryNames, countryCodes, true);
        spinnerNativeLanguage.setAdapter(customAdapter);

        //pongo la pos actual del spinner
        if (SessionPrefs.get(activity).getPrefNativeLanguage() != 0){
            spinnerNativeLanguage.setSelection(SessionPrefs.get(activity).getPrefNativeLanguage());
        } else{
            spinnerNativeLanguage.setSelection(0);
        }

        posCurrentSelection = SessionPrefs.get(activity).getPrefNativeLanguage();

        //initListener spinner
        spinnerNativeLanguage.setOnItemSelectedListener(this);

        //pongo la pos actual del spinner
//        if (SessionPrefs.get(activity).getPrefNativeLanguage() != 0){
//            spinnerNativeLanguage.setSelection(SessionPrefs.get(activity).getPrefNativeLanguage());
//        } else{
//            spinnerNativeLanguage.setSelection(0);
//        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.layout_profile_first_name:
                CustomDialog.startCustomDialog(activity, "Change First Name", getString(R.string.custom_input) + " " , firstName, 1);
                break;

            case R.id.layout_profile_last_name:
                CustomDialog.startCustomDialog(activity, "Change Last Name", getString(R.string.custom_input) + " " , lastName, 2);
                break;

            case R.id.layout_profile_old_password:
                CustomDialog.startCustomDialog(activity, "Change old Password", getString(R.string.custom_input) + " " , oldPass, 3);
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
        SessionPrefs.get(activity).saveSessionnativeLanguage(spinnerNativeLanguage.getSelectedItemPosition());

        if (SessionPrefs.get(activity).getPrefNativeLanguage() != 0){
            spinnerNativeLanguage.setSelection(spinnerNativeLanguage.getSelectedItemPosition());
            Log.d("spinnerProfile4", spinnerNativeLanguage.getSelectedItemPosition()+"");
        } else{
            spinnerNativeLanguage.setSelection(0);
        }

        if(isSpinnerSelected){
            //Enviar a server
            sendText();
        }

        isSpinnerSelected = false;

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }


    private void sendText(){

         fName = SessionPrefs.get(getActivity()).getPrefFname();
         lName = SessionPrefs.get(getActivity()).getPrefLname();
         access = Constant.accessProfile;
         pass = SessionPrefs.get(getActivity()).getPefPass();
         email = SessionPrefs.get(getActivity()).getPrefEmail();
         oldPass = SessionPrefs.get(getActivity()).getPefPass();

        int posLenguajeNativo = SessionPrefs.get(getActivity()).getPrefNativeLanguage();
        String lenguajeNativo = countryNames[posLenguajeNativo];

        SendLogin sendLogin = new SendLogin(Constant.url,
                access, firstName, lName, email, oldPass,
                userAgent, lenguajeNativo,
                pass,null);
        Log.d("profileFragment1", Constant.accessProfile);
        Log.d("profileFragment2", fName);
        Log.d("profileFragment3", lName);
        Log.d("profileFragment4", email);
        Log.d("profileFragment5", pass);
        Log.d("profileFragment6", oldPass);
        Log.d("profileFragment7", lenguajeNativo);

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

        Log.d("profileFragment8", response+"");

        LoginPojo model = gson.fromJson(response, LoginPojo.class);

        //La web
        String received = model.getFindEnd();
        Log.d("profileFragment9", received+"");
//        Toast.makeText(getActivity(), received, Toast.LENGTH_SHORT).show();


        if(received != null){
            if(model.getFindEnd().equals("209")){
                Log.d("profileFragment10", "Email No encontrado o pass");
            } else if(model.getFindEnd().equals("200")){
                //Guardo la respuesta del login del servidor
                SessionPrefs.get(getActivity()).saveSessionLogin(model);
                Log.d("profileFragment11", "cambiado");
                // TODO Tb lanzar a pantalla mensaje de cambio realizado con exito
                Toast.makeText(activity, "Cambio realizado con exito", Toast.LENGTH_SHORT).show();

            } else if(model.getFindEnd().equals("206")){
                Log.d("profileFragment12", "user inactivo");
            } else if(model.getFindEnd().equals("301")){
                if(fName.equals(textProfile)){
                    Toast.makeText(activity, "Cambio realizado con exito", Toast.LENGTH_SHORT).show();
                }else if(lName.equals(textProfile)){
                    Toast.makeText(activity, "Cambio realizado con exito", Toast.LENGTH_SHORT).show();
                }else{
                    Log.d("profileFragment13", "Error interno, prueba mas adelante.");
                }
            }
        }


    }


    /**
     * Interface del Listener de CustomDialog
     *
     **/
    @Override
    public void onCompleteCustomDialog(String text, int modo) {
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
