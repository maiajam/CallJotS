package com.maiajam.calljots.util.workmanger;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.maiajam.calljots.helper.helperMethodes.HelperMethodes;

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

        Toast.makeText(mContext,"fdgdg ",Toast.LENGTH_LONG).show();
        HelperMethodes.CallNotifcation(mContext,0,mName,mNote,mActionTitle,null,null,1,0,1);
        return WorkerResult.SUCCESS;
    }
}
