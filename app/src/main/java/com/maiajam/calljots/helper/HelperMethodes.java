package com.maiajam.calljots.helper;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
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
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.WINDOW_SERVICE;
import static java.lang.Thread.sleep;

public class HelperMethodes {



    public static void beginTransAction(FragmentTransaction fragmentTransaction, Fragment fragment, int frame) {
        FragmentTransaction ft = fragmentTransaction;
        ft.replace(frame, fragment);
        ft.commit();
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
        Cursor contact = activity.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null,
                ContactsContract.Contacts._ID + "=?", new String[]{String.valueOf(contact_id)}, null, null);

        if (contact != null && contact.moveToNext()) {
            String lookupKey = contact.getString(contact.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
            int contactId = contact.getInt(contact.getColumnIndex(ContactsContract.Contacts._ID));
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


    public  static void CallNotifcation(Context context, int type,String name,String Note,String Actiontitel,String Numer,String imgUrl,int reminerIndecater)
    {
        Intent intent ;
        PendingIntent pendingIntent ;
        if(type == 1)
        {
            // this notifacation to notify the user to add a note for his specail contact
            intent = new Intent(context, NewNoteActivity.class);
            intent.putExtra("name",name);
            intent.putExtra("phoneNo",Numer);
            intent.putExtra("image_uri",imgUrl);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        }else if (type == 2)
        {
            // this notifacation to notify the user to add a new contact for his phone contact
            intent = new Intent(context, MainNewContactActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        }else
        {
            // this notifacation to notify the user to add this contact to a specail contact list
            intent = new Intent(context, MainNewContactActivity.class);
            intent.putExtra(context.getResources().getString(R.string.NameExtra),name);
            intent.putExtra(context.getResources().getString(R.string.FirstPhone),Numer);
            intent.putExtra(context.getResources().getString(R.string.imageUrl),imgUrl);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence namE = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("10", namE, importance);
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
                    .setContentTitle( name + "Note")
                    .setContentText(Note)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setCategory(NotificationCompat.CATEGORY_EVENT)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .addAction(R.drawable.addnewnote,Actiontitel,pendingIntent)
                    ;
        }else
        {
            builder = new NotificationCompat.Builder(context,"v")
                    .setSmallIcon(R.mipmap.logo)
                    .setContentTitle(name + "Note")
                    .setContentText(Note)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setCategory(NotificationCompat.CATEGORY_EVENT)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .addAction(R.drawable.addnewnote,Actiontitel,pendingIntent)
                    ;
        }
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, builder.build());
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
    public static void drawInfo(Context context) {

        final WindowManager wm = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View v = inflater.inflate(R.layout.contactinfo_note_dialoge, null);
        // fill data in the field
        TextView ConName_txt = (TextView) v.findViewById(R.id.ContNameToa_txt);
        TextView ConNo_txt = (TextView) v.findViewById(R.id.ContPhoNoToast_txt);
        TextView NoteTitle_txt = (TextView) v.findViewById(R.id.NoteTitle_Toast_txt);

        ConName_txt.setText(getDailerInfo(context).getContName());
        ConNo_txt.setText(getDailerInfo(context).getContPhoneNo());
        NoteTitle_txt.setText("' This Contact Is Not one Of your speacal contact '");
        wm.addView(v, getWindoesMangerParam(context));
    }

    private static void addContentToTheView(Context context, View v, DialerInfoAndNote contact) {

        ImageView contPhotot_img = (ImageView) v.findViewById(R.id.ContPhotToast_img);
        ImageView StatusIcon_img = (ImageView) v.findViewById(R.id.status_img);
        TextView ConName_txt = (TextView) v.findViewById(R.id.ContNameToa_txt);
        TextView ConNo_txt = (TextView) v.findViewById(R.id.ContPhoNoToast_txt);
        TextView FirsClass_txt = (TextView) v.findViewById(R.id.ContFirstClassi_txt);
        TextView SecClass_txt = (TextView) v.findViewById(R.id.ContSecClass_txt);
        TextView NoteTitle_txt = (TextView) v.findViewById(R.id.NoteTitle_Toast_txt);
        TextView Status_txt = (TextView) v.findViewById(R.id.status_txt);
        //
        String ContName = contact.getContName();
        String first = contact.getContFirstClassf();
        String Sec = contact.getContSecClassF();
        String ContPhoneNo = String.valueOf(contact.getContPhoneNo());
        String NoteTitle = contact.getContact_NoteTitle();
        int status = contact.getContact_NoteStuts();

        ConName_txt.setText(ContName);
        ConNo_txt.setText(ContPhoneNo);
        FirsClass_txt.setText(first);
        SecClass_txt.setText(Sec);
        ConNo_txt.setText(ContPhoneNo);
        NoteTitle_txt.setText("'" + NoteTitle + "'");
        if (status == 1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                StatusIcon_img.setImageDrawable(context.getDrawable(R.drawable.check_done));
            } else {
                StatusIcon_img.setImageDrawable(context.getResources().getDrawable(R.drawable.check_done));
            }
            Status_txt.setText("done");
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                StatusIcon_img.setImageDrawable(context.getDrawable(R.drawable.pending));
            } else {
                StatusIcon_img.setImageDrawable(context.getResources().getDrawable(R.drawable.pending));
            }
            Status_txt.setText("pending");
        }
    }
    //
    public static void drawContactInfo(final Context context, final DialerInfoAndNote contact) {

        final WindowManager wm = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View v = inflater.inflate(R.layout.contactinfo_note_dialoge, null);
        // fill data in the field
        addContentToTheView(context,v,contact);
        wm.addView(v, getWindoesMangerParam(context));
    }

    private static WindowManager.LayoutParams getWindoesMangerParam(Context context)
    {
        int FLAG;
        if (Build.VERSION.SDK_INT >= 26) {
            FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            FLAG = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
        }
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSPARENT);

        params.gravity = Gravity.CENTER_VERTICAL ;
        return params ;
    }
}
