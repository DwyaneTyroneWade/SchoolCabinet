package com.xiye.sclibrary.net.volley;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.xiye.sclibrary.base.L;
import com.xiye.sclibrary.serializable.Parser;

public class GsonRequest<T> extends Request<T> implements WillCancelDelegate {

    private final Listener<T>   mListener;
    private boolean             mWillCancel = true;
    private Class<T>            mClassName;
    private Map<String, String> mHeaders;
    private String 				mOriginalString;

    public GsonRequest(int method, String url, Listener<T> listener, ErrorListener errListener, Class<T> className) {
        super(method, url, errListener);
        mListener = listener;
        mClassName = className;
        setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public static <T> GsonRequest<T> newGsonGetRequest(String url, Listener<T> listener, ErrorListener errListener, final Map<String, String> headers, Class<T> className) {
        GsonRequest<T> req = new GsonRequest<T>(Method.GET, url, listener, errListener, className) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> existHeaders = super.getHeaders();
                if (headers != null) {
                    existHeaders.putAll(headers);
                }
                return existHeaders;
            }
        };
        return req;
    }

    public static <T> GsonRequest<T> newGsonGetRequest(String url, Listener<T> listener, Class<T> className) {
        return newGsonGetRequest(url, listener, null, null, className);
    }

    public static <T> GsonRequest<T> newGsonPostRequest(String url, final byte[] contentData, Listener<T> listener, ErrorListener errListener, final Map<String, String> headers, Class<T> className) {
        GsonRequest<T> req = new GsonRequest<T>(Method.POST, url, listener, errListener, className) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> existHeaders = super.getHeaders();
                if (headers != null) {
                    existHeaders.putAll(headers);
                }
                return existHeaders;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return contentData;
            }

        };
        return req;
    }

    public static <T> GsonRequest<T> newGsonPostRequest(String url, final byte[] contentData, Listener<T> listener, Class<T> className) {
        return newGsonPostRequest(url, contentData, listener, null, null, className);
    }

    public static <T> GsonRequest<T> newGsonPostRequest(String url, final Map<String, String> contentData, Listener<T> listener, ErrorListener errListener, final Map<String, String> headers,
            Class<T> className) {
        L.d("gson_volley", "url:" + url);
//        L.wtf("gson_volley", "url:" + url);
        GsonRequest<T> req = new GsonRequest<T>(Method.POST, url, listener, errListener, className) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> existHeaders = super.getHeaders();
                if (headers != null) {
                    existHeaders.putAll(headers);
                }
                return existHeaders;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                L.i(contentData);
//                L.wtf("gson_volley", contentData);
                return contentData;
            }

        };
        return req;
    }

    public static <T> GsonRequest<T> newGsonPostRequest(String url, final Map<String, String> contentData, Listener<T> listener, Class<T> className) {
        return newGsonPostRequest(url, contentData, listener, null, null, className);
    }

    public GsonRequest<T> willNotCancel() {
        mWillCancel = false;
        return this;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            L.d("gson_volley", "response:" + parsed);
//            L.wtf("gson_volley", parsed);
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        mOriginalString = parsed;
        mHeaders = response.headers;
        L.d("mHeaders: " + mHeaders);
//        final Gson gson = new GsonBuilder().create();
//        T t = gson.fromJson(parsed, mClassName);
        T t = Parser.parse(parsed, mClassName);
//        updateCookie(mHeaders);
        return Response.success(t, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();
        return headers;
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

    @Override
    public boolean willCancelWhenOnDestroy() {
        return mWillCancel;
    }

    public Map<String, String> getResponseHeaders() {
        return mHeaders;
    }
    
    public String getOriginalString(){
    	return mOriginalString;
    }
    
    private void updateCookie(Map<String, String> headers){
    	String cookieKey = "Set-Cookie";
    	String cookie = headers.get(cookieKey);
    	if (TextUtils.isEmpty(cookie)){
    		return;
    	}
//    	if (cookie.contains("cheng95=")){
//    		LoginAndRegisterManager.saveSessionAfterVerifyVCodeOrDoLoginSuccess(cookie);
//    	}
    }
}
