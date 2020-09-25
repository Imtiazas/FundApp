package com.example.fundapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity {
    TextView textView,forgotPassword;
    FirebaseAuth mAuth;
    EditText siEmail;
    EditText siPassword;
    Button siButton;
    ProgressDialog mProgressDialog;
    FirebaseDatabase mDatabase;
    DatabaseReference mdbRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        siButton = findViewById(R.id.btn_singin);
        siEmail= findViewById(R.id.email);
        siPassword = findViewById(R.id.siPassword);
        textView = findViewById(R.id.textView);
        forgotPassword=findViewById(R.id.forgotPassword);
        mAuth= FirebaseAuth.getInstance();
        mDatabase=FirebaseDatabase.getInstance();
        mProgressDialog = new ProgressDialog(this);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,signup.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();

            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this,ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
        siButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = siEmail.getText().toString();
                String password = siPassword.getText().toString();
                if(!email.trim().isEmpty() && !password.trim().isEmpty())
                {
                    mProgressDialog.setTitle("Logging In");
                    mProgressDialog.setMessage("Please Wait");
                    mProgressDialog.setCanceledOnTouchOutside(false);
                    mProgressDialog.show();
                    loginUser(email,password);


                }
                else
                {

                    Toast.makeText(MainActivity.this,"Fields can't be Put Empty",Toast.LENGTH_SHORT).show();
                    siEmail.requestFocus();
                }

            }
        });
    }
    public void loginUser(String email, String password)
    {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    if(mAuth.getCurrentUser().isEmailVerified()) {
                        mProgressDialog.dismiss();
                        Intent intent = new Intent(MainActivity.this,user.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    else
                    {
                        mProgressDialog.hide();
                        mAuth.signOut();
                        Toast.makeText(MainActivity.this,"Email is not Verified Chek Your Email Inbox",Toast.LENGTH_SHORT).show();

                    }



                }
                else
                {

                    mProgressDialog.hide();

                    Toast.makeText(MainActivity.this,task.getException().toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!checkInternetService())
        {
            AlertDialog.Builder alertDialog= new AlertDialog.Builder(MainActivity.this);
            alertDialog.setMessage("You are not Connected to internet");
            alertDialog.show();


        }
    }
    public boolean checkInternetService()
    {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else {
            connected = false;
        }
        return connected;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProgressDialog.dismiss();
    }

}
