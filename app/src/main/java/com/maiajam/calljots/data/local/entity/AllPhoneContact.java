package com.maiajam.calljots.data.local.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.Nullable;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "AllPhoneContact")
public class AllPhoneContact {

    @PrimaryKey(autoGenerate = true)
    private int Id;

    private int contId;
    private String contName;
    private String contPhoneNo;
    private int contIsSpec;
    private String contactPhotoUri;
    private int contPrimaryClassf;
    private String contFirstClassf;
    private String contSecClassF;
    private String contAddress;
    private String contCompanyName;

    public AllPhoneContact() {
    }
    public AllPhoneContact(int contId, String contName, String contPhoneNo, int contIsSpec, String contactPhotoUri) {
        this.contId = contId;
        this.contName = contName;
        this.contPhoneNo = contPhoneNo;
        this.contIsSpec = contIsSpec;
        this.contactPhotoUri = contactPhotoUri;
    }

    // setter
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

    public void setContSecClassF(String contSecClassF) {
        this.contSecClassF = contSecClassF;
    }

    public void setContCompanyName(String contCompanyName) {
        this.contCompanyName = contCompanyName;
    }

    public void setContAddress(String contAddress) {
        this.contAddress = contAddress;
    }

    public void setContFirstClassf(String contFirstClassf) {
        this.contFirstClassf = contFirstClassf;
    }

    public void setContPrimaryClassf(int contPrimaryClassf) {
        this.contPrimaryClassf = contPrimaryClassf;
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

    public String getContCompanyName() {
        return contCompanyName;
    }

    public String getContSecClassF() {
        return contSecClassF;
    }

    public int getContPrimaryClassf() {
        return contPrimaryClassf;
    }

    public String getContFirstClassf() {
        return contFirstClassf;
    }

    public String getContAddress() {
        return contAddress;
    }
}
