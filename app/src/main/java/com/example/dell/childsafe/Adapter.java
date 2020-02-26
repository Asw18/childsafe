package com.example.dell.childsafe;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyHolder> {

    Context context;
    List<ChildDetils> childDetils;
    public Adapter(Context register, List<ChildDetils> childDetilsListView) {
        context = register;
        childDetils = childDetilsListView;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview, viewGroup, false);
        return new MyHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, final int i) {
      myHolder.name.setText(childDetils.get(i).getName());
        myHolder.uname.setText(childDetils.get(i).getUsername());
        myHolder.age.setText(childDetils.get(i).getAge());
        myHolder.password.setText(childDetils.get(i).getPassword());
        myHolder.distance.setText(childDetils.get(i).getDistance());
        myHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                childDetils.remove(childDetils.get(i));
                notifyDataSetChanged();
            }
        });



    }

    @Override
    public int getItemCount() {

        return childDetils.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

       private TextView name;
       private TextView uname;
       private TextView distance;
       private TextView password;
       private Button delete;

       private TextView age;



        public MyHolder(@NonNull View itemView) {
            super(itemView);
            name= itemView.findViewById(R.id.nme);
            uname= itemView.findViewById(R.id.ume);
            password=itemView.findViewById(R.id.password);
            distance= itemView.findViewById(R.id.dtnce);
            delete=itemView.findViewById(R.id.delete);
            age =itemView.findViewById(R.id.Ag2);





        }
    }


}
