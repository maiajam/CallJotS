package com.maiajam.calljots.helper;

import android.content.Context;
import android.database.AbstractCursor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.provider.MediaStore;

import com.maiajam.calljots.R;
import com.maiajam.calljots.data.local.entity.AllPhoneContact;
import com.maiajam.calljots.data.local.room.RoomDao;
import com.maiajam.calljots.data.local.room.RoomManger;
import com.maiajam.calljots.ui.fragment.AllContactFrag;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class GetAllPhoneContactThread extends Thread{

    private final Context mContext;
    private ArrayList<Object> CeckChphoneList;
    private ArrayList<Object> phoneContactsList;
    private Cursor Cr_phonesNo;
    private RoomManger roomManger;


    public GetAllPhoneContactThread(Context context,Cursor cursor) {
        mContext = context ;
        Cr_phonesNo= cursor ;
    }
    @Override
    public void run() {
        Cr_phonesNo =  mContext.getContentResolver().
                query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null, null, null,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        CeckChphoneList = new ArrayList<>();
        phoneContactsList = new ArrayList<>();

        if (Cr_phonesNo.moveToFirst()) {
            do {
                AllPhoneContact Phonecontact = new AllPhoneContact();
                int contact_ID = Integer.parseInt(Cr_phonesNo.getString(Cr_phonesNo.getColumnIndex
                        (ContactsContract.CommonDataKinds.Phone._ID)));
                Phonecontact.setContId(contact_ID);
                Phonecontact.setContName(Cr_phonesNo.getString(Cr_phonesNo.getColumnIndex
                        (ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
                Phonecontact.setContactPhotoUri(Cr_phonesNo.getString(Cr_phonesNo.getColumnIndex(
                        ContactsContract.CommonDataKinds.Photo.PHOTO_URI)));
                Phonecontact.setContPhoneNo(Cr_phonesNo.getString(Cr_phonesNo.getColumnIndex
                        (ContactsContract.CommonDataKinds.Phone.NUMBER)));
                Bitmap contact_photo = getContactPhoto(Cr_phonesNo.getString(Cr_phonesNo.getColumnIndex
                        (ContactsContract.CommonDataKinds.Photo.PHOTO_URI)), contact_ID);
                if (!CeckChphoneList.contains(Phonecontact.getContName())) {
                    roomManger = RoomManger.getInstance(mContext);
                    RoomDao roomDao = roomManger.roomDao();
                    CeckChphoneList.add(Phonecontact.getContName());
                    phoneContactsList.add(Phonecontact);
                    roomDao.AddPhoneContacts(Phonecontact);
                }
            } while (Cr_phonesNo.moveToNext());
            Cr_phonesNo.close();

            roomManger = RoomManger.getInstance(mContext);
            RoomDao roomDao = roomManger.roomDao();
            AllPhoneContact personalContactRow = new AllPhoneContact();
            personalContactRow.setContName("Personal");
            phoneContactsList.add(personalContactRow);


                                        }
    }

    private Bitmap getContactPhoto(String image_Uri, int contact_ID) {

        Bitmap photo = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.conphoto);

        if (image_Uri != null) {
            try {
                photo = MediaStore.Images.Media
                        .getBitmap(mContext.getContentResolver(),
                                Uri.parse(image_Uri));

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return photo;


    }




}
