package com.example.fundapp;

public class MoneySettings {
    int paymentValue;

    public int getPaymentValue() {
        return paymentValue;
    }

    public void setPaymentValue(int paymentValue) {
        this.paymentValue = paymentValue;
    }

    public MoneySettings(int paymentValue) {
        this.paymentValue = paymentValue;
    }

    public MoneySettings() {

    }
}
