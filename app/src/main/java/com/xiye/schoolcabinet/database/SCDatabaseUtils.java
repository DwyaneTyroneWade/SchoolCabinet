package com.xiye.schoolcabinet.database;

import android.content.ContentValues;
import android.database.Cursor;

import com.xiye.schoolcabinet.beans.Card;
import com.xiye.schoolcabinet.beans.Record;
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

    /**
     * RecordTable**start
     ***/
    public static Cursor queryRecord(Record record) {
        return C.get()
                .getContentResolver()
                .query(SCTables.RecordTable.CONTENT_URI, null, SCTables.RecordTable.CARDID + " =? ",
                        new String[]{record.cardId}, null);
    }

    public static Cursor queryRecord() {
        return C.get().getContentResolver()
                .query(SCTables.RecordTable.CONTENT_URI, null, null, null, null);
    }

    public static void updateRecord(Record record) {
        ContentValues values = appendValues(record);
        C.get().getContentResolver()
                .update(SCTables.RecordTable.CONTENT_URI, values, SCTables.RecordTable.CARDID + "=?",
                        new String[]{record.cardId});
    }

    public static void insertRecord(Record record) {
        ContentValues values = appendValues(record);
        C.get().getContentResolver().insert(SCTables.RecordTable.CONTENT_URI, values);
    }

    public static void deleteRecord(Record record) {
        C.get().getContentResolver()
                .delete(SCTables.RecordTable.CONTENT_URI, SCTables.RecordTable.CARDID + "=?",
                        new String[]{record.cardId});
    }

    public static void deleteRecord() {
        C.get().getContentResolver().delete(SCTables.RecordTable.CONTENT_URI, null, null);
    }

    private static ContentValues appendValues(Record record) {
        ContentValues values = new ContentValues();
        values.put(SCTables.RecordTable.CARDID, record.cardId);
        values.put(SCTables.RecordTable.CABINETID, record.cabinetId);
        values.put(SCTables.RecordTable.BOXID, record.boxId);
        values.put(SCTables.RecordTable.OPERATION_TIME, record.operationTime);

        return values;
    }

    public static ArrayList<Record> getRecords(Cursor c) {
        ArrayList<Record> recordsArr = new ArrayList<Record>();

        if (c == null || c.getCount() <= 0) {
            return recordsArr;
        }

        c.moveToFirst();
        while (!c.isAfterLast()) {
            Record record = new Record();
            record.cardId = c.getString(c.getColumnIndex(SCTables.RecordTable.CARDID));
            record.cabinetId = c.getString(c.getColumnIndex(SCTables.RecordTable.CABINETID));
            record.boxId = c.getString(c.getColumnIndex(SCTables.RecordTable.BOXID));
            record.operationTime = c.getString(c.getColumnIndex(SCTables.RecordTable.OPERATION_TIME));

            recordsArr.add(record);
            c.moveToNext();
        }

        return recordsArr;
    }

    /** RecordTable**end ***/
}
