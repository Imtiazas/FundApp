package com.example.fundapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    public static int paymentPerWeek;
    public static int paymentPerMonth;
    public static int paymentPerYear;
    FirebaseDatabase database;
    DatabaseReference myRef;
    EditText edPaymentPerWeek ,edPaymentPerMonth, edPaymentPerYear;
    Button btn_changePayment, btn_savePayment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        edPaymentPerWeek = findViewById(R.id.edweekPayment);
        edPaymentPerMonth = findViewById(R.id.edmonthPayment);
        edPaymentPerYear = findViewById(R.id.edYearPayment);
       btn_changePayment= findViewById(R.id.changePayment_btn);
       btn_savePayment = findViewById(R.id.savePayment_btn);
       btn_savePayment.setEnabled(false);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        database = FirebaseDatabase.getInstance();
        myRef=database.getReference().child("settings").child("paymentValue");
        getSupportActionBar().setTitle("Settings");
        btn_changePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edPaymentPerWeek.setEnabled(true);
                btn_changePayment.setEnabled(false);
                edPaymentPerWeek.requestFocus();
                btn_savePayment.setEnabled(true);
            }
        });

        btn_savePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newPaymentPerWeek = edPaymentPerWeek.getText().toString().trim();

                SettingsActivity.paymentPerWeek=Integer.parseInt(newPaymentPerWeek);

                edPaymentPerWeek.setText(String.valueOf(SettingsActivity.paymentPerWeek));
                edPaymentPerWeek.setEnabled(false);
                edPaymentPerMonth.setEnabled(true);
                edPaymentPerMonth.setText(String.valueOf(paymentPerWeek*4));
                SettingsActivity.paymentPerMonth =Integer.parseInt(edPaymentPerMonth.getText().toString());
                edPaymentPerMonth.setEnabled(false);
                edPaymentPerYear.setEnabled(true);
                edPaymentPerYear.setText(String.valueOf(paymentPerMonth*12));
                SettingsActivity.paymentPerYear=Integer.parseInt(edPaymentPerYear.getText().toString());
                edPaymentPerYear.setEnabled(false);
                myRef.setValue(SettingsActivity.paymentPerWeek).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {

                            Toast.makeText(SettingsActivity.this,"Value is Saved As"+SettingsActivity.paymentPerWeek,Toast.LENGTH_SHORT).show();
                        }
                        else{
                         Toast.makeText(SettingsActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //Save in firebase settings Node
            }
        });
    }


    public void sendToStart()
    {

        Intent i = new Intent(SettingsActivity.this,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);

    }

}
