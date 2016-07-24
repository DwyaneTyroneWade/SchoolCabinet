package com.xiye.schoolcabinet.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class SCTables {

    public static final String AUTHORITY = "com.xiye.schoolcabinet.provider";

    public static class CardTable implements BaseColumns {
        public static final String TABLE_NAME = "card_info";
        public static final Uri CONTENT_URI = Uri.parse("content://"
                + AUTHORITY + "/" + TABLE_NAME);

        public static final String CARDID = "card_id";
        public static final String CABINETID = "cabinet_id";

        public static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME + "(_id INTEGER PRIMARY KEY, " + CARDID + " TEXT, "
                + CABINETID + " TEXT );";
    }

    public static class RecordTable implements BaseColumns {
        public static final String TABLE_NAME = "record_table";
        public static final Uri CONTENT_URI = Uri.parse("content://"
                + AUTHORITY + "/" + TABLE_NAME);

        public static final String CARDID = "card_id";
        public static final String CABINETID = "cabinet_id";
        public static final String BOXID = "box_id";
        //chestNo,card_id,box_id,box_status,is_filled,operation_time
        public static final String BOX_DOOR_STATUS = "box_door_status";
        public static final String BOX_IS_FILLED = "box_is_filled";
        public static final String OPERATION_TIME = "operation_time";

        public static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME + "(_id INTEGER PRIMARY KEY, " + CARDID + " TEXT, "
                + CABINETID + " TEXT, "
                + BOXID + " TEXT, "
                + BOX_DOOR_STATUS + " TEXT, "
                + BOX_IS_FILLED + " TEXT, "
                + OPERATION_TIME + " TEXT );";
    }
}
