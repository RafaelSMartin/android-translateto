package com.ticktalk.translateto.purchase;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.ticktalk.translateto.App;
import com.ticktalk.translateto.R;
import com.ticktalk.translateto.setting.SettingActivity;
import com.ticktalk.translateto.webservicesvolley.DefaultExclusionStrategy;
import com.ticktalk.translateto.webservicesvolley.sendpojo.SendPaypal;

import org.json.JSONException;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;
import static com.ticktalk.translateto.humantranslation.HumanTranslationFragment.audience_gender;
import static com.ticktalk.translateto.humantranslation.HumanTranslationFragment.audience_genderPos;
import static com.ticktalk.translateto.humantranslation.HumanTranslationFragment.author_gender;
import static com.ticktalk.translateto.humantranslation.HumanTranslationFragment.author_genderPos;
import static com.ticktalk.translateto.humantranslation.HumanTranslationFragment.email_in;
import static com.ticktalk.translateto.humantranslation.HumanTranslationFragment.instructions_language;
import static com.ticktalk.translateto.humantranslation.HumanTranslationFragment.language_source;
import static com.ticktalk.translateto.humantranslation.HumanTranslationFragment.language_source_pos;
import static com.ticktalk.translateto.humantranslation.HumanTranslationFragment.language_target;
import static com.ticktalk.translateto.humantranslation.HumanTranslationFragment.language_target_pos;
import static com.ticktalk.translateto.humantranslation.HumanTranslationFragment.numero_language;
import static com.ticktalk.translateto.humantranslation.HumanTranslationFragment.password_in;
import static com.ticktalk.translateto.humantranslation.HumanTranslationFragment.text_translation;
import static com.ticktalk.translateto.humantranslation.HumanTranslationFragment.tone_language;
import static com.ticktalk.translateto.humantranslation.HumanTranslationFragment.tone_languagePos;
import static com.ticktalk.translateto.humantranslation.HumanTranslationFragment.topic_language;
import static com.ticktalk.translateto.humantranslation.HumanTranslationFragment.topic_languagePos;
import static com.ticktalk.translateto.humantranslation.HumanTranslationFragment.total_precio;
import static com.ticktalk.translateto.humantranslation.HumanTranslationFragment.total_words;
import static com.ticktalk.translateto.spinner.ArraySpinner.countryCodes;
import static com.ticktalk.translateto.spinner.ArraySpinner.countryNames;
import static com.ticktalk.translateto.spinner.ArraySpinner.flags;


/**
 * Created by Indogroup02 on 08/03/2018.
 */

public class PurchaseFragment extends Fragment implements
        Toolbar.OnMenuItemClickListener
//        Response.ErrorListener,
//        Response.Listener<String>
{

    public static final String TAG = "PURCHASE_FRAGMENT";
    public static String TARGET = "target_bundle";
    public static String TEXT = "text_bundle";
    public static String SOURCE = "source_bundle";
    public static String INSTRUCTIONS = "instructions_source";
    public static String TOPIC = "topic_source";
    public static String TONE = "tone_source";
    public static String AUTHOR = "author_source";
    public static String AUDIENCE = "audience_source";
    public static String PRICE = "price_bundle";
    public static String WORDS = "words_bundle";

    // Paypal Config
    private static final String CONFIG_ENVIRONMENT =
            PayPalConfiguration.ENVIRONMENT_SANDBOX; // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
    // or live (ENVIRONMENT_PRODUCTION)

    private static final String CONFIG_CLIENT_ID =
            "AbNv_uDfHr13Q-JS3Td-IB1taAW5IT9_Iv8XOIt-Nbeoy_McgqDfzPOaD2oh1phTp6zsXHNtJU4ckElw"; //for PaypalExample

    private static final int PAYPAL_REQUEST_CODE = 7171; //request

    private static PayPalConfiguration configuration = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
            // Configuracion minima
//            .merchantName("Translate Voice")
            .merchantName("Example Paypal")
            .merchantPrivacyPolicyUri(
                    Uri.parse("https://www.ticktalksoft.com/policy"))
            .merchantUserAgreementUri(
                    Uri.parse("https://www.ticktalksoft.com"));
    //End Paypal Config

    private View view;
    private AppCompatActivity activity;
    private PaymentDetailsFragment paymentDetailsFragment;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.image_source_language)
    ImageView imageSourceLanguage;

    @BindView(R.id.text_source_language)
    TextView textSourceLanguage;

    @BindView(R.id.text_traduced_source_language)
    TextView textTraducedSourdeLanguage;

    @BindView(R.id.image_target_language)
    ImageView imageTargetLanguage;

    @BindView(R.id.text_target_language)
    TextView textTargetLanguage;

    @BindView(R.id.text_instructions_purchase_optional)
    TextView textInstructionsPurchaseOptional;

    @BindView(R.id.tone_purchase)
    TextView tonePurchase;

    @BindView(R.id.topic_purchase)
    TextView topicPurchase;

    @BindView(R.id.author_gender_purchase)
    TextView authorPurchase;

    @BindView(R.id.audience_gender_purchase)
    TextView audiencePurchase;

    @BindView(R.id.total_words_purchase)
    TextView totalWordsPurchase;

    @BindView(R.id.total_price_purchase)
    TextView totalPricePurchase;

