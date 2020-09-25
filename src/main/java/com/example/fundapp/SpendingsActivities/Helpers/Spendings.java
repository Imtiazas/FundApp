package com.example.fundapp;

public class Spendings {
    String purchased, money;

    public Spendings(String purchased, String money) {
        this.purchased = purchased;
        this.money = money;

    }

    public Spendings() {
    }

    public String getPurchased() {
        return purchased;
    }

    public void setPurchased(String purchased) {
        this.purchased = purchased;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

}
