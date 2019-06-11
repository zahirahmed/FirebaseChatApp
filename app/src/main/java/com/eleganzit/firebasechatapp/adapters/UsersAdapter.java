package com.eleganzit.firebasechatapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eleganzit.firebasechatapp.MessageActivity;
import com.eleganzit.firebasechatapp.R;
import com.eleganzit.firebasechatapp.model.User;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewHolder>
{
    Context context;
    ArrayList<User> arrayList;

    public UsersAdapter(Context context, ArrayList<User> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_row_layout,viewGroup,false);
        MyViewHolder myViewHolder=new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        final User user=arrayList.get(i);

        if(user.getImageURL().equalsIgnoreCase("default"))
        {
            myViewHolder.img.setImageResource(R.mipmap.ic_launcher_round);
        }
        else
        {
            Glide.with(context)
                    .load(user.getImageURL())
                    .into(myViewHolder.img);
        }

        myViewHolder.txt_username.setText(user.getUsername());

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, MessageActivity.class).putExtra("user_id",user.getId()));

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {

        ImageView img;
        TextView txt_username;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            img=itemView.findViewById(R.id.img);
            txt_username=itemView.findViewById(R.id.txt_username);
        }
    }
}
