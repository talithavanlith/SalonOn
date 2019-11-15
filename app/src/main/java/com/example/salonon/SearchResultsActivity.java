package com.example.salonon;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SearchResultsActivity extends AppCompatActivity {

    private API api;


    //address vars
    private String address;
    private String city;
    private String state;
    private String postalCode;
    private String radius;
    private Profile userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        // get extras from passed intent:
        Intent currentIntent = getIntent();
        Bundle bundle = currentIntent.getExtras();
        String email = bundle.getString("email");
        address = bundle.getString("address");
        city = bundle.getString("city");
        state = bundle.getString("state");
        postalCode = bundle.getString("postalCode");
        radius = bundle.getString("radius");

        //get user profile
        api = new API();
        userProfile = api.getClientProfile(email);

        if(userProfile == null) {
            Toast.makeText(this, "Failed to re-fetch profile", Toast.LENGTH_LONG).show();
        }

        findStylists();
    }

    //maybe takes address information but maybe uses global vars??
    private void findStylists(){

        try{
            // Display stylists in activity_search
            Profile[] arrayOfStylists = api.searchStylistByLocation(address, city, state, postalCode, radius);

            if (arrayOfStylists[0] != null) {
                fillSearchActivityWithData(arrayOfStylists);
                Toast.makeText(SearchResultsActivity.this, "Stylist 1 is: \n" + arrayOfStylists[0].first, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to get stylists by location", Toast.LENGTH_LONG).show();
            }

        }catch (Exception e){
            System.err.println("couldn't convert address: " + e);
        }

    }

    private void fillSearchActivityWithData(Profile [] profiles){
        // Get TextViews from view:
        TextView name = findViewById(R.id.txtName1);
        TextView info = findViewById(R.id.txtInfo1);

        //todo: fix this. Need to be able to display any number of profiles. not just 3
        for(int i = 0; i < profiles.length; i++) {
            if(i > 3) {
                // return here because I do not currently have a way to dynamically generate the xml for each profile item, so I only want to have 4 profile objects be read if we can read them.
                return;
            }
            // render profile information to XML.
            if(i == 0) {
                name = findViewById(R.id.txtName1);
                info = findViewById(R.id.txtInfo1a);
            } else if (i == 1) {
                name = findViewById(R.id.txtName2);
                info = findViewById(R.id.txtInfo2a);
            } else if (i == 2) {
                name = findViewById(R.id.txtName3);
                info = findViewById(R.id.txtInfo3a);
            }

            name.setText(profiles[i].first + " " + profiles[i].last);
            info.setText(profiles[i].stylistBio);
        }
    }

    public void btnSelectLocationOnClick(View v) {
        Intent mapIntent = new Intent(SearchResultsActivity.this, MapsActivity.class);
        mapIntent.putExtra("email", userProfile.email);
        startActivity(mapIntent);
    }

    public void stylistProfileOnClick(View v) {
        // Create activity_profile Intent;
        Intent stylistProfileIntent = new Intent(SearchResultsActivity.this, ProfileActivity.class);
        startActivity(stylistProfileIntent);
    }
}
