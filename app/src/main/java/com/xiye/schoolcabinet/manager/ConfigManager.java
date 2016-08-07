package com.xiye.schoolcabinet.manager;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.xiye.schoolcabinet.base.BaseActivity;
import com.xiye.schoolcabinet.beans.CardInfo;
import com.xiye.schoolcabinet.beans.CardInfoBean;
import com.xiye.schoolcabinet.utils.net.RequestFactory;
import com.xiye.schoolcabinet.utils.net.ServerConstants;
import com.xiye.sclibrary.listener.BaseCallBackListener;
import com.xiye.sclibrary.net.volley.GsonRequest;
import com.xiye.sclibrary.utils.Tools;

import java.util.List;

/**
 * Created by wushuang on 6/28/16.
 */
public class ConfigManager {
    public static void getAllCardInfo(final String cabinetId, final BaseActivity activity, final GetAllCardInfoCallBack callBack) {
        if (activity == null) {
            return;
        }

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
                    ConfigManager.setCabinetId(cabinetId);
                    saveCardInfoToDB(bean);
                    //download bg
                    if (bean.results != null) {
                        MainBackgroundImageManager.download(bean.results.url);
                    }
                    if (callBack != null) {
                        callBack.onGetDataSuc(bean);
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
        CacheManager.setSerializableCache(CacheManager.CACHE_KEY_CARD_INFO_BEAN, setRealCardAndStudentId(bean));
    }

    /**
     * 处理card_id,"card_id": "129998899989_000003"前CARDID后STUDENTID
     *
     * @param bean
     * @return
     */
    private static CardInfoBean setRealCardAndStudentId(CardInfoBean bean) {
        if (bean.results != null) {
            List<CardInfo> cardInfoList = bean.results.card_info;
            if (cardInfoList != null && cardInfoList.size() > 0) {
                for (int i = 0; i < cardInfoList.size(); i++) {
                    CardInfo cardInfo = cardInfoList.get(i);
                    if (cardInfo != null) {
                        String card_id = cardInfo.card_id;
                        if (!Tools.isStringEmpty(card_id)) {
                            String[] arr = card_id.split("_");
                            if (arr != null && arr.length == 2) {
                                cardInfo.realCardId = arr[0];
                                cardInfo.realStudentId = arr[1];
                            }
                        }
                    }
                }
            }
        }

        return bean;
    }


    public static CardInfoBean getCardInfoFromDB() {
        return (CardInfoBean) CacheManager.loadSerializableCache(CacheManager.CACHE_KEY_CARD_INFO_BEAN);
    }

    public static String getCabinetId() {
        return CacheManager.loadCache(CacheManager.CACHE_KEY_CABINET_ID);
    }

    public static void setCabinetId(String cabinetId) {
        CacheManager.setCache(CacheManager.CACHE_KEY_CABINET_ID, cabinetId);
    }

    public static String getBoxType() {
        CardInfoBean bean = getCardInfoFromDB();
        if (bean != null && bean.results != null) {
            return bean.results.boxtype;
        } else {
            return "";
        }
    }

    public static String getClassName() {
        CardInfoBean bean = getCardInfoFromDB();
        if (bean != null && bean.results != null) {
            String school = bean.results.school;
            String classname = bean.results.className;
            StringBuilder builder = new StringBuilder();
            builder.append(school);
            builder.append(classname);
            return builder.toString();
        } else {
            return "";
        }
    }

    public static int getBoxTotal() {
        CardInfoBean bean = getCardInfoFromDB();
        if (bean != null && bean.results != null) {
            String boxTotal = bean.results.boxtotal;
            if (Tools.isStringEmpty(boxTotal)) {
                return 0;
            } else {
                return Integer.parseInt(boxTotal.trim());
            }
        } else {
            return 0;
        }
    }

    public interface GetAllCardInfoCallBack extends BaseCallBackListener {
        void onGetDataSuc(CardInfoBean bean);
    }
}
