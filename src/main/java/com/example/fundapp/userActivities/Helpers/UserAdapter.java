package com.example.fundapp;


import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyviewHolder> {
    UserRetrieveData userRetrieveData;
    Context context;
    ArrayList<UserRetrieveData> userData;
    private onItemClickListner mListner;
    public interface onItemClickListner{
        void onItemClick(int position);
    }
    public void setOnClickListner(onItemClickListner listner){
        mListner= listner;
    }

    public UserAdapter(Context context, ArrayList<UserRetrieveData> userRetrieveData) {
        this.context = context;
        this.userData = userRetrieveData;
    }

    @NonNull
    @Override


    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.all_users,parent,false);

        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyviewHolder holder, final int position) {
         userRetrieveData = this.userData.get(position);
        holder.textView.setText(userRetrieveData.getName());
        //holder.textView.setText(userRetrieveData.get());
       /* holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,AddMoneyActivity.class);

                intent.putExtra("userName",userRetrieveData.getName());
                intent.putExtra("userID",userRetrieveData.getuID());
                System.out.println("<><><><> UserAdapter id and name are "+userRetrieveData.getName()+userRetrieveData.getuID());
                System.out.println("<><><><> position is "+position);
                Toast.makeText(holder.linearLayout.getContext(),userRetrieveData.uID,Toast.LENGTH_SHORT).show();

                context.startActivity(intent);


            }
        });*/


    }


    @Override
    public int getItemCount() {

        return userData.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        LinearLayout linearLayout;
        public MyviewHolder(@NonNull View itemView) {

            super(itemView);
            linearLayout = itemView.findViewById(R.id.allusersLayout);
            textView = itemView.findViewById(R.id.user);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListner!=null)
                    {

                        int position =getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION)
                        {
                            mListner.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
