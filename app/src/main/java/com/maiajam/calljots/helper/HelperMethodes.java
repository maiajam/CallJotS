package com.maiajam.calljots.helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maiajam.calljots.R;
import com.maiajam.calljots.data.local.entity.AllPhoneContact;
import com.maiajam.calljots.data.model.ContactLogs;
import com.maiajam.calljots.data.model.DialerInfoAndNote;
import com.maiajam.calljots.ui.activity.MainNewContactActivity;
import com.maiajam.calljots.ui.activity.NewNoteActivity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.EUICC_SERVICE;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.WINDOW_SERVICE;
import static java.lang.Thread.sleep;

public class HelperMethodes {



    public static void beginTransAction(FragmentTransaction fragmentTransaction,FragmentManager fragmentManager, Fragment fragment, int frame) {
        FragmentTransaction ft = fragmentTransaction;
        FragmentManager manager = fragmentManager;
        ft.replace(frame, fragment);
        ft.commit();
        manager.popBackStackImmediate();
    }

    public static Drawable getBitmapImage(String contactPhotoUri, Context con) {

        Bitmap photo = BitmapFactory.decodeResource(con.getResources(), R.drawable.conphoto);
        RoundedBitmapDrawable roundDrawable = RoundedBitmapDrawableFactory.create(con.getResources(), photo);
        roundDrawable.setCircular(true);

        if (contactPhotoUri != null) {
            try {
                photo = MediaStore.Images.Media
                        .getBitmap(con.getContentResolver(),
                                Uri.parse(contactPhotoUri));

                roundDrawable = RoundedBitmapDrawableFactory.create(con.getResources(), photo);
                roundDrawable.setCircular(true);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return roundDrawable;
    }
    @SuppressLint("MissingPermission")
    public static String getContactName(String phoneNumber, Context context) {

        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};
        String contactName = "";
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                contactName = cursor.getString(0);
            }
            cursor.close();
        }

