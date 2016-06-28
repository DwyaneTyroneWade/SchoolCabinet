package com.xiye.schoolcabinet.database;

import android.content.ContentValues;
import android.database.Cursor;

import com.xiye.schoolcabinet.beans.Card;
import com.xiye.schoolcabinet.database.SCTables.CardTable;
import com.xiye.sclibrary.base.C;

import java.util.ArrayList;


public class SCDatabaseUtils {
    /**
     * CardTable**start
     ***/
    public static Cursor queryCard(Card card) {
        return C.get()
                .getContentResolver()
                .query(CardTable.CONTENT_URI, null, CardTable.CARDID + " =? ",
                        new String[]{card.cardId}, null);
    }

    public static Cursor queryCard() {
        return C.get().getContentResolver()
                .query(CardTable.CONTENT_URI, null, null, null, null);
    }

    public static void updateCard(Card card) {
        ContentValues values = appendValues(card);
        C.get().getContentResolver()
                .update(CardTable.CONTENT_URI, values, CardTable.CARDID + "=?",
                        new String[]{card.cardId});
    }

    public static void insertCard(Card card) {
        ContentValues values = appendValues(card);
        C.get().getContentResolver().insert(CardTable.CONTENT_URI, values);
    }

    public static void deleteCard(Card card) {
        C.get().getContentResolver()
                .delete(CardTable.CONTENT_URI, CardTable.CARDID + "=?",
                        new String[]{card.cardId});
    }

    public static void deleteCard() {
        C.get().getContentResolver().delete(CardTable.CONTENT_URI, null, null);
    }

    private static ContentValues appendValues(Card card) {
        ContentValues values = new ContentValues();
        values.put(CardTable.CARDID, card.cardId);
        values.put(CardTable.CABINETID, card.cabinetId);

        return values;
    }

    public static ArrayList<Card> getCards(Cursor c) {
        ArrayList<Card> cardArr = new ArrayList<Card>();

        if (c == null || c.getCount() <= 0) {
            return cardArr;
        }

        c.moveToFirst();
        while (!c.isAfterLast()) {
            Card card = new Card();
            card.cardId = c.getString(c.getColumnIndex(CardTable.CARDID));
            card.cabinetId = c.getString(c.getColumnIndex(CardTable.CABINETID));

            cardArr.add(card);
            c.moveToNext();
        }

        return cardArr;
    }

    /** CardTable**end ***/
}
