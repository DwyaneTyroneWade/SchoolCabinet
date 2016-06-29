package com.xiye.schoolcabinet.utils.net;

import com.android.volley.Response;
import com.xiye.schoolcabinet.beans.CardInfoBean;
import com.xiye.schoolcabinet.utils.SCUtils;
import com.xiye.sclibrary.net.volley.GsonRequest;

/**
 * Created by wushuang on 6/28/16.
 */
public class RequestFactory {
    public static GsonRequest<CardInfoBean> getAllCardInfoRequest(Response.Listener<CardInfoBean> listener, Response.ErrorListener errorListener, String cabinetId) {
        String url = ServerConstants.SERVER_URL + ServerConstants.URL_GET_ALL_CARD_INFO + cabinetId;
        GsonRequest<CardInfoBean> request = GsonRequest.newGsonGetRequest(url, listener, errorListener, SCUtils.getDefaultHeaders(), CardInfoBean.class);
        return request;
    }
}
