package com.maiajam.calljots.util.workmanger;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.maiajam.calljots.R;
import com.maiajam.calljots.helper.helperMethodes.HelperMethodes;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class ReminerSchudleWorker extends Worker {


    String mName,mNote,mActionTitle,mImgUrl ;
    private String mPhoneNumber;
    private int mContactId;

    public ReminerSchudleWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        getNoteInfo();
        HelperMethodes.CallNotifcation(getApplicationContext(),mName,mNote,mActionTitle,
                mPhoneNumber,mImgUrl,1,mContactId,1);

        return Result.success();
    }

    private void getNoteInfo() {
        mName = getInputData().getString(getApplicationContext().getString(R.string.ContactName_Extra));
        mNote = getInputData().getString(getApplicationContext().getString(R.string.Note_Extra));
        mActionTitle = getInputData().getString(getApplicationContext().getString(R.string.NoteTitle_Extra));
        mImgUrl = getInputData().getString(getApplicationContext().getString(R.string.ContactImgUrl_Extra));
        mPhoneNumber =  getInputData().getString(getApplicationContext().getString(R.string.ContactPhoneNumber_Extra));
        mContactId =  getInputData().getInt(getApplicationContext().getString(R.string.ContactId_Extra),0);
    }

}
