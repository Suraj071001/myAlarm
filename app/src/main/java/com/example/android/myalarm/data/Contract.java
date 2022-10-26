package com.example.android.myalarm.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class Contract implements BaseColumns {
    public static final String CONTENT_AUTHORITY = "com.example.android.myalarm";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String ALARM = "alarm";
    public static class Alarm{
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,ALARM);
        public static final String TABLE_NAME = "alarm";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_MILLIS = "millis";
        public static final String COLUMN_ID = BaseColumns._ID;
    }
}
