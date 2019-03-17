package com.maiajam.calljots.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.maiajam.calljots.R;
import com.maiajam.calljots.data.model.ContactLogs;
import com.maiajam.calljots.helper.Constant;
import com.maiajam.calljots.helper.HelperMethodes;
import com.maiajam.calljots.helper.ReadDataThread;

import java.util.ArrayList;

/**
 * Created by maiAjam on 7/10/2018.
 */

public class history extends ContentObserver {
    Context context;

    int Type,Dir;
    ArrayList<ContactLogs> listCall;
    String name , callDuration ,date ,ContactName;
    private Handler handler;
    private ReadDataThread readDataThrea;

    // type value to indicate that we will show the dialoge or print the call log
    // type = 1 means show call log
    public history(Handler handler, Context c) {
        super(handler);
        context = c;
    }
    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        SharedPreferences sp = context.getSharedPreferences("LastCall", Activity.MODE_PRIVATE);
        String phoneNo = sp.getString("phoneNumber", null);
        if (phoneNo != null) {
            getCallDetials();
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
            return ;
        }

        Cursor managedCursor ;
        managedCursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null,
                CallLog.Calls.DATE + " DESC");

        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int duration1 = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        int type1 = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date1 = managedCursor.getColumnIndex(CallLog.Calls.DATE);
      //  int name1 =  managedCursor.getColumnIndex(CallLog.Calls.);

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
            String imgUri = HelperMethodes.getContactImage(context,phNumber);
            String name = HelperMethodes.getContactName(phNumber,context);
            int contactId = HelperMethodes.getContactId(phNumber,context);
            showDialge(phNumber, name, callDuration, date, Dir,imgUri,contactId);
        }


    }




    private void showDialge(final String Number, final String name, String callDuration, String date, int dir, final String imgUri,int ContactId) {

        switch (dir){
            case 1 :
                //out going call
               Toast.makeText(context,"missed",Toast.LENGTH_LONG).show();
            case 2:
                // incoming call and missed call
                if(name == null)
                {
                    // new contact
                    HelperMethodes.CallNotifcation(context,2,Number
                            ,context.getString(R.string.newCon),context.getString(R.string.addCont),
                            Number,imgUri,0,HelperMethodes.getContactId(Number,context));
                }else
                {
                    // check is this contact a special contact
                    handler = new Handler(){

                        @Override
                        public void handleMessage(Message msg) {
                            if(msg.arg1 == 1)
                            {
                                // its a speacail contact ... so add a note for this calllog
                                HelperMethodes.CallNotifcation(context,1,name,
                                        context.getString(R.string.savNote),context.getString(R.string.addNote),Number,imgUri,
                                        0,HelperMethodes.getContactId(Number,context));
                            }else
                            {
                                // its is not a speacail contact ... add it to specal then add a note for hime
                                HelperMethodes.CallNotifcation(context,0,name,
                                        context.getString(R.string.addToSpec),context.getString(R.string.AddTospec),Number,imgUri,
                                        0,HelperMethodes.getContactId(Number,context));
                            }
                            super.handleMessage(msg);
                        }
                    };
                    readDataThrea = new ReadDataThread(handler,context,Constant.CHECK_IS_SPECIAL_CONTACT,name);
                    readDataThrea.start();
                }
        }


    }

    private void ShowToast_ContactNote(int number, String name, String callDuration, String date, int dir) {

       // ContactInfo_Toast toast = new ContactInfo_Toast(context,);

    }
}
