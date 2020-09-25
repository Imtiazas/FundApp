package com.example.fundapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShowTotalSavings extends AppCompatActivity {
    private Toolbar toolbar;
    TextView tvShowTotalSavings;
    FirebaseDatabase db;
    DatabaseReference dbRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_total_savings);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Total Money");
        tvShowTotalSavings=findViewById(R.id.tvShowTotalSavings);
        db=FirebaseDatabase.getInstance();
        dbRef=db.getReference("Savings");
        dbRef.addValueEventListener(new ValueEventListener() {
            Savings savings;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                savings=dataSnapshot.getValue(Savings.class);
                tvShowTotalSavings.setText(tvShowTotalSavings.getText()+savings.gettotalSavings());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}