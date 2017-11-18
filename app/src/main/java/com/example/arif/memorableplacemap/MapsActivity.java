package com.example.arif.memorableplacemap;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;

    LocationManager locationManager;
    LocationListener locationListener;


    /// Center Map on Location

    public void CenterMapOnLocation(Location location, String Title) {
        LatLng userLoc = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.clear();

        if(Title != "Your Location")
        {
            mMap.addMarker(new MarkerOptions().position(userLoc).title(Title));
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLoc, 10));


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults != null && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    CenterMapOnLocation(lastKnownLocation, "Your Location");

                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Intent intent = getIntent();

        mMap.setOnMapLongClickListener(this);

        //int pos;

       // pos = intent.getIntExtra("PlaceNumber", 0);

        if (intent.getIntExtra("PlaceNumber", 0) == 0) {
            /// User Location
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            locationListener = new LocationListener() {

                @Override
                public void onLocationChanged(Location location) {

                    CenterMapOnLocation(location, "Your Location");
                    Log.i("Location", location.toString());

                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            };


            if (Build.VERSION.SDK_INT < 23) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
            else {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 5, locationListener);
                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    CenterMapOnLocation(lastKnownLocation, "Your Location");
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }

            }

        } // Intend  END

    } /// onMapReady End

    @Override
    public void onMapLongClick(LatLng latLng) {
        String address = "";

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());


        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if(addresses != null && addresses.size() > 0){
                if(addresses.get(0).getThoroughfare() != null)
                {
                    if(addresses.get(0).getSubThoroughfare() != null)
                    {
                        address += addresses.get(0).getSubThoroughfare().toString() +" ";
                    }

                    address += addresses.get(0).getThoroughfare().toString();
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();

        }

        if(address == "")
        {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy");
            address =  sdf.format(new Date());
            Log.i("Date", address);
        }

        mMap.addMarker(new MarkerOptions().position(latLng).title(address));

        MainActivity.places.add(address);
        MainActivity.locations.add(latLng);
        MainActivity.arrayAdapter.notifyDataSetChanged();

        Toast.makeText(this, "Location Saved !", Toast.LENGTH_SHORT).show();

    }
} // Class END
