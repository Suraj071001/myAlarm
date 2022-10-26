package com.example.android.myalarm.services;

import android.app.IntentService;
import android.content.Intent;

import com.example.android.myalarm.AlarmNotification;
import com.example.android.myalarm.AlarmReceiver;


public class MyIntentService extends IntentService {


    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AlarmNotification.cancelNotification(getApplicationContext());
        if(AlarmReceiver.isPlaying()){
            AlarmReceiver.stopAlarm();
        }
    }
}