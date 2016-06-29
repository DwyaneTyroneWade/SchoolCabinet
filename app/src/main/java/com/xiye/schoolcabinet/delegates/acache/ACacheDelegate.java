package com.xiye.schoolcabinet.delegates.acache;

import com.xiye.schoolcabinet.beans.CardInfoBean;
import com.xiye.sclibrary.acache.ACache;
import com.xiye.sclibrary.base.C;

public class ACacheDelegate {
//    public static final String ACACHE_CARD_INFO_BEAN = "acache_card_info_bean";
    private static ACacheDelegate INSTANCE;
    private ACache mCache;

    private ACacheDelegate() {
        mCache = ACache.get(C.get());
    }

    public static ACacheDelegate getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ACacheDelegate();
        }
        return INSTANCE;
    }

//    public void saveCardInfoBean(CardInfoBean bean) {
//        mCache.put(ACACHE_CARD_INFO_BEAN, bean);
//    }
//
//    public CardInfoBean getCardInfoBean() {
//        return (CardInfoBean) mCache.getAsObject(ACACHE_CARD_INFO_BEAN);
//    }
//
//    public void removeCardBeanFromACache() {
//        mCache.remove(ACACHE_CARD_INFO_BEAN);
//    }
}
