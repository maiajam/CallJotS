package com.maiajam.calljots.data.model;

import java.util.Date;

public class DialerInfoAndNote {

    private int Id;

    private int contId;
    private String contName;
    private String contPhoneNo;
    private int contIsSpec;
    private String contactPhotoUri;
    private int contPrimaryClassf;
    private String contFirstClassf;
    private String contSecClassF;


    private String Contact_Name;
    private String Contact_Note;
    private String Contact_NoteTitle;
    private int Contact_NoteStuts;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getContId() {
        return contId;
    }

    public void setContId(int contId) {
        this.contId = contId;
    }

    public String getContName() {
        return contName;
    }

    public void setContName(String contName) {
        this.contName = contName;
    }

    public String getContPhoneNo() {
        return contPhoneNo;
    }

    public void setContPhoneNo(String contPhoneNo) {
        this.contPhoneNo = contPhoneNo;
    }

    public int getContIsSpec() {
        return contIsSpec;
    }

    public void setContIsSpec(int contIsSpec) {
        this.contIsSpec = contIsSpec;
    }

    public String getContactPhotoUri() {
        return contactPhotoUri;
    }

    public void setContactPhotoUri(String contactPhotoUri) {
        this.contactPhotoUri = contactPhotoUri;
    }

    public int getContPrimaryClassf() {
        return contPrimaryClassf;
    }

    public void setContPrimaryClassf(int contPrimaryClassf) {
        this.contPrimaryClassf = contPrimaryClassf;
    }

    public String getContFirstClassf() {
        return contFirstClassf;
    }

    public void setContFirstClassf(String contFirstClassf) {
        this.contFirstClassf = contFirstClassf;
    }

    public String getContSecClassF() {
        return contSecClassF;
    }

    public void setContSecClassF(String contSecClassF) {
        this.contSecClassF = contSecClassF;
    }

    public String getContact_Name() {
        return Contact_Name;
    }

    public void setContact_Name(String contact_Name) {
        Contact_Name = contact_Name;
    }

    public String getContact_Note() {
        return Contact_Note;
    }

    public void setContact_Note(String contact_Note) {
        Contact_Note = contact_Note;
    }

    public String getContact_NoteTitle() {
        return Contact_NoteTitle;
    }

    public void setContact_NoteTitle(String contact_NoteTitle) {
        Contact_NoteTitle = contact_NoteTitle;
    }

    public int getContact_NoteStuts() {
        return Contact_NoteStuts;
    }

    public void setContact_NoteStuts(int contact_NoteStuts) {
        Contact_NoteStuts = contact_NoteStuts;
    }
}
