package com.maiajam.calljots.util.workmanger;

import android.content.Context;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.maiajam.calljots.util.CallReciver;

import androidx.work.Worker;

public class myworker extends Worker {

    CallReciver reciver = new CallReciver();
    IntentFilter iNtent =  new IntentFilter("android.intent.action.PHONE_STATE");;
    @NonNull
    @Override
    public WorkerResult doWork() {

        getApplicationContext().registerReceiver(reciver,iNtent);
        Toast.makeText(getApplicationContext(),"done",Toast.LENGTH_LONG).show();
        return null;
    }

    @Override
    public void onStopped() {
        getApplicationContext().unregisterReceiver(reciver);
    }
}
