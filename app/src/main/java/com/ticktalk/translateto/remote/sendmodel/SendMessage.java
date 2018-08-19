package com.ticktalk.translateto.remote.sendmodel;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.ticktalk.translateto.utils.Constant;
import com.ticktalk.translateto.remote.LongTimeoutAndTryRetryPolicy;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * Modelo que enviara y recibira los datos
 *
 * */

public class SendMessage {

    private String text;
    private String fromLanguage;
    private String toLanguage;
    private String access;
    private String unaPalabra;
    private String keyToken;

    // Creacion de variable que contiene la direccion del webService
//    private String url = "http://82.223.37.214/api/translateto";



    public SendMessage(String keyToken, String access, String message, String fromLanguage, String toLanguage, String unaPalabra){
        this.keyToken = keyToken;
        this.text = message;
        this.access = access;
        this.fromLanguage = fromLanguage;
        this.toLanguage = toLanguage;
        this.unaPalabra = unaPalabra;
    }

    //Peticion de webService con parametros
    public StringRequest getRequest(Response.Listener<String> responseListener, Response.ErrorListener errorListener){

        //Esto es lo que se manda al webService
        final HashMap<String, String> credentials = new HashMap<>();

        //Envio de cabeceras
        credentials.put("Content-Type", "application/json");
        credentials.put("User-agent", "My useragent");
        credentials.put("keyToken", keyToken);

        // Solo envio un parametro por lo que solo habra una credencial,
        // que recibe Texto el webService como parametro y variable text
        credentials.put("Acceso", access);
        credentials.put("TextoTraducir", text);
        credentials.put("IdiomaOrigen", fromLanguage);
        credentials.put("IdiomaFinal", toLanguage);
        credentials.put("UnaPalabra", unaPalabra);



        // Creacion de la peticion tipo string request
        StringRequest request = new StringRequest(Request.Method.POST, Constant.urlMain, responseListener, errorListener){
            @Override
            public String getBodyContentType(){
                // Condificacion que va a tener UTF8
                return "application/json charset="+getParamsEncoding();
            }

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
