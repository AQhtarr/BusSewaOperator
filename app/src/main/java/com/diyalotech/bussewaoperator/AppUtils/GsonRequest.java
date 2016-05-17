/**
 * Copyright 2013 Ognyan Bankov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.diyalotech.bussewaoperator.AppUtils;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;


public class GsonRequest<T> extends Request<T> {

    private Gson gson = new Gson();
    private Class<T> clazz;
    private Map<String, String> headers;
    private Map<String, String> params;
    private Listener<T> listener;
    JSONObject jsonObject=null;

    /**
     * Make a GET request and return a parsed object from JSON.
     *
     * @param url URL of the request to make
     * @param clazz Relevant class object, for Gson's reflection
     */
    public GsonRequest(int method,
                       String url,
                       Class<T> clazz,
                       Listener<T> listener,
                       ErrorListener errorListener) {
        super(method, url, errorListener);
        this.clazz = clazz;
        this.listener = listener;
        gson = new Gson();
    }

    /**
     * Make a POST request and return a parsed object from JSON.
     *
     * @param url URL of the request to make
     * @param clazz Relevant class object, for Gson's reflection
     */
    public GsonRequest(int method,
                       String url,
                       Class<T> clazz,
                       Map<String, String> headers,
                       JSONObject jsonObject,
                       Listener<T> listener,
                       ErrorListener errorListener) {

        super(method, url, errorListener);
        this.jsonObject = jsonObject;
        this.listener = listener;
        this.clazz = clazz;
        this.headers = headers;
        gson = new Gson();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return (headers != null) ? headers : super.getHeaders();
    }

    @Override
    public String getBodyContentType() {
        return "application/json; charset=utf-8";
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }
//
//    @Override
//    public String getBodyContentType() {
//        return "application/x-www-form-urlencoded";
//    }
//


    @Override
    public byte[] getBody() throws AuthFailureError {
        try {
            return jsonObject.toString().getBytes(getParamsEncoding());
        } catch (UnsupportedEncodingException e) {

        }
        return null;
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {

        String json="";
        try {
                 json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            AppLog.showLog("Json String::::::: "+json);
            Log.i("RESPONSE3:::::", json);
                return Response.success(gson.fromJson(json, clazz), HttpHeaderParser.parseCacheHeaders(response));


        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}
