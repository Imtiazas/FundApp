package com.example.fundapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;

import java.util.ArrayList;

public class ChangeSavingsActivity extends AppCompatActivity {

    Button btn_UpdateSavings;
    TextView tv_calculatedSavingsCSA;
    EditText etSavingsNewValueCSA;
    FirebaseDatabase db;
    DatabaseReference dbRef;
    FirebaseDatabase dbSavings;
    DatabaseReference dbSavingRef;
    ArrayList<Payment> userData;
    int sumOfTotalAmounts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_savings);
        tv_calculatedSavingsCSA=findViewById(R.id.tv_calculatedSavingsCSA);
        btn_UpdateSavings=findViewById(R.id.btn_updateSavingsCSA);
        etSavingsNewValueCSA=findViewById(R.id.etSavingsNewValueCSA);
        userData= new ArrayList<Payment>();

        db=FirebaseDatabase.getInstance();
        dbRef=db.getReference("Payment");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    Payment userPayments= dataSnapshot1.getValue(Payment.class);
                    //sumTotal(userPayments.gettotalAmount());
                    sumOfTotalAmounts=sumOfTotalAmounts+parseInt(userPayments.gettotalAmount());
                }
                tv_calculatedSavingsCSA.setText(tv_calculatedSavingsCSA.getText()+valueOf(sumOfTotalAmounts));
                btn_UpdateSavings.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String temp=etSavingsNewValueCSA.getText().toString();
                        if(!temp.isEmpty())
                        {   Savings savings= new Savings(temp);
                            dbSavings=FirebaseDatabase.getInstance();

                            dbSavingRef=dbSavings.getReference("Savings");
                            dbSavingRef.setValue(savings).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(ChangeSavingsActivity.this,"Savings Updated Successfully",Toast.LENGTH_SHORT).show();
                                        etSavingsNewValueCSA.setText("");
                                    }
                                    else
                                    {
                                        Toast.makeText(ChangeSavingsActivity.this,"ERROR",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

}