package com.example.dell.childsafe;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
///AIzaSyC_NvRTckzRzyT81AgmZoyQCu-InkQEqUA
public class Register extends AppCompatActivity {
    private Button signup;
    private Button add;
    private CardView cardView;
    private EditText cname;
    private EditText cuname;
    private EditText distance;
    private  EditText password;
    private  EditText age;
    private Button edit;
    private EditText Name,Number,Uname,Password;
    static  String role = "";

   private RecyclerView recyclerView;
   List<ChildDetils> childDetilsListView = new ArrayList<>();
   DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        signup=(Button)findViewById(R.id.signup);
        add=(Button)findViewById(R.id.btnadd);

        cname=(EditText)findViewById(R.id.chname);
        cuname=(EditText)findViewById(R.id.cuname);
        distance=(EditText)findViewById(R.id.distance);
        password=(EditText)findViewById(R.id.pass);
        Name=(EditText)findViewById(R.id.Name);
        Number=(EditText)findViewById(R.id.number);
        Uname=(EditText)findViewById(R.id.User);
        Password=(EditText)findViewById(R.id.Pass);
        age=(EditText)findViewById(R.id.Age) ;
        edit=(Button)findViewById(R.id.edit);


        recyclerView=(RecyclerView)findViewById(R.id.cycle);


        Adapter adapter = new Adapter(this, childDetilsListView);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);





        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(TextUtils.isEmpty(cname.getText()) || TextUtils.isEmpty(cuname.getText()) || TextUtils.isEmpty(age.getText()) || TextUtils.isEmpty(password.getText()) || TextUtils.isEmpty(distance.getText()) ){
                    Toast.makeText(Register.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                }

                else {

                    if (Integer.parseInt(distance.getText().toString())<20) {
                        Toast.makeText(Register.this, "Please enter distance minimum 20 meter", Toast.LENGTH_SHORT).show();
                    }

                    else {


                        final ChildDetils childDetils = new ChildDetils();
                        childDetils.setName(cname.getText().toString());
                        childDetils.setUsername(cuname.getText().toString());
                        childDetils.setAge(age.getText().toString());
                        childDetils.setPassword(password.getText().toString());
                        childDetils.setDistance(distance.getText().toString());
                        childDetilsListView.add(childDetils);

                        Adapter adapter = new Adapter(Register.this, childDetilsListView);
                        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Register.this);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(adapter);
                        cname.setText("");
                        cuname.setText("");
                        password.setText("");
                        distance.setText("");
                        age.setText("");

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < childDetilsListView.size(); i++) {
                                    View view = layoutManager.findViewByPosition(i);
                                    Button edit = view.findViewById(R.id.edit);

                                    final int finalI = i;
                                    edit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            cname.setText(childDetilsListView.get(finalI).getName());
                                            cuname.setText(childDetilsListView.get(finalI).getUsername());
                                            age.setText(childDetilsListView.get(finalI).getAge());
                                            password.setText(childDetilsListView.get(finalI).getPassword());
                                            distance.setText(childDetilsListView.get(finalI).getDistance());
                                            childDetilsListView.remove(childDetilsListView.get(finalI));
                                            recyclerView.getAdapter().notifyDataSetChanged();

                                        }
                                    });

                                }
                            }
                        }, 1000);
                    }

                }


            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(Name.getText() ) || TextUtils.isEmpty(Number.getText()) || TextUtils.isEmpty(Uname.getText()) || TextUtils.isEmpty(Password.getText()) ){
                    Toast.makeText(Register.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(childDetilsListView.isEmpty()){
                        Toast.makeText(Register.this, "please add child details", Toast.LENGTH_SHORT).show();

                    }
                    else {

                        if (Number.getText().length() != 10) {
                            Toast.makeText(Register.this, "Please enter valid phone number", Toast.LENGTH_SHORT).show();
                        } else {


                            final RegDetails regDetails = new RegDetails();
                            regDetails.setPname(Name.getText().toString());
                            regDetails.setPnumber(Number.getText().toString());
                            regDetails.setPpassword(Password.getText().toString());
                            regDetails.setPusername(Uname.getText().toString());
                            Log.e("regde", regDetails + "");
                            Log.e("usernames", Uname.getText() + "");
                            databaseReference.child("ParentDetails").child(Uname.getText().toString()).setValue(regDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {



                                    ChildDetils childDetils = new ChildDetils();
                                    for (int i = 0; i < childDetilsListView.size(); i++) {
                                        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Register.this);
                                        View view = layoutManager.findViewByPosition(i);
                                        Log.e("data", childDetilsListView.size() + "" + view);


                                        final int finalI = i;
                                        childDetils.setName(childDetilsListView.get(i).name);
                                        childDetils.setAge(childDetilsListView.get(i).age);
                                        childDetils.setDistance(childDetilsListView.get(i).distance);
                                        childDetils.setUsername(childDetilsListView.get(i).username);
                                        childDetils.setPassword(childDetilsListView.get(i).password);
                                        databaseReference.child("ParentDetails").child(Uname.getText().toString()).child("ChildDetails").child(childDetilsListView.get(i).getUsername()).setValue(childDetils);
                                        Intent login = new Intent(Register.this, Parentlogin.class);
                                        startService(new Intent(Register.this, ParentService.class).putExtra("child2", childDetilsListView.get(i).getUsername()).putExtra("parent2", regDetails.getPusername()));
                                        Toast.makeText(Register.this, "Please log into child account firstly", Toast.LENGTH_SHORT).show();

                                        startActivity(login);

                                        finish();

                                    }


                                }
                            });
                        }


                        }


                    }
                }



        });


    }
}
