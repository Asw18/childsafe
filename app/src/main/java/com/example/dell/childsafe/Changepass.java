package com.example.dell.childsafe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Changepass extends AppCompatActivity {

    private EditText pass;
    private EditText p;
    private EditText user;
    private Button change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepass);
        pass = (EditText)findViewById(R.id.pa2);
        p=(EditText)findViewById(R.id.pa);
        user=(EditText)findViewById(R.id.use);
        change=(Button)findViewById(R.id.ch);
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(p.getText().toString().equals(pass.getText().toString())){

                    databaseReference.child("ParentDetails").child(user.getText().toString()).child("ppassword").setValue(pass.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Toast.makeText(Changepass.this, "Password changed succesfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Changepass.this,Parentlogin.class);
                            startActivity(intent);
                            finish();

                        }
                    });


                }
                else {
                    Toast.makeText(Changepass.this, "Password Mismatch", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
