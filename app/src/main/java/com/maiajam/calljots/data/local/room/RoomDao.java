package com.maiajam.calljots.data.local.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import com.maiajam.calljots.data.local.entity.AllPhoneContact;
import com.maiajam.calljots.data.local.entity.ContactNoteEnitiy;
import com.maiajam.calljots.data.model.DialerInfoAndNote;


import java.util.Date;
import java.util.List;

@Dao
public abstract class RoomDao {

    // insert
    @Insert
    public abstract long AddPhoneContacts(AllPhoneContact allPhoneContact);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(ContactNoteEnitiy contactNoteEnitiy);

    @Query("UPDATE AllPhoneContact SET contIsSpec = 1 WHERE contId = :contId ")
    public abstract void AddContact(int contId);

    // get
    @Query("SELECT * FROM AllPhoneContact WHERE NOT (contName = 'Personal')")
    public abstract List<AllPhoneContact> getAllPhoneContact();

    @Query("SELECT * FROM ContactNoteEnitiy WHERE id = :Id")
    public abstract ContactNoteEnitiy getnoteById(int Id);

    @Query("SELECT *  FROM  ContactNoteEnitiy WHERE  Contact_Name = :name  OR Contact_NoteStuts = 0 ORDER BY id ASC LIMIT 1  ")
    public abstract ContactNoteEnitiy getLastNote(String name);

    @Query("SELECT *  FROM  ContactNoteEnitiy WHERE Contact_Name = :name")
    public abstract List<ContactNoteEnitiy> getAllContactsNotes(String name);

    @Query("SELECT * FROM AllPhoneContact WHERE contIsSpec = 1")
    public abstract List<AllPhoneContact> getAllSpecContact();

    @Query("SELECT AllPhoneContact.*,ContactNoteEnitiy.* FROM AllPhoneContact INNER JOIN " +
            "ContactNoteEnitiy ON AllPhoneContact.Id = ContactNoteEnitiy.Note_Parent_Id AND AllPhoneContact.contName = :Name " +
            "ORDER BY ContactNoteEnitiy.Contact_LastCallTime DESC LIMIT 1")
    public abstract DialerInfoAndNote getContactInfoByName(String Name);

    @Query("SELECT Id FROM AllPhoneContact WHERE contName = :name")
    public abstract int getIdPersonalNote(String name);
    @Query("SELECT * FROM ContactNoteEnitiy")
    public abstract List<ContactNoteEnitiy> getAllNote();

    // delete
    @Query("DELETE FROM ContactNoteEnitiy  WHERE Contact_LastCallTime = :Contact_LastCallTime")
    public abstract void deleteNote(Date Contact_LastCallTime);


    // else
    @Query("UPDATE AllPhoneContact SET contIsSpec =  1  WHERE id = :id ")
    public abstract int SetIsSpecialContact(int id);

    @Query("SELECT contIsSpec FROM AllPhoneContact WHERE contName = :name ")
    public abstract int CheckIsSpec(String name);

    @Query("UPDATE ContactNoteEnitiy SET Contact_NoteTitle = :title AND Contact_Note = :Note WHERE id = :id")
    public abstract void updateNoteByID(int id, String title, String Note);

    @Query("UPDATE ContactNoteEnitiy SET Contact_NoteStuts = 1 WHERE id = :id")
    public abstract void updateDoneNoteByID(int id);

    @Query("SELECT *  FROM  ContactNoteEnitiy WHERE  Contact_Name = :name AND Contact_NoteTitle = :NoteTitle ")
    public abstract ContactNoteEnitiy ViewNote(String name, String NoteTitle);

    @Transaction
     public void setContAsSpec(AllPhoneContact contact)
    {
        SetIsSpecialContact(contact.getId());
        ContactNoteEnitiy ContactNoteEnit = new ContactNoteEnitiy();
        ContactNoteEnit.setNote_Parent_Id(contact.getId());
        ContactNoteEnit.setContact_Id(contact.getContId());
        ContactNoteEnit.setContact_Name(contact.getContName());
        insert(ContactNoteEnit);
    }
}
