package com.example.fundapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import static com.example.fundapp.AllUserActivity.clickedUserID;
import static com.example.fundapp.AllUserActivity.clickedUserName;


public class AddMoneyActivity extends AppCompatActivity {
    private ViewPager mviewPager;
    private Toolbar toolbarr;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private TabLayout tabLayout;
    public static String userId;
   public static String userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);
        toolbarr = findViewById(R.id.toolbar);
        setSupportActionBar(toolbarr);
        getSupportActionBar().setTitle("Add Money");
        mviewPager = findViewById(R.id.tab_pager);
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mviewPager.setAdapter(sectionsPagerAdapter);
        tabLayout = findViewById(R.id.main_tab);
        tabLayout.setupWithViewPager(mviewPager);
        Intent intent= getIntent();
        AddMoneyActivity.userId=intent.getStringExtra(clickedUserID);
        AddMoneyActivity.userName=intent.getStringExtra(clickedUserName);
       //System.out.println("<><><><> AddMoneyActivity id Passed  "+userId);
       //System.out.println("<><><><> AddMoneyActivity username Passed "+userName);


    }
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.functions_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {

            case R.id.All_users:
                startActivity (new Intent(this, AllUserActivity.class));
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                sendToStart();
                break;
            case R.id.UpdateProfile:
                startActivity (new Intent(this, UpdateProfileActivity.class));
                break;
            case R.id.spendings:
                startActivity (new Intent(this, MoneySpendingsActivity.class));
                break;
            case R.id.settings:
                startActivity (new Intent(this, SettingsActivity.class));
                break;
            case R.id.updatePaymentRecord:
                startActivity(new Intent(this,UsersToUpdateRecord.class));
                break;
            case R.id.showPayments:
                startActivity(new Intent(this,UsersToShowPayments.class));
                break;
            case R.id.Total_Savings:
                startActivity(new Intent(this,ShowTotalSavings.class));
                break;
            case R.id.otherOptions:
                startActivity(new Intent(this,OtherOptionsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void sendToStart()
    {

        Intent i = new Intent(AddMoneyActivity.this,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);

    }
}
