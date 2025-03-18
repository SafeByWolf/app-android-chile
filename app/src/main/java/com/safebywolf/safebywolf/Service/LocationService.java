package com.safebywolf.safebywolf.Service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import com.safebywolf.safebywolf.Class.Utils.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocationService extends Service {

    private FusedLocationProviderClient locationClient;
    private LocationCallback locationCallback;
    LocationRequest mLocationRequest;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("locationS","se ejecuta on destroy");
        if(locationClient != null && locationCallback != null){
            Log.v("locationS","se detuvo locationclient");
            locationClient.removeLocationUpdates(locationCallback);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        if (intent == null){
            Log.d("locationServiceExtra","intent nulo, retorno");
            return;
        }
        Bundle extras = intent.getExtras();

        if(extras == null) {
            Log.d("locationServiceExtra","null");
        } else {
            Log.d("locationServiceExtra","not null");
            String power = (String) extras.get("power");
            startListenerLocation(power);
        }
    }

    public void startListenerLocation(String power){
        locationClient = LocationServices.getFusedLocationProviderClient(LocationService.this);
        Log.v("locationS", "locationClient " +locationClient);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.v("locationSPriority","entra aca?");
            return;
        }

        mLocationRequest = new LocationRequest();

        if(power.equalsIgnoreCase("Low")) {
            mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
            Log.v("locationSPriority",power);
            mLocationRequest.setInterval(10000);
        } else {
            //normal
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            mLocationRequest.setInterval(10000);
            Log.v("locationSPriority",power);
        }

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Log.v("locationS", "location is null");
                    return;
                }
                Location location = locationResult.getLastLocation();
                Log.v("locationSs", "latitud: " + location.getLatitude() +" longitud: "+location.getLongitude()+" speed: "+location.getSpeed());
                Intent i=new Intent("location_update");
                i.putExtra("latitud",location.getLatitude());
                i.putExtra("longitud",location.getLongitude());
                i.putExtra("speed",location.getSpeed());
                i.putExtra("location_avaible" , true);
                sendBroadcast(i);
            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);

                if(locationAvailability.isLocationAvailable()){
                    Log.v("locationS", "location avaible");
                } else {
                    Log.v("locationS", "location NOT avaible");
                }

                Intent i=new Intent("location_update");

                i.putExtra("location_avaible" , locationAvailability.isLocationAvailable());
                sendBroadcast(i);
            }
        };

        locationClient.requestLocationUpdates(mLocationRequest, locationCallback, Looper.getMainLooper());
    }

}