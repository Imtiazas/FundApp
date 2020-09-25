package com.example.fundapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminPanelActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout drawer;
    TextView tvAdminName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        tvAdminName=findViewById(R.id.adminName);
        //toolbar
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Admin Panel");
        //

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        drawer=findViewById(R.id.draw_layout);
        NavigationView navigationview =findViewById(R.id.nav_view);
        /*View vHeader= navigationview.inflateHeaderView(R.layout.nav_header);
        header_tv= vHeader.findViewById(R.id.header_tv);
        header_tv.setText(nameOfCurrentUser);*/
        navigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId())
                {
                    case R.id.All_users:
                        startActivity (new Intent(AdminPanelActivity.this, AllUserActivity.class));
                        break;
                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        sendToStart();
                        break;
                    case R.id.UpdateProfile:
                        startActivity (new Intent(AdminPanelActivity.this, UpdateProfileActivity.class));
                        break;
                    case R.id.spendings:
                        startActivity (new Intent(AdminPanelActivity.this, MoneySpendingsActivity.class));
                        break;
                    case R.id.settings:
                        startActivity (new Intent(AdminPanelActivity.this, SettingsActivity.class));
                        break;
                    case R.id.updatePaymentRecord:
                        startActivity(new Intent(AdminPanelActivity.this,UsersToUpdateRecord.class));
                        break;
                    case R.id.showPayments:
                        startActivity(new Intent(AdminPanelActivity.this,UsersToShowPayments.class));
                        break;
                    case R.id.Total_Savings:
                        startActivity(new Intent(AdminPanelActivity.this,ShowTotalSavings.class));
                        break;
                    case R.id.otherOptions:
                        startActivity(new Intent(AdminPanelActivity.this,OtherOptionsActivity.class));
                        break;
                    case R.id.delUnAuthUsers:
                        startActivity(new Intent(AdminPanelActivity.this,UnAuthUsersActivity.class));
                        break;
                    case R.id.adminNotifications:
                        startActivity(new Intent(AdminPanelActivity.this,AdminNotifications.class));
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
    public void sendToStart()
    {

        Intent i = new Intent(AdminPanelActivity.this,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(!user.userRole)
        {

            AlertDialog.Builder alertDialog= new AlertDialog.Builder(AdminPanelActivity.this);
            alertDialog.setTitle("RESTRICTED!");
            alertDialog.setMessage("You are not Admin so You can not access Admin Panel  Please contact to your App admin for further convenience Thanks..");
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("Take Me Back", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(AdminPanelActivity.this,user.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });
            alertDialog.show();



        }
        else
        {
            tvAdminName.setText(user.userName);
        }

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