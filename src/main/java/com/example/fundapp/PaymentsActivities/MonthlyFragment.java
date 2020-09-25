package com.example.fundapp;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
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

import static java.lang.Integer.parseInt;


/**
 * A simple {@link Fragment} subclass.
 */
public class MonthlyFragment extends Fragment {
    public static String monthlySpinnerValue;
    public static String yearlySpinnerValue;
    Context context;
    TextView addMoneyUName,mfMonthPaid,mfYearPaid;
    Spinner mfMonthSpinner,mfYearSpinner;
    EditText mfEditText;
    Button mfAddMoneyButton;
    String week,month, year,duePayment,totalAmount;
    String monthsArr[]={"January","February","March","April","May","June","July","August",
            "September","October","November","December"};
    String yearArr[]={"2017","2018","2019","2020","2021","2022","2023",
            "2024","2025","2026"};
    String  newmonthsArr[], newyearArr[];
    FirebaseDatabase db;
    DatabaseReference dbRef;
    FirebaseDatabase dbSavings;
    DatabaseReference dbRefSavings;
    String savingsGot;
    int savingTemp;


    AddMoneyActivity addMoneyActivity;
    public MonthlyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_monthly, container, false);
        context=v.getContext();
        addMoneyUName = v.findViewById(R.id.mfaddMoneyUName);
        addMoneyUName.setText(addMoneyActivity.userName);
        mfMonthPaid=v.findViewById(R.id.monthPaid);
        mfYearPaid=v.findViewById(R.id.YearPaid);
        mfMonthSpinner=v.findViewById(R.id.mfmonthsSpinner);
        mfYearSpinner=v.findViewById(R.id.mfyearSpinner);
        mfEditText=v.findViewById(R.id.mfaddMoneyrupees);
        mfAddMoneyButton=v.findViewById(R.id.mfsubmitPayment);
        db=FirebaseDatabase.getInstance();

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



        dbRef=db.getReference("Payment").child(AddMoneyActivity.userId);

        dbRef.addValueEventListener(new ValueEventListener() {
            Payment userPayment;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    userPayment=dataSnapshot.getValue(Payment.class);
                    week=userPayment.getweek();
                    month=userPayment.getmonth();
                    mfMonthPaid.setText(month);
                    year=userPayment.getyear();
                    mfYearPaid.setText(year);
                    duePayment=userPayment.getduePayment();
                    totalAmount=userPayment.gettotalAmount();

                        if(!month.isEmpty() && !year.isEmpty())
                        {
                            PaymentRecord pr= new PaymentRecord();

                            if(month.equals("December"))
                            {
                                newmonthsArr=monthsArr;
                                int yearPosition=pr.findPosition(year,yearArr);
                                year=yearArr[yearPosition+1];
                                newyearArr=pr.setUpArray(yearPosition,yearArr);
                            }
                            else
                            {
                                int pos=pr.findPosition(month,monthsArr);
                                newmonthsArr=pr.setUpArray(pos+1,monthsArr);
                                int yearPosition=pr.findPosition(year,yearArr);
                                newyearArr=pr.setUpArray(yearPosition,yearArr);
                            }
                            //monthlySpinner
                            final ArrayAdapter<String> monthlyArrayAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,newmonthsArr);
                            monthlyArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            mfMonthSpinner.setAdapter(monthlyArrayAdapter);
                            mfMonthSpinner.setOnItemSelectedListener(new MonthlyFragment.MonthlySpinnerClass());

                            //YearlySpinner

                            final ArrayAdapter<String> yearArrayAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,newyearArr);
                            yearArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            mfYearSpinner.setAdapter(yearArrayAdapter);
                            mfYearSpinner.setOnItemSelectedListener(new MonthlyFragment.YearlySpinnerClass());


                        }
                        else
                        {
                            Toast.makeText(context,"Data Is Null"+dataSnapshot,Toast.LENGTH_SHORT).show();
                        }




                }
                catch (NullPointerException ignored)
                {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mfAddMoneyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(!MonthlyFragment.monthlySpinnerValue.isEmpty() && !MonthlyFragment.yearlySpinnerValue.isEmpty())
               {
                   String edRupees=mfEditText.getText().toString();
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

                       Payment setPayment= new Payment(AddMoneyActivity.userId,yearlySpinnerValue,monthlySpinnerValue,
                               week,duePayment,totalAmount);
                       db.getReference().child("Payment").
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
                                   mfEditText.setText("");

                                   Toast.makeText(context,"Payment Added Successfully ",Toast.LENGTH_SHORT).show();

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
    public class MonthlySpinnerClass implements AdapterView.OnItemSelectedListener
    {


        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //Toast.makeText(view.getContext(),"Your Chose"+newmonthsArr[position],Toast.LENGTH_SHORT).show();
            MonthlyFragment.monthlySpinnerValue=newmonthsArr[position];
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
            MonthlyFragment.yearlySpinnerValue=newyearArr[position];
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

}
