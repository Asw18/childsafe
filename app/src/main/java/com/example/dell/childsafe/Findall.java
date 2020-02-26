package com.example.dell.childsafe;

import android.Manifest;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.media.RingtoneManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Findall extends AppCompatActivity implements OnMapReadyCallback {



    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findall);

        ChildDetils childDetils= (ChildDetils) getIntent().getSerializableExtra("child");





        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(
                R.id.map2);

        mapFragment.getMapAsync((OnMapReadyCallback) this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        final RegDetails regDetails = (RegDetails) getIntent().getSerializableExtra("pname");

        databaseReference.child("ParentDetails").child(regDetails.getPusername()).child("ChildDetails").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final List<ChildDetils> childDetils = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    final ChildDetils child = ds.getValue(ChildDetils.class);
                    childDetils.add(child);

                    for (int i = 1; i <= childDetils.size(); i++) {

                        databaseReference.child("ParentDetails").child(regDetails.getPusername()).child("ChildDetails").child(child.getUsername()).child("Location").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final Double lon = Double.parseDouble(dataSnapshot.getValue(String.class).split(" ")[0]);
                                final Double lat = Double.parseDouble(dataSnapshot.getValue(String.class).split(" ")[1]);
                                final MarkerOptions marker = new MarkerOptions().position(new LatLng(lat, lon)).title(child.getUsername());
                                googleMap.addMarker(marker);
                                Log.e("qwe", child.getUsername());

                                databaseReference.child("ParentDetails").child(regDetails.getPusername()).child("ChildDetails").child(child.getUsername()).child("Location").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        final Double lon = Double.parseDouble(dataSnapshot.getValue(String.class).split(" ")[0]);
                                        final Double lat = Double.parseDouble(dataSnapshot.getValue(String.class).split(" ")[1]);








                                        databaseReference.child("ParentDetails").child(regDetails.getPusername()).child("ChildDetails").child(marker.getTitle()).child("distance").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                final Double  dis = Double.parseDouble(dataSnapshot.getValue(String.class));



                                                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                                    @Override
                                                    public boolean onMarkerClick(Marker marker) {

                                                        final CircleOptions circle = new CircleOptions();
                                                        circle.center(new LatLng(lat,lon));

                                                        circle.radius(dis);
                                                        circle.fillColor(Color.parseColor("#84acdcff"));
                                                        circle.strokeWidth(3);
                                                        circle.strokeColor(Color.BLUE);
                                                        googleMap.addCircle(circle);


                                                        return false;
                                                    }
                                                });

                                                Log.e("tance",dis+ "");









                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }

                databaseReference.child("ParentDetails").child(regDetails.getPusername()).child("ChildDetails").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<ChildDetils> childDetils = new ArrayList<>();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            final ChildDetils child = ds.getValue(ChildDetils.class);
                            childDetils.add(child);






                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });











            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



          /*  MarkerOptions marker = new MarkerOptions().position(new LatLng(lat, lon)).title("Puneet");

        googleMap.addMarker(marker);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                new LatLng(lat, lon)).zoom(12).build();

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        MarkerOptions marker2 = new MarkerOptions().position(new LatLng(lat2, lon2)).title("Prashant");
        googleMap.addMarker(marker2);
        CameraPosition cameraPosition2 = new CameraPosition.Builder().target(
                new LatLng(lat2, lon2)).zoom(12).build();

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition2));*/
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
    }
}
