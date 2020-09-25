package com.example.fundapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminNotifications extends AppCompatActivity {
    private Toolbar toolbar;
    ListView listviewNotifications;
    FirebaseDatabase myDataBase;
    DatabaseReference notifDbRef;
    List<NotificationsModel> notificationsModelList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_notifications);
 /*       toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Requests From Users");*/
        listviewNotifications=findViewById(R.id.listviewNotifications);
        myDataBase=FirebaseDatabase.getInstance();
        notifDbRef=myDataBase.getReference("Notifications");
        notificationsModelList=new ArrayList<>();
        notifDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notificationsModelList.clear();
                for (DataSnapshot spendingSnapshot : dataSnapshot.getChildren()) {


                    NotificationsModel notifications = spendingSnapshot.getValue(NotificationsModel.class);
                    notificationsModelList.add(notifications);
                }

                NotificationList adapter = new NotificationList(AdminNotifications.this,notificationsModelList);

                listviewNotifications.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}