package com.example.dell.childsafe;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.sip.SipSession;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import static android.widget.Toast.*;

public class Locator extends AppCompatActivity {
    private LocationManager locationManager;
    private LocationListener listner;
    private Button find;
    private TextView textgps;
    double lon=77.646975;
    double lat=13.060238;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locator);
        textgps = (TextView) findViewById(R.id.txtgps);
        find = (Button) findViewById(R.id.btnfind);



        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        final Location loc1 = new Location("");
        loc1.setLatitude(lat);
        loc1.setLongitude(lon);
// Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {


            public void onLocationChanged(Location location) {

                // Called when a new location is found by the network location provider.

                final float distanceInMeters = loc1.distanceTo(location);
                find.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (distanceInMeters<=20){
                            Toast.makeText(Locator.this, "Child is within 20 meters", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(Locator.this, "Child is Beyond 20 meters", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

// Register the listener with the Location Manager to receive location updates
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


