package com.maiajam.calljots.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;

import com.maiajam.calljots.data.local.entity.AllPhoneContact;
import com.maiajam.calljots.data.local.room.RoomDao;
import com.maiajam.calljots.data.local.room.RoomManger;

public class NewContactObserver extends ContentObserver {

    Context contexT;
    private RoomManger roomManger;
    public NewContactObserver(Handler handler) {
        super(handler);
    }

    public NewContactObserver(Handler handler, Context context) {
        super(handler);
        contexT = context ;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);

            addToContact();
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);

            addToContact();

    }

    private void addToContact()
    {
        ContentResolver cr = contexT.getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            //moving cursor to last position
            //to get last element added
            cursor.moveToLast();
            String contactName = null, photo_uri = null, contactNumber = null;
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

            if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                if (pCur != null) {
                    while (pCur.moveToNext()) {
                        contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        if (contactNumber != null && contactNumber.length() > 0) {
                            contactNumber = contactNumber.replace(" ", "");
                        }
                        contactName = pCur.getString(pCur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        photo_uri = pCur.getString(pCur.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
                        String msg = "Name : " + contactName + " Contact No. : " + contactNumber;
                        //Add to phone contact table

                        AllPhoneContact newContact = new AllPhoneContact();
                        newContact.setContName(contactName);
                        newContact.setContPhoneNo(contactNumber);
                        newContact.setContactPhotoUri(photo_uri);
                        newContact.setContIsSpec(0);

                        roomManger = RoomManger.getInstance(contexT);
                        RoomDao roomDao = roomManger.roomDao();
                        roomDao.AddPhoneContacts(newContact);
                    }
                    pCur.close();
                }
            }
            cursor.close();
        }
    }
    @Override
    public boolean deliverSelfNotifications() {
        return super.deliverSelfNotifications();
    }
}
