package com.example.fundapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SpendingsList extends ArrayAdapter<Spendings>
{

private Activity context;
private List<Spendings> spendingsList;


    public SpendingsList(Activity context, List<Spendings> spendingsList)
    {
        super(context,R.layout.spendings_listview,spendingsList);
        this.context = context;
        this.spendingsList= spendingsList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View listviewItem =layoutInflater.inflate(R.layout.spendings_listview,null, true);
        TextView purchasetextView = listviewItem.findViewById(R.id.listViewpurchase);
        TextView moneyetextView = listviewItem.findViewById(R.id.listviewMoney);
           Spendings spending =spendingsList.get(position);
        System.out.println("<><><><>Spendings position is "+position+" purchase = "+spending.getPurchased()+" Money is "+spending.getMoney());
           purchasetextView.setText(spending.getPurchased());
           moneyetextView.setText(spending.getMoney());
           return listviewItem;
    }
}
