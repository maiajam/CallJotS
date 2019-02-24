package com.maiajam.calljots.data.local.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.maiajam.calljots.data.local.entity.AllPhoneContact;
import com.maiajam.calljots.data.local.entity.ContactNoteEnitiy;
import com.maiajam.calljots.data.local.entity.SpecialContactInfo;

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

    @Update
    public void update(ContactNoteEnitiy contactNoteEnitiy);

    @Delete
    public void delete(ContactNoteEnitiy contactNoteEnitiy);

    @Query("SELECT * FROM ContactNote")
    public List<ContactNoteEnitiy> getAllNote();

    @Query("DELETE FROM ContactNote  WHERE Contact_LastCallTime = :Contact_LastCallTime")
    public void deleteNote(String Contact_LastCallTime);

    @Query("UPDATE ContactNote SET Contact_NoteStuts = 1 WHERE id = :id")
    public void updateNoteByID(int id);

    @Query("SELECT * FROM ContactNote WHERE id = :Id")
    public ContactNoteEnitiy getnoteById(int Id);

    @Query("SELECT *  FROM  ContactNote WHERE  Contact_Name = :name  OR Contact_NoteStuts = 0 ORDER BY id LIMIT 1  ")
    public ContactNoteEnitiy getLastNote(String name);

    @Query("SELECT *  FROM  ContactNote WHERE  Contact_Name = :name AND Contact_NoteTitle = :NoteTitle ")
    public ContactNoteEnitiy ViewNote(String name, String NoteTitle);

    @Query("UPDATE ContactNote SET Contact_NoteStuts = 1 WHERE id = :id ")
    public void NoteIsDone(int id);

    @Query("SELECT *  FROM  ContactNote ")
    public List<ContactNoteEnitiy> getAllContactsNotes();

    @Insert
    public void AddContact(SpecialContactInfo contact);

    @Query("SELECT * FROM SpecialContactInfo")
    public List<SpecialContactInfo> getAllSpecContact();

    @Query("SELECT * FROM SpecialContactInfo WHERE contactName = :Name ")
    public SpecialContactInfo getContactInfoByName(String Name);

}
