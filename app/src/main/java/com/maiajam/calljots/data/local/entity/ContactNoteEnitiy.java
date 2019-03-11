package com.maiajam.calljots.data.local.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.Nullable;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;


@Entity(foreignKeys = @ForeignKey(entity = AllPhoneContact.class,
        parentColumns = "Id",
        childColumns = "Note_Parent_Id",
        onDelete = CASCADE),
  indices = {@Index(value = "Note_Parent_Id")})

public class ContactNoteEnitiy {

    @PrimaryKey(autoGenerate = true)
    private int Id;

    @ColumnInfo(name = "Note_Parent_Id")
    private int Note_Parent_Id;
    private int Contact_Id;
    private String Contact_Name;
    private String Contact_Note;
    private String Contact_NoteTitle;
    private int Contact_NoteStuts;
    private Date Contact_LastCallTime;

    public ContactNoteEnitiy() {

    }

    public ContactNoteEnitiy(int id, int note_Parent_Id, int contact_Id, String contact_Name,
                             String contact_Note, String contact_NoteTitle, int contact_NoteStuts, Date contact_LastCallTime) {
        Id = id;
        Note_Parent_Id = note_Parent_Id;
        Contact_Id = contact_Id;
        Contact_Name = contact_Name;
        Contact_Note = contact_Note;
        Contact_NoteTitle = contact_NoteTitle;
        Contact_NoteStuts = contact_NoteStuts;
        Contact_LastCallTime = contact_LastCallTime;
    }

    public void setId(@Nullable int id) {
        Id = id;
    }

    public void setContact_Id(int contact_Id) {
        Contact_Id = contact_Id;
    }

    public void setContact_Name(String contact_Name) {
        Contact_Name = contact_Name;
    }

    public void setContact_Note(String contact_Note) {
        Contact_Note = contact_Note;
    }

    public void setContact_LastCallTime(Date contact_LastCallTime) {
        Contact_LastCallTime = contact_LastCallTime;
    }

    public void setContact_NoteStuts(int contact_NoteStuts) {
        Contact_NoteStuts = contact_NoteStuts;
    }

    public void setContact_NoteTitle(String contact_NoteTitle) {
        Contact_NoteTitle = contact_NoteTitle;
    }

    public void setNote_Parent_Id(int note_Parent_Id) {
        Note_Parent_Id = note_Parent_Id;
    }

    //getter


    public int getNote_Parent_Id() {
        return Note_Parent_Id;
    }

    @Nullable
    public int getId() {
        return Id;
    }

    public int getContact_Id() {
        return Contact_Id;
    }

    public String getContact_Name() {
        return Contact_Name;
    }

    public Date getContact_LastCallTime() {
        return Contact_LastCallTime;
    }

    public String getContact_Note() {
        return Contact_Note;
    }

    public int getContact_NoteStuts() {
        return Contact_NoteStuts;
    }

    public String getContact_NoteTitle() {
        return Contact_NoteTitle;
    }
}
