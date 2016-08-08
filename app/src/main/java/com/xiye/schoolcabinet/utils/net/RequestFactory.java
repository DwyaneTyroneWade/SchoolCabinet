package com.xiye.schoolcabinet.utils.net;

import com.android.volley.Response;
import com.xiye.schoolcabinet.beans.CardInfoBean;
import com.xiye.schoolcabinet.beans.Record;
import com.xiye.schoolcabinet.beans.RemoteBean;
import com.xiye.schoolcabinet.manager.ConfigManager;
import com.xiye.schoolcabinet.utils.SCUtils;
import com.xiye.sclibrary.net.volley.BaseResultBean;
import com.xiye.sclibrary.net.volley.GsonRequest;
import com.xiye.sclibrary.utils.TimeUtils;

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

    /**
     * 登录
     *
     * @param chestNo
     * @param cardId
     * @param cardPwd
     * @param cardNewPwd
     * @param listener
     * @param errorListener
     * @return
     */
    public static GsonRequest<BaseResultBean> getLoginRequest(String chestNo, String cardId, String cardPwd, String cardNewPwd, Response.Listener<BaseResultBean> listener, Response.ErrorListener errorListener) {
        String operationTime = TimeUtils.getDateTimeyyyy_MM_dd_HH_mm_ss();
        String url = ServerConstants.SERVER_URL + ServerConstants.URL_LOGIN + chestNo + "/" + cardId + "/" + cardPwd + "/" + cardNewPwd + "/" + operationTime;
        GsonRequest<BaseResultBean> request = GsonRequest.newGsonGetRequest(url, listener, errorListener, SCUtils.getDefaultHeaders(), BaseResultBean.class);
        return request;
    }

    /**
     * 上传远程开箱结束
     *
     * @param boxId
     * @param listener
     * @param errorListener
     * @return
     */
    public static GsonRequest<BaseResultBean> getReportRemoteEndRequest(String boxId, Response.Listener<BaseResultBean> listener, Response.ErrorListener errorListener) {
        String cabinetAndBoxId = ConfigManager.getCabinetId() + boxId;
        String url = ServerConstants.SERVER_URL + ServerConstants.URL_REPORT_REMOTE_END + cabinetAndBoxId;
        GsonRequest<BaseResultBean> request = GsonRequest.newGsonGetRequest(url, listener, errorListener, SCUtils.getDefaultHeaders(), BaseResultBean.class);
        return request;
    }

    /**
     * 上传箱子状态
     *
     * @param record
     * @param listener
     * @param errorListener
     * @return
     */
    //chestNo/card_id/box_id/box_status/is_filled/operation_time
    public static GsonRequest<BaseResultBean> getReportBoxStatusRequest(Record record, Response.Listener<BaseResultBean> listener, Response.ErrorListener errorListener) {
        String url = ServerConstants.SERVER_URL + ServerConstants.URL_REPORT_BOX_STATUS
                + record.cabinetId + "/"
                + record.cardId + "/"
                + record.boxId + "/"
                + record.boxDoorStatus + "/"
                + record.boxIsFilled + "/"
                + record.operationTime;
        GsonRequest<BaseResultBean> request = GsonRequest.newGsonGetRequest(url, listener, errorListener, SCUtils.getDefaultHeaders(), BaseResultBean.class);
        return request;
    }
}
