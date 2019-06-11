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
import com.eleganzit.firebasechatapp.model.Chat;
import com.eleganzit.firebasechatapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MessageAdapter  extends RecyclerView.Adapter<MessageAdapter.MyViewHolder>
{

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    Context context;
    ArrayList<Chat> arrayList;
    FirebaseUser firebaseUser;
    String imageurl;

    public MessageAdapter(Context context, ArrayList<Chat> arrayList,String imageurl) {
        this.context = context;
        this.arrayList = arrayList;
        this.imageurl = imageurl;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if(i == MSG_TYPE_RIGHT){
            View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_item_right,viewGroup,false);
            return new MyViewHolder(view);
        }
        else
        {
            View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_item_left,viewGroup,false);
            return new MyViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        final Chat chat=arrayList.get(i);

        myViewHolder.show_message.setText(chat.getMessage());

        if(imageurl.equals("default"))
        {
            myViewHolder.img.setImageResource(R.mipmap.ic_launcher_round);
        }
        else
        {
            Glide.with(context).load(imageurl).into(myViewHolder.img);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {

        ImageView img;
        TextView show_message;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            img=itemView.findViewById(R.id.img);
            show_message=itemView.findViewById(R.id.show_message);
        }
    }

    @Override
    public int getItemViewType(int position) {

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if(arrayList.get(position).getSender().equals(firebaseUser.getUid()))
        {
            return MSG_TYPE_RIGHT;
        }
        else
        {
            return MSG_TYPE_LEFT;
        }

    }
}
