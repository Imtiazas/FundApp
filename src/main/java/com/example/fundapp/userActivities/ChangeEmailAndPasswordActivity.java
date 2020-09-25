package com.example.fundapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangeEmailAndPasswordActivity extends AppCompatActivity {
    EditText etChangeEmailCEPA,etChangePasswordCEPA;
    Button btn_changeEmailCEPA,btn_changePasswordCEPA;
    FirebaseAuth firebaseAuth;
    String changeEmail;
    String changePassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth= FirebaseAuth.getInstance();
        setContentView(R.layout.activity_change_email_and_password);
        etChangeEmailCEPA=findViewById(R.id.etChangeEmailCEPA);
        etChangePasswordCEPA=findViewById(R.id.etChangePasswordCEPA);
        btn_changeEmailCEPA=findViewById(R.id.btn_changeEmailCEPA);
        btn_changePasswordCEPA=findViewById(R.id.btn_changePasswordCEPA);


        btn_changeEmailCEPA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changeEmail=etChangeEmailCEPA.getText().toString();
                if(!changeEmail.isEmpty())
                {
                   // System.out.println("<><><><> InTheChangeEmailPasswordActivity if changeEmail is "+changeEmail);
                    final FirebaseUser firebaseUser= firebaseAuth.getCurrentUser();
                    firebaseUser.updateEmail(changeEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful())
                                {
                                   if(FirebaseAuth.getInstance().getCurrentUser()!=null)
                                   {
                                       FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                           @Override
                                           public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    Toast.makeText(ChangeEmailAndPasswordActivity.this,"Email Chnaged Successfully \n Chek Email inbox",Toast.LENGTH_LONG).show();
                                                    firebaseAuth.signOut();
                                                    sendToStart();
                                                }
                                           }
                                       });
                                   }
                                   else
                                   {
                                       Toast.makeText(ChangeEmailAndPasswordActivity.this,"User is NULL",Toast.LENGTH_SHORT).show();

                                   }

                                }
                                else
                                {
                                    Toast.makeText(ChangeEmailAndPasswordActivity.this,"Can not update Email this  time",Toast.LENGTH_SHORT).show();
                                }


                        }
                    });
                }
                else
                {
                    System.out.println("<><><><> InTheChangeEmailPasswordActivity else changeEmail is "+changeEmail);
                    Toast.makeText(ChangeEmailAndPasswordActivity.this,"Please Enter a Valid Email First",Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_changePasswordCEPA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword=etChangePasswordCEPA.getText().toString();
                if(!changePassword.isEmpty())
                {
                   // System.out.println("<><><><> InTheChangeEmailPasswordActivity if changePassword is "+changePassword);
                    FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
                    firebaseUser.updatePassword(changePassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(ChangeEmailAndPasswordActivity.this,"Password changed Successfully",Toast.LENGTH_SHORT).show();
                                firebaseAuth.signOut();
                                sendToStart();
                            }
                            else
                            {
                                Toast.makeText(ChangeEmailAndPasswordActivity.this,"Logout and then Login Again to change Password ",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else
                {
                    System.out.println("<><><><> InTheChangeEmailPasswordActivity else changePassword is "+changePassword);
                    Toast.makeText(ChangeEmailAndPasswordActivity.this,"Please Enter a Valid Password First",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void sendToStart()
    {

        Intent i = new Intent(ChangeEmailAndPasswordActivity.this,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);

    }
}