package com.example.fundapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.fundapp.AllUserActivity.clickedUserID;
import static com.example.fundapp.AllUserActivity.clickedUserName;

public class PaymentsShow extends AppCompatActivity {
    private Toolbar toolbar;
    public static String userId;
    public static String userName;
    TextView userNameSPR;
    EditText etYearPRS,etMonthPRS,etWeekPRS,etDuePaymentPRs,etTotalAmountPRS;
    FirebaseDatabase db;
    DatabaseReference dbRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments_show);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Payments");
        Intent intent= getIntent();
        PaymentsShow.userId=intent.getStringExtra(clickedUserID);
        PaymentsShow.userName=intent.getStringExtra(clickedUserName);
        userNameSPR=findViewById(R.id.userNameSPR);
        userNameSPR.setText(PaymentsShow.userName);
        etYearPRS=findViewById(R.id.etYearPRS);
        etMonthPRS=findViewById(R.id.etMonthPRS);
        etWeekPRS=findViewById(R.id.etWeekPRS);
        etDuePaymentPRs=findViewById(R.id.etDuePaymentPRS);
        etTotalAmountPRS=findViewById(R.id.etTotalAmountPRS);
        db=FirebaseDatabase.getInstance();
        dbRef=db.getReference("Payment").child(PaymentsShow.userId);
        dbRef.addValueEventListener(new ValueEventListener() {
            Payment userPayment;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userPayment=dataSnapshot.getValue(Payment.class);
                etYearPRS.setText(etYearPRS.getText()+userPayment.getyear());
                etMonthPRS.setText(etMonthPRS.getText()+userPayment.getmonth());
                etWeekPRS.setText(etWeekPRS.getText()+userPayment.getweek());
                etDuePaymentPRs.setText(etDuePaymentPRs.getText()+userPayment.getduePayment());
                etTotalAmountPRS.setText(etTotalAmountPRS.getText()+userPayment.gettotalAmount());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}