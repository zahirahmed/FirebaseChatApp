package com.eleganzit.firebasechatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.eleganzit.firebasechatapp.adapters.MessageAdapter;
import com.eleganzit.firebasechatapp.model.Chat;
import com.eleganzit.firebasechatapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageActivity extends AppCompatActivity {


    FirebaseUser firebaseUser;
    DatabaseReference reference;
    Intent intent;
    RecyclerView rc_messages;
    ImageButton btn_send;
    EditText ed_text;
    MessageAdapter messageAdapter;
    ArrayList<Chat> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        btn_send=findViewById(R.id.btn_send);
        ed_text=findViewById(R.id.ed_text);
        rc_messages=findViewById(R.id.rc_messages);
        rc_messages.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        rc_messages.setLayoutManager(linearLayoutManager);

        intent=getIntent();

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        final String user_id=intent.getStringExtra("user_id");

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!ed_text.getText().toString().isEmpty())
                {
                    sendMessage(firebaseUser.getUid(), user_id, ed_text.getText().toString());
                }
                else
                {
                    Toast.makeText(MessageActivity.this, "cannot send empty message", Toast.LENGTH_SHORT).show();
                }
                ed_text.setText("");

            }
        });

        reference= FirebaseDatabase.getInstance().getReference("Users").child(user_id);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user=dataSnapshot.getValue(User.class);

                readMessages(firebaseUser.getUid(),user_id,user.getImageURL());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sendMessage(String sender, String receiver,String message){

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);

        reference.child("Chats").push().setValue(hashMap);

    }

    private void readMessages(final String myid, final String user_id, final String imageurl)
    {
        arrayList=new ArrayList<>();

        reference=FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();

                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    Chat chat=dataSnapshot1.getValue(Chat.class);
                    if(chat.getReceiver().equals(myid) && chat.getSender().equals(user_id) ||
                        chat.getReceiver().equals(user_id) && chat.getSender().equals(myid)){
                        arrayList.add(chat);
                    }

                    messageAdapter=new MessageAdapter(MessageActivity.this,arrayList,imageurl);
                    rc_messages.setAdapter(messageAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
