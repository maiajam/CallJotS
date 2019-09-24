package com.maiajam.calljots.helper.helperMethodes;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.maiajam.calljots.R;
import com.maiajam.calljots.data.local.entity.AllPhoneContact;

public class SharedPrefHelperMethodes {

    private static SharedPreferences sp;

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

    public static void setHaveAdialgoe(Context context,Boolean hasDialoge)
    {
        sp = context.getSharedPreferences("Dialoge", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("hasAdailgoe",hasDialoge);
        editor.commit();
    }
    public static boolean isTheirAnyDialoge(Context context)
    {
        sp = context.getSharedPreferences("Dialoge", Activity.MODE_PRIVATE);
        return  sp.getBoolean("hasAdailgoe",false);
    }
}
