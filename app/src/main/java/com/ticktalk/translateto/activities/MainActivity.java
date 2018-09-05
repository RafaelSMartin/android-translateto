package com.ticktalk.translateto.activities;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.ticktalk.translateto.App;
import com.ticktalk.translateto.database.DatabaseManager;
import com.ticktalk.translateto.database.FromResult;
import com.ticktalk.translateto.database.ToResult;
import com.ticktalk.translateto.utils.Utils;
import com.ticktalk.translateto.database.SessionPrefs;
import com.ticktalk.translateto.adapters.CustomAdapter;
import com.ticktalk.translateto.remote.DefaultExclusionStrategy;
import com.ticktalk.translateto.remote.model.MessagePojo;
import com.ticktalk.translateto.R;
import com.ticktalk.translateto.remote.sendmodel.SendMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ticktalk.translateto.remote.model.SynonymsPojo;

import org.jsoup.Jsoup;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ticktalk.translateto.utils.Utils.clearFirtsAndLast;
import static com.ticktalk.translateto.utils.Utils.clearText;
import static com.ticktalk.translateto.utils.Utils.contardorPalabras;
import static com.ticktalk.translateto.utils.Utils.hideSoftKeyboard;
import static com.ticktalk.translateto.utils.ArraySpinner.countryCodes;
import static com.ticktalk.translateto.utils.ArraySpinner.countryNames;
import static com.ticktalk.translateto.utils.ArraySpinner.flags;
import static com.ticktalk.translateto.utils.Utils.isPar;
import static com.ticktalk.translateto.utils.Utils.trimEnd;
import static com.ticktalk.translateto.utils.Utils.trimStart;

public class MainActivity extends AppCompatActivity implements
        Response.ErrorListener,
        Response.Listener<String>,
        View.OnClickListener,
        AdapterView.OnItemSelectedListener,
        Toolbar.OnMenuItemClickListener,
        TextToSpeech.OnInitListener
{

//    @BindView(R.id.card_loading)
//    CardView cardLoading;

//    @BindView(R.id.animation_view)
    public static LottieAnimationView animationView;


    @BindView(R.id.constraint_layout)
    ConstraintLayout constraintLayout;

    @BindView(R.id.root_constraint_layout)
    ConstraintLayout rootConstraintLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.spinner_layout)
    CardView cardSpinner;

    @BindView(R.id.from_spinner)
    Spinner fromSpinner;

    @BindView(R.id.to_spinner)
    Spinner toSpinner;

//    @BindView(R.id.image_spinner)
//    ImageView imageSpinner;

    @BindView(R.id.animation_spinner)
    LottieAnimationView animationSpinner;

    @BindView(R.id.numero)
    EditText mEditText;

    @BindView(R.id.card_send)
    CardView cardSend;

    @BindView(R.id.title_card_send)
    TextView titleCardSend;

    @BindView(R.id.clear)
    LinearLayout clear;

    @BindView(R.id.card_receive)
    CardView cardReceive;

    @BindView(R.id.favourite_layout)
    LinearLayout favoriteLayout;

    @BindView(R.id.favourite_amimation)
    LottieAnimationView favoriteAnimation;

    @BindView(R.id.text_human_translation)
    TextView tvHumanTranslation;

    @BindView(R.id.layout_request_human_translation)
    RelativeLayout requestHumanTranslation;

    @BindView(R.id.card_synonyms)
    CardView cardSynonyms;

    @BindView(R.id.title_card_receive)
    TextView titleCardReceive;

    @BindView(R.id.bh_translation)
    Button buttonHumanTranslation;

//    @BindView(R.id.linear_buttons)
//    LinearLayout linearButtons;

    @BindView(R.id.envia)
    Button bSend;

