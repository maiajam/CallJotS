package com.maiajam.calljots.util.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.maiajam.calljots.util.workmanger.MyWorker;

public class BootCompletedReciver extends BroadcastReceiver {
    private OneTimeWorkRequest CallRevicerRequest;

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context,"boot",Toast.LENGTH_LONG).show();
     /*   if(intent.getExtras().equals(Intent.ACTION_BOOT_COMPLETED))
        {
            CallRevicerRequest = new OneTimeWorkRequest.Builder(MyWorker.class).build();
            WorkManager.getInstance().enqueue(CallRevicerRequest);
        }
        */
    }
}
