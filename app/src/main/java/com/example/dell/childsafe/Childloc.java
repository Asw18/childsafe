package com.example.dell.childsafe;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

public class Childloc extends AppCompatActivity implements OnMapReadyCallback {

    double lat2;
    double lon2;
    private LocationManager locationManager;
    private LocationListener listner;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    String pname;
    String cname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_childloc);
         pname = (String) getIntent().getSerializableExtra("qwe");
         cname= (String) getIntent().getSerializableExtra("qwe2");


        // Acquire a reference to the system Location Manager
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
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(
                R.id.map);

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        Log.e("loca111", pname + "   " + cname);
        databaseReference.child("ParentDetails").child(pname).child("ChildDetails").child(cname).child("Location").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                Double lon = Double.parseDouble(dataSnapshot.getValue(String.class).split(" ")[0]);
                    Double lat = Double.parseDouble(dataSnapshot.getValue(String.class).split(" ")[1]);
                    Log.e("latitude", lat+ "");

                    MarkerOptions marker = new MarkerOptions().position(new LatLng(lat, lon));
                    marker.title(cname);
                    googleMap.addMarker(marker);

                CameraPosition cameraPosition = new CameraPosition.Builder().target(
                        new LatLng(lat, lon)).zoom(12).build();

                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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




        Log.e("coming below map", "True");

        locationManager = (LocationManager) getSystemService(Childloc.LOCATION_SERVICE);
        LocationManager locationManager = (LocationManager) getSystemService(Childloc.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                lat2=location.getLatitude();
                lon2=location.getLongitude();
                databaseReference.child("ParentDetails").child(pname).child("Location").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Double lon3 = Double.parseDouble(dataSnapshot.getValue(String.class).split(" ")[0]);
                        Double lat3 = Double.parseDouble(dataSnapshot.getValue(String.class).split(" ")[1]);

                        final CircleOptions circle = new CircleOptions();
                        circle.center(new LatLng(lat3,lon3));
                        databaseReference.child("ParentDetails").child(pname).child("ChildDetails").child(cname).child("distance").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {




                                circle.radius(Double.parseDouble(dataSnapshot.getValue(String.class)));
                                circle.fillColor(Color.parseColor("#84acdcff"));
                                circle.strokeWidth(3);
                                circle.strokeColor(Color.BLUE);
                                googleMap.addCircle(circle);

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
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

    }
}
