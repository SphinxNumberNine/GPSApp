package org.a10019420sbstudents.gps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    List<Address> addresses;

    TextView distanceCovered;
    TextView latitude;
    TextView longitude;
    TextView address;
    TextView elapsedTime;
    Geocoder geocoder;
    Location oldLocation;
    long startTime;
    float distanceSum = 0;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        latitude = (TextView) findViewById(R.id.latitude);
        longitude = (TextView) findViewById(R.id.longitude);
        address = (TextView) findViewById(R.id.address);
        distanceCovered = (TextView) findViewById(R.id.distance);
        elapsedTime = (TextView) findViewById(R.id.elapsedTime);
        geocoder = new Geocoder(this, Locale.US);
        startTime = System.currentTimeMillis();
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        try{
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, MainActivity.this);
        }
        catch(SecurityException e){
            e.printStackTrace();
            Log.d("ErrorLOL",e.toString());
        }
    }

    public void onProviderEnabled(String provider){

    }

    public void onProviderDisabled(String provider){

    }

    public void onLocationChanged(Location location){
        if((oldLocation != null) && (SystemClock.elapsedRealtime() >= 5000)){
            float distance = location.distanceTo(oldLocation);
            distanceSum += distance;
        }
        latitude.setText((Double.toString(location.getLatitude())));
        longitude.setText((Double.toString(location.getLongitude())));
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        }
        catch(IOException e){

        }
        catch(Exception e){

        }
        address.setText(addresses.get(0).getAddressLine(0));
        oldLocation = location;
        distanceCovered.setText("distance:" + distanceSum);
        long currentTime = System.currentTimeMillis();
        elapsedTime.setText("Elapsed Time: " + ((currentTime - startTime)/1000));
    }

    public void onStatusChanged(String provider, int status, Bundle extras){

    }
}
