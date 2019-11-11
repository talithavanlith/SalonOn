package com.example.salonon;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    private Boolean mLocationPermissionsGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // get extras from passed intent:
        Intent currentIntent = getIntent();
        Bundle bundle = currentIntent.getExtras();
        String email = bundle.getString("email");

        //get user profile
        API api = new API();
        Profile userProfile = api.getClientProfile(email);

        if(userProfile == null) {
            Toast.makeText(this, "Failed to re-fetch profile", Toast.LENGTH_LONG).show();
        }

        //in here we want to request permission to get location

        if(isServicesOK()) {
            getLocationPermissions();
        }

        //then we want to get the locations
        if(mLocationPermissionsGranted){
            getDeviceLocation();
        }else{
            // Make default map location Chapel Hill
            LatLng chapelHill = new LatLng(35.913200, -79.055847);
            processLocation(chapelHill);
        }


        //then convert location to address

        //then send address to method below

        // Display stylists in activity_search
        Profile[] arrayOfStylists = api.searchStylistByLocation(userProfile);
        //^^^ we actually want this to search with their location so i think i need to change the api method too
        if (arrayOfStylists != null) {
            fillSearchActivityWithData(arrayOfStylists);
        } else {
            Toast.makeText(this, "Failed to get stylists by location", Toast.LENGTH_LONG).show();
        }
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
                            Location currentLocation = (Location) task.getResult(); //return this to main to send to next method
                            LatLng curLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                            processLocation(curLocation);
                            return;
                        }else{
                            // current location is null
                            Toast.makeText(SearchActivity.this, "Unable to get current location",
                                    Toast.LENGTH_SHORT).show();

                            //TODO: maybe eventually ask for a postcode or something
                        }
                    }
                });
            }

        }catch (SecurityException e){
            System.err.println("getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    private void processLocation(LatLng loc){

        Toast.makeText(SearchActivity.this, "the location is found and it is:" + loc,
                Toast.LENGTH_SHORT).show();

        //convert to address then send to API

    }

    public boolean isServicesOK(){
        // checking google services version

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(SearchActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            return true;
        }else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occurred but we can resolve it
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(SearchActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            //if they don't have the correct services, theres nothing we can do
            //TODO: make something happen if they deny services e.g. set a default location
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void getLocationPermissions(){
        String[] permissions = {FINE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            // we are good and permission has been granted
            mLocationPermissionsGranted = true;
//            initMap();
        }else{
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void fillSearchActivityWithData(Profile [] profiles){
        // Get TextViews from view:
        TextView stylistNameTextView = findViewById(R.id.txtName1);
        TextView stylistInfoTextView = findViewById(R.id.txtInfo1);

        //todo: fix this. Need to be able to display any number of profiles. not just 3
        for(int i = 0; i < profiles.length; i++) {
            if(i > 3) {
                // return here because I do not currently have a way to dynamically generate the xml for each profile item, so I only want to have 4 profile objects be read if we can read them.
                return;
            }
            // render profile information to XML.
            if(i == 0) {
                stylistNameTextView = findViewById(R.id.txtName1);
                stylistInfoTextView = findViewById(R.id.txtInfo1);
            } else if (i == 1) {
                stylistNameTextView = findViewById(R.id.txtName2);
                stylistInfoTextView = findViewById(R.id.txtInfo2);
            } else if (i == 2) {
                stylistNameTextView = findViewById(R.id.txtName3);
                stylistInfoTextView = findViewById(R.id.txtInfo3);
            } else if (i == 3) {
                stylistNameTextView = findViewById(R.id.txtName4);
                stylistInfoTextView = findViewById(R.id.txtInfo4);
            }

            stylistNameTextView.setText(profiles[i].first);
        }
    }

    public void btnSelectLocationOnClick(View v) {
        Intent mapIntent = new Intent(SearchActivity.this, MapsActivity.class);
        startActivity(mapIntent);
    }

    public void stylistProfileOnClick(View v) {
        // Create activity_profile Intent;
        Intent stylistProfileIntent = new Intent(SearchActivity.this, ProfileActivity.class);
        startActivity(stylistProfileIntent);
    }
}
