package com.example.fundapp;

public class NotificationsModel {
    String date;
    String msg;
    String name;
    String uID;

    public NotificationsModel(String date, String msg, String name, String uID) {
        this.date = date;
        this.msg = msg;
        this.name = name;
        this.uID = uID;
    }

    public NotificationsModel() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }
}
