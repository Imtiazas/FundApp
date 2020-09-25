package com.example.fundapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;


public class signup extends AppCompatActivity {
    Context context;
    Button btn_signup;
   EditText suUserName, suEmail, suPassword;
    Spinner roleSpinner;
    FirebaseAuth mAuth;
    FirebaseDatabase mDataBase;
    FirebaseDatabase dbPayment;
    DatabaseReference userRef;
    String currentUserId;
    Boolean isAdmin;
    ProgressDialog mProgressDialog;
    FirebaseUser firebaseUser;
    DatabaseReference savingsRef;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mProgressDialog = new ProgressDialog(this);
        btn_signup = findViewById(R.id.btn_singup);
        suUserName = findViewById(R.id.up_name);
        suEmail = findViewById(R.id.up_email);
        roleSpinner = findViewById(R.id.upRole_spinner);
        roleSpinner.setEnabled(false);
        suPassword = findViewById(R.id.su_password);
        mAuth = FirebaseAuth.getInstance();
        dbPayment=FirebaseDatabase.getInstance();
        mDataBase = FirebaseDatabase.getInstance();
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
               final String userName= suUserName.getText().toString();
              final String Email = suEmail.getText().toString();
             final String password = suPassword.getText().toString();

                //isAdmin= chekRole(roleSpinner.getSelectedItem().toString());
                isAdmin=false;

                if (!userName.trim().isEmpty() && !Email.trim().isEmpty() && !password.trim().isEmpty()) {
                    if(password.trim().length()>=6)
                    {
                    mProgressDialog.setTitle("Creating Account");
                    //mProgressDialog.setMessage("Please Wait");
                    mProgressDialog.setCanceledOnTouchOutside(false);
                    mProgressDialog.show();
                    mAuth.createUserWithEmailAndPassword(Email, password)

                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        //Store Additional fields to Users
                                        final UserInsertData user = new UserInsertData(
                                                userName,
                                                Email,
                                                mAuth.getCurrentUser().getUid().toString(),
                                                isAdmin

                                        );
                                        firebaseUser =FirebaseAuth.getInstance().getCurrentUser();
                                        final String devToken= FirebaseInstanceId.getInstance().getToken();

                                        currentUserId = mAuth.getCurrentUser().getUid().toString();
                                        mDataBase.getReference("Users")
                                                .child(mAuth.getCurrentUser().getUid())
                                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    //stroe Additional fields to Payments
                                                    Payment payment = new Payment(
                                                            currentUserId, "2017", "January", "Week1", "0", "0"
                                                    );
                                                    dbPayment.getReference("Payment")
                                                            .child(currentUserId)
                                                            .setValue(payment).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                //Toast.makeText(signup.this, "User Successfully Registered \n Registered in Payment Section", Toast.LENGTH_SHORT).show();
                                                                if (mAuth.getCurrentUser() != null) {

                                                                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {
                                                                                mProgressDialog.hide();
                                                                                suEmail.setText("");
                                                                                suPassword.setText("");
                                                                                suUserName.setText("");
                                                                                mAuth.signOut();
                                                                                Intent i = new Intent(signup.this, MainActivity.class);
                                                                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                                startActivity(i);
                                                                                finish();
                                                                                Toast.makeText(signup.this, "Check Your Email inbox to sign in", Toast.LENGTH_LONG).show();
                                                                            }
                                                                            else {

                                                                                Toast.makeText(signup.this, "Couldn't send Verification email", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }
                                                                    });




                                                                }

                                                            } else {
                                                                Toast.makeText(signup.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });

                                                } else {
                                                    Toast.makeText(signup.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                        });

                                    } else {

                                        Toast.makeText(signup.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                    else
                    {
                        Toast.makeText(signup.this,"Password must be above 5 characters",Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {

                    Toast.makeText(signup.this,"Fields Can't be put Empty",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private Boolean chekRole(String toString) {
        if (toString.equals("Admin"))
        {
            isAdmin=true;

        }
        else if (toString.equals("User"))
        {
            isAdmin=false;

        }
        else {

            isAdmin = false;
        }
        return isAdmin;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProgressDialog.dismiss();
    }
}
