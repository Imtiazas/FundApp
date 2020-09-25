package com.example.fundapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class MoneySpendingsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    ListView listViewSpendings;
    EditText purchaseEditText, moneyEditText;
    Button saveSpendings;
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
        setContentView(R.layout.activity_money_spendings);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Spendings");
        listViewSpendings = findViewById(R.id.spendingslistview);
        purchaseEditText =findViewById(R.id.editText_purchased);
        moneyEditText =findViewById(R.id.editText_moneySpend);
        saveSpendings = findViewById(R.id.addSpendings_btn);
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


        saveSpendings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String purchases= purchaseEditText.getText().toString().trim();
                String money= moneyEditText.getText().toString().trim();
                if(!purchases.isEmpty() && !money.isEmpty()) {

                    if(savingTemp>=parseInt(money))
                    {
                    savingTemp = savingTemp - parseInt(money);
                    Spendings newSpendings = new Spendings(purchases, money);

                    spendingDatabse.child(newSpendings.getPurchased()).setValue(newSpendings).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                dbSavings = FirebaseDatabase.getInstance();
                                //savingTemp=savingTemp+parseInt(totalAmount);
                                savingsGot = String.valueOf(savingTemp);
                                Savings savings = new Savings(savingsGot);
                                dbRefSavings = dbSavings.getReference().child("Savings");
                                dbRefSavings.setValue(savings).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        purchaseEditText.setText("");
                                        moneyEditText.setText("");
                                        Toast.makeText(MoneySpendingsActivity.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                });


                            } else {

                                Toast.makeText(MoneySpendingsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }

                        }
                    });
                }
                    else
                    {
                        Toast.makeText(MoneySpendingsActivity.this,"Savings are not enough to Spend On these Item(s)",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(MoneySpendingsActivity.this,"fields can't be put Empty",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    /*public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.functions_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {

            case R.id.All_users:
                startActivity (new Intent(this, AllUserActivity.class));
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                sendToStart();
                break;
            case R.id.UpdateProfile:
                startActivity (new Intent(this, UpdateProfileActivity.class));
                break;
            case R.id.spendings:
                startActivity (new Intent(this, MoneySpendingsActivity.class));
                break;
            case R.id.settings:
                startActivity (new Intent(this, SettingsActivity.class));
                break;
            case R.id.updatePaymentRecord:
                startActivity(new Intent(this,UsersToUpdateRecord.class));
                break;
            case R.id.showPayments:
                startActivity(new Intent(this,UsersToShowPayments.class));
                break;
            case R.id.otherOptions:
                startActivity(new Intent(this,OtherOptionsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }*/
    public void sendToStart()
    {

        Intent i = new Intent(MoneySpendingsActivity.this,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!user.userRole)
        {
            moneyEditText.setEnabled(false);
            moneyEditText.setHint("");
            purchaseEditText.setEnabled(false);
            purchaseEditText.setHint("");
            saveSpendings.setEnabled(false);
            saveSpendings.setText("");
        }

        spendingDatabse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                spendingsList.clear();
                for (DataSnapshot spendingSnapshot : dataSnapshot.getChildren()) {


                        Spendings spendings = spendingSnapshot.getValue(Spendings.class);
                        spendingsList.add(spendings);
                    }

                    SpendingsList adapter = new SpendingsList(MoneySpendingsActivity.this, spendingsList);

                listViewSpendings.setAdapter(adapter);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
