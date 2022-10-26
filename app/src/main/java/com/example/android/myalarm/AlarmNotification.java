package com.example.android.myalarm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.android.myalarm.services.MyIntentService;

public class AlarmNotification {

    private static final String CHANNEL_ID = "channel_id" ;
    private static final int PENDING_ID = 101;

    public static final String STOP_ALARM = "stop_alarm";
    private static final int PENDING_STOP_ID = 102 ;

    public static void cancelNotification(Context context){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public static void createNotification(Context context){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,"alarm_reminder",NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,CHANNEL_ID);
        builder.setColor(ContextCompat.getColor(context,R.color.white))
                .setContentTitle("Alarm notification")
                .setContentText("alarm will be triggered after 15 min")
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setContentIntent(contentIntent(context))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .addAction(stopAlarm(context))
                .setAutoCancel(true);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN && Build.VERSION.SDK_INT<Build.VERSION_CODES.O){
            builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }
        notificationManager.notify(PENDING_ID,builder.build());
    }

    public static NotificationCompat.Action stopAlarm(Context context){
        Intent intent  = new Intent(context, MyIntentService.class);
        intent.setAction(STOP_ALARM);

        PendingIntent pendingIntent = PendingIntent.getService(context,PENDING_STOP_ID,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        return new NotificationCompat.Action(android.R.drawable.ic_menu_close_clear_cancel,"Stop",pendingIntent);
    }

    public static PendingIntent contentIntent(Context context){
        Intent intent = new Intent(context,MainActivity.class);

        return PendingIntent.getActivity(context,PENDING_ID,intent,PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
