package com.maiajam.calljots.helper;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.maiajam.calljots.data.local.entity.AllPhoneContact;
import com.maiajam.calljots.data.local.entity.ContactNoteEnitiy;
import com.maiajam.calljots.data.local.entity.SpecialContactInfo;
import com.maiajam.calljots.data.local.room.RoomDao;
import com.maiajam.calljots.data.local.room.RoomManger;

;import java.util.List;

public class ReadDataThread extends Thread {

    public static ReadDataThread readDataThread ;
    private final String mName;
    Context mContext ;
    Handler mhandler ;
    int dataOperation_Type;
    AllPhoneContact contact ;
    private List<AllPhoneContact> Spec_contactList;
    private List<AllPhoneContact> All_Contact;
    private SpecialContactInfo Special_contact;
    private List<ContactNoteEnitiy> allContactNote;
    private ContactNoteEnitiy oneNote;
    private int NoteId;

    public ReadDataThread(Handler h, Context context,int Type,String name) {
        mhandler = h;
        mContext = context ;
        dataOperation_Type = Type;
        mName = name ;
    }


    @Override
    public void run() {

        RoomManger roomManger = RoomManger.getInstance(mContext);
        RoomDao roomDao = roomManger.roomDao();
        Message message = new Message();
        switch (dataOperation_Type)
        {
            case Constant.GET_CONTACTINFO_BY_NAME:
                contact = roomDao.getContactInfoByName(mName);
                message.obj = contact ;
                break;
             case Constant.GET_ALL_SPECIAL_CONTACT:
                 Spec_contactList =roomDao.getAllSpecContact();
                 message.obj = Spec_contactList ;
                 break;
            case Constant.GET_ALL_PHONE_CONTACT:
                All_Contact = roomDao.getAllPhoneContact();
                message.obj = All_Contact ;
                break;
            case Constant.ADD_NEW_CONTACT:
                roomDao.AddPhoneContacts(contact);
                // added successfully
                message.arg1 = 1;
                break;
            case Constant.ADD_TO_SPECIAL_CONTACT:
                roomDao.AddContact(Special_contact);
                // added successfully
                message.arg1 = 1 ;
                break;
            case Constant.GET_CONTACT_NOTES:
                allContactNote = roomDao.getAllContactsNotes();
                message.obj =  allContactNote ;
                break;
            case Constant.GET_ALL_NOTES:
                allContactNote = roomDao.getAllNote();
                message.obj =  allContactNote ;
                break;
            case Constant.GET_NOTE_BY_ID:
                oneNote =roomDao.getnoteById(NoteId);
                message.obj = oneNote ;
                break;
            case Constant.ADD_NEW_NOTE:
                roomDao.insert(oneNote);
                message.arg1 = 1 ;
                break;
             case Constant.UPDATE_NOTE:
                 roomDao.update(oneNote);
                 message.arg1 = 1;
                 break;


        }
        mhandler.sendMessage(message);
        }


    public void setContactInfo(AllPhoneContact newcontact) {
        contact = newcontact ;
    }

    public void setSpecialContactInfo(SpecialContactInfo contactInfo) {
        Special_contact = contactInfo;
    }
    public void setNoteId(int noteId) {
        NoteId = noteId;
    }

    public void setNote(ContactNoteEnitiy oneNote) {
        this.oneNote = oneNote;
    }
}
