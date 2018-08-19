package com.ticktalk.translateto.webservicesvolley.sendpojo;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.ticktalk.translateto.webservicesvolley.LongTimeoutAndTryRetryPolicy;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * Modelo que enviara y recibira los datos
 *
 * */

public class SendLogin {

    private String url;
    private String access;

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    private String ultimoLogin;
    private String userAgent;
    private String fechaCreacion;
    private String lenguajeNativo;

    private String session;
    private String premium;


    // Creacion de variable que contiene la direccion del webService
//    private String url = "http://82.223.37.214/rest/rest.php";

//    private String url = "http://82.223.37.214/rest/restlogin3.php";


    public SendLogin(String url, String access,
                     String firstName, String lastName, String email, String password,
                     String userAgent, String lenguajeNativo,
                     String session, String premium){
        this.url = url;
        this.access = access;

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;

        this.ultimoLogin = ultimoLogin;
        this.userAgent = userAgent;
        this.fechaCreacion = fechaCreacion;
        this.lenguajeNativo = lenguajeNativo;

        this.session = session;
        this.premium = premium;
    }

    //Peticion de webService con dos parametros
    public StringRequest getRequest(Response.Listener<String> responseListener,
                                    Response.ErrorListener errorListener){

//        Esto es lo que se manda al webService
        final HashMap<String, String> credentials = new HashMap<>();

        // Solo envio un parametro por lo que solo habra una credencial,
        // que recibe Num el webService como parametro y variable text
        credentials.put("access", access);

        credentials.put("first_name_in", firstName);
        credentials.put("last_name_in", lastName);
        credentials.put("email_in", email);
        credentials.put("password_in", password);
        credentials.put("language_in", lenguajeNativo);

        credentials.put("UserAgent", userAgent);
        credentials.put("code_lenguaje_in", lenguajeNativo);

        credentials.put("Sesion", session);
        credentials.put("Premium", premium);




        // Creacion de variable
        StringRequest request = new StringRequest(Request.Method.POST, url, responseListener,
                errorListener){
            @Override
            public String getBodyContentType(){
                // Condificacion que va a tener UTF8
                return "application/json charset="+getParamsEncoding();
            }
//            @Override
//            protected  HashMap<String, String> getParams(){
//                //Esto es lo que se manda al webService
////        final HashMap<String, String> credentials = new HashMap<>();
//
//                // Solo envio un parametro por lo que solo habra una credencial,
//                // que recibe Num el webService como parametro y variable text
//                credentials.put("Acceso", access);
//
//                credentials.put("Nombre", firstName);
//                credentials.put("Apellido", lastName);
//                credentials.put("Email", email);
//                credentials.put("Password", password);
//
//                credentials.put("IpAddress", ipAddress);
//                credentials.put("UserAgent", userAgent);
//                credentials.put("LenguajeNativo", lenguajeNativo);
//
//                credentials.put("Sesion", session);
//                credentials.put("Premium", premium);
//
//                return credentials;
//            }
            @Override
            public HashMap<String, String> getHeaders() throws AuthFailureError{
                final HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("apiKey", "xxxxxxxxxxxxxxx");

                return headers;
            }
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
        request.setRetryPolicy(new LongTimeoutAndTryRetryPolicy(
                LongTimeoutAndTryRetryPolicy.RETRIES_PHONE_ISP));
        return request;
    }





}

