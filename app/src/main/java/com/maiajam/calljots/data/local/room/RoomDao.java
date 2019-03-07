package com.maiajam.calljots.data.local.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.maiajam.calljots.data.local.entity.AllPhoneContact;
import com.maiajam.calljots.data.local.entity.ContactNoteEnitiy;
import com.maiajam.calljots.data.model.DialerInfoAndNote;


import java.util.Date;
import java.util.List;

@Dao
public interface RoomDao {

    @Insert
    public void AddPhoneContacts(AllPhoneContact allPhoneContact);

    @Query("SELECT * FROM AllPhoneContact")
    public List<AllPhoneContact> getAllPhoneContact();

    @Query("UPDATE AllPhoneContact SET contIsSpec =  1  WHERE id = :id ")
    public void SetIsSpecialContact(int id);

    @Query("SELECT contIsSpec FROM AllPhoneContact WHERE contName = :name ")
    public int CheckIsSpec(String name);

    @Insert
    public void insert(ContactNoteEnitiy contactNoteEnitiy);

    @Delete
    public void delete(ContactNoteEnitiy contactNoteEnitiy);

    @Query("SELECT * FROM ContactNoteEnitiy")
    public List<ContactNoteEnitiy> getAllNote();

    @Query("DELETE FROM ContactNoteEnitiy  WHERE Contact_LastCallTime = :Contact_LastCallTime")
    public void deleteNote(Date Contact_LastCallTime);

    @Query("UPDATE ContactNoteEnitiy SET Contact_NoteTitle = :title AND Contact_Note = :Note WHERE id = :id")
    public void updateNoteByID(int id,String title,String Note);

    @Query("UPDATE ContactNoteEnitiy SET Contact_NoteStuts = 1 WHERE id = :id")
    public void updateDoneNoteByID(int id);

    @Query("SELECT * FROM ContactNoteEnitiy WHERE id = :Id")
    public ContactNoteEnitiy getnoteById(int Id);

    @Query("SELECT *  FROM  ContactNoteEnitiy WHERE  Contact_Name = :name  OR Contact_NoteStuts = 0 ORDER BY id LIMIT 1  ")
    public ContactNoteEnitiy getLastNote(String name);

    @Query("SELECT *  FROM  ContactNoteEnitiy WHERE  Contact_Name = :name AND Contact_NoteTitle = :NoteTitle ")
    public ContactNoteEnitiy ViewNote(String name, String NoteTitle);

    @Query("SELECT *  FROM  ContactNoteEnitiy ")
    public List<ContactNoteEnitiy> getAllContactsNotes();

    @Query("UPDATE AllPhoneContact SET contIsSpec = 1 WHERE id = :id")
    public void AddContact(int id);

    @Query("SELECT * FROM AllPhoneContact WHERE contIsSpec = 1")
    public List<AllPhoneContact> getAllSpecContact();

    @Query("SELECT AllPhoneContact.*,ContactNoteEnitiy.* FROM AllPhoneContact INNER JOIN ContactNoteEnitiy ON AllPhoneContact.contName = ContactNoteEnitiy.Contact_Name WHERE contName = :Name ")
    public DialerInfoAndNote getContactInfoByName(String Name);

}
