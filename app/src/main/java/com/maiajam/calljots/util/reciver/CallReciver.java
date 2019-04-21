package com.maiajam.calljots.util.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.CallLog;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.maiajam.calljots.data.local.room.RoomDao;
import com.maiajam.calljots.data.local.room.RoomManger;
import com.maiajam.calljots.data.model.DialerInfoAndNote;
import com.maiajam.calljots.helper.Constant;
import com.maiajam.calljots.helper.HelperMethodes;
import com.maiajam.calljots.helper.ReadDataThread;
import com.maiajam.calljots.helper.helperMethodes.DialogeHelperMethods;
import com.maiajam.calljots.util.history;

/**
 * Created by maiAjam on 7/10/2018.
 */

public class CallReciver extends BroadcastReceiver {

    private static String lastState;
    Handler h;
    private DialerInfoAndNote contactNoteAndInfo;
    private Object history;

    @Override
    public void onReceive(final Context context, Intent intent) {
        // recived call info
        final String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        if (!state.equals(lastState)) {
            lastState = state;
            checkCaller(intent, context, state);
        }
    }

    private void checkCaller(Intent intent, final Context context, final String state) {
        String NOCont = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
        final String Contact_name = HelperMethodes.getContactName(NOCont, context);
        // save dialer info at shared prefrence
        HelperMethodes.saveDialerInfo(context, Contact_name, NOCont);
        if (!TextUtils.isEmpty(Contact_name)) {
            h = new Handler() {
                @Override
                public void handleMessage(Message msg) {

                    if (Message.obtain() != null) {

                        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                            // rining state
                            if (msg.obj == null) {
                                // this contact is not one of your speacal contact
                                DialogeHelperMethods.drawInfo(context);
                            } else {
                                // this contact is a special contact
                                contactNoteAndInfo = (DialerInfoAndNote) msg.obj;
                                if (contactNoteAndInfo.getContact_Note() != null) {
                                    DialogeHelperMethods.drawContactInfo(context, contactNoteAndInfo, 0);
                                } else {// dont have any note for this special contact yet
                                    DialogeHelperMethods.drawContactInfo(context, contactNoteAndInfo, 1);
                                }
                            }
                        } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                            if (msg.obj == null) {
                                // this contact is not one of your speacal contact
                                history = new history(new Handler(Looper.getMainLooper()), context, contactNoteAndInfo,0);
                                context.getContentResolver().registerContentObserver(CallLog.Calls.CONTENT_URI,
                                        true, (ContentObserver) history);
                            } else {
                                contactNoteAndInfo = (DialerInfoAndNote) msg.obj;
                                history = new history(new Handler(Looper.getMainLooper()), context, contactNoteAndInfo,1);
                                context.getContentResolver().registerContentObserver(CallLog.Calls.CONTENT_URI,
                                        true, (ContentObserver) history);
                            }
                        } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                            // during the call draw the logo and enable the user to add a new note for this contact
                            //   if(msg.obj == null){
                            // this contact is not one of your speacal contact
                            //     HelperMethodes.drawInfo(context);
                            //}else {

                            //  HelperMethodes.enableAddNoteDuringCall(context,null,null);
                            //  }
                        }
                        super.handleMessage(msg);
                    }
                }
            };
            // get contact info for the dialer from database
            final ReadDataThread myThread = new ReadDataThread(h, context, Constant.GET_CONTACTINFO_BY_NAME, Contact_name);
            myThread.start();

        } else {// new number .. not one of the contact phone number... no toast msg.
            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                // add to phone contact
                history = new history(new Handler(Looper.getMainLooper()), context, contactNoteAndInfo,0);
                context.getContentResolver().registerContentObserver(CallLog.Calls.CONTENT_URI,
                        true, (ContentObserver) history);
            } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                // during the call draw the logo and enable the user to add a tp phone contact then add a new note for this contact
                HelperMethodes.enableAddNoteDuringCall(context, null, null);
            }
        }
    }
}



