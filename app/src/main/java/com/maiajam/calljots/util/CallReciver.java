package com.maiajam.calljots.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.maiajam.calljots.data.local.entity.AllPhoneContact;
import com.maiajam.calljots.data.local.room.RoomDao;
import com.maiajam.calljots.data.local.room.RoomManger;
import com.maiajam.calljots.helper.Constant;
import com.maiajam.calljots.helper.HelperMethodes;
import com.maiajam.calljots.helper.ReadDataThread;

/**
 * Created by maiAjam on 7/10/2018.
 */

public class CallReciver extends BroadcastReceiver {

    private AllPhoneContact contact;
    RoomManger roomManger;

    RoomDao roomDao ;
    Handler h;
    @Override
    public void onReceive(final Context context, Intent intent) {

        Toast.makeText(context,"work done",Toast.LENGTH_LONG).show();

        // recived call info
        /*
       final String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE );
       String NOCont = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
       final String Contact_name = HelperMethodes.getContactName(NOCont, context);
       // save dialer info at shared prefrence
        HelperMethodes.saveDialerInfo(context,Contact_name,NOCont);

        // get contact info for the dialer from database
     /*   AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                roomManger = RoomManger.getInstance(context);
                 roomDao = roomManger.roomDao();
                contact = roomDao.getContactInfoByName(Contact_name);

            }
        });


//        h = new Handler() {

            @Override
        //    public void handleMessage(Message msg) {

                if(Message.obtain() != null)
                {
                     contact = (AllPhoneContact) msg.obj;
                    if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {

                        if (!Contact_name.isEmpty()) {
                            if (contact.getContIsSpec() == 1) {
                                // this contact is a special contact
                                HelperMethodes.drawContactInfo(context, contact);

                            } else {

                                HelperMethodes.drawContactInfo(context, contact);


                                // context.startActivity(new Intent(context,TransparentActivity.class).putExtra("name",Contact_name));

                            }

                        } else {
                            //   // new number ... no toast msg
                        }
                    }
                }

                super.handleMessage(msg);
            }
        };

        final ReadDataThread myThread  = new ReadDataThread(h,context,Constant.GET_CONTACTINFO_BY_NAME,Contact_name);
        myThread.start();





      /*  if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {

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
