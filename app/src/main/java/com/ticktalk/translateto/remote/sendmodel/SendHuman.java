package com.ticktalk.translateto.remote.sendmodel;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.ticktalk.translateto.remote.LongTimeoutAndTryRetryPolicy;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * Modelo que enviara y recibira los datos
 *
 * */

public class SendHuman {

    private String url;
    private String access;
    private String email_in;
    private String password_in;

    private String language_source;
    private String language_target;
    private String text_translation;
    private String numero_language;

    private String instructions_language;

    private String topic_language;
    private String tone_language;
    private String author_gender;
    private String audience_gender;

    private String total_precio;
    private String total_words;

    private String payer_email, payer_fname, payer_lname, payer_id, payer_address, payer_city,
            payer_state, payer_country_code, payer_zip, currency, payment_fee, payment_gross,
            payment_status, payment_item, qty, date, payer_street;


    public SendHuman(String url, String access, String email_in, String password_in, String language_source,
                     String language_target, String text_translation, String numero_language,
                     String instructions_language, String topic_language, String tone_language,
                     String author_gender, String audience_gender, String total_precio, String total_words,
                     String payer_email, String payer_fname, String payer_lname, String payer_id,
                     String payer_address, String payer_city, String payer_state, String payer_country_code,
                     String payer_zip, String currency, String payment_fee, String payment_gross,
                     String payment_status, String payment_item, String qty, String date, String payer_street){
        this.url = url;
        this.access = access;

        this.email_in = email_in;
        this.password_in = password_in;
        this.language_source = language_source;
        this.language_target = language_target;
        this.text_translation = text_translation;
        this.numero_language = numero_language;
        this.instructions_language = instructions_language;

        this.topic_language = topic_language;
        this.tone_language = tone_language;
        this.author_gender = author_gender;
        this.audience_gender = audience_gender;

        this.total_precio = total_precio;
        this.total_words = total_words;

        this.payer_email = payer_email;
        this.payer_fname = payer_fname;
        this.payer_lname = payer_lname;
        this.payer_id = payer_id;
        this.payer_address = payer_address;
        this.payer_city = payer_city;
        this.payer_state = payer_state;
        this.payer_country_code = payer_country_code;
        this.payer_zip = payer_zip;
        this.currency = currency;
        this.payment_fee = payment_fee;
        this.payment_gross = payment_gross;
        this.payment_status = payment_status;
        this.payment_item = payment_item;
        this.qty = qty;
        this.date = date;
        this.payer_street = payer_street;
    }

    public StringRequest getRequest(Response.Listener<String> responseListener,
                                    Response.ErrorListener errorListener){

        final HashMap<String, String> credentials = new HashMap<>();

        //Envio de cabeceras
//        credentials.put("Content-Type", "application/json");
//        credentials.put("User-agent", "My useragent");

        // Solo envio un parametro por lo que solo habra una credencial,
        // que recibe Texto el webService como parametro y variable text
        credentials.put("access", access);

        credentials.put("email_in", email_in);
        credentials.put("password_in", password_in);
        credentials.put("language_source", language_source);
        credentials.put("language_target", language_target);
        credentials.put("text_translation", text_translation);
        credentials.put("numero_language", numero_language);
        credentials.put("instructions_language", instructions_language);

        credentials.put("topic_language", topic_language);
        credentials.put("tone_language", tone_language);
        credentials.put("author_gender", author_gender);
        credentials.put("audience_gender", audience_gender);

        credentials.put("total_precio", total_precio);
        credentials.put("total_words", total_words);

        credentials.put("payer_email", payer_email);
        credentials.put("payer_fname", payer_fname);
        credentials.put("payer_lname", payer_lname);
        credentials.put("payer_id", payer_id);
        credentials.put("payer_address", payer_address);
        credentials.put("payer_city", payer_city);
        credentials.put("payer_state", payer_state);
        credentials.put("payer_country_code", payer_country_code);
        credentials.put("payer_zip", payer_zip);
        credentials.put("currency", currency);
        credentials.put("payment_fee", payment_fee);
        credentials.put("payment_gross", payment_gross);
        credentials.put("payment_status", payment_status);
        credentials.put("payment_item", payment_item);
        credentials.put("qty", qty);
        credentials.put("date", date);
        credentials.put("payer_street", payer_street);

        StringRequest request = new StringRequest(Request.Method.POST, url,
                responseListener, errorListener){
            @Override
            public String getBodyContentType(){
                return "application/json charset="+getParamsEncoding();
            }


            @Override
            public byte[] getBody(){
                try {
                    return new JSONObject(credentials).toString().getBytes(getParamsEncoding());
                } catch (UnsupportedEncodingException e){

                }
                return null;
            }
        };
        request.setRetryPolicy(new LongTimeoutAndTryRetryPolicy(
                LongTimeoutAndTryRetryPolicy.RETRIES_PHONE_ISP));
//        Log.d("HumanSendRequest", credentials.toString()+"");
        return request;
    }


}
