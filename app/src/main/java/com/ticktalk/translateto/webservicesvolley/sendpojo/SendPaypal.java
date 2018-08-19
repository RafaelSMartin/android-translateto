package com.ticktalk.translateto.webservicesvolley.sendpojo;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.ticktalk.translateto.webservicesvolley.LongTimeoutAndTryRetryPolicy;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * Created by Rafael S. Martin
 */

public class SendPaypal {

    private String url;
    private String id;

    public SendPaypal(String url, String id){
        this.url = url;
        this.id = id;
    }

    //Peticion de webService con parametros
    public StringRequest getRequest(Response.Listener<String> responseListener,
                                    Response.ErrorListener errorListener){

        //Esto es lo que se manda al webService
        final HashMap<String, String> credentials = new HashMap<>();

        //Envio
        credentials.put("Content-Type", "application/json");
        credentials.put("User-agent", "My useragent");

        credentials.put("id", id);



        // Creacion de la peticion tipo string request
        StringRequest request = new StringRequest(Request.Method.GET, url+id+"/execute",
                responseListener, errorListener){
            @Override
            public String getBodyContentType(){
                // Condificacion que va a tener UTF8
                return "application/json charset="+getParamsEncoding();
            }

//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<>();
//                headers.put("Accept", "application/json");
//                headers.put("Accept-Language", "en_US");
//                headers.put("content-type", "application/x-www-form-urlencoded; charset=utf-8");
//                String credentials = id +":"+ PurchaseFragment.CONFIG_CLIENT_ID;
//                String encodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.URL_SAFE | Base64.NO_WRAP);
//                headers.put("Authorization", "Basic " + encodedCredentials);
//                return headers;
//            }


            // es un custom request body que ignora el getParams
            @Override
            public byte[] getBody(){
                // Devuelve un objeto de tipo JSON formado con las credenciales y codificacion
                try {
                    return new JSONObject(credentials).toString().getBytes(getParamsEncoding());
                } catch (UnsupportedEncodingException e){

                }
                return null;
            }
        };
        // Las veces que queremos que intente la transaccion
        request.setRetryPolicy(new LongTimeoutAndTryRetryPolicy(LongTimeoutAndTryRetryPolicy.RETRIES_PHONE_ISP));

        Log.d("MainSendRequest", credentials.toString()+"");


        return request;
    }


}
