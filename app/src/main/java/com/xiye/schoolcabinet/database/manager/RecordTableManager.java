package com.xiye.schoolcabinet.database.manager;

import android.database.Cursor;

import com.xiye.schoolcabinet.beans.Record;
import com.xiye.schoolcabinet.database.SCDatabaseUtils;

import java.util.ArrayList;

public class RecordTableManager {

    public static ArrayList<Record> get() {
        // query
        Cursor cursor = SCDatabaseUtils.queryRecord();
        if (cursor != null && cursor.getCount() > 0) {
            // get
            ArrayList<Record> recordDB = SCDatabaseUtils
                    .getRecords(cursor);
            cursor.close();
            return recordDB;
        }
        return null;
    }

    private static void save(ArrayList<Record> recordArr) {
        if (recordArr != null && recordArr.size() > 0) {
            for (int i = 0; i < recordArr.size(); i++) {
                Record recordItem = recordArr.get(i);
                save(recordItem);
            }
        }
    }

    public static void save(Record recordItem) {
        if (recordItem != null) {
            Cursor c = SCDatabaseUtils.queryRecord(recordItem);
            if (c != null) {
                if (c.getCount() > 0) {
                    // update
                    SCDatabaseUtils.updateRecord(recordItem);
                } else {
                    // insert
                    SCDatabaseUtils.insertRecord(recordItem);
                }
                c.close();
            } else {
                // insert
                SCDatabaseUtils.insertRecord(recordItem);
            }
        }
    }

    public static boolean saveBoolean(Record recordItem) {
        boolean isUpdate = false;
        if (recordItem != null) {
            Cursor c = SCDatabaseUtils.queryRecord(recordItem);
            if (c != null) {
                if (c.getCount() > 0) {
                    // update
                    isUpdate = true;
                    SCDatabaseUtils.updateRecord(recordItem);
                } else {
                    // insert
                    SCDatabaseUtils.insertRecord(recordItem);
                }
                c.close();
            } else {
                // insert
                SCDatabaseUtils.insertRecord(recordItem);
            }
        }
        return isUpdate;
    }

    private static void insert(Record recordItem) {
        if (recordItem != null) {
            SCDatabaseUtils.insertRecord(recordItem);
        }
    }

    // delete同样存在id 为空的问题 解决 save之后返回的id补全
    public static void delete(Record record) {
        SCDatabaseUtils.deleteRecord(record);
    }

    public static void clear() {
        SCDatabaseUtils.deleteRecord();
    }

    public static boolean isRecord() {
        boolean isRecord = false;
        ArrayList<Record> recordArrDB = get();
        if (recordArrDB != null && recordArrDB.size() > 0) {
            isRecord = true;
        }
        return isRecord;
    }

    public static void saveAfterDeleteAll(ArrayList<Record> recordArr) {
        SCDatabaseUtils.deleteRecord();
        save(recordArr);
    }
}
