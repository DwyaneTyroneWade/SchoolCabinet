package com.xiye.schoolcabinet.database.manager;

import android.database.Cursor;

import com.xiye.schoolcabinet.beans.Card;
import com.xiye.schoolcabinet.database.SCDatabaseUtils;

import java.util.ArrayList;

public class CardTableManager {

    public ArrayList<Card> get() {
        // query
        Cursor cursor = SCDatabaseUtils.queryCard();
        if (cursor != null && cursor.getCount() > 0) {
            // get
            ArrayList<Card> cardDB = SCDatabaseUtils
                    .getCards(cursor);
            cursor.close();
            return cardDB;
        }
        return null;
    }

    private void save(ArrayList<Card> cardArr) {
        if (cardArr != null && cardArr.size() > 0) {
            for (int i = 0; i < cardArr.size(); i++) {
                Card cardItem = cardArr.get(i);
                save(cardItem);
            }
        }
    }

    public void save(Card cardItem) {
        if (cardItem != null) {
            Cursor c = SCDatabaseUtils.queryCard(cardItem);
            if (c != null) {
                if (c.getCount() > 0) {
                    // update
                    SCDatabaseUtils.updateCard(cardItem);
                } else {
                    // insert
                    SCDatabaseUtils.insertCard(cardItem);
                }
                c.close();
            } else {
                // insert
                SCDatabaseUtils.insertCard(cardItem);
            }
        }
    }

    public boolean saveBoolean(Card cardItem) {
        boolean isUpdate = false;
        if (cardItem != null) {
            Cursor c = SCDatabaseUtils.queryCard(cardItem);
            if (c != null) {
                if (c.getCount() > 0) {
                    // update
                    isUpdate = true;
                    SCDatabaseUtils.updateCard(cardItem);
                } else {
                    // insert
                    SCDatabaseUtils.insertCard(cardItem);
                }
                c.close();
            } else {
                // insert
                SCDatabaseUtils.insertCard(cardItem);
            }
        }
        return isUpdate;
    }

    private void insert(Card cardItem) {
        if (cardItem != null) {
            SCDatabaseUtils.insertCard(cardItem);
        }
    }

    public void saveAfterDeleteAll(ArrayList<Card> cardArr) {
        SCDatabaseUtils.deleteCard();
        save(cardArr);
    }

    // delete同样存在id 为空的问题 解决 save之后返回的id补全
    public void delete(Card card) {
        SCDatabaseUtils.deleteCard(card);
    }

    public void clear() {
        SCDatabaseUtils.deleteCard();
    }

    public boolean isCard() {
        boolean isCard = false;
        ArrayList<Card> cardArrDB = get();
        if (cardArrDB != null && cardArrDB.size() > 0) {
            isCard = true;
        }
        return isCard;
    }
}
