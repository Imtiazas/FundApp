package com.example.fundapp;

public class UserRetrieveData {

    String Name;
    String uID;
    String Email;
    Boolean Admin;

    public UserRetrieveData() {
    }
    public UserRetrieveData(Boolean admin,String email,String name, String uID ) {
        this.Name = name;
        this.uID = uID;
        this.Email = email;
        this.Admin = admin;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {

        this.uID = uID;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public Boolean getAdmin() {
        return Admin;
    }

    public void setAdmin(Boolean admin) {
        Admin = admin;
    }


}
