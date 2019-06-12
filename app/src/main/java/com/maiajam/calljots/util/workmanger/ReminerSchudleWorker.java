package com.maiajam.calljots.util.workmanger;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.maiajam.calljots.R;
import com.maiajam.calljots.helper.helperMethodes.HelperMethodes;

import androidx.work.Worker;

public class ReminerSchudleWorker extends Worker {

    String mName,mNote,mActionTitle ;
    @NonNull
    @Override
    public WorkerResult doWork() {
        getNoteInfo();
        HelperMethodes.CallNotifcation(getApplicationContext(),0,mName,mNote,mActionTitle,null,null,1,0,1);
        return WorkerResult.SUCCESS;
    }

    private void getNoteInfo() {
        mName = getInputData().getString(getApplicationContext().getString(R.string.ContactName_Extra),"");
        mNote = getInputData().getString(getApplicationContext().getString(R.string.Note_Extra),"");
        mActionTitle = getInputData().getString(getApplicationContext().getString(R.string.NoteTitle_Extra),"");
    }
}
