package com.maiajam.calljots.util;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.maiajam.calljots.data.local.entity.AllPhoneContact;
import com.maiajam.calljots.data.local.entity.SpecialContactInfo;
import com.maiajam.calljots.data.local.room.RoomDao;
import com.maiajam.calljots.data.local.room.RoomManger;
import com.maiajam.calljots.helper.HelperMethodes;

/**
 * Created by maiAjam on 7/10/2018.
 */

public class CallReciver extends BroadcastReceiver {

    private AllPhoneContact contact;
    RoomManger roomManger;
    RoomDao roomDao ;
    @Override
    public void onReceive(final Context context, Intent intent) {



        // recived call info
       String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE );
       String NOCont = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
       final String Contact_name = HelperMethodes.getContactName(NOCont, context);
       // save dialer info at shared prefrence
        HelperMethodes.saveDialerInfo(context,Contact_name,NOCont);

        // get contact info for the dialer from database
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                roomManger = RoomManger.getInstance(context);
                 roomDao = roomManger.roomDao();
                contact = roomDao.getContactInfoByName(Contact_name);

            }
        });


        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {

            if(!Contact_name.isEmpty())
            {
                if (contact.getContIsSpec()== 1) {
                    // this contact is a special contact
                        HelperMethodes.drawContactInfo(context,contact);

                } else {

                    HelperMethodes.drawContactInfo(context,contact);


                   // context.startActivity(new Intent(context,TransparentActivity.class).putExtra("name",Contact_name));

                }

            }else
            {
                //   // new number ... no toast msg
            }

        }else if(state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {

        /*
            HandlerThread thread = new HandlerThread("MyHandlerThread");
            thread.start();
            // creates the handler using the passed looper
            Handler handler = new Handler(thread.getLooper());
            // creates the content observer which handles onChange on a worker thread
           history h = new history(handler,context);

            context.getContentResolver().registerContentObserver(CallLog.Calls.CONTENT_URI,true,h);


        }else if(state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))
        {

        */

        }



    }
}