package com.example.salonon;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    private Boolean mLocationPermissionsGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private API api;
    private Profile userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

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

    private void processLocation(LatLng latLng){

        //convert to address then send to API
        Geocoder geocoder;
        List<Address> addresses;

        geocoder = new Geocoder(this, Locale.getDefault());

        try{
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
//            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();

//            Toast.makeText(SearchActivity.this, "Your location is: \n" + address + "\n" + city + ", " + state + "\n" + postalCode, Toast.LENGTH_SHORT).show();

            // Display stylists in activity_search
            Profile[] arrayOfStylists = api.searchStylistByLocation(address, city, state, postalCode, "10");

            if (arrayOfStylists.length > 0) {
                fillSearchActivityWithData(arrayOfStylists);

            } else {
                Toast.makeText(this, "Failed to get stylists by location", Toast.LENGTH_LONG).show();
            }

        }catch (IOException e){
            System.err.println("couldn't convert address: " + e);
        }

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
            //if they don't have the correct services, there's nothing we can do
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

    private void fillSearchActivityWithData(Profile [] profiles) {
        //*****DYNAMICALLY ADD search results to screen ******

        LinearLayout insertPoint = (LinearLayout) findViewById(R.id.searchResults);
        LayoutInflater inflater = getLayoutInflater();

        //GET INFO FROM AMENITY LIST
        for (int i = 0; i < profiles.length; i++) {

            //CREATE A INFLATED VIEW BY GIVING REFERENCE TO CHILD FILE (amenity.xml) AND FUTURE PARENT VIEW (insertPoint)
            View v;

            //Gets the odd layout file (different colour) if the iteration is odd
            if(i % 2 == 0){
                v = inflater.inflate(R.layout.search_results, insertPoint, false);
            }else{
                v = inflater.inflate(R.layout.search_results_odd, insertPoint, false);
            }

            //ACCESS THE ELEMENTS IN THE INFLATED VIEW (THIS IS WHERE WE EDIT THEM)
            TextView name = v.findViewById(R.id.txtName);
            TextView infoA = v.findViewById(R.id.txtInfoA);
            TextView infoB = v.findViewById(R.id.txtInfoB);
            ImageView image = v.findViewById(R.id.imageView);

            // set the details
            name.setText(profiles[i].first + " " + profiles[i].last);
            infoA.setText("Stylist");
            infoB.setText(Math.round(profiles[i].distance)+" miles away");
            if(profiles[i].first == "Andrew"){
                image.setBackgroundResource(R.drawable.man2);
            }else if (profiles[i].first == "Jeff"){
                image.setBackgroundResource(R.drawable.jeff);

            }else if (profiles[i].first == "KMMMMP"){
                image.setBackgroundResource(R.drawable.man3);

            }else if (profiles[i].first == "Susie"){
                image.setBackgroundResource(R.drawable.woman1);
            }else {
                image.setBackgroundResource(R.drawable.blankprofilepic);

            }
            v.setTag(profiles[i].email);

//            Bitmap bitmap = api.getProfilePic(profiles[i].email);
//            if(bitmap == null){
//                image.setBackgroundResource(R.drawable.blankprofilepic);
//            } else {
//                image.setImageBitmap(bitmap);
//            }

            //FINALLY, USE INSERT POINT TO ADD INFLATED VIEW.
            insertPoint.addView(v);
        }

    }

    public void btnSelectLocationOnClick(View v) {
        Intent mapIntent = new Intent(SearchActivity.this, MapsActivity.class);
        mapIntent.putExtra("email", userProfile.email);
        startActivity(mapIntent);
    }

    public void stylistProfileOnClick(View v) {
        // Create activity_profile Intent;
        String stylistID = (String)v.getTag();
        Log.v("stylistID", stylistID);
        Intent stylistProfileIntent = new Intent(SearchActivity.this, ProfileActivity.class);
        stylistProfileIntent.putExtra("id", stylistID);
        stylistProfileIntent.putExtra("email", userProfile.email);
        startActivity(stylistProfileIntent);
    }

    public void bookingsButton(View v){
        Intent mapIntent = new Intent(SearchActivity.this, BookingsActivity.class);
        mapIntent.putExtra("email", userProfile.email);
        startActivity(mapIntent);
        finish();
    }
}
