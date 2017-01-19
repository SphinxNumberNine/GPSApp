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
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    List<Address> addresses;
    ArrayList<Long> times;
    ArrayList<FavoriteLocation> favoriteLocations = new ArrayList<>();
    TextView distanceCovered;
    TextView latitude;
    TextView longitude;
    TextView address;
    TextView elapsedTime;
    TextView elapsedTimeAtPrev;
    TextView favoriteLatitude;
    TextView favoriteLongitude;
    TextView favoriteAddress;
    TextView timeAtFavoriteLocation;
    Geocoder geocoder;
    Location oldLocation;
    long startTime;
    long currentLocationStartTime;
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
        elapsedTimeAtPrev = (TextView) findViewById(R.id.lastLocationElapsedTime);
        favoriteLatitude = (TextView) findViewById(R.id.latitude);
        favoriteLongitude = (TextView) findViewById(R.id.longitude);
        favoriteAddress = (TextView) findViewById(R.id.favoriteAddress);
        timeAtFavoriteLocation = (TextView) findViewById(R.id.timeAtFavorite);
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
        try {
            long timeSpentAtPrev = System.currentTimeMillis() - currentLocationStartTime;
            elapsedTimeAtPrev.setText((timeSpentAtPrev / 1000) + " seconds");
            boolean newLocation = true;
            for(int x = 0; x < favoriteLocations.size(); x++){
                if(favoriteLocations.get(x).getLocation() == oldLocation){
                    newLocation = false;
                }
            }
            if(newLocation){
                favoriteLocations.add(new FavoriteLocation(oldLocation, timeSpentAtPrev));
            }
            else{
                for(int x = 0; x < favoriteLocations.size(); x++){
                    if(favoriteLocations.get(x).getLocation() == oldLocation){
                        favoriteLocations.get(x).setTime(favoriteLocations.get(x).getTime() + timeSpentAtPrev);
                    }
                }
            }
        }
        catch(Exception e){

        }
        currentLocationStartTime = System.currentTimeMillis();
        if((oldLocation != null) && (SystemClock.elapsedRealtime() >= 10000)){
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
        distanceCovered.setText(distanceSum + " meters");
        long currentTime = System.currentTimeMillis();
        elapsedTime.setText(((currentTime - startTime)/1000) + " seconds");
        int largestElement = -1;
        try {
            for (int x = 0; x < favoriteLocations.size(); x++) {
                int largest = x;
                for (int y = x + 1; y < favoriteLocations.size(); y++) {
                    if (favoriteLocations.get(y).getTime() > favoriteLocations.get(x).getTime()) {
                        largest = y;
                    }
                }
                FavoriteLocation temp = favoriteLocations.get(x);
                favoriteLocations.set(x, favoriteLocations.get(largest));
                favoriteLocations.set(largest, temp);
            }
            Location favoriteLocation = favoriteLocations.get(0).getLocation();
            favoriteLatitude.setText(Double.toString(favoriteLocation.getLatitude()));
            favoriteLongitude.setText(Double.toString(favoriteLocation.getLongitude()));
            long timeSpentAtFavoriteLocation = favoriteLocations.get(0).getTime();
            timeAtFavoriteLocation.setText(timeSpentAtFavoriteLocation + " seconds");
            try {
                addresses = geocoder.getFromLocation(favoriteLocation.getLatitude(), favoriteLocation.getLongitude(), 1);
                Address favoriteAddress1 = addresses.get(0);
                favoriteAddress.setText(favoriteAddress1.getAddressLine(0));
            } catch (IOException e) {

            } catch (Exception e) {

            }
        }
        catch(Exception e){

        }

    }

    public void onStatusChanged(String provider, int status, Bundle extras){

    }
}
