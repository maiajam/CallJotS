package com.maiajam.calljots.helper.helperMethodes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maiajam.calljots.R;
import com.maiajam.calljots.data.model.DialerInfoAndNote;
import com.maiajam.calljots.helper.Constant;
import com.maiajam.calljots.helper.Global.WindowsMangerSingleTon;
import com.maiajam.calljots.ui.activity.MainNewContactActivity;
import com.maiajam.calljots.ui.activity.NewNoteActivity;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.WINDOW_SERVICE;
import static com.maiajam.calljots.helper.helperMethodes.HelperMethodes.addAsSpecialContact;
import static com.maiajam.calljots.helper.helperMethodes.HelperMethodes.addNoteForCallLog;
import static com.maiajam.calljots.helper.helperMethodes.HelperMethodes.getDailerInfo;
import static com.maiajam.calljots.helper.helperMethodes.SharedPrefHelperMethodes.isTheirAnyDialoge;
import static com.maiajam.calljots.helper.helperMethodes.SharedPrefHelperMethodes.setHaveAdialgoe;
import static java.lang.Thread.sleep;

public class DialogeHelperMethods {


    private static Context context;
    private static WindowManager windowsMangerInstance;
    private static View v, viewDialgoe;
    private static DialogeHelperMethods Instance;
    private final LayoutInflater inflater;

    private DialogeHelperMethods(Context context) {
        windowsMangerInstance = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        viewDialgoe = inflater.inflate(R.layout.contactinfo_note_dialoge, null);
    }

    public static DialogeHelperMethods getInstance(Context context) {
        if (Instance == null)
            Instance = new DialogeHelperMethods(context);
        return Instance;
    }

