package com.xiye.schoolcabinet.manager;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.xiye.schoolcabinet.base.BaseActivity;
import com.xiye.schoolcabinet.beans.CardInfoBean;
import com.xiye.schoolcabinet.utils.net.RequestFactory;
import com.xiye.schoolcabinet.utils.net.ServerConstants;
import com.xiye.sclibrary.listener.BaseCallBackListener;
import com.xiye.sclibrary.net.volley.GsonRequest;
import com.xiye.sclibrary.utils.Tools;

/**
 * Created by wushuang on 6/28/16.
 */
public class ConfigManager {
    public static void getAllCardInfo(final BaseActivity activity, final GetAllCardInfoCallBack callBack) {
        if (activity == null) {
            return;
        }

        String cabinetId = getCabinetId();
        if (Tools.isStringEmpty(cabinetId)) {
            return;
        }

        if (callBack != null) {
            callBack.onGetDataStart();
        }

        GsonRequest<CardInfoBean> request = RequestFactory.getAllCardInfoRequest(new Response.Listener<CardInfoBean>() {
            @Override
            public void onResponse(CardInfoBean bean) {
                if (bean != null && bean.err_no == ServerConstants.SUCCESS_CODE) {
                    saveCardInfoToDB(bean);
                    if (callBack != null) {
                        callBack.onGetDataSuc();
                    }
                } else {
                    if (bean != null) {
                        activity.processErrorCode(bean);
                    }
                    if (callBack != null) {
                        callBack.onGetDataFail();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                activity.processVolleyError(volleyError);
                if (callBack != null) {
                    callBack.onGetDataFail();
                }
            }
        }, cabinetId);

        activity.executeRequest(request);
    }

    private static void saveCardInfoToDB(CardInfoBean bean) {
        CacheManager.setSerializableCache(CacheManager.CACHE_KEY_CARD_INFO_BEAN, bean);
    }

    public static CardInfoBean getCardInfoToDB() {
        return (CardInfoBean) CacheManager.loadSerializableCache(CacheManager.CACHE_KEY_CARD_INFO_BEAN);
    }

    public static String getCabinetId() {
        return CacheManager.loadCache(CacheManager.CACHE_KEY_CABINET_ID);
    }

    public static void setCabinetId(String cabinetId) {
        CacheManager.setCache(CacheManager.CACHE_KEY_CABINET_ID, cabinetId);
    }

    public interface GetAllCardInfoCallBack extends BaseCallBackListener {
        void onGetDataSuc();
    }

}
