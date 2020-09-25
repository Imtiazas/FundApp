package com.example.fundapp;

public class Payment {

    String uID,year,month,week,duePayment,totalAmount;

    public Payment(String uID,String year, String month, String week, String duePayment, String totalAmount) {
        this.uID=uID;
        this.year = year;
        this.month = month;
        this.week = week;
        this.duePayment = duePayment;
        this.totalAmount = totalAmount;
    }

    public Payment() {
    }

    public String getyear() {
        return year;
    }

    public void setyear(String year) {
        this.year = year;
    }

    public String getmonth() {
        return month;
    }

    public void setmonth(String month) {
        this.month = month;
    }

    public String getweek() {
        return week;
    }

    public void setweek(String week) {
        this.week =week ;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uIDy) {
        this.uID = uID;
    }

    public String getduePayment() {
        return duePayment;
    }

    public void setduePayment(String duePayment) {
        this.duePayment = duePayment;
    }

    public String gettotalAmount() {
        return totalAmount;
    }

    public void settotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
