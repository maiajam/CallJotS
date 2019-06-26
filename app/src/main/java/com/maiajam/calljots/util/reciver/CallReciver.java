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

import com.maiajam.calljots.data.model.DialerInfoAndNote;
import com.maiajam.calljots.helper.Constant;
import com.maiajam.calljots.helper.helperMethodes.HelperMethodes;
import com.maiajam.calljots.helper.ReadDataThread;
import com.maiajam.calljots.helper.helperMethodes.DialogeHelperMethods;

/**
 * Created by maiAjam on 7/10/2018.
 */

public class CallReciver extends BroadcastReceiver {

    private Handler h;
    private DialerInfoAndNote contactNoteAndInfo;
    private static String PrevState;

    @Override
    public void onReceive(final Context context, Intent intent) {
        // recived call info
        PrevState = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        final String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        final String NOCont = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
        if(TextUtils.isEmpty(NOCont))
        {
            return;
        }else {
            checkCaller(intent, context, state,NOCont);
        }
    }

    private void checkCaller(Intent intent, final Context context, final String state, final String NO) {
        final String Contact_name = HelperMethodes.getContactName(NO, context);
        final String ContactImg_url = HelperMethodes.getContactImage(context,NO);
        // save dialer info at shared prefrence
        HelperMethodes.saveDialerInfo(context, Contact_name, NO,ContactImg_url);
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
                                DialogeHelperMethods.dialogeAfterCallLog(context, Constant.NOT_SPECAIL_CONTACT_HINT,
                                        NO, HelperMethodes.getContactImage(context,NO), HelperMethodes.getContactId(NO, context),
                                        null);
                            } else {
                                contactNoteAndInfo = (DialerInfoAndNote) msg.obj;
                                DialogeHelperMethods.dialogeAfterCallLog(context, Constant.SPECIAL_CONTACT_HINT,
                                        NO, HelperMethodes.getContactImage(context,NO), HelperMethodes.getContactId(NO, context),
                                        contactNoteAndInfo);
                            }
                        } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                            // during the call draw the logo and enable the user to add a new note for this contact
                               if(msg.obj == null) {
                                   // this contact is not one of your speacal contact
                                   //   HelperMethodes.drawInfo(context);
                                   }else {

                                   DialogeHelperMethods.enableAddNoteDuringCall(context,Contact_name,NO,HelperMethodes.getContactId(NO, context));
                                   //
                               }
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
                DialogeHelperMethods.dialogeAfterCallLog(context, Constant.NEW_CONTACT_HINT,
                        NO, HelperMethodes.getContactImage(context,NO), HelperMethodes.getContactId(NO, context),
                        null);
            }
        }
    }
}



