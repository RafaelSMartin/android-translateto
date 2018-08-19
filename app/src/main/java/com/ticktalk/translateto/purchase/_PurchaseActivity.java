package com.ticktalk.translateto.purchase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.ticktalk.translateto.R;
import com.ticktalk.translateto.setting.SettingActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Rafael S. Martin
 */

public class _PurchaseActivity extends AppCompatActivity implements
        Toolbar.OnMenuItemClickListener {

    public static final String TAG = "PURCHASE_FRAGMENT";
    public static String TEXT = "text_bundle";
    public static String TARGET = "target_bundle";
    public static String SOURCE = "source_bundle";
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

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.textView)
    TextView textView;

    @BindView(R.id.button_paypal)
    Button bPaypal;

    private String text = "";
    private String target = "";
    private String source = "";
    private Double price;
    private String priceString = "";
    private String words = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_purchase);
        ButterKnife.bind(this);

        initToolbar();

        Intent i = getIntent();

        text = i.getStringExtra(TEXT);
        target = i.getStringExtra(TARGET);
        source = i.getStringExtra(SOURCE);

        priceString = i.getStringExtra(PRICE);
        price = Double.valueOf(i.getStringExtra(PRICE));

        words = i.getStringExtra(WORDS);


        textView.setText("Compra text: " + text + "\n" +
                "Idioma origen: " + target + "\n" +
                "idioma destino: " + source + "\n" +
                "precio total: " + price + "$" + "\n" +
                "palabras totales: " + words + "\n");

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        startService(intent);

        bPaypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processPayment();
            }
        });

    }

    @Override
    public void onDestroy(){
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }


    private void initToolbar(){
        toolbar.inflateMenu(R.menu.menu_human);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        toolbar.setTitle("Paypal");
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onBackPressed();
            }
        });
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

    private void processPayment(){

        PayPalPayment toBuy = new PayPalPayment(new BigDecimal(price), "USD",
                "Name of product for PaypalExample", PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, toBuy);

        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Pagao ResultOK", Toast.LENGTH_SHORT).show();

                PaymentConfirmation confirm = data
                        .getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    Toast.makeText(this, "Pagao confirm!=null", Toast.LENGTH_SHORT).show();

                    try {
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Toast.makeText(this, "Pagao try", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(this, _PaymentDetails.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", priceString));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Toast.makeText(this, "Pagao PAYPAL_REQUEST_CODE", Toast.LENGTH_SHORT).show();

            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Cancel user payment", Toast.LENGTH_SHORT).show();
            }

        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Toast.makeText(this, "Invalid extras", Toast.LENGTH_SHORT).show();
        }
//    super.onActivityResult(requestCode, resultCode, data);
    }



}
