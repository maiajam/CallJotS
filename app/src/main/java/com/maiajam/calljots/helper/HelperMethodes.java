package com.maiajam.calljots.helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.maiajam.calljots.R;
import com.maiajam.calljots.data.local.entity.AllPhoneContact;

import java.io.FileNotFoundException;
import java.io.IOException;

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

    //


    public static void drawContactInfo(Context context, AllPhoneContact contact) {

        int FLAG;
        if (Build.VERSION.SDK_INT >= 26) {

            FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            FLAG = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
        }


        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.OPAQUE);

        final WindowManager wm = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View v = inflater.inflate(R.layout.contactinfo_note_dialoge, null);

        ImageView contPhotot_img = (ImageView) v.findViewById(R.id.ContPhotToast_img);
        ImageView StatusIcon_img = (ImageView) v.findViewById(R.id.status_img);

        TextView ConName_txt = (TextView)v.findViewById(R.id.ContNameToa_txt);
        TextView ConNo_txt = (TextView)v.findViewById(R.id.ContPhoNoToast_txt);
        TextView FirsClass_txt = (TextView)v.findViewById(R.id.ContFirstClassi_txt);
        TextView SecClass_txt = (TextView)v.findViewById(R.id.ContSecClass_txt);
        TextView NoteTitle_txt = (TextView)v.findViewById(R.id.NoteTitle_Toast_txt);
        TextView Status_txt = (TextView)v.findViewById(R.id.status_txt);
        TextView CatTypeToa_txt =(TextView)v.findViewById(R.id.CatTypeToa_txt);

        contPhotot_img.setImageDrawable(getBitmapImage(contact.getContactPhotoUri(),context));

        // Add layout to window manager
        wm.addView(v, params);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    sleep(1*1000);

                   wm.removeView(v);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


        thread.start();

    }



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
}