//    @BindView(R.id.shimmer_layout_button_human)
//    ShimmerFrameLayout shimmerButtonHumanTranslation;

    @BindView(R.id.mensaje)
    TextView mMessage;

    @BindView(R.id.sinonimos)
    TextView mSynonyms;

    @BindView(R.id.tts)
    ImageView tts;
    @BindView(R.id.copy)
    ImageView copy;
    @BindView(R.id.share)
    ImageView share;

    @BindView(R.id.tts_traduced)
    ImageView ttsTraduced;
    @BindView(R.id.copy_traduced)
    ImageView copyTraduced;
    @BindView(R.id.share_traduced)
    ImageView shareTraduced;

    @BindView(R.id.tts_synonyms)
    ImageView ttsSynonyms;
    @BindView(R.id.copy_synonyms)
    ImageView copySynonyms;
    @BindView(R.id.share_synonyms)
    ImageView shareSynonyms;

    private String receivedCodMessageSynonyms, receivedMessage, receivedCodMessage,
            received, receivedSynonyms, sendMessageString;

    private String access = "TranslateMain";

    private MessagePojo model;
    private SynonymsPojo modelSynonym;

    private TextToSpeech textToSpeech;
    private TextToSpeech ttSpeech;

    private int cantidadNombres;
    private String unaPalabra;

    private String keyToken;
    private Utils utils;

    private boolean isLogged;

    FromResult fromResult;
    ToResult toResult;

    private Boolean isReceived = false;
    private Boolean isFavorite = false;
    private int contadorAnimacionSpinner = 0;


