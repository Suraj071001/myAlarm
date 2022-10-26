package com.example.android.myalarm.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AlarmData extends SQLiteOpenHelper {
    private static final String name = "alarm";
    private static final int version = 1;

    public AlarmData(@Nullable Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_DATABASE_STRING = "CREATE TABLE "+Contract.Alarm.TABLE_NAME+" ("+
                Contract.Alarm.COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                Contract.Alarm.COLUMN_DATE+ " TEXT NOT NULL,"+
                Contract.Alarm.COLUMN_MILLIS+" INTEGER NOT NULL,"+
                Contract.Alarm.COLUMN_TIME+ " TEXT NOT NULL);";

        sqLiteDatabase.execSQL(SQL_DATABASE_STRING);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
