package com.example.fundapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class DeleteSpendingsActivity extends AppCompatActivity {
    public static String purchased=null;
    public static int moneySpend=0;
    private Toolbar toolbar;
    ListView listViewSpendings;
    EditText purchaseEditText, moneyEditText;
    Button deleteSpendings;
    FirebaseDatabase myDataBase;
    DatabaseReference spendingDatabse;
    List<Spendings> spendingsList;
    FirebaseDatabase dbSavings;
    DatabaseReference dbRefSavings;
    String savingsGot;
    int savingTemp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_spendings);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Delete From Savings");
        listViewSpendings = findViewById(R.id.deleteSpendingslistview);
        purchaseEditText =findViewById(R.id.editText_purchased);
        moneyEditText =findViewById(R.id.editText_moneySpend);
        deleteSpendings = findViewById(R.id.deleteSpendings_btn);
        myDataBase= FirebaseDatabase.getInstance();
        spendingsList = new ArrayList<>();
        spendingDatabse = myDataBase.getReference().child("spendings");

        //Getting Savings

        dbSavings=FirebaseDatabase.getInstance();
        dbRefSavings=dbSavings.getReference().child("Savings");
        dbRefSavings.addValueEventListener(new ValueEventListener() {
            Savings savings;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                savings=dataSnapshot.getValue(Savings.class);
                savingsGot=savings.gettotalSavings();
                savingTemp=parseInt(savingsGot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        deleteSpendings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DeleteSpendingsActivity.purchased!=null && DeleteSpendingsActivity.moneySpend!=0) {
                    spendingDatabse = myDataBase.getReference("spendings").child(purchased);
                    spendingDatabse.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(DeleteSpendingsActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                moneyEditText.setText("");
                                purchaseEditText.setText("");
                                savingTemp=savingTemp+DeleteSpendingsActivity.moneySpend;
                                Savings savings = new Savings(String.valueOf(savingTemp));
                                dbRefSavings.setValue(savings).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(DeleteSpendingsActivity.this,"Savings Updated",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(DeleteSpendingsActivity.this,"No Spending Chosen",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(DeleteSpendingsActivity.this,"Onstart",Toast.LENGTH_SHORT).show();
        spendingDatabse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                spendingsList.clear();
                for (DataSnapshot spendingSnapshot : dataSnapshot.getChildren()) {


                    Spendings spendings = spendingSnapshot.getValue(Spendings.class);
                    spendingsList.add(spendings);
                }

                SpendingsList adapter = new SpendingsList(DeleteSpendingsActivity.this, spendingsList);
                System.out.println("<><><><> InTheDeleteActivity setting Adapter");
                listViewSpendings.setAdapter(adapter);
                listViewSpendings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        moneyEditText.setText(spendingsList.get(position).getMoney());
                        purchaseEditText.setText(spendingsList.get(position).getPurchased());
                        DeleteSpendingsActivity.purchased=spendingsList.get(position).getPurchased();
                        DeleteSpendingsActivity.moneySpend=parseInt(spendingsList.get(position).getMoney());

                    }
                });

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}