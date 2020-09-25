package com.example.fundapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NotificationList extends ArrayAdapter<NotificationsModel> {
    private Activity context;
    private List<NotificationsModel> notificationList;
    FirebaseDatabase db;
    DatabaseReference userRef;
    DatabaseReference notifiRef;
    public NotificationList(Activity context, List<NotificationsModel> notificationList)
    {
        super(context,R.layout.notification_lisviewt,notificationList);
        this.context = context;
        this.notificationList= notificationList;

    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View listviewItem = layoutInflater.inflate(R.layout.notification_lisviewt, null, true);
        TextView noti_username = listviewItem.findViewById(R.id.noti_username);
        TextView noti_msg = listviewItem.findViewById(R.id.noti_msg);
        TextView noti_date = listviewItem.findViewById(R.id.noti_date);
        Button noti_btn_accept = listviewItem.findViewById(R.id.noti_btn_accept);
        Button noti_btn_delete = listviewItem.findViewById(R.id.noti_btn_delete);
        final NotificationsModel notificationsModel = notificationList.get(position);
        //System.out.println("<><><><> Name " + notificationsModel.getName() + "MSG " + notificationsModel.getMsg() + "Date " + notificationsModel.getDate()

              //  + "UID " + notificationsModel.getuID());
        noti_username.setText(notificationsModel.getName());
        noti_msg.setText(notificationsModel.getMsg());
        noti_date.setText(notificationsModel.getDate());
    try{
        noti_btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), notificationsModel.getName(), Toast.LENGTH_SHORT).show();
                db = FirebaseDatabase.getInstance();
                String uid = notificationsModel.getuID();
                userRef = db.getReference("Users").child(uid);

                userRef.addValueEventListener(new ValueEventListener() {
                    UserRetrieveData userRetrieveData;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        userRetrieveData = dataSnapshot.getValue(UserRetrieveData.class);
                        String name = userRetrieveData.getName();
                        final String uid = userRetrieveData.getuID();
                        String email = userRetrieveData.getEmail();
                        Boolean isAdmin = userRetrieveData.getAdmin();
                        UserInsertData userInsertData = new UserInsertData(
                                name, email, uid, true
                        );
                        userRef.setValue(userInsertData).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    db=FirebaseDatabase.getInstance();
                                    notifiRef = db.getReference("Notifications").child(uid);
                                    notifiRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getContext(), "User Updated to Admin", Toast.LENGTH_SHORT).show();

                                            } else {
                                                Toast.makeText(getContext(),task.getException().toString(),Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(getContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                //here
            }
        });

        noti_btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db=FirebaseDatabase.getInstance();
                String uid = notificationsModel.getuID();
                notifiRef = db.getReference("Notifications").child(uid);
                notifiRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getContext()," Request Deleted Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else
                            {
                                Toast.makeText(getContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                    }
                });

            }
        });
    }
    catch (Exception ex)
    {
        Toast.makeText(getContext(),ex.getMessage().toString(),Toast.LENGTH_SHORT).show();
    }
    //
        return listviewItem;
    }



}
