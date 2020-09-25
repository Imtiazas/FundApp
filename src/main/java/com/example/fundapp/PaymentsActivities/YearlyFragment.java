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
public class YearlyFragment extends Fragment {

    public static String yearlySpinnerValue;
    Context context;
    TextView addMoneyUName,yfYearPaid;
    Spinner yfYearSpinner;
    EditText yfEditText;
    Button yfsubmitPaymentButton;
    String week,month, year,duePayment,totalAmount;
    AddMoneyActivity addMoneyActivity;
    String newyearArr[];
    String yearArr[]={"2017","2018","2019","2020","2021","2022","2023",
            "2024","2025","2026"};
    FirebaseDatabase db;
    DatabaseReference dbRef;
    FirebaseDatabase dbSavings;
    DatabaseReference dbRefSavings;
    String savingsGot;
    int savingTemp;

    public YearlyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_yearly, container, false);
        context = v.getContext();
        addMoneyUName = v.findViewById(R.id.yfaddMoneyUName);
        addMoneyUName.setText(AddMoneyActivity.userName);
        yfYearPaid=v.findViewById(R.id.yearlyYear);
        yfEditText=v.findViewById(R.id.yfaddMoneyrupees);
        yfYearSpinner=v.findViewById(R.id.yfyearSpinner);
        yfsubmitPaymentButton=v.findViewById(R.id.yfsubmitPayment);
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
        dbRef=db.getReference().child("Payment").child(AddMoneyActivity.userId);
        dbRef.addValueEventListener(new ValueEventListener() {
            Payment userPayment;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    userPayment=dataSnapshot.getValue(Payment.class);
                    week=userPayment.getweek();
                    month=userPayment.getmonth();
                    year=userPayment.getyear();
                    yfYearPaid.setText(year);
                    duePayment=userPayment.getduePayment();
                    totalAmount=userPayment.gettotalAmount();
                    System.out.println("<><><><> Payment Values are Gained Year Is "+year+"Savings Got = "+savingsGot);

                        if(!year.isEmpty())
                        {
                            //Toast.makeText(context,"Year is Working",Toast.LENGTH_SHORT).show();
                            PaymentRecord pr= new PaymentRecord();
                            int pos=pr.findPosition(year,yearArr);
                            newyearArr=pr.setUpArray(pos,yearArr);
                            final ArrayAdapter<String> yearArrayAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,newyearArr);
                            yearArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            yfYearSpinner.setAdapter(yearArrayAdapter);
                            yfYearSpinner.setOnItemSelectedListener(new YearlyFragment.YearlySpinnerClass() );

                        }
                        else
                        {
                            Toast.makeText(context,"Data is Null",Toast.LENGTH_SHORT).show();
                        }






                } catch (NullPointerException ignored)
                {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

            yfsubmitPaymentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!YearlyFragment.yearlySpinnerValue.isEmpty()) {
                        String edRupees=yfEditText.getText().toString();
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

                            Payment setPayment= new Payment(AddMoneyActivity.userId,yearlySpinnerValue,month,
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
                                        yfEditText.setText("");

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
                        Toast.makeText(context,"Please Select a Year Value",Toast.LENGTH_SHORT).show();
                    }
                }
            });

    return v;
    }
    public class YearlySpinnerClass implements AdapterView.OnItemSelectedListener
    {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //Toast.makeText(context,"Item Chosen is"+newyearArr[position],Toast.LENGTH_SHORT).show();
            YearlyFragment.yearlySpinnerValue=newyearArr[position];

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }


}
