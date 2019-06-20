package com.maiajam.calljots.util.workmanger;

import android.content.Context;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.maiajam.calljots.util.reciver.CallReciver;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyWorker extends Worker {

    CallReciver reciver = new CallReciver();
    IntentFilter iNtent =  new IntentFilter("android.intent.action.PHONE_STATE");;

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        getApplicationContext().registerReceiver(reciver,iNtent);
        return null;
    }

    @Override
    public void onStopped() {
        getApplicationContext().unregisterReceiver(reciver);
    }
}
