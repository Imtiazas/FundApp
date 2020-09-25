package com.example.fundapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class UpdateProfileActivity extends AppCompatActivity {
    Context context;
    Button btn_update;
    EditText upUserName, upEmail, suPassword;
    Spinner upRoleSpinner;
    FirebaseAuth mAuth;
    FirebaseDatabase mDataBase;
    Boolean isAdmin;
    ProgressDialog mProgressDialog;
    DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        mProgressDialog = new ProgressDialog(this);
        btn_update = findViewById(R.id.btn_singup);
        upUserName = findViewById(R.id.up_name);
        upEmail = findViewById(R.id.up_email);
        upRoleSpinner = findViewById(R.id.upRole_spinner);
        upRoleSpinner.setEnabled(false);

        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance();
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final String userName= upUserName.getText().toString();
                final String Email = upEmail.getText().toString();
                //isAdmin= chekRole(upRoleSpinner.getSelectedItem().toString());
                    isAdmin=false;
                if (!userName.isEmpty() && !Email.isEmpty())
                {
                    mProgressDialog.setTitle("Updating Account");
                    mProgressDialog.setMessage("Please Wait");
                    mProgressDialog.setCanceledOnTouchOutside(false);
                    mProgressDialog.show();
                    UserInsertData user = new UserInsertData(
                            userName,
                            Email,
                            mAuth.getCurrentUser().getUid().toString(),
                            isAdmin

                    );
                   final  String devToken= FirebaseInstanceId.getInstance().getToken();
                    mDataBase.getReference("Users")
                            .child(mAuth.getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {

                                mProgressDialog.hide();

                                Toast.makeText(UpdateProfileActivity.this,"User Successfully Updated",Toast.LENGTH_SHORT).show();

                            }
                            else
                            {
                                Toast.makeText(UpdateProfileActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                }



                else
                {

                    Toast.makeText(UpdateProfileActivity.this,"Fields Can't be put Empty",Toast.LENGTH_SHORT).show();
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



}
