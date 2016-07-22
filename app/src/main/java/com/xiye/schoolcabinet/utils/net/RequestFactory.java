package com.xiye.schoolcabinet.utils.net;

import com.android.volley.Response;
import com.xiye.schoolcabinet.beans.CardInfoBean;
import com.xiye.schoolcabinet.beans.RemoteBean;
import com.xiye.schoolcabinet.utils.SCUtils;
import com.xiye.sclibrary.net.volley.GsonRequest;

/**
 * Created by wushuang on 6/28/16.
 */
public class RequestFactory {
    /**
     * 获取所有数据（本柜子的所有信息+本学校的管理员信息+厂商的管理员信息）
     *
     * @param listener
     * @param errorListener
     * @param cabinetId
     * @return
     */
    public static GsonRequest<CardInfoBean> getAllCardInfoRequest(Response.Listener<CardInfoBean> listener, Response.ErrorListener errorListener, String cabinetId) {
        String url = ServerConstants.SERVER_URL + ServerConstants.URL_GET_ALL_CARD_INFO + cabinetId;
        GsonRequest<CardInfoBean> request = GsonRequest.newGsonGetRequest(url, listener, errorListener, SCUtils.getDefaultHeaders(), CardInfoBean.class);
        return request;
    }

    /**
     * 远程开箱
     *
     * @param listener
     * @param errorListener
     * @return
     */
    public static GsonRequest<RemoteBean> getRemoteFromBackStageRequest(Response.Listener<RemoteBean> listener, Response.ErrorListener errorListener) {
        String url = ServerConstants.SERVER_URL + ServerConstants.URL_REMOTE_FROM_BACK_STAGE;
        GsonRequest<RemoteBean> request = GsonRequest.newGsonGetRequest(url, listener, errorListener, SCUtils.getDefaultHeaders(), RemoteBean.class);
        return request;
    }


}
