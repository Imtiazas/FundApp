package com.example.fundapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

public class UnAuthUsersActivity extends AppCompatActivity implements UserAdapter.onItemClickListner {
    private Toolbar toolbarr;
    private RecyclerView recyclerView;
    private ArrayList<UserRetrieveData> userRetrieveData;
    private UserAdapter userAdapter;
    DatabaseReference dRef;
    FirebaseDatabase udb;
    FirebaseDatabase paymentdb;
    DatabaseReference udbRef;
    DatabaseReference paymentdbRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_un_auth_users);
        recyclerView = findViewById(R.id.userRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userRetrieveData = new ArrayList<UserRetrieveData>();
        toolbarr = findViewById(R.id.toolbar);
        setSupportActionBar(toolbarr);
        getSupportActionBar().setTitle("Unauthorised Users");
        dRef= FirebaseDatabase.getInstance().getReference().child("Users");
        dRef.addValueEventListener(valueEventListener);
    }



    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            userRetrieveData.clear();
            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
            {
                UserRetrieveData userRData = dataSnapshot1.getValue(UserRetrieveData.class);
                //assert userRData != null;
                //System.out.println("<><><><> data retrived as"+"id is  "+userRData.getuID()+"name is "+userRData.getName()+"email is "+userRData.getEmail());
                userRetrieveData.add(userRData);


            }


            userAdapter = new UserAdapter(UnAuthUsersActivity.this,userRetrieveData);

            recyclerView.setAdapter(userAdapter);
            userAdapter.setOnClickListner(UnAuthUsersActivity.this);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };



    public void sendToStart()
    {

        Intent i = new Intent(UnAuthUsersActivity.this,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);

    }


    @Override
    public void onItemClick(int position) {
        final UserRetrieveData clickedItem= userRetrieveData.get(position);
        AlertDialog.Builder deleteDialog= new AlertDialog.Builder(UnAuthUsersActivity.this);
        deleteDialog.setTitle("");
        deleteDialog.setMessage("Are you sure the user ' "+clickedItem.getName()+ " ' you want to delete is unAuthorised??");
        deleteDialog.setCancelable(false);


        deleteDialog.setPositiveButton("Yes sure Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //userDB
                udb=FirebaseDatabase.getInstance();
                udbRef=udb.getReference("Users").child(clickedItem.getuID());
                udbRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {   //userRetrieveData.clear();
                            paymentdb=FirebaseDatabase.getInstance();
                            paymentdbRef=paymentdb.getReference("Payment").child(clickedItem.getuID());
                            paymentdbRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {   /*Intent intent= new Intent(UnAuthUsersActivity.this,UnAuthUsersActivity.class);
                                            startActivity(intent);*/
                                            Toast.makeText(UnAuthUsersActivity.this,"User's Data Successfully Deleted",Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            Toast.makeText(UnAuthUsersActivity.this,task.getException().toString(),Toast.LENGTH_SHORT).show();
                                        }
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(UnAuthUsersActivity.this,task.getException().toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });



            }
        });


        deleteDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(UnAuthUsersActivity.this,"No Clicked",Toast.LENGTH_SHORT).show();
            }
        });
        deleteDialog.show();
    }



    @Override
    protected void onStart() {
        super.onStart();
        final AlertDialog.Builder alert= new AlertDialog.Builder(UnAuthUsersActivity.this);
        alert.setTitle("WARNING!");
        alert.setMessage("Make sure you choose only those users whose Email is not verified from this list" +
                "Data will be deleted of Selected user!");
        alert.setCancelable(false);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               dialog.dismiss();
            }
        });
        alert.show();
    }
}