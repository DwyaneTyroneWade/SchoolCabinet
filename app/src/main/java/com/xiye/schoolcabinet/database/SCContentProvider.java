package com.xiye.schoolcabinet.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.xiye.schoolcabinet.database.SCTables.CardTable;
import com.xiye.schoolcabinet.database.SCTables.RecordTable;

public class SCContentProvider extends ContentProvider {
    private static final int CARD = 1000;
    private static final int RECORD = 1001;

    private static final UriMatcher MATCHER;

    static {
        MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        MATCHER.addURI(SCTables.AUTHORITY, CardTable.TABLE_NAME, CARD);
        MATCHER.addURI(SCTables.AUTHORITY, RecordTable.TABLE_NAME, RECORD);
    }

    private SCDatabaseHelper mHelper;

    @Override
    public boolean onCreate() {
        // TODO Auto-generated method stub
        mHelper = new SCDatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO Auto-generated method stub
        final SQLiteDatabase db = mHelper.getReadableDatabase();
        final SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        final int match = MATCHER.match(uri);
        String groupBy = null;
        String having = null;
        switch (match) {
            case CARD:
                builder.setTables(CardTable.TABLE_NAME);
                break;
            case RECORD:
                builder.setTables(RecordTable.TABLE_NAME);
                break;
            default:
                throw new UnsupportedOperationException("unkown uri:"
                        + uri.toString());
        }
        String limit = uri.getQueryParameter("limit");
        Cursor c = null;
        if (!TextUtils.isEmpty(limit)) {
            c = builder.query(db, projection, selection, selectionArgs,
                    groupBy, having, sortOrder, limit);
        } else {
            c = builder.query(db, projection, selection, selectionArgs,
                    groupBy, having, sortOrder);
        }

        if (c != null) {
            c.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return c;
    }

    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO Auto-generated method stub
        final int match = MATCHER.match(uri);
        long rowId = 0;
        switch (match) {
            case CARD:
                rowId = mHelper.getWritableDatabase().insert(CardTable.TABLE_NAME,
                        null, values);
                break;
            case RECORD:
                rowId = mHelper.getWritableDatabase().insert(RecordTable.TABLE_NAME,
                        null, values);
                break;
            default:
                throw new UnsupportedOperationException("unkown uri:"
                        + uri.toString());
        }

        if (rowId > 0) {
            getContext().getContentResolver().notifyChange(uri, null, false);
            return ContentUris.withAppendedId(uri, rowId);
        }

        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        final int match = MATCHER.match(uri);
        String table;
        switch (match) {
            case CARD:
                table = CardTable.TABLE_NAME;
                break;
            case RECORD:
                table = RecordTable.TABLE_NAME;
                break;
            default:
                throw new UnsupportedOperationException("unkown uri:"
                        + uri.toString());
        }

        final SQLiteDatabase db = mHelper.getWritableDatabase();
        final int count = db.delete(table, selection, selectionArgs);
        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null, false);
        }
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO Auto-generated method stub
        final SQLiteDatabase db = mHelper.getWritableDatabase();
        final int match = MATCHER.match(uri);
        String table;
        switch (match) {
            case CARD:
                table = CardTable.TABLE_NAME;
                break;
            case RECORD:
                table = RecordTable.TABLE_NAME;
                break;
            default:
                throw new UnsupportedOperationException("unkown uri:"
                        + uri.toString());
        }

        int count = db.update(table, values, selection, selectionArgs);
        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null, false);
        }
        return count;
    }

}
