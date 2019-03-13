package com.maiajam.calljots.helper;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.maiajam.calljots.data.local.entity.AllPhoneContact;
import com.maiajam.calljots.data.local.entity.ContactNoteEnitiy;
import com.maiajam.calljots.data.local.room.RoomDao;
import com.maiajam.calljots.data.local.room.RoomManger;
import com.maiajam.calljots.data.model.DialerInfoAndNote;

;import java.util.Date;
import java.util.List;

public class ReadDataThread extends Thread {

    public static ReadDataThread readDataThread ;
    private final String mName;
    Context mContext ;
    Handler mhandler ;
    int dataOperation_Type;
    AllPhoneContact contact ;
    private List<AllPhoneContact> Spec_contactList;
    private List<AllPhoneContact> All_Contact;
    private List<ContactNoteEnitiy> allContactNote;
    private ContactNoteEnitiy oneNote;
    private int NoteId;
    private int id;
    private Date noteDate;
    private DialerInfoAndNote contactNoteAndInfo;

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
                contactNoteAndInfo = roomDao.getContactInfoByName(mName);
                 message.obj = contactNoteAndInfo ;
                break;
             case Constant.GET_ALL_SPECIAL_CONTACT:
                 Spec_contactList =roomDao.getAllSpecContact();
                 message.obj = Spec_contactList ;
                 break;
            case Constant.GET_ALL_PHONE_CONTACT:
                All_Contact = roomDao.getAllPhoneContact();
                message.obj = All_Contact ;
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
            case Constant.ADD_NEW_CONTACT:
                roomDao.AddPhoneContacts(contact);
                // added successfully
                message.arg1 = 1;
                break;
            case Constant.ADD_TO_SPECIAL_CONTACT:
                roomDao.AddContact(contact.getContId());
                // added successfully
                message.arg1 = 1 ;
                break;
            case Constant.ADD_NEW_NOTE:
                roomDao.insert(oneNote);
                message.arg1 = 1 ;
                break;
              case Constant.CHECK_IS_SPECIAL_CONTACT:
                  message.arg1 = roomDao.CheckIsSpec(mName);
                  break;
               case Constant.UPDATE_NOTE_BY_ID:
                   roomDao.updateNoteByID(NoteId,oneNote.getContact_NoteTitle(),oneNote.getContact_Note());
                   // arg value to indicate that this thread update the note
                   message.arg1 = 1;
                   break;
                case Constant.UPDATE_NOTE_IS_DONE:
                    roomDao.updateDoneNoteByID(NoteId);
                    break;
                case Constant.DELETE_NOTE_BY_time:
                    roomDao.deleteNote(noteDate);
                    // arg value to indicate that this thread delete the note will help us at the handler
                    message.arg1 = 3;
                    break;
              case Constant.GET_LAST_CONTACT_NOTES:
                  roomDao.getLastNote(mName);
                  break;
              case Constant.GET_PERSONAL_NOTE_PARENT_ID:
                  message.obj = roomDao.getIdPersonalNote("Personal");
                  break;

        }
        mhandler.sendMessage(message);
        }


    public void setContactInfo(AllPhoneContact newcontact) {
        contact = newcontact ;
    }

    public void setSpecialContactInfo(AllPhoneContact contactInfo) {
        contact = contactInfo;
    }
    public void setNoteId(int noteId) {
        NoteId = noteId;
    }

    public void setNote(ContactNoteEnitiy oneNote) {
        this.oneNote = oneNote;
    }

    public void setNoteDate(Date noteDate) {
        this.noteDate = noteDate;
    }
}
