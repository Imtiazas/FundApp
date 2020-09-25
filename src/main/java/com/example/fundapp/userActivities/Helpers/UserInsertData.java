package com.example.fundapp;

public class UserInsertData {

     String Name, Email ,uID;
    Boolean isAdmin;

    public UserInsertData(String name, String email, String uID, Boolean isAdmin) {
        Name = name;
        Email = email;
        this.uID = uID;
        this.isAdmin = isAdmin;
    }

    public String getName() {
        return Name;
    }

    public UserInsertData() {
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }


}
