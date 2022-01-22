package com.fmoreno.countrylistapplication.internet;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class WebApiRequest extends StringRequest {
    private Map<String, String> headers = new HashMap<String, String>();
    private Map<String, String> params = new HashMap<String, String>();

    public WebApiRequest(int method, String url, Response.Listener<String> listener,
                         Response.ErrorListener errorListener, String token) {
        super(method, url, listener, errorListener);
        if (token != null) {
            this.headers.put("Cookie", token);
        }
    }

    public WebApiRequest(int method, String url,
                         Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }


    public WebApiRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener,
                         String token, Map<String, String> params) {
        super(method, url, listener, errorListener);
        if (token != null && params != null) {
            this.headers.put("Cookie", token);
            this.params = params;
        }
    }



    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers;
    }

    /**
     * To add headers in any api request in the form of
     * key and value
     * @param title
     * @param content
     */
    public void setHeader(String title, String content) {
        headers.put(title, content);
    }


    /**
     * To add parameters in the api request
     * @return
     * @throws AuthFailureError
     */
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }

    public void setParam(String title, String content) {
        params.put(title, content);
    }
}
