package com.example.dell.childsafe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Parentlogin extends AppCompatActivity {
    private Button button;
    private EditText user;
    private EditText pass;
    private Button register;
    private TextView forgot;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parentlogin);
        user=(EditText)findViewById(R.id.UserName);
        pass=(EditText)findViewById(R.id.Password);
        button = (Button)findViewById(R.id.btnlog);
        register=(Button) findViewById(R.id.regi);
        forgot=(TextView) findViewById(R.id.forgot) ;
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Parentlogin.this,Changepass.class);
                startActivity(intent);
                finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Parentlogin.this,Register.class);
                startActivity(intent);
                finish();
            }
        });


        final RegDetails register = (RegDetails)getIntent().getSerializableExtra("full");


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = new ProgressDialog(Parentlogin.this);
                progressDialog.setTitle("Loading...");

                progressDialog.show();


                databaseReference.child("ParentDetails").child(user.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final RegDetails regDetails = dataSnapshot.getValue(RegDetails.class);

                        if (user.getText().toString().isEmpty() || pass.getText().toString().isEmpty()){
                            progressDialog.dismiss();
                            Toast.makeText(Parentlogin.this, "Please enter username and password", Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
                            if (regDetails == null) {
                                progressDialog.dismiss();
                                Toast.makeText(Parentlogin.this, "User name or password is not correct", Toast.LENGTH_SHORT).show();

                            }

                            else if (pass.getText().toString().equals(regDetails.getPpassword()) || pass.getText().toString().equals(register.getPpassword())){
                                databaseReference.child("ParentDetails").child(user.getText().toString()).child("ChildDetails").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        ChildDetils childDetils = dataSnapshot.getValue(ChildDetils.class);

                                        Intent login = new Intent(Parentlogin.this,Parent.class);

                                        login.putExtra("child",childDetils );
                                        login.putExtra("parent",regDetails);
                                        startService(new Intent(Parentlogin.this,ParentService.class).putExtra("child2",childDetils.getUsername()).putExtra("parent2",regDetails.getPusername()));
                                        Log.e("kkkk",childDetils.getUsername()+ "");


                                        startActivity(login);


                                        progressDialog.dismiss();
                                        finish();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });



                            }
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
