package com.maiajam.calljots.data.local.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.Nullable;

@Entity(tableName = "AllPhoneContact")
public class AllPhoneContact {

    @PrimaryKey(autoGenerate = true)
    private int Id;

    private int contId;
    private String contName;
    private String contPhoneNo;
    private int contIsSpec;
    private String contactPhotoUri;

    public AllPhoneContact() {

    }

    public AllPhoneContact(int contId, String contName, String contPhoneNo, int contIsSpec, String contactPhotoUri) {
        this.contId = contId;
        this.contName = contName;
        this.contPhoneNo = contPhoneNo;
        this.contIsSpec = contIsSpec;
        this.contactPhotoUri = contactPhotoUri;
    }

    public void setContId(int contId) {
        this.contId = contId;
    }

    public void setId(@Nullable int id) {
        Id = id;
    }

    public void setContIsSpec(int contIsSpec) {
        this.contIsSpec = contIsSpec;
    }

    public void setContactPhotoUri(String contactPhotoUri) {
        this.contactPhotoUri = contactPhotoUri;
    }

    public void setContName(String contName) {
        this.contName = contName;
    }

    public void setContPhoneNo(String contPhoneNo) {
        this.contPhoneNo = contPhoneNo;
    }

    //getter
    @Nullable
    public int getId() {
        return Id;
    }

    public int getContId() {
        return contId;
    }

    public int getContIsSpec() {
        return contIsSpec;
    }

    public String getContactPhotoUri() {
        return contactPhotoUri;
    }

    public String getContName() {
        return contName;
    }

    public String getContPhoneNo() {
        return contPhoneNo;
    }
}
