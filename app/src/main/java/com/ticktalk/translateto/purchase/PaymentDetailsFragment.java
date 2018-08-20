package com.ticktalk.translateto.purchase;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ticktalk.translateto.R;
import com.ticktalk.translateto.remote.DefaultExclusionStrategy;
import com.ticktalk.translateto.remote.model.HumanPojo;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rafael S. Martin
 */

public class PaymentDetailsFragment extends Fragment implements
        Response.ErrorListener,
        Response.Listener<String>
{

    public static final String TAG = "PAYMENT_DETAILS_FRAGMENT";
    public static final String PAYMENT_DETAILS = "PaymentDetails";
    public static final String PAYMENT_AMOUNT = "PaymentAmount";
    public static final String PAYMENT_RESPONSE = "response";

    private View view;
    private AppCompatActivity activity;

    @BindView(R.id.txt_id)
    TextView txtId;
    @BindView(R.id.txt_amount)
    TextView txtAmount;
    @BindView(R.id.txt_status)
    TextView txtStatus;

    private String jsonStringDetails, jsonStringResponse, amount;
    private HumanPojo model;

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
        view = inflater.inflate(R.layout.payment_details, container, false);
        ButterKnife.bind(this, view);


//        SendHuman sendHuman = new SendHuman(Constant.url, Constant.accessHuman,
//                email_in, password_in, language_source, language_target,
//                text_translation, numero_language,instructions_language,
//                topic_language, tone_language, author_gender, audience_gender,
//                total_precio, total_words);
//        Request<?> request = sendHuman.getRequest(this, this);
//        App.getInstance().addToRequestQueue(request, App.TAG);


        Bundle args = getArguments();
        if(args != null) {
            jsonStringDetails = args.getString(PAYMENT_DETAILS);
//            jsonStringResponse = args.getString(PAYMENT_RESPONSE);
            amount = args.getString(PAYMENT_AMOUNT);
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonStringDetails);
            showDetails(jsonObject.getJSONObject(PAYMENT_RESPONSE),
                    amount);

        }catch (JSONException e){
            e.printStackTrace();
        }

        return view;
    }

    private void showDetails(JSONObject response, String paymentAmount) {
        try{
            Log.d("VueltaPaypal", response.toString());
            txtId.setText(response.getString("id"));
            txtStatus.setText(response.getString("state"));
            txtAmount.setText("USD " + String.format("%.3f", paymentAmount));
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
        Toast.makeText(activity, "Something has happened in server.", Toast.LENGTH_SHORT).show();
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


        // Recogemos la respuesta del WebService
        model = gson.fromJson(response, HumanPojo.class);


//        String keyTokenFromServer = model.getKeyToken();
//        Log.d("PaymenDetailsFragment", keyTokenFromServer+"");


    }


}
