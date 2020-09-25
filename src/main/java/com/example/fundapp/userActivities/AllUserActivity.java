package com.example.fundapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllUserActivity extends AppCompatActivity implements UserAdapter.onItemClickListner {
    public static final String clickedUserID="userID";
    public static final String clickedUserName="userName";

    private Toolbar toolbarr;
    private RecyclerView recyclerView;
    private ArrayList<UserRetrieveData> userRetrieveData;
    private UserAdapter userAdapter;
    DatabaseReference dRef;
    ProgressDialog progressDialog;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_user);
        recyclerView = findViewById(R.id.userRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userRetrieveData = new ArrayList<UserRetrieveData>();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("");
        progressDialog.setMessage("Loading");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();
        toolbarr = findViewById(R.id.toolbar);
        setSupportActionBar(toolbarr);
        getSupportActionBar().setTitle("All Members");
        dRef=FirebaseDatabase.getInstance().getReference().child("Users");
        dRef.addValueEventListener(valueEventListener);
    }



    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
            {
                UserRetrieveData userRData = dataSnapshot1.getValue(UserRetrieveData.class);
                //assert userRData != null;
                System.out.println("<><><><> data retrived as"+"id is  "+userRData.getuID()+"name is "+userRData.getName()+"email is "+userRData.getEmail());
                userRetrieveData.add(userRData);


            }


            userAdapter = new UserAdapter(AllUserActivity.this,userRetrieveData);
            progressDialog.dismiss();
            recyclerView.setAdapter(userAdapter);
            userAdapter.setOnClickListner(AllUserActivity.this);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };


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
            case R.id.Total_Savings:
                startActivity(new Intent(this,ShowTotalSavings.class));
                break;
            case R.id.otherOptions:
                startActivity(new Intent(this,OtherOptionsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }*/
    public void sendToStart()
    {

        Intent i = new Intent(AllUserActivity.this,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);

    }


    @Override
    public void onItemClick(int position) {
        Intent AMAintent = new Intent(this,AddMoneyActivity.class);
        UserRetrieveData clickedItem= userRetrieveData.get(position);
        AMAintent.putExtra(clickedUserID,clickedItem.getuID());
        AMAintent.putExtra(clickedUserName,clickedItem.getName());
        startActivity(AMAintent);
    }
}
