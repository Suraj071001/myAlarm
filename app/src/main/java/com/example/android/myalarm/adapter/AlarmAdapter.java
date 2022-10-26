package com.example.android.myalarm.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.myalarm.R;
import com.example.android.myalarm.data.Contract;

public class AlarmAdapter extends CursorAdapter {
    public AlarmAdapter(Context context, Cursor c) {
        super(context, c,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.alarm_adapter,viewGroup,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView date_view = view.findViewById(R.id.textView6);
        TextView time_view = view.findViewById(R.id.textView5);
        TextView millis_view = view.findViewById(R.id.textView7);

        String date = cursor.getString(cursor.getColumnIndexOrThrow(Contract.Alarm.COLUMN_DATE));
        String time = cursor.getString(cursor.getColumnIndexOrThrow(Contract.Alarm.COLUMN_TIME));
        String millis = cursor.getString(cursor.getColumnIndexOrThrow(Contract.Alarm.COLUMN_MILLIS));

        date_view.setText(date);
        time_view.setText(time);
        millis_view.setText(millis);

    }
}
