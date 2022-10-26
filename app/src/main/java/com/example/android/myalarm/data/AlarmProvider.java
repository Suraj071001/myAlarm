package com.example.android.myalarm.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AlarmProvider extends ContentProvider {
    AlarmData alarmData;

    private static final int ALARM = 100;
    private static final int ALARM_ID = 101;

    public static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(Contract.CONTENT_AUTHORITY,Contract.ALARM,ALARM);
        uriMatcher.addURI(Contract.CONTENT_AUTHORITY,Contract.ALARM+"/#",ALARM_ID);
    }

    @Override
    public boolean onCreate() {
        alarmData = new AlarmData(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sort) {
        SQLiteDatabase database = alarmData.getReadableDatabase();

        Cursor cursor;
        int match = uriMatcher.match(uri);
        switch(match){
            case ALARM:
                cursor = database.query(Contract.Alarm.TABLE_NAME,projection,selection,selectionArgs,null,null,sort);
                break;
            case ALARM_ID:
                selection = Contract.Alarm.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(Contract.Alarm.TABLE_NAME,projection,selection,selectionArgs,null,null,sort);
            default:
                throw new IllegalStateException("Unexpected value: " + match);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase database = alarmData.getWritableDatabase();
        long id = database.insert(Contract.Alarm.TABLE_NAME,null,contentValues);
        return ContentUris.withAppendedId(uri,id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = alarmData.getWritableDatabase();
        int count;
        int match = uriMatcher.match(uri);
        switch(match){
            case ALARM:
                count = database.delete(Contract.Alarm.TABLE_NAME,selection,selectionArgs);
                break;
            case ALARM_ID:
                selection = Contract.Alarm.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                count = database.delete(Contract.Alarm.TABLE_NAME,selection,selectionArgs);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + match);
        }
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = alarmData.getWritableDatabase();
        selection = Contract.Alarm.COLUMN_ID + "=?";
        selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
        return database.update(Contract.Alarm.TABLE_NAME,contentValues,selection,selectionArgs);
    }
}
