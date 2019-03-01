package com.maiajam.calljots.data.model;


public class ContactLogs {


    String CallDuration;
    String date,type ,name;

    public ContactLogs()
    {

    }

    public ContactLogs(String callDuration1, String dateCallLog, String dir) {
        this.CallDuration = callDuration1 ;
        this.date = dateCallLog;
        this.type = dir ;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCallDuration(String callDuration) {
        CallDuration = callDuration;
    }

    public void setType(String type) {
        this.type = type;
    }

    //
    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getCallDuration() {
        return CallDuration;
    }

    public String getDir() {
        return type;
    }

}
