package com.ticktalk.translateto.purchase;

/**
 * Created by Rafael S. Martin
 */
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.ticktalk.translateto.R;
import com.ticktalk.translateto.SessionPrefs;
import com.ticktalk.translateto.utils.Constant;
import com.ticktalk.translateto.utils.Utils;
import com.ticktalk.translateto.webservicesvolley.DefaultExclusionStrategy;
import com.ticktalk.translateto.webservicesvolley.pojo.HumanPojo;
import com.ticktalk.translateto.webservicesvolley.sendpojo.SendHuman;

import org.json.JSONException;
import org.json.JSONObject;

import static com.ticktalk.translateto.fragments.HumanTranslationFragment.audience_genderPos;
import static com.ticktalk.translateto.fragments.HumanTranslationFragment.author_genderPos;
import static com.ticktalk.translateto.fragments.HumanTranslationFragment.email_in;
import static com.ticktalk.translateto.fragments.HumanTranslationFragment.instructions_language;
import static com.ticktalk.translateto.fragments.HumanTranslationFragment.language_source;
import static com.ticktalk.translateto.fragments.HumanTranslationFragment.language_target;
import static com.ticktalk.translateto.fragments.HumanTranslationFragment.numero_language;
import static com.ticktalk.translateto.fragments.HumanTranslationFragment.password_in;
import static com.ticktalk.translateto.fragments.HumanTranslationFragment.text_translation;
import static com.ticktalk.translateto.fragments.HumanTranslationFragment.tone_languagePos;
import static com.ticktalk.translateto.fragments.HumanTranslationFragment.topic_languagePos;
import static com.ticktalk.translateto.fragments.HumanTranslationFragment.total_precio;
import static com.ticktalk.translateto.fragments.HumanTranslationFragment.total_words;

public class _PaymentDetails extends AppCompatActivity
        implements
        Response.ErrorListener,
        Response.Listener<String>
{

    TextView txtId, txtAmount, txtStatus;
    Button back;
    private HumanPojo model;
    private String payer_id, payment_status, date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_details);

        txtId = (TextView)findViewById(R.id.txt_id_number);
        txtAmount = (TextView)findViewById(R.id.txt_amount_number);
        txtStatus = (TextView)findViewById(R.id.txt_status_number);
        back = (Button)findViewById(R.id.button_back);

        Intent intent = getIntent();

        try {
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("PaymentDetails"));
            showDetails(jsonObject.getJSONObject("response"), intent.getStringExtra("PaymentAmount"));

        }catch (JSONException e){
            e.printStackTrace();
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.launchActivityNewTask(MainActivity.class,_PaymentDetails.this);
            }
        });

        SendHuman sendHuman = new SendHuman(Constant.url, Constant.accessHuman,
                email_in, password_in,
                language_source, language_target,
                text_translation, numero_language, instructions_language,
                topic_languagePos, tone_languagePos, author_genderPos, audience_genderPos,
                total_precio, total_words,
                email_in, SessionPrefs.get(this).getPrefFname(), SessionPrefs.get(this).getPrefLname(),
                payer_id, "","", "", "", "",
                "USD", "", total_precio, payment_status, "HumanTranslation",
                "1", date, "");

        Request<?> request = sendHuman.getRequest(this, this);
        App.getInstance().addToRequestQueue(request, App.TAG);



    }

    private void showDetails(JSONObject response, String paymentAmount) {
        try{
            Log.d("VueltaPaypal", response.toString());
            payer_id = response.getString("id");
            txtId.setText(payer_id);

            payment_status = response.getString("state");
            txtStatus.setText(payment_status);

            txtAmount.setText("USD " + paymentAmount);

            date = response.getString("create_time");

        }catch (JSONException e){
            e.printStackTrace();
        }
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        if (error instanceof AuthFailureError){
            Log.e("ErrorResponse", "Error credentials " + error.getMessage());
        } else if(error instanceof NetworkError){
            Log.e("ErrorResponse", "Error network " + error.getMessage());
        } else if (error instanceof NoConnectionError){
            Log.e("ErrorResponse", "Error no conection " + error.getMessage());
        } else if (error instanceof ParseError){
            Log.e("ErrorResponse", "Error no process the response " + error.getMessage());
        } else if (error instanceof ServerError){
            Log.e("ErrorResponse", "Error from server " + error.getMessage());
        } else if (error instanceof TimeoutError){
            Log.e( "ErrorResponse", "Error out of time " + error.getMessage());
        }
        Toast.makeText(this, "Something has happened in server.", Toast.LENGTH_SHORT).show();
    }

    //Recibimos un tipo string y deberemos convertirlo a objeto
    @Override
    public void onResponse(String response) {

        GsonBuilder builder = new GsonBuilder();
        builder.setExclusionStrategies(new DefaultExclusionStrategy());

        //Creacion de objeto gson con builder
        Gson gson = builder.create();
        Gson gsonSynonym = builder.create();

        Log.d("PaymenDetailsFragment", response+"");


         //Recogemos la respuesta del WebService
        model = gson.fromJson(response, HumanPojo.class);

        if(model.getFindEnd().equals("200")){
            Toast.makeText(this, "Insert ok in DB.", Toast.LENGTH_SHORT).show();

        }else if(model.getFindEnd().equals("206")){
            Toast.makeText(this, "Inactive user.", Toast.LENGTH_SHORT).show();
        }
        else if(model.getFindEnd().equals("209")){
            Toast.makeText(this, "Pass or email wrong.", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Something has happened in server db.", Toast.LENGTH_SHORT).show();
        }



    }







}
