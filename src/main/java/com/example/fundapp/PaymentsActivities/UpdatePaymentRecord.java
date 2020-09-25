package com.example.fundapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.example.fundapp.AllUserActivity.clickedUserID;
import static com.example.fundapp.AllUserActivity.clickedUserName;
import static java.lang.Integer.parseInt;

public class UpdatePaymentRecord extends AppCompatActivity {
    String monthsArr[]={"January","February","March","April","May","June","July","August",
            "September","October","November","December"};
    String yearArr[]={"2018","2019","2020","2021","2022","2023",
            "2024","2025","2026"};
    String weekArr[] = {"Week1","Week2","Week3","Week4"};
    Spinner spinnerYearUPR,spinnerMonthUPR,spinnerWeekUPR;
    public static String weeklySpinnerVlaue,monthlySpinnerValue,yearlySpinnerValue;
    private Toolbar toolbar;
    public static String userId;
    public static String userName;
    EditText etDuePaymentUPR,etTotalAmountUPR;
    Button btnUpdatePaymentRecord;
    TextView userNameUPR,tvCalDueAmount;
    String duePayment,totalAmount;
    FirebaseDatabase db;
    DatabaseReference dbRef;
    FirebaseDatabase dbSavings;
    DatabaseReference settingsRef;
    DatabaseReference dbRefSavings;
    String savingsGot,p_week,p_month,p_year;
    int savingTemp;
    int previousTotalAmount;
    String[] dateSplits;
    int year,difYear;
    String week,month;
    int day,totalDue;
    public static int dueAmountCalculated;
    int weeklyVal,monthlyVal,yearlyValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_payment_record);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Update Payment Record");
        Intent intent= getIntent();
        UpdatePaymentRecord.userId=intent.getStringExtra(clickedUserID);
        UpdatePaymentRecord.userName=intent.getStringExtra(clickedUserName);
        userNameUPR=findViewById(R.id.userNameUPR);
        tvCalDueAmount=findViewById(R.id.tvCalDueamount);
        userNameUPR.setText(UpdatePaymentRecord.userName);
        spinnerWeekUPR=findViewById(R.id.spinnerWeekUPR);
        spinnerMonthUPR=findViewById(R.id.spinnerMonthUPR);
        spinnerYearUPR=findViewById(R.id.spinnerYearUPR);
        etDuePaymentUPR=findViewById(R.id.etDuePaymentUPR);
        etTotalAmountUPR=findViewById(R.id.etTotalAmountUPR);
        btnUpdatePaymentRecord=findViewById(R.id.buttonUpdateUPR);
        db=FirebaseDatabase.getInstance();
        dbRef=db.getReference();
        //weeklySpinner
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, weekArr);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWeekUPR.setAdapter(arrayAdapter);
        spinnerWeekUPR.setOnItemSelectedListener(new UpdatePaymentRecord.WeeklySpinnerClass());
        //monthlySpinner
        final ArrayAdapter<String> monthlyArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,monthsArr);
        monthlyArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonthUPR.setAdapter(monthlyArrayAdapter);
        spinnerMonthUPR.setOnItemSelectedListener(new UpdatePaymentRecord.MonthlySpinnerClass());

        //YearlySpinner

        final ArrayAdapter<String> yearArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,yearArr);
        yearArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYearUPR.setAdapter(yearArrayAdapter);
        spinnerYearUPR.setOnItemSelectedListener(new UpdatePaymentRecord.YearlySpinnerClass());

        //Getting Savings

        dbSavings=FirebaseDatabase.getInstance();
        dbRefSavings=dbSavings.getReference().child("Savings");
        dbRefSavings.addValueEventListener(new ValueEventListener() {
            Savings savings;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                savings=dataSnapshot.getValue(Savings.class);
                savingsGot=savings.gettotalSavings();
                savingTemp=parseInt(savingsGot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Getting Payment Of Current User

        dbRef=db.getReference("Payment").child(UpdatePaymentRecord.userId);
        dbRef.addValueEventListener(new ValueEventListener() {
            Payment userPayment;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userPayment=dataSnapshot.getValue(Payment.class);
                try {
                    previousTotalAmount = parseInt(userPayment.gettotalAmount());
                    p_week=userPayment.getweek();
                    p_month=userPayment.getmonth();
                    p_year=userPayment.getyear();
                    System.out.println("<><><><> Inside p_week is"+p_week+"p_month="+p_month+"p_year is "+p_year);
                    settingsRef=db.getReference("settings");

                    settingsRef.addValueEventListener(new ValueEventListener() {
                        MoneySettings moneySettings;
                        @Override

                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            moneySettings=dataSnapshot.getValue(MoneySettings.class);
                            weeklyVal=moneySettings.getPaymentValue();
                            monthlyVal=weeklyVal*4;
                            yearlyValue=monthlyVal*12;
                            System.out.println("<><><><> Due amount before call calculated "+UpdatePaymentRecord.dueAmountCalculated);
                            calculateDue();
                            System.out.println("<><><><> Due amount after calculated INSIDE"+UpdatePaymentRecord.dueAmountCalculated);

                            tvCalDueAmount.setText("Calculated Due Amount is "+UpdatePaymentRecord.dueAmountCalculated);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
                catch (NullPointerException ignored)
                {

                }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //dueAmountCal
        System.out.println("<><><><> Due amount after calculated "+UpdatePaymentRecord.dueAmountCalculated);





        //OnClick Button Update Payment Record

        btnUpdatePaymentRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                duePayment=etDuePaymentUPR.getText().toString();
                totalAmount=etTotalAmountUPR.getText().toString();
                if(!UpdatePaymentRecord.weeklySpinnerVlaue.isEmpty() && !UpdatePaymentRecord.monthlySpinnerValue.isEmpty()
                        && !UpdatePaymentRecord.yearlySpinnerValue.isEmpty() && !duePayment.isEmpty() && !totalAmount.isEmpty())
                {

                    Payment setPayment= new Payment(UpdatePaymentRecord.userId,UpdatePaymentRecord.yearlySpinnerValue,UpdatePaymentRecord.monthlySpinnerValue,
                            UpdatePaymentRecord.weeklySpinnerVlaue,duePayment,totalAmount);
                            savingTemp=savingTemp-previousTotalAmount;
                            savingTemp=savingTemp+parseInt(totalAmount);

                    db.getReference("Payment")
                            .child(UpdatePaymentRecord.userId)
                            .setValue(setPayment).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                dbSavings=FirebaseDatabase.getInstance();
                                //savingTemp=savingTemp+parseInt(totalAmount);
                                savingsGot=String.valueOf(savingTemp);
                                Savings savings= new Savings(savingsGot);
                                dbRefSavings=dbSavings.getReference().child("Savings");
                                dbRefSavings.setValue(savings).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                        {
                                            etDuePaymentUPR.setText("");
                                            etTotalAmountUPR.setText("");
                                            Toast.makeText(UpdatePaymentRecord.this,"Updated successfully",Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            Toast.makeText(UpdatePaymentRecord.this,"Payemnt added but Savings ERROR",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                            else
                            {
                                Toast.makeText(UpdatePaymentRecord.this,"ERROR!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



                }
                else
                {
                    Toast.makeText(UpdatePaymentRecord.this, "Please Fill All Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Kala Jadu


    }
    public class WeeklySpinnerClass implements AdapterView.OnItemSelectedListener
    {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            UpdatePaymentRecord.weeklySpinnerVlaue=weekArr[position];
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
    public class MonthlySpinnerClass implements AdapterView.OnItemSelectedListener
    {


        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            UpdatePaymentRecord.monthlySpinnerValue=monthsArr[position];
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
    public class YearlySpinnerClass implements AdapterView.OnItemSelectedListener
    {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            UpdatePaymentRecord.yearlySpinnerValue=yearArr[position];
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
    //methods

    public void setUpDueWithYearGreater()
    {   totalDue=totalDue+(difYear*yearlyValue);
        int monthDif=findThePositionOfCurrent(monthsArr,month)-findThePositionOf(monthsArr,p_month);
        totalDue=totalDue+(monthDif*monthlyVal);
        int weekDif=findThePositionOf(weekArr,week)+1;

        if(weekDif<0)
        {
            weekDif=weekDif*-1;
        }
        totalDue=totalDue+(weekDif*weeklyVal);

    }

    public void setUpDueWithYearEqual()
    {
        System.out.println("<><><><> inthe Year Equal due amount is "+totalDue);
        totalDue = totalDue + (3 - findThePositionOf(weekArr, p_week)) * weeklyVal;
        int monthDif=findThePositionOfCurrent(monthsArr,month)-findThePositionOf(monthsArr,p_month);
        totalDue=totalDue+(monthDif*monthlyVal);
        System.out.println("<><><><> due amount is "+totalDue);
        int weekDif=findThePositionOf(weekArr,week)-findThePositionOf(weekArr,p_week);
        //int weekDif=(findThePositionOf(weekArr,week)+1);

        if(weekDif<0)
        {
            weekDif=weekDif*-1;
            System.out.println("<><><><> inthe else if due amount is "+totalDue);
        }
        totalDue=totalDue+(weekDif*weeklyVal);

    }

    public int findThePositionOf(String[] arr, String val)
    {
        int position=0;
        if(val.equals("December"))
        {
            position=0;
        }
        if(!val.equals("December"))
        {
            for(int i=0;i<arr.length;i++)
            {
                if(arr[i].equals(val))
                {
                    position=i;
                    break;
                }
            }
        }
        return position;
    }
    public int findThePositionOfCurrent(String[] arr, String val)
    {
        int position=0;

        for(int i=0;i<arr.length;i++)
        {
            if(arr[i].equals(val))
            {
                position=i;
                break;
            }

        }
        return position;
    }


    public String getWeek(int day)
    {
        String week="";
        if(day>0 && day<=7)
        {
            week="Week1";
        }
        else if(day>7 && day<=14)
        {
            week="Week2";
        }
        else if(day>14 && day<=21)
        {
            week="Week3";
        }
        else if(day>21 && day<=31)
        {
            week="Week4";
        }
        else
        {
            week=null;
        }

        return  week;
    }

    public  void calculateDue()
    {
        dateSplits = new String[3];
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault());
        String locDate = df.format(c);
        dateSplits = locDate.split("-");
        year = parseInt(dateSplits[2]);
        month = dateSplits[1];
        day = parseInt(dateSplits[0]);
        week=getWeek(day);
        //difYear = year - parseInt(p_year);
        System.out.println("<><><><> p_week is"+p_week+"p_month="+p_month+"p_year is "+p_year);
        System.out.println("<><><><> week is"+week+"p_month="+month+"year is "+year);
        difYear=year-parseInt(p_year);
        try {
            if (difYear > 0) {

                if (difYear == 1) {
                    if (p_month.equals("December") && p_week.equals("Week4")) {
                        setUpDueWithYearEqual();
                    } else if (p_month.equals("December") && !p_week.equals("Week4")) {
                        totalDue = totalDue + (3 - findThePositionOf(weekArr, p_week)) * weeklyVal;



                        setUpDueWithYearEqual();
                    } else if (!p_month.equals("December")) {

                        totalDue = totalDue + (3 - findThePositionOf(weekArr, p_week)) * weeklyVal;
                        totalDue = totalDue + ((11 - findThePositionOf(monthsArr, p_month)) * monthlyVal);
                        p_month = "December";
                        setUpDueWithYearEqual();
                    }
                }
                if (difYear > 1) {
                    System.out.println("<><><><> difYear is "+difYear);
                    for (int j = 1; j <= difYear; j++) {
                        if (p_month.equals("December") && p_week.equals("Week4")) {
                            difYear = difYear - 1;
                            j++;
                            setUpDueWithYearGreater();
                        } else if (p_month.equals("December") && !p_week.equals("Week4")) {
                            totalDue = totalDue + ((3 - findThePositionOf(weekArr, p_week)) * weeklyVal);
                            difYear = difYear - 1;
                            j++;
                            setUpDueWithYearGreater();
                        } else if (!p_month.equals("December")) {
                            totalDue = totalDue + (3 - findThePositionOf(weekArr, p_week)) * weeklyVal;
                            totalDue = totalDue + ((11 - findThePositionOf(monthsArr, p_month)) * monthlyVal);
                            p_month = "December";
                            difYear = difYear - 1;
                            j++;
                            setUpDueWithYearGreater();
                        }

                    }

                }


            }
            else {
                if(p_month.equals(month) || p_month.equals("December"))
                {
                    int weekDif=findThePositionOf(weekArr,week)+1;

                    if(weekDif<0)
                    {
                        weekDif=weekDif*-1;
                    }
                    totalDue=totalDue+(weekDif*weeklyVal);
                }
                else
                {
                    System.out.println("<><><><> inthe else of else due amount is "+totalDue);
                    totalDue = totalDue + ((3 - findThePositionOf(weekArr, p_week)) * weeklyVal);
                    p_month=monthsArr[findThePositionOf(monthsArr,p_month)+1];
                    int monthDif=findThePositionOfCurrent(monthsArr,month)-findThePositionOf(monthsArr,p_month);
                    totalDue=totalDue+(monthDif*monthlyVal);
                    int weekDif=findThePositionOf(weekArr,week)+1;

                    if(weekDif<0)
                    {
                        weekDif=weekDif*-1;
                    }
                    totalDue=totalDue+(weekDif*weeklyVal);



                }

            }
        }
        catch (Exception ex)
        {

        }
        System.out.println("<><><><> yeardif ="+difYear);
        System.out.println("<><><><> week is ="+p_week);
        System.out.println("<><><><> total due is ="+totalDue);
        UpdatePaymentRecord.dueAmountCalculated=totalDue;

    }

}