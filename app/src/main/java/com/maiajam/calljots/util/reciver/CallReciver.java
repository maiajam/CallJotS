package com.maiajam.calljots.util.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.maiajam.calljots.data.model.DialerInfoAndNote;
import com.maiajam.calljots.helper.Constant;
import com.maiajam.calljots.helper.helperMethodes.HelperMethodes;
import com.maiajam.calljots.helper.thread.ReadDataThread;
import com.maiajam.calljots.helper.helperMethodes.DialogeHelperMethods;

/**
 * Created by maiAjam on 7/10/2018.
 */

public class CallReciver extends BroadcastReceiver {

    private static String LastCallState;
    private Handler h;
    private DialerInfoAndNote contactNoteAndInfo;

    @Override
    public void onReceive(final Context context, Intent intent) {

        final String NOCont = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
        checkReciverHasNo(NOCont, context, intent);
    }

    private void checkReciverHasNo(String NOCont, Context context, Intent intent) {
        if (TextUtils.isEmpty(NOCont)) {
            return;
        } else {
            DialogeHelperMethods.getInstance(context).removeAnyView();
            checkCaller(intent, context, intent.getStringExtra(TelephonyManager.EXTRA_STATE), NOCont);
            HelperMethodes.saveCallState(context, intent.getStringExtra(TelephonyManager.EXTRA_STATE));
        }
    }

    private void checkCaller(Intent intent, final Context context, final String state, final String NO) {

        LastCallState = HelperMethodes.getLastCallState(context);
        final String Contact_name = HelperMethodes.getContactName(NO, context);
        final String ContactImg_url = HelperMethodes.getContactImage(context, NO);

        HelperMethodes.saveDialerInfo(context, Contact_name, NO, ContactImg_url);
        if (!TextUtils.isEmpty(Contact_name)) {
            getContactInfoForThisContact(context, intent.getStringExtra(TelephonyManager.EXTRA_STATE), NO, Contact_name);

        } else {// new number .. not one of the contact phone number... no toast msg.
            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                DialogeHelperMethods.dialogeAfterCallLog(context, Constant.NEW_CONTACT_HINT,
                        NO, HelperMethodes.getContactImage(context, NO), HelperMethodes.getContactId(NO, context),
                        null);
            }
        }
    }

    private void getContactInfoForThisContact(final Context context, final String state, final String NO, final String Contact_name) {
        h = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                if (Message.obtain() != null) {

                    if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                        // rining state
                        if (msg.obj == null) {
                            // this contact is not one of your speacal contact
                            DialogeHelperMethods.getInstance(context).drawInfo(context);
                        } else {
                            // this contact is a special contact
                            contactNoteAndInfo = (DialerInfoAndNote) msg.obj;
                            if (contactNoteAndInfo.getContact_Note() != null) {
                                DialogeHelperMethods.getInstance(context).drawContactInfo(context, contactNoteAndInfo, 0);
                            } else {// dont have any note for this special contact yet
                                DialogeHelperMethods.getInstance(context).drawContactInfo(context, contactNoteAndInfo, 1);
                            }
                        }
                    } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                        if (msg.obj == null) {
                            // this contact is not one of your speacal contact
                            DialogeHelperMethods.getInstance(context).dialogeAfterCallLog(context, Constant.NOT_SPECAIL_CONTACT_HINT,
                                    NO, HelperMethodes.getContactImage(context, NO), HelperMethodes.getContactId(NO, context),
                                    null);
                        } else {
                            contactNoteAndInfo = (DialerInfoAndNote) msg.obj;
                            DialogeHelperMethods.getInstance(context).dialogeAfterCallLog(context, Constant.SPECIAL_CONTACT_HINT,
                                    NO, HelperMethodes.getContactImage(context, NO), HelperMethodes.getContactId(NO, context),
                                    contactNoteAndInfo);
                        }
                    } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {

                        if (LastCallState.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                            if (msg.obj != null) {
                                DialogeHelperMethods.getInstance(context).enableAddNoteDuringCall(context, Contact_name, NO, HelperMethodes.getContactId(NO, context));
                            }
                        } else {
                            // out going call
                            if (msg.obj == null) {
                                // this contact is not one of your speacal contact
                                DialogeHelperMethods.getInstance(context).drawInfo(context);
                            } else {
                                // this contact is a special contact
                                contactNoteAndInfo = (DialerInfoAndNote) msg.obj;
                                if (contactNoteAndInfo.getContact_Note() != null) {
                                    DialogeHelperMethods.getInstance(context).drawContactInfo(context, contactNoteAndInfo, 0);
                                } else {// dont have any note for this special contact yet
                                    DialogeHelperMethods.getInstance(context).drawContactInfo(context, contactNoteAndInfo, 1);
                                }
                            }
                        }

                    }
                    super.handleMessage(msg);
                }
            }
        };

        final ReadDataThread myThread = new ReadDataThread(h, context, Constant.GET_CONTACTINFO_BY_NAME, Contact_name);
        myThread.start();
    }
}



