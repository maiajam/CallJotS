package com.maiajam.calljots.util;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by maiAjam on 7/10/2018.
 */

public class CallReciver extends BroadcastReceiver {




    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences sp = context.getSharedPreferences("LastCall", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        Toast.makeText(context,"run reciver",Toast.LENGTH_LONG).show();

       String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE );

        String NOCont = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
      //  String Contact_name = Util.getContactName(NOCont, context);
        //contact = db.getContactInfoByName(Contact_name);

        //Util.drawContactInfo(context,contact);

      /*  editor.putString("Name",Contact_name);
        editor.putString("phoneNumber", (NOCont));
        editor.commit();

        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {

            if(!Contact_name.isEmpty())
            {
                if (db.CheckIsSpec(Contact_name)== 1) {

                    // this contact is a special contact
                        Util.drawContactInfo(context,contact);

                  //  context.startActivity(new Intent(context,TransparentActivity.class).putExtra("name",Contact_name));


                } else {

                    Util.drawContactInfo(context,contact);


                   // context.startActivity(new Intent(context,TransparentActivity.class).putExtra("name",Contact_name));

                }

            }else
            {
                //   // new number ... no toast msg
            }

        }else if(state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {


            HandlerThread thread = new HandlerThread("MyHandlerThread");
            thread.start();
            // creates the handler using the passed looper
            Handler handler = new Handler(thread.getLooper());
            // creates the content observer which handles onChange on a worker thread
           history h = new history(handler,context);

            context.getContentResolver().registerContentObserver(CallLog.Calls.CONTENT_URI,true,h);


        }else if(state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))
        {



        }

    */

    }
}
