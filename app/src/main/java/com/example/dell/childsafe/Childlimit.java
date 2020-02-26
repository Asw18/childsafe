package com.example.dell.childsafe;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
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

public class Childlimit extends AppCompatActivity implements OnMapReadyCallback {
    private LocationManager locationManager;
    private LocationListener listner;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    String cname;
    String pname;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_childlimit);
        pname = (String) getIntent().getSerializableExtra("parent2");
        cname= (String) getIntent().getSerializableExtra("child2");


        startService(new Intent(Childlimit.this,MyService.class).putExtra("child2",cname).putExtra("parent2",pname));


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(
                R.id.map3);

        mapFragment.getMapAsync((OnMapReadyCallback) this);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(Childlimit.this);
        builder.setContentTitle("Warning");
        builder.setContentText("Your child is beyond limit");
        builder.build();




    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
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
        databaseReference.child("ParentDetails").child(pname).child("Location").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               final Double  lon = Double.parseDouble(dataSnapshot.getValue(String.class).split(" ")[0]);
               final Double  lat = Double.parseDouble(dataSnapshot.getValue(String.class).split(" ")[1]);
                MarkerOptions marker = new MarkerOptions().position(new LatLng(lat, lon)).title("Parent");
                googleMap.addMarker(marker);

                databaseReference.child("ParentDetails").child(pname).child("ChildDetails").child(cname).child("distance").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        CircleOptions circle = new CircleOptions();
                        circle.center(new LatLng(lat, lon));
                        circle.radius(Double.parseDouble(dataSnapshot.getValue(String.class)));
                        circle.fillColor(Color.parseColor("#84acdcff"));
                        circle.strokeWidth(3);



                        circle.strokeColor(Color.BLUE);
                        googleMap.addCircle(circle);
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                                new LatLng(lat, lon)).zoom(12).build();

                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

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
