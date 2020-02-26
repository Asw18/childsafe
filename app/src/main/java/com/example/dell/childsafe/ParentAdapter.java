package com.example.dell.childsafe;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ParentAdapter extends RecyclerView.Adapter<ParentAdapter.MyHolder> {


    Context context;
    float distanceInMeters;
    List<ChildDetils> childDetils;
    String parentUsername;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    public ParentAdapter(Context register, List<ChildDetils> childDetilsListView, String re) {
        context =register ;
        childDetils = childDetilsListView;
        parentUsername = re;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.child, viewGroup, false);
        return new MyHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, final int i) {
      myHolder.name.setText(childDetils.get(i).getName());
        myHolder.uname.setText(childDetils.get(i).getUsername());
        myHolder.age.setText(childDetils.get(i).getPassword());
        myHolder.distance.setText(childDetils.get(i).getDistance());

        myHolder.change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.chnge_diatance);
                dialog.show();
                Button btn = dialog.findViewById(R.id.close);
                Button button = dialog.findViewById(R.id.change);
                final EditText text = dialog.findViewById(R.id.chdis);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(text.getText().toString())){
                            Toast.makeText(context, "Please enter Distance", Toast.LENGTH_SHORT).show();
                        }
                        else{

                            if (Integer.parseInt(text.getText().toString())< 20) {
                                Toast.makeText(context, "Please enter distance minimum 20 meter", Toast.LENGTH_SHORT).show();
                            } else {

                                databaseReference.child("ParentDetails").child(parentUsername).child("ChildDetails").child(childDetils.get(i).getUsername()).child("distance").setValue(text.getText().toString());
                                dialog.dismiss();
                                myHolder.distance.setText(text.getText().toString());

                            }

                        }

                    }
                });
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        myHolder.status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (distanceInMeters<=20){
                    Toast.makeText(context, "Child is within 20 meters", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context, "Child is Beyond 20 meters", Toast.LENGTH_SHORT).show();
                }
            }
        });


        myHolder.locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent locate = new Intent(context,Childloc.class);
                locate.putExtra("qwe",parentUsername);
                locate.putExtra("qwe2",childDetils.get(i).getUsername());
                Log.e("parent",parentUsername);
                context.startActivity(locate);

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
       private Button status;
       private Button locate;
       private TextView age;
       private Button change;
        double lon=77.646975;
        double lat=13.060238;

        private LocationManager locationManager;
        private LocationListener listner;



        public MyHolder(@NonNull final View itemView) {
            super(itemView);
            name= itemView.findViewById(R.id.nme2);
            uname= itemView.findViewById(R.id.ume2);
            distance= itemView.findViewById(R.id.dtnce2);
            delete=itemView.findViewById(R.id.delete);
            age =itemView.findViewById(R.id.age2);
            status=itemView.findViewById(R.id.status);
            locate=itemView.findViewById(R.id.locate);
            change=itemView.findViewById(R.id.chge);
            password=itemView.findViewById(R.id.password);

            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            // Acquire a reference to the system Location Manager
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);


// Define a listener that responds to location updates
            LocationListener locationListener = new LocationListener() {


                public void onLocationChanged(final Location location) {

                    databaseReference.child("ParentDetails").child(parentUsername).child("ChildDetails").child(uname.getText().toString()).child("Location").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Double lon2 = Double.parseDouble(dataSnapshot.getValue(String.class).split(" ")[0]);
                            Double lat2 = Double.parseDouble(dataSnapshot.getValue(String.class).split(" ")[1]);

                            final Location loc1 = new Location("");
                            loc1.setLatitude(lat2);
                            loc1.setLongitude(lon2);

                            distanceInMeters = loc1.distanceTo(location);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    // Called when a new location is found by the network location provider.





                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                public void onProviderEnabled(String provider) {
                }

                public void onProviderDisabled(String provider) {
                }
            };

// Register the listener with the Location Manager to receive location updates
           if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);



        }





        }
    }



