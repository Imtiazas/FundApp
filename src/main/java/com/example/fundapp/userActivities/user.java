package com.example.fundapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class user extends AppCompatActivity {

    private DrawerLayout drawer;
   private Toolbar toolbar;
   private FirebaseAuth mAuth;
   FirebaseDatabase database;
   DatabaseReference userNameRef;
    DatabaseReference userPaymentRef;
   TextView user_Name , tv_week, tv_month,tv_year,tv_dueValue,header_tv,btn_netAmount;
    String currentUserID;
    Payment userPayment;
    ProgressBar progressBar;
    String week=null,month=null,year=null,dueValue=null, netAmount=null,nameOfCurrentUser=null;
    public static boolean userRole;
    public static String userName;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        tv_week = findViewById(R.id.u_week);
        tv_month = findViewById(R.id.u_month);
        tv_year = findViewById(R.id.u_year);
        tv_dueValue =findViewById(R.id.tv_dueValue);
        btn_netAmount = findViewById(R.id.net_income);

        user_Name = findViewById(R.id.user_name);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);


       FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
       progressBar.setVisibility(View.VISIBLE);
       //toolbar
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("User Profile");
        //

        database=FirebaseDatabase.getInstance();

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel channel= new
                    NotificationChannel("MyNotifications","MyNotifications" ,NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager= getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Successful";
                        if (!task.isSuccessful()) {
                            msg = "Failed";
                        }

                       // Toast.makeText(user.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        drawer=findViewById(R.id.draw_layout);
        NavigationView  navigationview =findViewById(R.id.nav_view);
        /*View vHeader= navigationview.inflateHeaderView(R.layout.nav_header);
        header_tv= vHeader.findViewById(R.id.header_tv);
        header_tv.setText(nameOfCurrentUser);*/
        navigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId())
                {
                    case R.id.Total_Savings:
                        startActivity(new Intent(user.this,ShowTotalSavings.class));
                        break;
                    case R.id.adminPanel:
                        startActivity(new Intent(user.this,AdminPanelActivity.class));
                        break;
                    case R.id.spendings:
                        startActivity (new Intent(user.this, MoneySpendingsActivity.class));
                        break;
                    case R.id.otherOptions:
                        startActivity(new Intent(user.this,OtherOptionsActivity.class));
                        break;
                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        sendToStart();
                        break;
                    case R.id.showPayments:
                        startActivity(new Intent(user.this,UsersToShowPayments.class));
                        break;
                    case R.id.UpdateProfile:
                        startActivity (new Intent(user.this, UpdateProfileActivity.class));
                        break;
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        ActionBarDrawerToggle actionBarDrawerToggle= new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


    }


    @Override
    protected void onStart() {
        super.onStart();
        if(!checkInternetService())
        {
            AlertDialog.Builder alertDialog= new AlertDialog.Builder(user.this);
            alertDialog.setMessage("You are not Connected to internet");
            progressBar.setVisibility(View.GONE);
            alertDialog.show();


        }
        if(mAuth.getCurrentUser()==null){

            sendToStart();
        }
        else{

            currentUserID=mAuth.getCurrentUser().getUid();
            userNameRef=database.getReference().child("Users").child(currentUserID);
            userPaymentRef=database.getReference().child("Payment").child(currentUserID);

            userInfo();
        }


    }

    private void userInfo() {
        try {

            if (!currentUserID.isEmpty()) {
                System.out.println("----Current UserId" + currentUserID);
                userNameRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserRetrieveData userRet = dataSnapshot.getValue(UserRetrieveData.class);
                        try {
                            nameOfCurrentUser=(userRet.getName());
                            user.userName=nameOfCurrentUser;
                            user.userRole=userRet.getAdmin();
                            setNameOfUser(nameOfCurrentUser);
                            if(userRole)
                            {
                                FirebaseMessaging.getInstance().subscribeToTopic("adminPanel")
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                String msg = "Successful";
                                                if (!task.isSuccessful()) {
                                                    msg = "Failed";
                                                }

                                                // Toast.makeText(user.this, msg, Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }


                        }
                        catch (NullPointerException ignored)
                        {
                            Toast.makeText(user.this,ignored.getMessage(),Toast.LENGTH_SHORT).show();

                        }

                        //initiating

                        userPaymentRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                userPayment = dataSnapshot.getValue(com.example.fundapp.Payment.class);
                                try {
                                    System.out.println("----Snap is "+dataSnapshot);
                                    System.out.println("----Snap is Week is "+userPayment.getweek());
                                    week= (userPayment.getweek());
                                    month =(userPayment.getmonth());
                                    year =(userPayment.getyear());
                                    dueValue =(userPayment.getduePayment());
                                    System.out.println("----Snap is Year is "+userPayment.getyear());
                                //btn_netAmount.setText(userPayment.getTotalAmount());
                                    netAmount= (userPayment.gettotalAmount());
                                    progressBar.setVisibility(View.GONE);
                                        setOnviews(week,month,year,dueValue,netAmount);
                                }
                                catch (NullPointerException ignored)
                                {
                                    progressBar.setVisibility(View.GONE);
                                    //Toast.makeText(user.this,ignored.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });




                        //Exiting
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            } else {

                Toast.makeText(user.this, currentUserID, Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(user.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    private void setNameOfUser(String nameOfCurrentUser) {
        user_Name.setText(nameOfCurrentUser);
    }

    private void setOnviews(String week, String month, String year, String dueValue, String netAmount) {

        tv_week.setText(week);
        tv_month.setText(month);
        tv_year.setText(year);
        tv_dueValue.setText(dueValue);
        btn_netAmount.setText(netAmount);


    }

    public void sendToStart()
    {

        Intent i = new Intent(user.this,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);

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
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }



    }
}
