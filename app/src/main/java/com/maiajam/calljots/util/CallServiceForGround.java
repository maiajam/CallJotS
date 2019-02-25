package com.maiajam.calljots.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;


public class CallServiceForGround extends Service {

    // this srevice to register the reciver for android oreo and above
    CallReciver reciver = new CallReciver();
    IntentFilter iNtent =  new IntentFilter("android.intent.action.PHONE_STATE");;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT >= 26) {
            registerReceiver(reciver,iNtent);
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();
            startForeground(1, notification);

        }else {
            registerReceiver(reciver,iNtent);
        }
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        if (Build.VERSION.SDK_INT >= 26) {
            registerReceiver(reciver,iNtent);
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();
            startForeground(1, notification);

        }else {
            registerReceiver(reciver,iNtent);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

      //  IntentFilter intent =  new IntentFilter("android.intent.action.PHONE_STATE");;
        try{
            if(reciver != null) {
                this.unregisterReceiver(reciver);
            }
        } catch (Exception e){
            // already unregistered
        }
    }


}
