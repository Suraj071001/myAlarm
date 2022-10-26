package com.example.android.myalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import com.example.android.myalarm.data.Contract;

public class AlarmReceiver extends BroadcastReceiver {
    static MediaPlayer mediaPlayer;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("tag", "onReceive: called");
        int ring_index = intent.getIntExtra(Set_New_Alarm.RINGTONE,0);
        String millis = String.valueOf(intent.getLongExtra("millis",0));
//        JobNotification.createJob(context,0);
        AlarmNotification.createNotification(context);

        switch(ring_index){
            case 0:
                mediaPlayer = MediaPlayer.create(context,R.raw.wano_kuni_song);
                break;
            case 1:
                mediaPlayer = MediaPlayer.create(context,R.raw.ringtone1);
                break;
        }
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        int count = context.getContentResolver().delete(Contract.Alarm.CONTENT_URI,Contract.Alarm.COLUMN_MILLIS+"=?",new String[]{millis});
        Toast.makeText(context, ""+count, Toast.LENGTH_SHORT).show();
    }
    public static void stopAlarm(){
        mediaPlayer.stop();
    }
    public static boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }
}
