package com.example.fundapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText etEmailFPA;
    Button btn_sendEmailFPA;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        etEmailFPA=findViewById(R.id.editTextemailFPA);
        btn_sendEmailFPA=findViewById(R.id.button_sendEmailFPA);

        btn_sendEmailFPA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=etEmailFPA.getText().toString();
                if(!email.isEmpty())
                {
                    FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(ForgotPasswordActivity.this,"Email Sent please Chek Email Inbox",Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(ForgotPasswordActivity.this,"ERROR sending Email Try Again later",Toast.LENGTH_SHORT).show();
                                }

                        }
                    });
                }
                else
                {
                    Toast.makeText(ForgotPasswordActivity.this,"Enter Email First",Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}