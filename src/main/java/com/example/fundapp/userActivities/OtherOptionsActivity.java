package com.example.fundapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationManagerCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static java.lang.Integer.parseInt;

public class OtherOptionsActivity extends AppCompatActivity {

    Context context;
    Button bt_UpdateSavingsOOA,btn_deleteSpendingsOOA,btn_emailPassChangeOOA,btn_deleteUserOOA,btn_reqAdminOOA;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase db;
    DatabaseReference dbRef;
    FirebaseDatabase dbUser;
    DatabaseReference userRef;
    FirebaseDatabase dbSavings;
    DatabaseReference dbRefSavings;
    DatabaseReference notifRef;
    String savingsGot;
    int savingTemp;
    String userId;
    int previousTotalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_options);
        context= OtherOptionsActivity.this;
        //Firebase
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        userId=firebaseUser.getUid();
        db=FirebaseDatabase.getInstance();
        dbRef=db.getReference();
      /*  db=FirebaseDatabase.getInstance();
        dbSavings=FirebaseDatabase.getInstance();
        dbUser=FirebaseDatabase.getInstance();
        dbRef=db.getReference("Payment").child(userId);
        userRef=dbUser.getReference("Users").child(userId);
        dbRefSavings=dbSavings.getReference("savings");*/
        dbUser=FirebaseDatabase.getInstance();
        userRef=dbUser.getReference("Users").child(userId);
        //Views
        /*toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Other Options");*/
        bt_UpdateSavingsOOA=findViewById(R.id.bt_UpdateSavingsOOA);
        btn_reqAdminOOA=findViewById(R.id.btn_reqAdminOOA);
        bt_UpdateSavingsOOA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(OtherOptionsActivity.this,ChangeSavingsActivity.class);
                startActivity(i);
            }
        });
        //Getting Savings

        dbSavings=FirebaseDatabase.getInstance();
        dbRefSavings=dbSavings.getReference().child("Savings");
        dbRefSavings.addValueEventListener(new ValueEventListener() {
            Savings savings;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {

                    savings = dataSnapshot.getValue(Savings.class);
                    savingsGot = savings.gettotalSavings();
                    savingTemp = parseInt(savingsGot);
                }
                catch (Exception e)
                {

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Getting Payment Of Current User

        dbRef=db.getReference("Payment").child(userId);
        dbRef.addValueEventListener(new ValueEventListener() {
            Payment userPayment;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userPayment=dataSnapshot.getValue(Payment.class);
                try {
                    previousTotalAmount = parseInt(userPayment.gettotalAmount());
                }
                catch (NullPointerException ignored)
                {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(OtherOptionsActivity.this);
        alertDialog.setTitle("Delete Account?");
        alertDialog.setMessage("Caution : If you Delete Account You will not be allowed to login and Data wiil be Deleted");
        btn_deleteSpendingsOOA=findViewById(R.id.btn_deleteSpendingsOOA);
       btn_deleteSpendingsOOA.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(OtherOptionsActivity.this,DeleteSpendingsActivity.class);
               startActivity(intent);
           }
       });
        btn_emailPassChangeOOA=findViewById(R.id.btn_emailPassChangeOOA);
        btn_deleteUserOOA=findViewById(R.id.btn_deleteUserOOA);
        btn_deleteUserOOA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           try
                           {
                                //Change savings
                               savingTemp=savingTemp-previousTotalAmount;
                               savingsGot=String.valueOf(savingTemp);
                               Savings savings= new Savings(savingsGot);
                               dbRefSavings.setValue(savings).addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                       if (task.isSuccessful())
                                       {
                                           //Deleting From Payments
                                           dbRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                               @Override
                                               public void onComplete(@NonNull Task<Void> task) {
                                                   if (task.isSuccessful())
                                                   {
                                                       userRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                           @Override
                                                           public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful())
                                                                {
                                                                    firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            firebaseAuth.signOut();
                                                                            Intent i = new Intent(OtherOptionsActivity.this,MainActivity.class);
                                                                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                            startActivity(i);
                                                                            finish();

                                                                        }
                                                                    });
                                                                }

                                                           }
                                                       });
                                                   }
                                               }
                                           });
                                       }
                                   }
                               });



                               System.out.println("<><><><> InTheOtherOptionsActivityOutside  previousTotalAmount is "+previousTotalAmount);
                               System.out.println("<><><><> InTheOtherOptionsActivityOutside  savingsTemp is "+savingTemp);

                           }
                           catch (Exception ex)
                           {
                               //Toast.makeText(OtherOptionsActivity.this,ex.getMessage(),Toast.LENGTH_LONG).show();
                           }
                        }
                    });
                    alertDialog.show();

                //Toast.makeText(OtherOptionsActivity.this,userId,Toast.LENGTH_SHORT).show();
            }
        });
        btn_emailPassChangeOOA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OtherOptionsActivity.this,ChangeEmailAndPasswordActivity.class);
                startActivity(i);
            }
        });
        btn_reqAdminOOA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            final AlertDialog.Builder reqDialog= new AlertDialog.Builder(OtherOptionsActivity.this);
                reqDialog.setTitle("Become Admin?");
                reqDialog.setMessage("you really want to request to Admin");
                reqDialog.setCancelable(false);
                reqDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String uID=firebaseAuth.getCurrentUser().getUid();
                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("dd-MMMM-yyyy-HH:mm:ss", Locale.getDefault());
                        final String locDate = df.format(c);
                        db=FirebaseDatabase.getInstance();
                        userRef=db.getReference("Users").child(uID);
                        userRef.addValueEventListener(new ValueEventListener() {
                            UserRetrieveData userRetrieveData;
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                userRetrieveData=dataSnapshot.getValue(UserRetrieveData.class);
                                try{

                                    String name=userRetrieveData.getName();
                                    NotificationsModel notificationsModel= new NotificationsModel(locDate,"I want to Become Admin",name,uID);
                                    notifRef=db.getReference("Notifications").child(uID);
                                    notifRef.setValue(notificationsModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                Toast.makeText(OtherOptionsActivity.this,"Your Request is submitted",Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                            {
                                                Toast.makeText(OtherOptionsActivity.this,task.getException().toString(),Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                                catch (Exception ex)
                                {
                                    Toast.makeText(OtherOptionsActivity.this,ex.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });





                    }
                });
                reqDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        reqDialog.show();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!user.userRole)
        {
            btn_deleteSpendingsOOA.setEnabled(false);
            btn_deleteSpendingsOOA.setText("");
            bt_UpdateSavingsOOA.setEnabled(false);
            bt_UpdateSavingsOOA.setText("");
        }
    }
}