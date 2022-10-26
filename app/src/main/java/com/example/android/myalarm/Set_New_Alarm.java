package com.example.android.myalarm;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.myalarm.data.Contract;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;

public class Set_New_Alarm extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    public static final String RINGTONE = "ringtone";
    AutoCompleteTextView autoCompleteTextView;
    EditText hour_edit,min_edit;
    public static AlarmManager alarmManager;
    TextView showDate;
    int index;
    long triggeredTime;
    long position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_alarm);

        setRingtone();

        showDate = findViewById(R.id.textView4);
        hour_edit = findViewById(R.id.editHour);
        min_edit = findViewById(R.id.editMin);

        alarmManager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDailog();
            }
        });

    }

    private void showDatePickerDailog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.set_alarm,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.setAlarm){

            int hour1 = Integer.parseInt(hour_edit.getText().toString());
            int min1 = Integer.parseInt(min_edit.getText().toString());

            triggeredTime = getAlarmDateInMills()+((long)hour1*60*60*1000+(long) min1*60*1000);

            Intent intent1 = getIntent();
            String mode = intent1.getStringExtra("mode");

            if(mode.equals("add")){
//                JobNotification.createJob(this,triggeredTime);

                Intent intent = new Intent(this,AlarmReceiver.class);
                intent.putExtra(RINGTONE,index);
                intent.putExtra("millis",triggeredTime);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this,(int) triggeredTime,intent,0);
                alarmManager.set(AlarmManager.RTC_WAKEUP,triggeredTime,pendingIntent);

                insertData();

            }else if(mode.equals("update")){
                position = intent1.getLongExtra("id",0);
                long pending_id = Long.parseLong((intent1.getStringExtra("millis")));
                Intent intent = new Intent(this,AlarmReceiver.class);
                intent.putExtra(RINGTONE,index);
                intent.putExtra("millis",triggeredTime);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this,(int) pending_id,intent,0);
                alarmManager.cancel(pendingIntent);

//                JobNotification.createJob(this,triggeredTime);

                PendingIntent pending_update = PendingIntent.getBroadcast(this,(int) triggeredTime,intent,0);
                alarmManager.set(AlarmManager.RTC_WAKEUP,triggeredTime,pending_update);

                updateData();
            }
            return true;
        }
        else if(id==R.id.remove){
            Intent intent1 = getIntent();
            long position = intent1.getLongExtra("id",0);
            long pending_id = Long.parseLong((intent1.getStringExtra("millis")));
            Intent intent = new Intent(this,AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this,(int) pending_id,intent,0);
            alarmManager.cancel(pendingIntent);

            int count = getContentResolver().delete(Contract.Alarm.CONTENT_URI,Contract.Alarm.COLUMN_ID+"=?",new String[]{String.valueOf(position)});
            if(count>0){
                Intent intent2 = new Intent(this,MainActivity.class);
                startActivity(intent2);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private String getCurrentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date myDate = new Date();

        return simpleDateFormat.format(myDate);
    }

    public long milliseconds(String date) {
        //String date_ = date;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try
        {
            Date mDate = sdf.parse(date);
            assert mDate != null;
            long timeInMilliseconds = mDate.getTime();
            System.out.println("Date in milli :: " + timeInMilliseconds);
            return timeInMilliseconds;
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return 0;
    }

    public void setRingtone(){
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);

        String [] ringtone = getResources().getStringArray(R.array.ringtones);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.drop_down,ringtone);

        autoCompleteTextView.setAdapter(adapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                index = (int) l;
            }
        });
    }

    public long getAlarmDateInMills(){
        long date_in_mills;
        if(showDate.getText()==null||showDate.getText().equals("")){
            String current_date = getCurrentDate();
            date_in_mills = milliseconds(current_date);
        }else{
            Log.d("tag", "else");
            String date = showDate.getText().toString();
            date_in_mills = milliseconds(date);
        }
        return date_in_mills;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int date_of_month) {
        String date = year+"-"+(month+1)+"-"+date_of_month;
        showDate.setText(date);
    }
    public void insertData(){
        String date_data = showDate.getText().toString();
        String time_data = hour_edit.getText().toString() +" : "+ min_edit.getText().toString();

        ContentValues cv = new ContentValues();
        cv.put(Contract.Alarm.COLUMN_TIME,time_data);
        cv.put(Contract.Alarm.COLUMN_DATE,date_data);
        cv.put(Contract.Alarm.COLUMN_MILLIS,triggeredTime);

        Uri uri = getContentResolver().insert(Contract.Alarm.CONTENT_URI,cv);

        if(uri!=null){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
    }
    public void updateData(){
        String date_data = showDate.getText().toString();
        String time_data = hour_edit.getText().toString() +" : "+ min_edit.getText().toString();

        ContentValues cv = new ContentValues();
        cv.put(Contract.Alarm.COLUMN_TIME,time_data);
        cv.put(Contract.Alarm.COLUMN_DATE,date_data);
        cv.put(Contract.Alarm.COLUMN_MILLIS,triggeredTime);

        int count = getContentResolver().update(Uri.withAppendedPath(Contract.Alarm.CONTENT_URI, String.valueOf(position)),cv,null,null);
        if (count > 0) {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }

    }

}