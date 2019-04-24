package com.maiajam.calljots.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import com.maiajam.calljots.data.model.ContactLogs;
import com.maiajam.calljots.data.model.DialerInfoAndNote;
import com.maiajam.calljots.helper.Constant;
import com.maiajam.calljots.helper.HelperMethodes;
import com.maiajam.calljots.helper.helperMethodes.DialogeHelperMethods;

import java.util.ArrayList;

/**
 * Created by maiAjam on 7/10/2018.
 */

public class history extends ContentObserver {
    private final DialerInfoAndNote mContactInfo;
    private final int isSpec;
    private int hintFromReciver ;
    Context context;

    int  Dir;
    ArrayList<ContactLogs> listCall;

    public history(Handler handler, Context c, DialerInfoAndNote contactNoteAndInfo,int hintIsSpec,int hintFromReciver) {
        super(handler);
        context = c;
        mContactInfo = contactNoteAndInfo;
        isSpec = hintIsSpec ;
        this.hintFromReciver = hintFromReciver ;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        SharedPreferences sp = context.getSharedPreferences("LastCall", Activity.MODE_PRIVATE);
        String phoneNo = sp.getString("phoneNumber", null);
        if (phoneNo != null) {
            if(hintFromReciver == 1)
            {
                getCallDetials();
            }
        }
    }

    @Override
    public boolean deliverSelfNotifications() {
        return true;
    }

    public void getCallDetials() {

        listCall = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }

        Cursor managedCursor;
        managedCursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null,
                CallLog.Calls.DATE + " DESC");

        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int duration1 = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        int type1 = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date1 = managedCursor.getColumnIndex(CallLog.Calls.DATE);

        if (managedCursor.moveToFirst() == true) {

            String phNumber = managedCursor.getString(number);
            String callDuration = managedCursor.getString(duration1);
            String type = managedCursor.getString(type1);
            String date = managedCursor.getString(date1);

            String dir = null;
            int dircode = Integer.parseInt(type);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    Dir = 1;
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    Dir = 2;
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    Dir = 2;
                    break;
                default:
                    dir = "MISSED";
                    Dir = 2;
                    break;
            }
            managedCursor.close();
            String imgUri = HelperMethodes.getContactImage(context, phNumber);
            String name = HelperMethodes.getContactName(phNumber, context);
            int contactId = HelperMethodes.getContactId(phNumber, context);

               showDialge(phNumber, name, callDuration, date, Dir, imgUri, contactId, mContactInfo);

        }


    }

    private void showDialge(final String Number, final String name, String callDuration, String date, int dir, final String imgUri, int ContactId, DialerInfoAndNote mContactInfo) {

        switch (dir) {
            case 1:
                //out going call
            case 2:
                // incoming call and missed call
                if (TextUtils.isEmpty(name)) {// new contact
                  //  DialogeHelperMethods.dialogeAfterCallLog(context,Constant.NEW_CONTACT_HINT,Number,imgUri,
                    //        HelperMethodes.getContactId(Number,context),0, contactNoteAndInfo);

                } else {// check is this contact a special contact
                    if (isSpec == 1) { // its a speacail contact ... so add a note for this calllog
                     //   DialogeHelperMethods.dialogeAfterCallLog(context, Constant.SPECIAL_CONTACT_HINT, Number, imgUri, HelperMethodes.getContactId(Number, context), Constant.SPECIAL_CONTACT_HINT, contactNoteAndInfo);

                    } else { // its is not a speacail contact ... add it to specal then add a note for hime
                       // DialogeHelperMethods.dialogeAfterCallLog(context, Constant.NOT_SPECAIL_CONTACT_HINT, Number, imgUri, HelperMethodes.getContactId(Number, context), 0, contactNoteAndInfo);
                    }
                }
        }

    }

}
