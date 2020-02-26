package com.example.dell.childsafe;

import android.Manifest;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
    private LocationManager locationManager;
    private LocationListener listner;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    String childusername;
    Serializable parentusername;
    float distanceInMeters;


    public MyService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        childusername = intent.getStringExtra("child2");
        parentusername= intent.getSerializableExtra("parent2");
        locationManager = (LocationManager) getSystemService(MyService.LOCATION_SERVICE);
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager)getSystemService(MyService.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {
                Timer time = new Timer();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {

                        location.getLongitude();
                        location.getLongitude();
                        databaseReference.child("ParentDetails").child( parentusername.toString()).child("ChildDetails").child(childusername).child("Location").setValue(location.getLongitude()+ " "+
                        location.getLatitude());

                        Log.e("location",+location.getLatitude() +"  "+location.getLongitude());

                    }
                };time.schedule(timerTask,1000,40000);

                databaseReference.child("ParentDetails").child(parentusername.toString()).child("Location").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final Double  lon = Double.parseDouble(dataSnapshot.getValue(String.class).split(" ")[0]);
                        final Double  lat = Double.parseDouble(dataSnapshot.getValue(String.class).split(" ")[1]);

                        Location location1 = new Location("");
                        location1.setLatitude(lat);
                        location1.setLongitude(lon);


                        distanceInMeters = location.distanceTo(location1);

                        databaseReference.child("ParentDetails").child(parentusername.toString()).child("ChildDetails").child(childusername).child("distance").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                final Double  dis = Double.parseDouble(dataSnapshot.getValue(String.class));

                                if (distanceInMeters> dis) {

                                    long[] v = {500,1000};

                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(MyService.this)
                                            .setSmallIcon(R.drawable.children)
                                            .setContentTitle("Child Safe")
                                            .setContentText("You crossed the limit")

                                            .setVibrate(v)
                                            .setWhen(System.currentTimeMillis())
                                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);


                                    NotificationManager notificationManager = (NotificationManager) getSystemService(Notification.NOTIFICATION_SERVICE);
                                    notificationManager.notify(2, builder.build());

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

            //   Looper.prepare();

//

            //  Toast.makeText(MyService.this, "Running", Toast.LENGTH_SHORT).show();


        };
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
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);




    }
}
