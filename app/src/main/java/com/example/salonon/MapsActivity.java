package com.example.salonon;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import com.google.android.gms.location.FusedLocationProviderClient;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    //vars
    private Boolean mLocationPermissionsGranted = false;
    private UiSettings uiSettings;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private API api;
    private Profile userProfile;

    //address vars
    private String address;
    private String city;
    private String state;
    private String postalCode;
    private String radius;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        getLocationPermissions();
        api = new API();

        // get extras from passed intent:
        Intent currentIntent = getIntent();
        Bundle bundle = currentIntent.getExtras();
        String email = bundle.getString("email");

        //get user profile
        api = new API();
        userProfile = api.getClientProfile(email);

        if(userProfile == null) {
            Toast.makeText(this, "Failed to re-fetch profile", Toast.LENGTH_LONG).show();
        }
    }


    public void getLocationPermissions(){
        String[] permissions = {FINE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            // we are good and permission has been granted
            mLocationPermissionsGranted = true;
            initMap();
        }else{
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void initMap(){
        //initialise the map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsActivity.this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show();
        mMap = googleMap;

        if(mLocationPermissionsGranted){
            getDeviceLocation();
            mMap.setMyLocationEnabled(true);
        }else{
            // Make default map location Chapel Hill
            LatLng chapelHill = new LatLng(35.913200, -79.055847);
            moveCamera(chapelHill, DEFAULT_ZOOM);
        }
        uiSettings = mMap.getUiSettings();
        uiSettings.setZoomGesturesEnabled(true);
        uiSettings.setMapToolbarEnabled(false);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                MarkerOptions marker = new MarkerOptions().position(latLng).title("You chose here");
                mMap.addMarker(marker);
                moveCamera(latLng, DEFAULT_ZOOM);
                // Drawing circle on the map
                drawCircle(latLng, mMap);

                convertToAddress(latLng);
            }
        });
        // Sets the map type to be "hybrid"
//        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }

    private void getDeviceLocation(){
        //getting devices current location

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try{
            if(mLocationPermissionsGranted){

                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            //found location
                            Location currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(),
                                    currentLocation.getLongitude()), DEFAULT_ZOOM);
                        }else{
                            // current location is null
                            Toast.makeText(MapsActivity.this, "Unable to get current location",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }catch (SecurityException e){
            System.err.println("getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng, float zoom){
        //moving the camera to the given coordinates
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void drawCircle(LatLng point, GoogleMap mMap){

        // Instantiating CircleOptions to draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();

        // Specifying the center of the circle
        circleOptions.center(point);

        // Radius of the circle
        circleOptions.radius(600);

        // Border color of the circle
        circleOptions.strokeColor(Color.GRAY);

        // Fill color of the circle
//        circleOptions.fillColor(0x99cc66);

        // Border width of the circle
        circleOptions.strokeWidth(20);

        // Adding the circle to the GoogleMap
        mMap.addCircle(circleOptions);

    }

    private void convertToAddress(LatLng latLng){
        Geocoder geocoder;
        List<Address> addresses;

        geocoder = new Geocoder(this, Locale.getDefault());

        try{
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = addresses.get(0).getLocality();
            state = addresses.get(0).getAdminArea();
            postalCode = addresses.get(0).getPostalCode();

            //radius to be set
            radius = "10";

            Toast.makeText(MapsActivity.this, "You choose the location: \n" + address + "\n" + city + ", " + state + "\n" + postalCode, Toast.LENGTH_SHORT).show();

        }catch (IOException e){
            System.err.println("couldn't convert address: " + e);
        }

    }

    //this method will be called when "find stylists" button is clicked (maybe make it an event listener?)
    private void findStylistOnClick(View v){

        Intent stylistProfileIntent = new Intent(MapsActivity.this, SearchResultsActivity.class);
        stylistProfileIntent.putExtra("address", address);
        stylistProfileIntent.putExtra("city", city);
        stylistProfileIntent.putExtra("state", state);
        stylistProfileIntent.putExtra("postalCode", postalCode);
        stylistProfileIntent.putExtra("radius", radius);
        stylistProfileIntent.putExtra("email", userProfile.email);
        startActivity(stylistProfileIntent);

    }
}