//    @BindView(R.id.textView)
//    TextView textView;

    @BindView(R.id.button_paypal)
    Button bPaypal;

    private String target="";
    private String text="";
    private String source="";
    private String instructions="";
    private String topic="";
    private String tone="";
    private String author="";
    private String audience="";

    private Double price;
    private String words="";


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
    public void onDestroy(){
        if(activity!=null){
            activity.stopService(new Intent(activity, PayPalService.class));
        }
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_purchase, container, false);
        ButterKnife.bind(this, view);

        Bundle args = getArguments();
        if(args != null) {
            target = args.getString(TARGET);
            text = args.getString(TEXT);
            source = args.getString(SOURCE);
            topic = args.getString(TOPIC);
            tone = args.getString(TONE);
            author = args.getString(AUTHOR);
            audience = args.getString(AUDIENCE);
            price = Double.valueOf(args.getString(PRICE));
            words = args.getString(WORDS);


//            textView.setText(
//                    "Idioma origen: " + target + "\n" +
//                    "Compra text: " + text + "\n" +
//                    "Idioma destino: " + source + "\n" +
//                    "Topic: " + topic + "\n" +
//                    "Tono: " + tone + "\n" +
//                    "Autor: " + author + "\n" +
//                    "Audience: " + audience + "\n" +
//                    "precio total: " + String.format("%.3f", price) + "$" + "\n" +
//                    "palabras totales: " + words + "\n");

            imageSourceLanguage.setImageResource(flags[language_source_pos]);
            textSourceLanguage.setText(target);
            textTraducedSourdeLanguage.setText(text);

            imageTargetLanguage.setImageResource(flags[language_target_pos]);
            textTargetLanguage.setText(source);

            textInstructionsPurchaseOptional.setText(instructions_language);

            topicPurchase.setText(topic_language);
            tonePurchase.setText(tone_language);
            authorPurchase.setText(author_gender);
            audiencePurchase.setText(audience_gender);

            totalWordsPurchase.setText("Total words:  " + total_words);
            totalPricePurchase.setText("Total price:  $" + total_precio);

            Intent intent = new Intent(activity, PayPalService.class);
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
            activity.startService(intent);

            bPaypal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    processPayment();
//                    startActivity(new Intent(activity, _PaymentDetails.class)
//                            .putExtra("PaymentDetails", text)
//                            .putExtra("PaymentAmount", price));

                }
            });

        }
        initToolbar();

        return view;
    }

    private void initToolbar(){
        toolbar.inflateMenu(R.menu.menu_human);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        toolbar.setTitle("TranslateTo");
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                activity.onBackPressed();
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                SettingActivity.startSettingActivity(activity);
                break;
        }
        return true;
    }

    private void processPayment(){

        PayPalPayment toBuy = new PayPalPayment(new BigDecimal(price), "USD",
                "TranslateTo", PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(activity, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, toBuy);

        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
//                Toast.makeText(activity, "Pagao ResultOK", Toast.LENGTH_SHORT).show();

                PaymentConfirmation confirm = data
                        .getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
//                    Toast.makeText(activity, "Pagao confirm!=null", Toast.LENGTH_SHORT).show();

                    try {
                        String paymentDetails = confirm.toJSONObject().toString(4);
//                        Toast.makeText(activity, "Pagao try", Toast.LENGTH_SHORT).show();

                        Log.d("CogerDatosPaypal", paymentDetails);

                        String precio = String.valueOf(price);


                        startActivity(new Intent(activity, _PaymentDetails.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", precio));


//                        if(paymentDetailsFragment == null) {
//                            paymentDetailsFragment = new PaymentDetailsFragment();
//                        }
//                        Bundle args = new Bundle();
//                        args.putString(PaymentDetailsFragment.PAYMENT_DETAILS, paymentDetails);
//                        args.putString(PaymentDetailsFragment.PAYMENT_AMOUNT, precio);
//                        paymentDetailsFragment.setArguments(args);
//
//                        activity.getSupportFragmentManager()
//                                .beginTransaction()
//                                .replace(R.id.human_fragment_root, paymentDetailsFragment,
//                                        PaymentDetailsFragment.TAG)
//                                .commit();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
//                Toast.makeText(activity, "Pagao PAYPAL_REQUEST_CODE", Toast.LENGTH_SHORT).show();

            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(activity, "Cancel user payment", Toast.LENGTH_SHORT).show();
            }

        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Toast.makeText(activity, "Invalid extras", Toast.LENGTH_SHORT).show();
        }
    }

//    @Override
//    public void onErrorResponse(VolleyError error) {
//        if (error instanceof AuthFailureError){
//            Log.e("ErrorResponse", "Error credentials " + error.getMessage());
//        } else if(error instanceof NetworkError){
//            Log.e("ErrorResponse", "Error network " + error.getMessage());
//        } else if (error instanceof NoConnectionError){
//            Log.e("ErrorResponse", "Error no conection " + error.getMessage());
//        } else if (error instanceof ParseError){
//            Log.e("ErrorResponse", "Error no process the response " + error.getMessage());
//        } else if (error instanceof ServerError){
//            Log.e("ErrorResponse", "Error from server " + error.getMessage());
//        } else if (error instanceof TimeoutError){
//            Log.e( "ErrorResponse", "Error out of time " + error.getMessage());
//        }
//        Toast.makeText(getActivity(), "Something has happened in server.", Toast.LENGTH_SHORT).show();
//    }

    //Recibimos un tipo string y deberemos convertirlo a objeto
//    @Override
//    public void onResponse(String response) {
//
//        GsonBuilder builder = new GsonBuilder();
//        builder.setExclusionStrategies(new DefaultExclusionStrategy());
//
//        //Creacion de objeto gson con builder
//        Gson gson = builder.create();
//        Gson gsonSynonym = builder.create();
//
//        Log.d("CogerDatosPaypa2", response+"");


        // Recogemos la respuesta del WebService
//        model = gson.fromJson(response, HumanPojo.class);
//
//        if(model.getFindEnd().equals("200")){
//            Toast.makeText(this, "Insert ok in DB.", Toast.LENGTH_SHORT).show();
//
//        }else if(model.getFindEnd().equals("206")){
//            Toast.makeText(this, "Inactive user.", Toast.LENGTH_SHORT).show();
//        }
//        else if(model.getFindEnd().equals("209")){
//            Toast.makeText(this, "Pass or email wrong.", Toast.LENGTH_SHORT).show();
//        }
//        else {
//            Toast.makeText(this, "Something has happened in server db.", Toast.LENGTH_SHORT).show();
//        }



//    }



}
