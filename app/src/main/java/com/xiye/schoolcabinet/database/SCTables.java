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
}
