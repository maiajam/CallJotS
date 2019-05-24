package com.maiajam.calljots.util.workmanger;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.maiajam.calljots.helper.helperMethodes.HelperMethodes;

import androidx.work.Worker;

public class ReminerSchudleWorker extends Worker {

    Context mContext;
    String mName,mNote,mActionTitle ;
    @NonNull
    @Override
    public WorkerResult doWork() {

        Toast.makeText(getApplicationContext(),"fdgdg ",Toast.LENGTH_LONG).show();
        HelperMethodes.CallNotifcation(getApplicationContext(),0,mName,mNote,mActionTitle,null,null,1,0,1);
        return WorkerResult.SUCCESS;
    }
}