    public static void dialogeAfterCallLog(final Context context, int hintContactType,
                                           final String number, final String imgUri, final int contactId,
                                           DialerInfoAndNote contactInfo) {


        // fill data in the field
        TextView ConName_txt = (TextView) viewDialgoe.findViewById(R.id.ContNameToa_txt);
        TextView ConNo_txt = (TextView) viewDialgoe.findViewById(R.id.ContPhoNoToast_txt);
        TextView FirsClass_txt = (TextView) viewDialgoe.findViewById(R.id.ContFirstClassi_txt);
        TextView SecClass_txt = (TextView) viewDialgoe.findViewById(R.id.ContSecClass_txt);
        TextView CatagoryType = (TextView) viewDialgoe.findViewById(R.id.CatTypeToa_txt);
        TextView afterDialoge_Txt = (TextView) viewDialgoe.findViewById(R.id.noteDialoge_afterDialoge_Txt);
        ImageView contactImg = (ImageView) viewDialgoe.findViewById(R.id.ContPhotToast_img);
        Button ok_b = (Button) viewDialgoe.findViewById(R.id.noteDialoge_ok_b);
        Button cancel_b = (Button) viewDialgoe.findViewById(R.id.noteDialoge_cancel_b);

        //

        ConName_txt.setText(HelperMethodes.getDailerInfo(context).getContName());
        ConNo_txt.setText(number);
        contactImg.setImageDrawable(HelperMethodes.getBitmapImage(imgUri, context));
        if (contactInfo != null) {
            FirsClass_txt.setText(contactInfo.getContFirstClassf());
            SecClass_txt.setText(contactInfo.getContSecClassF());
            int cat = contactInfo.getContPrimaryClassf();
            if (cat == Constant.FAMILY_PRIMER_CAT) {
                CatagoryType.setText(context.getString(R.string.family_cat));
            } else if (cat == Constant.BUSSINESS_PRIMERY_CAT) {
                CatagoryType.setText(context.getString(R.string.Bussiness_cat));
            } else if (cat == Constant.FRIEND_PRIMERY_CAT) {
                CatagoryType.setText(context.getString(R.string.Friend_cat));
            } else {
                CatagoryType.setVisibility(View.INVISIBLE);
            }
        } else {
            CatagoryType.setVisibility(View.INVISIBLE);
        }


        LinearLayout noteAfterDialoge = (LinearLayout) viewDialgoe.findViewById(R.id.noteDialoge_Lin_afterCallLog);
        noteAfterDialoge.setVisibility(View.VISIBLE);
        //
        LinearLayout noteTitleLin = (LinearLayout) viewDialgoe.findViewById(R.id.ContactInfo_Lin_);
        noteTitleLin.setVisibility(View.GONE);
        //

        cancel_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                windowsMangerInstance.removeView(viewDialgoe);
            }
        });
        if (hintContactType == Constant.NEW_CONTACT_HINT) {
            afterDialoge_Txt.setText(context.getString(R.string.Qusetion_label));
            ok_b.setText(context.getString(R.string.Label_ADDNewContact));
            ok_b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addNewContact(context, number, contactId, 0, v);
                }
            });

        } else if (hintContactType == Constant.SPECIAL_CONTACT_HINT) {
            afterDialoge_Txt.setText(context.getString(R.string.label_TakeANote));
            ok_b.setText(context.getString(R.string.label_OkTakeAnote));
            ok_b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //
                    addNoteForCallLog(context, getDailerInfo(context).getContName(), number, imgUri, contactId, 0);
                  /*  windowsMangerInstance.RemoveView(viewDialgoe);
                    setHaveAdialgoe(context, false);*/
                }
            });

        } else if (hintContactType == Constant.NOT_SPECAIL_CONTACT_HINT) {

            afterDialoge_Txt.setText(context.getString(R.string.Label_ADDAddAsSpecialContact));
            ok_b.setText(context.getString(R.string.label_OKAddAsSpecial));
            ok_b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addAsSpecialContact(context, getDailerInfo(context).getContName(), number, imgUri, contactId);
                  /*  windowsMangerInstance.RemoveView(viewDialgoe);
                    setHaveAdialgoe(context, false);*/
                }
            });
        }
        //
        // if (isTheirAnyDialoge(context))
        //   windowsMangerInstance.RemoveView(v);

        windowsMangerInstance.addView(viewDialgoe,ge);
        windowsMangerInstance.AddView(viewDialgoe, windowsMangerInstance.getWindoesMangerParam(Constant.AFTER_Call_HINT));
        //  setHaveAdialgoe(context, true);
    }

    private static void addNewContact(Context context, String phoneNo, int conId, int id, View v) {
        Intent intent = new Intent(context, MainNewContactActivity.class);
        intent.putExtra("phoneNo", phoneNo);
        intent.putExtra("contact_Id", conId);
        intent.putExtra("Id", id);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        windowsMangerInstance.RemoveView(viewDialgoe);
        setHaveAdialgoe(context, false);
    }

    //
    public static void drawContactInfo(final Context context, final DialerInfoAndNote contact, int hint) {

        windowsMangerInstance = WindowsMangerSingleTon.getWindowmangerInstance(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View v = inflater.inflate(R.layout.contactinfo_note_dialoge, null);
        // fill data in the field
        addContentToTheView(context, v, contact, hint);
      /*  if (isTheirAnyDialoge(context))
            windowsMangerInstance.RemoveView(viewDialgoe);*/
        windowsMangerInstance.AddView(v, windowsMangerInstance.getWindoesMangerParam(Constant.RECIVED_CALL_HINT));
        //setHaveAdialgoe(context, true);
    }

    private static void addContentToTheView(Context context, View v, DialerInfoAndNote contact, int hint) {

        ImageView contPhotot_img = (ImageView) v.findViewById(R.id.ContPhotToast_img);
        ImageView StatusIcon_img = (ImageView) v.findViewById(R.id.status_img);
        TextView ConName_txt = (TextView) v.findViewById(R.id.ContNameToa_txt);
        TextView ConNo_txt = (TextView) v.findViewById(R.id.ContPhoNoToast_txt);
        TextView FirsClass_txt = (TextView) v.findViewById(R.id.ContFirstClassi_txt);
        TextView SecClass_txt = (TextView) v.findViewById(R.id.ContSecClass_txt);
        TextView NoteTitle_txt = (TextView) v.findViewById(R.id.NoteTitle_Toast_txt);
        TextView Status_txt = (TextView) v.findViewById(R.id.status_txt);
        TextView CatagoryType = (TextView) v.findViewById(R.id.CatTypeToa_txt);
        ImageView contactImg = (ImageView) v.findViewById(R.id.ContPhotToast_img);
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
        if (contact.getContactPhotoUri() != null) {
            contactImg.setImageDrawable(HelperMethodes.getBitmapImage(contact.getContactPhotoUri().toString(), context));
        }
        int cat = contact.getContPrimaryClassf();
        if (cat == Constant.FAMILY_PRIMER_CAT) {
            CatagoryType.setText(context.getString(R.string.family_cat));
        } else if (cat == Constant.BUSSINESS_PRIMERY_CAT) {
            CatagoryType.setText(context.getString(R.string.Bussiness_cat));
        } else {
            CatagoryType.setText(context.getString(R.string.Friend_cat));
        }
        if (hint == 1) {
            // this special contact dosnt has any note
            NoteTitle_txt.setText("you dont have any note for this contact");
            StatusIcon_img.setVisibility(View.GONE);
            Status_txt.setVisibility(View.GONE);

        } else {
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

    public static void enableAddNoteDuringCall(final Context context, final String contact_name, final String phoneNo, final int contactId) {

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
                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSPARENT);

        params.x = 40;
        params.gravity = Gravity.CENTER_VERTICAL;
        windowsMangerInstance = WindowsMangerSingleTon.getWindowmangerInstance(context);
        final View v = windowsMangerInstance.inflater.inflate(R.layout.dialoge_addnote_duringcall, null);
        // Add layout to window manager
        ImageView logo = (ImageView) v.findViewById(R.id.AddNoteDuringCall_ImgView_Logo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, NewNoteActivity.class).
                        setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).
                        putExtra("contact_Id", contactId).
                        putExtra(context.getString(R.string.phoneNoExtra), phoneNo).
                        putExtra("name", contact_name));
            }
        });
        windowsMangerInstance.AddView(v, params);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(10 * 1000);
                    windowsMangerInstance.RemoveView(v);
                    // setHaveAdialgoe(context, false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void drawInfo(Context context) {

        // fill data in the field
        TextView ConName_txt = (TextView) v.findViewById(R.id.ContNameToa_txt);
        TextView ConNo_txt = (TextView) v.findViewById(R.id.ContPhoNoToast_txt);
        TextView NoteTitle_txt = (TextView) v.findViewById(R.id.NoteTitle_Toast_txt);
        ImageView contactImg = (ImageView) v.findViewById(R.id.ContPhotToast_img);
        View pView = (View) v.findViewById(R.id.partView);
        LinearLayout linStuts = (LinearLayout) v.findViewById(R.id.linStuts);
        LinearLayout linClass = (LinearLayout) v.findViewById(R.id.linClassifcation);
        TextView CatagoryType = (TextView) v.findViewById(R.id.CatTypeToa_txt);

        ConName_txt.setText(getDailerInfo(context).getContName());
        ConNo_txt.setText(getDailerInfo(context).getContPhoneNo());
        CatagoryType.setVisibility(View.INVISIBLE);
        contactImg.setImageDrawable(HelperMethodes.getBitmapImage(HelperMethodes.getDailerInfo(context).getContactPhotoUri(), context));
        NoteTitle_txt.setText("' This Contact Is Not one Of your speacal contact '");
        linClass.setVisibility(View.GONE);
        linStuts.setVisibility(View.GONE);
        pView.setVisibility(View.GONE);
/*
        if (isTheirAnyDialoge(context))
            windowsMangerInstance.RemoveView(viewDialgoe);
        setHaveAdialgoe(context, true);*/

        windowsMangerInstance.AddView(v, windowsMangerInstance.getWindoesMangerParam(Constant.RECIVED_CALL_HINT));
        removeViewAfteraWhile(context, v);

    }

    private void removeViewAfteraWhile(final Context context, final View v) {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                WindowsMangerSingleTon.getWindowmangerInstance(context).RemoveView(v);
                setHaveAdialgoe(context, false);
            }
        }, 2000);
    }

    public void removeAnyView() {
        windowsMangerInstance.removeAnyView(v, viewDialgoe);
    }
}