        return contactName;
    }

    public static int getContactId(String phoneNumber,Context context)
    {

        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        String[] projection = new String[]{ContactsContract.PhoneLookup.CONTACT_ID};
        int COntactId = 0;
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                COntactId = cursor.getInt(0);
            }
            cursor.close();
        }

        return COntactId;
    }

    public static void saveDialerInfo(Context context, String contact_name, String noCont) {
        SharedPreferences sp = context.getSharedPreferences("LastCall", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("Name", contact_name);
        editor.putString("phoneNumber", (noCont));
        editor.commit();
    }

    public static AllPhoneContact getDailerInfo(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences("LastCall", Activity.MODE_PRIVATE);
      String Name = sp.getString("Name","");
      String no = sp.getString("phoneNumber" , "");
      AllPhoneContact c =new  AllPhoneContact();
      c.setContName(Name);
      c.setContPhoneNo(no);
      return c ;
    }

    //
    public static List<ContactLogs> getCallLogsList(FragmentActivity activity, int contact_id, String contact_number) {
        ArrayList<ContactLogs> list_Log = new ArrayList<>();
        String Numer = contact_number.replaceAll("\\D+","");
        Cursor calls = null;
            calls = activity.getContentResolver().query(CallLog.Calls.CONTENT_URI
                    , null
                    , null
                    , null
                    , CallLog.Calls.DATE + " DESC");
            int number = calls.getColumnIndex(CallLog.Calls.NUMBER);
            int duration1 = calls.getColumnIndex(CallLog.Calls.DURATION);
            int type1 = calls.getColumnIndex(CallLog.Calls.TYPE);
            int date1 = calls.getColumnIndex(CallLog.Calls.DATE);

            if (calls.moveToFirst() == true) {

                do {
                    String phNumber = calls.getString(number);
                    String callDuration1 = calls.getString(duration1);
                    String typeCallLog = calls.getString(type1);
                    String dateCallLog = calls.getString(date1);
                    String dir = null;
                    int dircode = Integer.parseInt(typeCallLog);
                    switch (dircode) {
                        case CallLog.Calls.OUTGOING_TYPE:
                            dir = "OUTGOING";
                            break;
                        case CallLog.Calls.INCOMING_TYPE:
                            dir = "INCOMING";
                            break;
                        case CallLog.Calls.MISSED_TYPE:
                            dir = "MISSED";
                            break;
                        default:
                            dir = "MISSED";
                            break;
                    }

                    if(Numer.equals(String.valueOf(phNumber)))
                    {
                        ContactLogs log = new ContactLogs(callDuration1, dateCallLog, dir);
                        list_Log.add(log);
                    }
                } while (calls.moveToNext());
        }
        return list_Log ;
    }
    public static String getContactImage(Context context,String PhoneNumber)
    {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(PhoneNumber));

        String[] projection = new String[]{ContactsContract.PhoneLookup.PHOTO_URI};
        String contactPhoto = "";
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                contactPhoto = cursor.getString(0);
            }
            cursor.close();
        }

        return contactPhoto;
    }
    public  static void CallNotifcation(Context context, int type,
                                        String name,String Note,
                                        String Actiontitel,String Numer,
                                        String imgUrl,int reminerIndecater,
                                        int conId,int id)
    {
        Intent intent ;
        PendingIntent pendingIntent ;
        String chanellId = "10" ;
        if(type == 1)
        {// this notifacation to notify the user to add a note for his specail contact
            intent = new Intent(context, NewNoteActivity.class);
            intent.putExtra("name",name);
            intent.putExtra("phoneNo",Numer);
            intent.putExtra("image_uri",imgUrl);
            intent.putExtra("contact_Id",conId);
            intent.putExtra("Id",id);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        }else if (type == 2)
        {// this notifacation to notify the user to add a new contact for his phone contact
            intent = new Intent(context, MainNewContactActivity.class);
            intent.putExtra("phoneNo",Numer);
            intent.putExtra("contact_Id",conId);
            intent.putExtra("Id",id);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        }else
        {// this notifacation to notify the user to add this contact to a specail contact list
            intent = new Intent(context, MainNewContactActivity.class);
            intent.putExtra("name",name);
            intent.putExtra("phoneNo",Numer);
            intent.putExtra(context.getString(R.string.imageUrl),imgUrl);
            intent.putExtra(context.getString(R.string.Contact_Id),conId);
            intent.putExtra("id",id);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence namE = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(chanellId, namE, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder ;
        if(reminerIndecater == 1)
        {
             builder = new NotificationCompat.Builder(context,"v")
                    .setSmallIcon(R.mipmap.logo)
                    .setContentTitle( name)
                    .setContentText(Note)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setCategory(NotificationCompat.CATEGORY_EVENT)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .addAction(R.drawable.addnewnote,Actiontitel,pendingIntent)
                     .setChannelId(chanellId)
                    .setAutoCancel(true)
                    ;
        }else
        {
            builder = new NotificationCompat.Builder(context,"v")
                    .setSmallIcon(R.mipmap.logo)
                    .setContentTitle(name)
                    .setContentText(Note)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setCategory(NotificationCompat.CATEGORY_EVENT)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .addAction(R.drawable.addnewnote,Actiontitel,pendingIntent)
                    .setChannelId(chanellId)
                    .setAutoCancel(true)

                    ;
        }

        Notification notification = builder.build();
        notification.flags = Notification.DEFAULT_ALL;
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify((int) System.currentTimeMillis(), notification);
    }

    public static void enableAddNoteDuringCall(Context context,String contact_name, String phoneNo) {

        int FLAG;
        if (Build.VERSION.SDK_INT >= 26) {
            FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            FLAG = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
        }
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSPARENT);

        params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL ;
        final WindowManager wm = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View v = inflater.inflate(R.layout.contactinfo_note_dialoge, null);
        // Add layout to window manager
        wm.addView(v, params);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(10*1000);
                    wm.removeView(v);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public static void setContactNameInfo(Context context,String Name,String Phone,String ImgUrl,int ContId)
    {
        SharedPreferences sp = context.getSharedPreferences(context.getResources().getString(R.string.Contact_Info), Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("Name", Name);
        editor.putString("phoneNumber", Phone);
        editor.putString("ImageUrl",ImgUrl);
        editor.putInt("ContId",ContId);
        editor.commit();
    }

    public static AllPhoneContact getContactInfo(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences(context.getResources().getString(R.string.Contact_Info), Activity.MODE_PRIVATE);
        String Name = sp.getString("Name","");
        String PhNo = sp.getString("phoneNumber", "");
        String ImgUrl = sp.getString("ImageUrl","");
        int ContId = sp.getInt("ContId",0);
        AllPhoneContact contact = new AllPhoneContact();
        contact.setContPhoneNo(PhNo);
        contact.setContName(Name);
        contact.setContactPhotoUri(ImgUrl);
        contact.setContId(ContId);
        return contact ;
    }
    public static void setParntIdNoteForPernol(Context context,int id) {

        SharedPreferences sp = context.getSharedPreferences("PersonalNote", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("idParent", id);
        editor.commit();
    }

    public static int getParntIdNoteForPernol(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences("PersonalNote", Activity.MODE_PRIVATE);
        int id = sp.getInt("idParent",0);
        return id ;
    }

    public static void callAction(Context context,String PhoneNo) {
        Intent CallAction = new Intent(Intent.ACTION_CALL);
        CallAction.setData(Uri.parse("tel:" + PhoneNo));
        context.startActivity(CallAction);
    }

}
