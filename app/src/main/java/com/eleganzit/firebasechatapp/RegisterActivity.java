package com.eleganzit.firebasechatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText ed_username,ed_email,ed_password;
    Button btn_register;
    FirebaseAuth auth;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ed_username=findViewById(R.id.ed_username);
        ed_email=findViewById(R.id.ed_email);
        ed_password=findViewById(R.id.ed_password);
        btn_register=findViewById(R.id.btn_register);

        auth=FirebaseAuth.getInstance();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ed_password.getText().toString().length() < 6)
                {
                    Toast.makeText(RegisterActivity.this, "pnot valid password", Toast.LENGTH_SHORT).show();
                }
                else if(!ed_email.getText().toString().isEmpty() && !ed_password.getText().toString().isEmpty() && !ed_username.getText().toString().isEmpty())
                {
                    register(ed_username.getText().toString(),ed_email.getText().toString(),ed_password.getText().toString());
                }
            }
        });

    }


    public void register(final String username, String email, String password)
    {
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser=auth.getCurrentUser();
                            assert firebaseUser != null;
                            String user_id=firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(user_id);

                            HashMap<String, String> hashMap=new HashMap<>();
                            hashMap.put("id",user_id);
                            hashMap.put("username",username);
                            hashMap.put("imageURL","default");

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        startActivity(new Intent(RegisterActivity.this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                                        finish();
                                    }
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(RegisterActivity.this, "Couldn't register with this email or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}