//    @BindView(R.id.adView)
//    AdView mAdView;

    /**
     *
     * Save traductions from translateto.com
     *
     * */
    private boolean restoreData = false;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("savedInstanceStateFrom", mEditText.getText().toString());
        outState.putString("savedInstanceStateTo", mMessage.getText().toString());
        outState.putString("savedInstanceStateSyn", mSynonyms.getText().toString());
        Log.d("savedInstanceState1", mEditText.getText().toString());
        Log.d("savedInstanceState2", mMessage.getText().toString());
        Log.d("savedInstanceState3", mSynonyms.getText().toString());

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null){
            received = savedInstanceState.getString("savedInstanceStateFrom");
            receivedMessage = savedInstanceState.getString("savedInstanceStateTo");
            receivedCodMessageSynonyms = savedInstanceState.getString("savedInstanceStateSyn");
            mEditText.setText(received);
            mMessage.setText(receivedMessage);
            mSynonyms.setText(receivedCodMessageSynonyms);
            restoreData = true;
            Log.d("savedInstanceState4", received);
            Log.d("savedInstanceState5", receivedMessage);
            Log.d("savedInstanceState6", receivedCodMessageSynonyms);
        }
    }



    /**
     *
     * TTS
     *
     * */
    @Override
    public void onInit(int status) {

        if(status==TextToSpeech.SUCCESS){
            setLocaleTTS(0);
        }else{
            Log.e("TTS","Initialization Failed");
        }
    }

    public void setLocaleTTS(int modeSpinner){
        String language, country;
        if (modeSpinner == 1){
             country = countryCodes[toSpinner.getSelectedItemPosition()];
        } else{
             country = countryCodes[fromSpinner.getSelectedItemPosition()];
        }

        Locale locale = new Locale(country);
        int result = ttSpeech.setLanguage(locale);
        if(result==TextToSpeech.LANG_MISSING_DATA || result==TextToSpeech.LANG_NOT_SUPPORTED ){
            Log.e("TTS","this lang is not supported");
        }else {
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Arranca TTS, envia y espera datos
        Intent intent = new Intent();
        intent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(intent, 0);

        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //Crea variable tts
        ttSpeech  = new TextToSpeech(this,this);


        //Activo la animacion del brillo del boton
//        shimmerButtonHumanTranslation.setBaseAlpha(0.5f);
//        shimmerButtonHumanTranslation.setDuration(1000);
//        shimmerButtonHumanTranslation.setRepeatDelay(1000);
//        shimmerButtonHumanTranslation.startShimmerAnimation();


//        if(!SessionPrefs.get(this).isLoggedIn()){
//            launchActivityLoggedIn(LoginActivity.class);
//            finish();
//            return;
//        }

        animationView = (LottieAnimationView)findViewById(R.id.animation_view);
        //Consulto si estoy logeado
        isLogged = SessionPrefs.get(this).isLoggedIn();


        initToolbar();
        initSpinner();
//        initTTS();
        initBanner();
//        List<FromResult> fromResults = DatabaseManager.getInstance().getAllResult();

        //Sets de los elementos del layout
        fromSpinner.setOnItemSelectedListener(this);
        // Si mi lenguaje nativo es diferente de cero significa que he seleccionado uno previamente
        if (SessionPrefs.get(getApplicationContext()).getPrefNativeLanguage() != 0)
            fromSpinner.setSelection(SessionPrefs.get(getApplicationContext()).getPrefNativeLanguage());

        toSpinner.setOnItemSelectedListener(this);
//        imageSpinner.setOnClickListener(this);
        animationSpinner.setOnClickListener(this);
        bSend.setOnClickListener(this);
        buttonHumanTranslation.setOnClickListener(this);
        favoriteAnimation.setOnClickListener(this);

        clear.setOnClickListener(this);
        tts.setOnClickListener(this);
        copy.setOnClickListener(this);
        share.setOnClickListener(this);

        ttsTraduced.setOnClickListener(this);
        copyTraduced.setOnClickListener(this);
        shareTraduced.setOnClickListener(this);
        tvHumanTranslation.setOnClickListener(this);

        ttsSynonyms.setOnClickListener(this);
        copySynonyms.setOnClickListener(this);
        shareSynonyms.setOnClickListener(this);

        /**
         *
         * Listener EditText
         *
         * **/
        //Oculto y pongo visibles los elementos dependiendo de como vaya escribiendo
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(mEditText.length()==0){
                    cardReceive.setVisibility(View.INVISIBLE);
                    bSend.setVisibility(View.VISIBLE);
                    cardSynonyms.setVisibility(View.INVISIBLE);
                    clear.setVisibility(View.INVISIBLE);
                }else{
                    bSend.setVisibility(View.VISIBLE);
                    clear.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //if(mEditText.length()==0){
                    cardReceive.setVisibility(View.INVISIBLE);
                    bSend.setVisibility(View.VISIBLE);
                    cardReceive.setVisibility(View.INVISIBLE);
                    clear.setVisibility(View.INVISIBLE);
                //}else{
               //     bSend.setVisibility(View.VISIBLE);
               //     clear.setVisibility(View.VISIBLE);
               // }

            }
            @Override
            public void afterTextChanged(Editable s) {
                if(mEditText.length()==0){
                    cardReceive.setVisibility(View.INVISIBLE);
                    bSend.setVisibility(View.VISIBLE);
                    cardSynonyms.setVisibility(View.INVISIBLE);
                    clear.setVisibility(View.INVISIBLE);
                } else{
                    bSend.setVisibility(View.VISIBLE);
                    clear.setVisibility(View.VISIBLE);
                }
            }
        });

        // Si no tengo el foco escondo el teclado
        mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){
                    hideSoftKeyboard(view);
                }
            }
        });


    }

    private void initToolbar(){
        toolbar.inflateMenu(R.menu.menu_toolbar);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

//        if(!SessionPrefs.get(getApplicationContext()).isLoggedIn()){
//            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
//
//        }
//        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                if(SessionPrefs.get(getApplicationContext()).isLoggedIn()){
//
//                }else{
//                    launchActivityLoggedIn(LoginActivity.class);
//                }
//            }
//        });
    }

    // Spinner personalizado con animacion por Lottie en json
    private void initSpinner(){
        animationSpinner.setAnimation("converter_arrows2.json");
        animationSpinner.loop(false);
        animationSpinner.setSpeed(.8f);
//        animationSpinner.setRotation(90);

        favoriteAnimation.setAnimation("favourite_animation.json");
        favoriteAnimation.loop(false);

//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.speak_language_array, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CustomAdapter customAdapterLeft = new CustomAdapter(getApplicationContext(),flags, countryNames, countryCodes, true);
        fromSpinner.setAdapter(customAdapterLeft);

        if (SessionPrefs.get(getApplicationContext()).getPrefNativeLanguage() != 0)
            fromSpinner.setSelection(SessionPrefs.get(getApplicationContext()).getPrefNativeLanguage());

        CustomAdapter customAdapterRight = new CustomAdapter(getApplicationContext(),flags, countryNames, countryCodes, false);
        toSpinner.setAdapter(customAdapterRight);

        titleCardSend.setText(countryNames[fromSpinner.getSelectedItemPosition()]);

    }

    private void initTTS(){
        textToSpeech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    setLocaleTTS(0);
//                    textToSpeech.setLanguage(Locale.getDefault());//dependera del spinner
                }
            }
        });
    }

    //Desabilitado
    private void initBanner(){

//        AdRequest adRequest2 = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                .addTestDevice("29861D979359C2239096D26FCB065DD6") // note 3
//                .addTestDevice("69719507A88941D975B3064F31ECB97F") // mama
//                .addTestDevice("EC757580015FADEB3318BC0DF27A668B") // tablet samsung A 8"
//                .addTestDevice("B04DCAB8D029DECDE561200B051D113E") // tablet lenovo 8"
//                .addTestDevice("07E4BCB0DE0B8A71A1DE43085A917BDB") // samsung j3
//                .addTestDevice("30B7F98E47BF3820BCB92DCF4E3EE686") // note 4
//                .addTestDevice("C1F501C333009E641D4A5CA2107898C2") // j7
//                .addTestDevice("EF83B6F2AA75563F2CBCA859A412B67C") // s7
//                .addTestDevice("BDCB7FEA1E522BF9C01EEB270BD5AE73") // mi note4
//                .addTestDevice("779AF027E66DEC2F9872D74ACA6D752C") // huawei
//                .build();
//        mAdView.loadAd(adRequest2);
//
//
//        mAdView.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                // Code to be executed when an ad finishes loading.
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                // Code to be executed when an ad request fails.
//            }
//
//            @Override
//            public void onAdOpened() {
//                // Code to be executed when an ad opens an overlay that
//                // covers the screen.
//            }
//
//            @Override
//            public void onAdLeftApplication() {
//                // Code to be executed when the user has left the app.
//            }
//
//            @Override
//            public void onAdClosed() {
//                // Code to be executed when when the user is about to return
//                // to the app after tapping on an ad.
//            }
//        });

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

            case R.id.envia:
                Utils.hideSoftKeyboard(v);
                // Si los dos idiomas son iguales no muestro traduccion y aviso al ususario.
                if (countryNames[fromSpinner.getSelectedItemPosition()].equals(countryNames[toSpinner.getSelectedItemPosition()]))
                {
                    if(mEditText.getText().length() > 0)
                    {
                        cardReceive.setVisibility(View.INVISIBLE);
                        cardSynonyms.setVisibility(View.INVISIBLE);
                        bSend.setVisibility(View.VISIBLE);
                        Toast.makeText(this, "Change the language to translate!!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    // Si hay texto cargo los elementos y realizo el envio.
                    if(mEditText.getText().length() >0){

                        animationView.setVisibility(View.VISIBLE);
                        animationView.setAnimation("loading_translating.json");
                        animationView.loop(true);
                        animationView.playAnimation();

                        if(isFavorite){
                            favoriteAnimation.reverseAnimation();
                            isFavorite = false;
                        }
                        fromResult = new FromResult();
                        fromResult.setId(null);
                        sendText();
                    }
                }
                break;

//            case R.id.image_spinner:
            case R.id.animation_spinner:
                //Cargo la animacion para un lado u otro dependiendo de la paridad.
//                playRotateSwapAnimation(this, imageSpinner);
                if (isPar(contadorAnimacionSpinner) ){
                    animationSpinner.playAnimation();
                    contadorAnimacionSpinner += 1;
                } else{
                    animationSpinner.reverseAnimation();
                    contadorAnimacionSpinner += 1;
                }
                // Cona ayuda de aux intercambio las posiciones de los spinners
                int aux = fromSpinner.getSelectedItemPosition();
                fromSpinner.setSelection(toSpinner.getSelectedItemPosition());
                toSpinner.setSelection(aux);

                titleCardSend.setText(countryNames[fromSpinner.getSelectedItemPosition()]);
                break;

            case R.id.clear:
                clearText(mEditText);
                break;

            case R.id.tts:
                String ttsString = mEditText.getText().toString();
                setLocaleTTS(0);
                ttSpeech.speak(ttsString, TextToSpeech.QUEUE_FLUSH, null);
                break;

            case R.id.copy:
                String copyString = mEditText.getText().toString();
                if(copyString.length()>0){
                    Utils.setClipboard(this, copyString);
                    Toast.makeText(this, getString(R.string.copy_text), Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(this, getString(R.string.insert_something), Toast.LENGTH_SHORT).show();

                }
                break;

            case R.id.share:
                if(mEditText.length()>0){
                    Utils.launchShare(v, mEditText.getText().toString(), MainActivity.this);
                } else{
                    Toast.makeText(this, getString(R.string.insert_something), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.favourite_amimation:
                if(isFavorite){
                    favoriteAnimation.reverseAnimation();
                    isFavorite = false;
                    fromResult.setFavoriteAnimation(false);

                } else {
                    favoriteAnimation.playAnimation();
                    isFavorite = true;
                    fromResult.setFavoriteAnimation(true);
                }
                // acualizo la BD al seleccionar favoritos con booleano.
                DatabaseManager.getInstance().updateFromResult(fromResult);
                DatabaseManager.getInstance().getAllResult();

                break;

            case R.id.tts_traduced:
                String ttsStringTraduced = mMessage.getText().toString();
                // 1 To, 2 From -> asi reutilizo el metodo
                setLocaleTTS(1);
                ttSpeech.speak(ttsStringTraduced, TextToSpeech.QUEUE_FLUSH, null);
                break;

            case R.id.copy_traduced:
                String copyStringTraduced = mMessage.getText().toString().trim();
                Utils.setClipboard(this, copyStringTraduced);
                Toast.makeText(this, getString(R.string.copy_traduction), Toast.LENGTH_SHORT).show();
                break;

            case R.id.share_traduced:
                if(mMessage.length()>0){
                    Utils.launchShare(v, mMessage.getText().toString(), MainActivity.this);
                } else{
                    Toast.makeText(this, "No hay traduccion para compartir", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.tts_synonyms:
                String ttsStringSynonyms = mSynonyms.getText().toString();
                setLocaleTTS(0);
                ttSpeech.speak(ttsStringSynonyms, TextToSpeech.QUEUE_FLUSH, null);
                break;

            case R.id.copy_synonyms:
                String copyStringSynonyms = mSynonyms.getText().toString().trim();
                Utils.setClipboard(this, copyStringSynonyms);
                Toast.makeText(this, getString(R.string.copy_synonyms), Toast.LENGTH_SHORT).show();
                break;

            case R.id.share_synonyms:
                if(mSynonyms.length()>0){
                    Utils.launchShare(v, mSynonyms.getText().toString(), MainActivity.this);
                } else{
                    Toast.makeText(this, "No hay synonimos para compartir", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.bh_translation:
                // Para lanzar H.Translation es necesario que el usuario este logeado.
                if(isLogged)
                    Utils.launchActivity(HumanTranslationActivity.class, MainActivity.this );
                else
//                    Utils.launchActivity(LoginForm.class, MainActivity.this);
                    LoginActivity.startLoginActivity(this);

                break;

            case R.id.text_human_translation:
                if(isLogged)
                    Utils.launchActivity(HumanTranslationActivity.class, MainActivity.this );
                else
//                    Utils.launchActivity(LoginForm.class, MainActivity.this);
                    LoginActivity.startLoginActivity(this);
                break;

            default:
                break;
        }
    }


    /**
     *
     * Spinner
     *
     ***/

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)

        titleCardSend.setText(countryNames[fromSpinner.getSelectedItemPosition()]);
//        titleCardReceive.setText(countryNames[toSpinner.getSelectedItemPosition()]);
        if (countryNames[fromSpinner.getSelectedItemPosition()].equals(countryNames[toSpinner.getSelectedItemPosition()]))
        {
            // Anteriormente estaba invisible, pero se modifico por facilidad de uso.
            bSend.setVisibility(View.VISIBLE);
        } else{
//            bSend.setVisibility(View.VISIBLE);
        }
        setLocaleTTS(0);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }


    /**
     *
     * Request WebServices Volley methods
     *
     * */

    //Se envian los datos al WebService
    private void sendText(){

//        progressBar = new ProgressBar(MainActivity.this, null,
//                android.R.attr.progressBarStyle);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
//        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
//        params.addRule(RelativeLayout.CENTER_VERTICAL);
//
//        rootConstraintLayout.addView(progressBar, params);

        // Desabilito el touch
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        //Recojo el texto a traducir y enviar
        sendMessageString = mEditText.getText().toString().trim();

        //Codificacion del KeyToken
        keyToken = "BabiecaHiede";
//        keyToken = Base64.encodeToString(keyToken.getBytes(), Base64.DEFAULT);
//        Log.d("MainSendEncodeToken", keyToken);

        //Recojo el string con la posicion de los spinners
        String sendFromSpinner = countryNames[fromSpinner.getSelectedItemPosition()];
        String sendToSpinner = countryNames[toSpinner.getSelectedItemPosition()];

        Locale localeTo = new Locale(countryNames[toSpinner.getSelectedItemPosition()]);
        Locale localeFrom = new Locale(countryNames[fromSpinner.getSelectedItemPosition()]);


        String sendFromCodesSpinner = countryCodes[fromSpinner.getSelectedItemPosition()];
        String sendToCodesSpinner = countryCodes[toSpinner.getSelectedItemPosition()];

        Log.d("MainSendAccess", access);
        Log.d("MainSendMensaje", sendMessageString);
        Log.d("MainSendFromSpinner", sendFromSpinner);
        Log.d("MainSendToSpinner", sendToSpinner);
        Log.d("MainSendToken", keyToken);
        Log.d("MainSendFromCodeSpinner", sendFromCodesSpinner);
        Log.d("MainSendToCodeSpinner", sendToCodesSpinner);


//        Log.d("MainSendToCodeSpinner", localeTo.getCountry());
//        Log.d("MainSendToCodeSpinner", localeTo.getLanguage());
//        Log.d("MainSendToCodeSpinner", localeTo.getDisplayCountry());
//        Log.d("MainSendToCodeSpinner", localeTo.getDisplayLanguage());
//        Log.d("MainSendToCodeSpinner", localeTo.getDisplayName());
//        Log.d("MainSendToCodeSpinner", localeTo.getISO3Country());
//        Log.d("MainSendToCodeSpinner", localeTo.getISO3Language());



        // Cuento las palabras y si es una quito espacios finales y setto unaPalabra a true
//        String temp[] = mEditText.getText().toString().split(" ");
//        cantidadNombres = temp.length;

        //Cuento las palabras y si es una quito espacios finales y setto unaPalabra a true
//        StringTokenizer cantidadNombres = new StringTokenizer(sendMessageString, " ");
//        int contadorNombres=0;
//        while (cantidadNombres.hasMoreTokens()) {
//            contadorNombres++;
//            cantidadNombres.nextToken();
//        }
//        Log.d("MainSendContador", String.valueOf(contadorNombres));


        int contadorPalabras = contardorPalabras(sendMessageString);

//        Log.d("MainSendCantidad", cantidadNombres+"");
        if(contadorPalabras == 1){
            unaPalabra = "true";
            //Quito espacios
            sendMessageString = trimEnd(mEditText.getText().toString());
            sendMessageString = trimStart(sendMessageString);
        } else {
            unaPalabra = "false";
            cardSynonyms.setVisibility(View.INVISIBLE);
        }

        // Creo la variable con el texto a enviar
        SendMessage sendMessage = new SendMessage(keyToken, access, sendMessageString, sendFromCodesSpinner, sendToCodesSpinner, unaPalabra);
        // Hace la peticion al WebService con dos parametros que estan definidos en forma de Listeners
        // onResponse y onErrorResponse
        //Request<?> requestHeader = sendMessage.getHeaders(this, this);
        Request<?> request = sendMessage.getRequest(this, this);
        // Instancio el singleton pasandole la peticion y/o alguna etiqueta
        //App.getInstance().addToRequestQueue(requestHeader, App.TAG);
        App.getInstance().addToRequestQueue(request, App.TAG);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (error instanceof AuthFailureError){
            Log.e("ErrorResponse", "Error credentials" + error.getMessage());
        } else if(error instanceof NetworkError ){
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
        animationView.cancelAnimation();
        animationView.setVisibility(View.GONE);
        Toast.makeText(this, getString(R.string.error_volley), Toast.LENGTH_SHORT).show();
        isReceived = false;
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
        Gson gsonSynonym = builder.create();

        Log.d("MainSend", response+"");


        // Recogemos la respuesta del WebService
        model = gson.fromJson(response, MessagePojo.class);


        String keyTokenFromServer = model.getKeyToken();
        Log.d("MainSendToken", keyTokenFromServer+"");


        receivedCodMessage = model.getCodMessage();
        received = model.getFindEnd();
        String receivedMessageAux = model.getTraducedText();
//        CharSequence receivedMessage = Html.fromHtml(receivedMessage2);
        receivedMessage = Jsoup.parse(receivedMessageAux).text();
//        receivedMessage = charSequence;
        receivedSynonyms = model.getSynonyms();

        Log.d("MainSendReceivedSynony1", receivedSynonyms);


        // Recogemos la respuesta de los sinonimos provenientes del JSON WebService
        modelSynonym = gsonSynonym.fromJson(receivedSynonyms, SynonymsPojo.class);

        if(unaPalabra.equals("true") && modelSynonym != null){
            if (modelSynonym.getWord().equals("")){
                receivedSynonyms = "No hay sinonimos";
                Log.d("MainSendUnaPalabraSynon", modelSynonym.getSynonyms()+"");
            }else{
                receivedSynonyms = modelSynonym.getSynonyms().toString();
                Log.d("MainSendUnaPalabraSynon", modelSynonym.getSynonyms()+"");
            }
        } else{
            receivedSynonyms = "";
        }


//        String languageOrigin = model.getLanguageOrigin();
//        String languageFinal = model.getLanguageFinal();

        //La web
        Log.d("MainSendReceived", received);
        Log.d("MainSendRecivCodMessage", receivedCodMessage);
        Log.d("MainSendReceivedMessage", receivedMessage+"");
        Log.d("MainSendReceivedSynony2", receivedSynonyms);

        //habilito el touch
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


        if(received.equals("200")) {

            isReceived = true;
//            cardLoading.setVisibility(View.GONE);

            cardReceive.setVisibility(View.VISIBLE);
            mMessage.setText(receivedMessage);

            titleCardReceive.setText(countryNames[toSpinner.getSelectedItemPosition()]);

            // Guarda en database lo traducido
            fromResult = new FromResult();
            fromResult.setId(null);

            fromResult.setLanguageCode(titleCardSend.getText().toString());
            fromResult.setBackgroundColor(R.color.blanco);
            fromResult.setFontSize(16);
            fromResult.setSynonyms(receivedSynonyms);
            fromResult.setText(mEditText.getText().toString());
            fromResult.setFavoriteAnimation(false);

            toResult = new ToResult();
            toResult.setId(null);
            toResult.setLanguageCode(titleCardReceive.getText().toString());
            toResult.setSynonyms(receivedSynonyms);
            toResult.setText(mMessage.getText().toString());

            fromResult.addToResult(toResult);
            DatabaseManager.getInstance().insertTranslation(fromResult);
            DatabaseManager.getInstance().getAllResult();

            if(DatabaseManager.getInstance().isClearedDatabase()) {
                DatabaseManager.getInstance().setClearedDatabase(false);
            }


            if(receivedSynonyms.equals("No hay sinonimos") || receivedSynonyms.equals("[]") || receivedSynonyms == null || receivedSynonyms.equals("")){
                mSynonyms.setText("");
                cardSynonyms.setVisibility(View.INVISIBLE);
                titleCardReceive.setText(countryNames[toSpinner.getSelectedItemPosition()]);
            } else{
                cardSynonyms.setVisibility(View.VISIBLE);
                mSynonyms.setText(clearFirtsAndLast(receivedSynonyms));
                titleCardReceive.setText(countryNames[toSpinner.getSelectedItemPosition()]);
                mSynonyms.setVisibility(View.VISIBLE);
            }

        } else {
            cardReceive.setVisibility(View.INVISIBLE);
            cardSynonyms.setVisibility(View.INVISIBLE);
//            Toast.makeText(this, response+" algo ha ocurrido", Toast.LENGTH_SHORT).show();
        }

        animationView.cancelAnimation();
        animationView.setVisibility(View.GONE);

    }


    /**
     *Ciclo de vida
     *
     **/
    @Override
    public void onResume() {
        super.onResume();
        initTTS();
        if (SessionPrefs.get(getApplicationContext()).getPrefNativeLanguage() != 0) {
            fromSpinner.setSelection(SessionPrefs.get(getApplicationContext()).getPrefNativeLanguage());
        } else {
//            fromSpinner.setSelection(0);
        }
        titleCardSend.setText(countryNames[fromSpinner.getSelectedItemPosition()]);

        if(restoreData){
            bSend.setVisibility(View.VISIBLE);
//            cardReceive.setVisibility(View.VISIBLE);
//            cardSynonyms.setVisibility(View.VISIBLE);
        } else{
            clearText(mEditText);
            bSend.setVisibility(View.VISIBLE);
            cardReceive.setVisibility(View.INVISIBLE);
            cardSynonyms.setVisibility(View.INVISIBLE);
        }


        if (cardReceive.getVisibility() == View.VISIBLE) {
            if (fromResult.getFavoriteAnimation()) {
                favoriteAnimation.playAnimation();
            } else {
                favoriteAnimation.reverseAnimation();
            }
        }

    }

    @Override
    public void onPause(){
//        if(textToSpeech !=null){
//            textToSpeech.stop();
//            textToSpeech.shutdown();
//        }
        super.onPause();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        if(ttSpeech !=null){
            ttSpeech.stop();
            ttSpeech.shutdown();
        }
    }





}
