package com.maiajam.calljots.util.workmanger;

import android.content.Context;
import android.support.annotation.NonNull;

import com.maiajam.calljots.helper.HelperMethodes;

import androidx.work.Worker;

public class ReminerSchudleWorker extends Worker {

    Context mContext;
    String mName,mNote,mActionTitle ;

    public ReminerSchudleWorker(Context context,String name,String Note,String ActionTitle) {
        mContext = context ;
        mName = name ;
        mNote = Note ;
        mActionTitle = ActionTitle ;
    }
    @NonNull
    @Override
    public WorkerResult doWork() {

        HelperMethodes.CallNotifcation(mContext,0,mName,mNote,mActionTitle,null,null,1,0,1);
        return WorkerResult.SUCCESS;
    }
}
