package com.maiajam.calljots.data.local.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.Nullable;

@Entity(tableName = "SpecialContactInfo")
public class SpecialContactInfo {

    @PrimaryKey(autoGenerate = true)
    private int ContId;

    private String contactName;
    private String contactPhoneNumber;
    private int contPrimaryClassf;
    private String contFirstClassf;
    private String contSecClassF;
    private String contAddress;
    private String contCompanyName;
    private String contactPhotoUri;


    public SpecialContactInfo() {

    }

    public SpecialContactInfo(String contactName, String contactPhoneNumber, int contPrimaryClassf, String contFirstClassf, String contSecClassF, String contAddress, String contCompanyName, String contactPhotoUri) {
        this.contactName = contactName;
        this.contactPhoneNumber = contactPhoneNumber;
        this.contPrimaryClassf = contPrimaryClassf;
        this.contFirstClassf = contFirstClassf;
        this.contSecClassF = contSecClassF;
        this.contAddress = contAddress;
        this.contCompanyName = contCompanyName;
        this.contactPhotoUri = contactPhotoUri;
    }

    public void setContId(@Nullable int contId) {
        ContId = contId;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public void setContactPhoneNumber(String contactPhoneNumber) {
        this.contactPhoneNumber = contactPhoneNumber;
    }

    public void setContPrimaryClassf(int contPrimaryClassf) {
        this.contPrimaryClassf = contPrimaryClassf;
    }

    public void setContFirstClassf(String contFirstClassf) {
        this.contFirstClassf = contFirstClassf;
    }

    public void setContactPhotoUri(String contactPhotoUri) {
        this.contactPhotoUri = contactPhotoUri;
    }

    public void setContAddress(String contAddress) {
        this.contAddress = contAddress;
    }

    public void setContCompanyName(String contCompanyName) {
        this.contCompanyName = contCompanyName;
    }

    public void setContSecClassF(String contSecClassF) {
        this.contSecClassF = contSecClassF;
    }

    // getter
    @Nullable
    public int getContId() {
        return ContId;
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactPhoneNumber() {
        return contactPhoneNumber;
    }

    public String getContAddress() {
        return contAddress;
    }

    public String getContFirstClassf() {
        return contFirstClassf;
    }

    public int getContPrimaryClassf() {
        return contPrimaryClassf;
    }

    public String getContSecClassF() {
        return contSecClassF;
    }

    public String getContactPhotoUri() {
        return contactPhotoUri;
    }

    public String getContCompanyName() {
        return contCompanyName;
    }
}
