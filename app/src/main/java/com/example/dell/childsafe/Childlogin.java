package com.example.dell.childsafe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Childlogin extends AppCompatActivity {
    private Button button;
    private EditText user;
    private EditText pass;
    private EditText puser;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_childlogin);

        user=(EditText)findViewById(R.id.UserName2);
        pass=(EditText)findViewById(R.id.Password2);
        button = (Button)findViewById(R.id.btnlog2);
        puser = (EditText) findViewById(R.id.parentname);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = new ProgressDialog(Childlogin.this);
                progressDialog.setTitle("Loading...");
                progressDialog.show();

                databaseReference.child("ParentDetails").child(puser.getText().toString()).child("ChildDetails").child(user.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ChildDetils childDetils = dataSnapshot.getValue(ChildDetils.class);
                        if (puser.getText().toString().isEmpty() || pass.getText().toString().isEmpty() || user.getText().toString().isEmpty() ){
                            progressDialog.dismiss();
                            Toast.makeText(Childlogin.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                        }
                        if (childDetils==null){
                            progressDialog.dismiss();
                            Toast.makeText(Childlogin.this, "User name or password is not correct", Toast.LENGTH_SHORT).show();
                        }
                        else if (pass.getText().toString().equals(childDetils.getPassword())){

                            Intent login2 = new Intent(Childlogin.this,Childlimit.class);

                            login2.putExtra("parent2",puser.getText().toString());
                            login2.putExtra("child2",user.getText().toString());
                            startActivity(login2);
                            progressDialog.dismiss();
                            finish();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });

    }
}
