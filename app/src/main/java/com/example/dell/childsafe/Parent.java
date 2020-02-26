package com.example.dell.childsafe;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Parent extends AppCompatActivity {
    RecyclerView recyclerView;
    Button locate;




    List<ChildDetils> childDetilsListView = new ArrayList<>();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);

        final RegDetails re = (RegDetails) getIntent().getSerializableExtra("parent");
        final ChildDetils ch = (ChildDetils) getIntent().getSerializableExtra("child");
        /*locate=(Button)findViewById(R.id.childloc);*/

        /*locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Parent.this,Findall.class);
                intent.putExtra("pname",re);
                startActivity(intent);
            }
        });*/

        recyclerView = (RecyclerView) findViewById(R.id.recycle);




        Log.e("reg", re.getPusername() + " jhgvkj");

        databaseReference.child("ParentDetails").child(re.getPusername()).child("ChildDetails").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<ChildDetils> childDetils = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ChildDetils child = ds.getValue(ChildDetils.class);
                    childDetils.add(child);
                    ParentAdapter adapter2 = new ParentAdapter(Parent.this, childDetils, re.getPusername());
                    final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Parent.this);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter2);


                    startService(new Intent(Parent.this,Notification.class).putExtra("child3",child.getUsername()).putExtra("parent3",re.getPusername()));


                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

}
