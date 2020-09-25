package com.example.fundapp;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

import static java.lang.Integer.parseInt;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeeklyFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    Context context;
    TextView addMoneyUName,weekpaid,monthpaid,yearpaid;
    EditText edwfEnterMoney;
    Button btn_wfAddMoney;
    public static String weeklySpinnerValue,monthlySpinnerValue,yearlySpinnervalue;
    Spinner wfWeeklySpinner, wfMonthlySpinner,wfYearlySpinner;
    AddMoneyActivity addMoneyActivity;
    FirebaseDatabase db;
    FirebaseDatabase dbSavings;
    DatabaseReference dbRef;
    DatabaseReference dbRefSavings;
    FirebaseDatabase dbToAddValues;
    String week, month, year,duePayment,totalAmount;
    String newweekArr[] , newmonthsArr[], newyearArr[];
    String weekArr[] = {"Week1","Week2","Week3","Week4"};
    String monthsArr[]={"January","February","March","April","May","June","July","August",
                         "September","October","November","December"};
    String yearArr[]={"2017","2018","2019","2020","2021","2022","2023",
                    "2024","2025","2026"};
   String savingsGot;
   int savingTemp;

    public WeeklyFragment() {


        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_weekly, container, false);
        addMoneyUName = v.findViewById(R.id.wfaddMoneyUName);
        weekpaid=v.findViewById(R.id.week);
        monthpaid=v.findViewById(R.id.month);
        yearpaid=v.findViewById(R.id.year);
        btn_wfAddMoney=v.findViewById(R.id.wfsubmitPayment);
        wfWeeklySpinner = v.findViewById(R.id.wfweeksSpinner);
        wfMonthlySpinner=v.findViewById(R.id.wfmonthsSpinner);
        wfYearlySpinner=v.findViewById(R.id.wfyearSpinner);
        edwfEnterMoney=v.findViewById(R.id.wfaddMoneyrupees);
        context=v.getContext();
        addMoneyUName.setText(AddMoneyActivity.userName);
        db=FirebaseDatabase.getInstance();
       dbToAddValues=FirebaseDatabase.getInstance();
       dbSavings=FirebaseDatabase.getInstance();
        //System.out.println("<><><><>id Passed to ADD Money Activity is "+addMoneyActivity.userId);
        //System.out.println("<><><><>Name Passed to ADD Money Activity is "+addMoneyActivity.userName);

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

        //DoneWork
        dbRef=db.getReference().child("Payment").child(AddMoneyActivity.userId);

        dbRef.addValueEventListener(new ValueEventListener() {
            Payment userPayment;
            @Override

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                     userPayment = dataSnapshot.getValue(Payment.class);

                     week = userPayment.getweek();
                    //week="Week4";
                    month = userPayment.getmonth();
                    year = userPayment.getyear();
                    duePayment=userPayment.getduePayment();
                    totalAmount=userPayment.gettotalAmount();
                    weekpaid.setText(week);
                    monthpaid.setText(month);
                    yearpaid.setText(year);

                    /*week="Week3";
                    month="August";
                    year="2020";*/
                if(!week.isEmpty() && !month.isEmpty() && !year.isEmpty()) {

                    PaymentRecord pr= new PaymentRecord();
                    //week
                        if(week.equals("Week4"))
                        {   weekpaid.setText("Full Month Paid ");
                            newweekArr=weekArr;
                        }
                        else{
                            int weekPosition= pr.findPosition(week,weekArr);
                            newweekArr=pr.setUpArray(weekPosition,weekArr);
                        }

                    //month
                    if(week.equals("Week4") && month.equals("December"))
                    {
                        newmonthsArr=monthsArr;
                        int yearPosition=pr.findPosition(year,yearArr);
                        year=yearArr[yearPosition+1];
                    }
                    else if(week.equals("Week4"))
                    {
                        int pos=pr.findPosition(month,monthsArr);
                        newmonthsArr=pr.setUpArray(pos+1,monthsArr);
                    }

                    else
                    {
                        int monthPosition=pr.findPosition(month,monthsArr);
                        newmonthsArr=pr.setUpArray(monthPosition,monthsArr);
                    }

                    //year

                    int yearPosition=pr.findPosition(year,yearArr);
                    newyearArr=pr.setUpArray(yearPosition,yearArr);
                    pr.showArray(newweekArr);
                    pr.showArray(newmonthsArr);
                    pr.showArray(newyearArr);
                    //weeklySpinner
                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, newweekArr);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    wfWeeklySpinner.setAdapter(arrayAdapter);
                    wfWeeklySpinner.setOnItemSelectedListener(WeeklyFragment.this);
                    //monthlySpinner
                    final ArrayAdapter<String> monthlyArrayAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,newmonthsArr);
                    monthlyArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    wfMonthlySpinner.setAdapter(monthlyArrayAdapter);
                    wfMonthlySpinner.setOnItemSelectedListener(new MonthlySpinnerClass());

                    //YearlySpinner

                    final ArrayAdapter<String> yearArrayAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,newyearArr);
                    yearArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    wfYearlySpinner.setAdapter(yearArrayAdapter);
                    wfYearlySpinner.setOnItemSelectedListener(new YearlySpinnerClass());

                    //
                    /*System.out.println("<><><><> Week is"+week);
                    System.out.println("<><><><> month is"+month);
                    System.out.println("<><><><> year is"+year);*/
                    }
                    else{

                        Toast.makeText(context,"Data Is Null"+dataSnapshot,Toast.LENGTH_SHORT).show();
                    }
                    }
                catch (NullPointerException ignored){


                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context,databaseError.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
            btn_wfAddMoney.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!WeeklyFragment.weeklySpinnerValue.isEmpty() && !WeeklyFragment.monthlySpinnerValue.isEmpty() &&
                            !WeeklyFragment.yearlySpinnervalue.isEmpty() )
                    {
                        final String edRupees=edwfEnterMoney.getText().toString();
                        if(!edRupees.isEmpty())
                        {

                            int DuePayment=parseInt(duePayment);

                             final int EdRupees=parseInt(edRupees);
                            if(DuePayment>0)
                            {

                                duePayment=String.valueOf(DuePayment-EdRupees);

                            }
                            int TOTALamount=parseInt(totalAmount);
                            totalAmount=String.valueOf(TOTALamount+EdRupees);

                            Payment setPayment= new Payment(AddMoneyActivity.userId,yearlySpinnervalue,monthlySpinnerValue,
                                    weeklySpinnerValue,duePayment,totalAmount);
                            dbToAddValues.getReference().child("Payment").
                                    child(AddMoneyActivity.userId).
                                    setValue(setPayment).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        dbSavings=FirebaseDatabase.getInstance();
                                        savingTemp=savingTemp+EdRupees;
                                        savingsGot=String.valueOf(savingTemp);
                                        Savings savings= new Savings(savingsGot);
                                        dbRefSavings=dbSavings.getReference().child("Savings");
                                        dbRefSavings.setValue(savings);
                                        edwfEnterMoney.setText("");
                                        Toast.makeText(context,"Added Successfully",Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                           /* System.out.println("<><><><> Values are As Follows uID "+AddMoneyActivity.userId +"year "+yearlySpinnervalue+
                                    "month "+monthlySpinnerValue+"week "+weeklySpinnerValue+" due "+duePayment+" total "+totalAmount
                            );*/
                        }
                        else
                        {
                            Toast.makeText(context,"Please Enter Amount",Toast.LENGTH_SHORT).show();
                        }


                    }
                    else
                    {
                        Toast.makeText(context,"Select WEEK, MONTH And YEAR First",Toast.LENGTH_SHORT).show();
                    }
                }
            });


        return v;
    }




    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        WeeklyFragment.weeklySpinnerValue=newweekArr[position];
        System.out.println("<><><><> Text is"+newweekArr[position]);
        //Toast.makeText(parent.getContext(),"Week chosen is "+newweekArr[position],Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {


    }
    public class MonthlySpinnerClass implements AdapterView.OnItemSelectedListener
    {


        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //Toast.makeText(view.getContext(),"Your Chose"+newmonthsArr[position],Toast.LENGTH_SHORT).show();
            WeeklyFragment.monthlySpinnerValue=newmonthsArr[position];
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    public class YearlySpinnerClass implements AdapterView.OnItemSelectedListener
    {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //Toast.makeText(view.getContext(),"Your Chose"+newyearArr[position],Toast.LENGTH_SHORT).show();
            WeeklyFragment.yearlySpinnervalue=newyearArr[position];
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
