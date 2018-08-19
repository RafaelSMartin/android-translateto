package com.ticktalk.translateto.webservicesvolley;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

/**
 * Interface para acceder a metodos de Volley
 **/

public interface RequestFactory {

    public StringRequest getRequest(Response.Listener<String> responseListener, Response.ErrorListener errorListener);

}
