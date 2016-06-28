package com.xiye.schoolcabinet.delegates.admin;

import com.xiye.schoolcabinet.base.BaseActivity;
import com.xiye.schoolcabinet.beans.Card;
import com.xiye.schoolcabinet.database.manager.CardTableManager;
import com.xiye.schoolcabinet.manager.CacheManager;
import com.xiye.schoolcabinet.manager.DBManager;
import com.xiye.sclibrary.base.L;
import com.xiye.sclibrary.base.LibConstants;
import com.xiye.sclibrary.net.needle.Needle;
import com.xiye.sclibrary.net.needle.UiRelatedTask;

import java.util.ArrayList;

/**
 * Created by wushuang on 6/7/16.
 */
public class AdminActivityDelegate {
    private BaseActivity activity;
    private AdminActivityCallBack callBack;
    private CardTableManager dbManager;

    public AdminActivityDelegate() {

    }

    public AdminActivityDelegate(BaseActivity activity, AdminActivityCallBack callBack) {
        this.activity = activity;
        this.callBack = callBack;
    }

    public void register(final String cardId) {
        Needle.onBackgroundThread().serially().withTaskType(LibConstants.NEEDLE_TYPE_DATABASE).execute(new UiRelatedTask<Card>() {
            @Override
            protected Card doWork() {
                dbManager = new CardTableManager();
                ArrayList<Card> list = dbManager.get();
                Card card = isCardIdExist(cardId, list);
                if (card == null) {
                    card = new Card();
                    card.cardId = cardId;
                    card.cabinetId = String.valueOf(CacheManager.loadIntCache(CacheManager.CACHE_KEY_CABINET_ID_ALREADY) + 1);
                    boolean isUpdate = dbManager.saveBoolean(card);
                    if (!isUpdate) {
                        CacheManager.setIntCache(CacheManager.CACHE_KEY_CABINET_ID_ALREADY, Integer.valueOf(card.cabinetId));
                    }
                }
                ArrayList<Card> listDB = dbManager.get();
                DBManager.getInstance().setList(listDB);
                for (int i = 0; i < listDB.size(); i++) {
                    L.d("cardid:" + listDB.get(i).cardId);
                    L.d("cabinetid:" + listDB.get(i).cabinetId);
                }
                return card;
            }

            @Override
            protected void thenDoUiRelatedWork(Card card) {
                if (callBack != null) {
                    callBack.onRegisterSuc();
                }
            }
        });
    }

    private Card isCardIdExist(String cardId, ArrayList<Card> list) {
        Card card = null;
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).cardId.equals(cardId)) {
                    card = list.get(i);
                    break;
                }
            }
        }

        return card;
    }

    public interface AdminActivityCallBack {
        void onRegisterSuc();
    }
}
