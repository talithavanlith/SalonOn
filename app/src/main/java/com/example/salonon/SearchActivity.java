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
import androidx.cardview.widget.CardView;
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
                            System.out.println(currentLocation.getLatitude() + " ---------------------------------iifrjijfioji------------------------");
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

            Toast.makeText(SearchActivity.this, "Your location is: \n" + address + "\n" + city + ", " + state + "\n" + postalCode, Toast.LENGTH_SHORT).show();

            // Display stylists in activity_search
            Profile[] arrayOfStylists = api.searchStylistByLocation(address, city, state, postalCode, "10");

            if (arrayOfStylists[0] != null) {
                fillSearchActivityWithData(arrayOfStylists);
                Toast.makeText(SearchActivity.this, "Stylist 1 is: \n" + arrayOfStylists[0].first, Toast.LENGTH_SHORT).show();

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

    private void fillSearchActivityWithData(Profile [] profiles){
        // Get TextViews from view:
        TextView name = findViewById(R.id.txtName1);
        TextView info = findViewById(R.id.txtInfo1a);

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
//    private void fillSearchActivityWithData(Profile [] profiles){
//        // Get TextViews from view and set stylist 1 details
//        CardView stylist1 = findViewById(R.id.crdStylist1);
//
//        TextView name1 = findViewById(R.id.txtName1);
//        TextView info1A = findViewById(R.id.txtInfo1a);
//        TextView info1B = findViewById(R.id.txtInfo1b);
//
//
//        name1.setText(profiles[0].first + " " + profiles[0].last);
//        info1A.setText(profiles[0].stylistBio);
//
//
//        for(int i = 2; i <= profiles.length; i++) {
//            String idCard = "crdStylist" + i;
//            String idConstraintTop = "crdStylist" + (i - 1);
//            String color;
//
//            //set alternating colours
//            if(i % 2 == 0){
//                color = "#272423";
//            }else{
//                color = "#322E2D";
//            }
//
//            String idName = "txtName" + i;
//            String name = profiles[i-1].first + " " + profiles[i-1].last;
//            String idImage = "imageView" + i;
//            String idInfoA = "txtInfo" + i + "a";
//            String infoA = profiles[i-1].stylistBio;
//            String idInfoB = "txtInfo" + i + "b";
//            String infoB = "for now we'll say hello";
//
//            String xmlText = "\n" +
//                    "                <androidx.cardview.widget.CardView\n" +
//                    "                    android:id=\"@+id/" + idCard +"\"\n" +
//                    "                    android:layout_width=\"match_parent\"\n" +
//                    "                    android:layout_height=\"wrap_content\"\n" +
//                    "                    app:cardBackgroundColor=\""+color+"\"\n" +
//                    "                    app:layout_constraintEnd_toEndOf=\"parent\"\n" +
//                    "                    app:layout_constraintStart_toStartOf=\"parent\"\n" +
//                    "                    app:layout_constraintTop_toBottomOf=\"@+id/"+idConstraintTop+"\">\n" +
//                    "\n" +
//                    "                    <androidx.constraintlayout.widget.ConstraintLayout\n" +
//                    "                        android:layout_width=\"match_parent\"\n" +
//                    "                        android:layout_height=\"match_parent\"\n" +
//                    "                        android:background=\""+color+"\">\n" +
//                    "\n" +
//                    "                        <TextView\n" +
//                    "                            android:id=\"@+id/"+idName+"\"\n" +
//                    "                            android:layout_width=\"wrap_content\"\n" +
//                    "                            android:layout_height=\"wrap_content\"\n" +
//                    "                            android:layout_marginTop=\"20dp\"\n" +
//                    "                            android:onClick=\"stylistProfileOnClick\"\n" +
//                    "                            android:text=\""+name+"\"\n" +
//                    "                            android:textColor=\"#f7f7f4\"\n" +
//                    "                            android:textSize=\"30sp\"\n" +
//                    "                            android:visibility=\"visible\"\n" +
//                    "                            app:fontFamily=\"sans-serif-black\"\n" +
//                    "                            app:layout_constraintBottom_toTopOf=\"@+id/"+idInfoA+"\"\n" +
//                    "                            app:layout_constraintEnd_toEndOf=\"parent\"\n" +
//                    "                            app:layout_constraintHorizontal_bias=\"0.867\"\n" +
//                    "                            app:layout_constraintStart_toEndOf=\"@+id/"+idImage+"\"\n" +
//                    "                            app:layout_constraintTop_toTopOf=\"parent\" />\n" +
//                    "\n" +
//                    "                        <ImageView\n" +
//                    "                            android:id=\"@+id/"+idImage+"\"\n" +
//                    "                            android:layout_width=\"90dp\"\n" +
//                    "                            android:layout_height=\"90dp\"\n" +
//                    "                            android:layout_marginTop=\"20dp\"\n" +
//                    "                            android:layout_marginBottom=\"20dp\"\n" +
//                    "                            app:layout_constraintBottom_toBottomOf=\"parent\"\n" +
//                    "                            app:layout_constraintEnd_toStartOf=\"@+id/"+idName+"\"\n" +
//                    "                            app:layout_constraintHorizontal_bias=\"0.439\"\n" +
//                    "                            app:layout_constraintStart_toStartOf=\"parent\"\n" +
//                    "                            app:layout_constraintTop_toTopOf=\"parent\"\n" +
//                    "                            tools:srcCompat=\"@tools:sample/avatars\" />\n" +
//                    "\n" +
//                    "                        <TextView\n" +
//                    "                            android:id=\"@+id/"+idInfoA+"\"\n" +
//                    "                            android:layout_width=\"wrap_content\"\n" +
//                    "                            android:layout_height=\"19dp\"\n" +
//                    "                            android:text=\""+infoA+"\"\n" +
//                    "                            android:textColor=\"#F5F5F1\"\n" +
//                    "                            app:layout_constraintBottom_toTopOf=\"@+id/"+idInfoB+"\"\n" +
//                    "                            app:layout_constraintEnd_toEndOf=\"parent\"\n" +
//                    "                            app:layout_constraintHorizontal_bias=\"0.208\"\n" +
//                    "                            app:layout_constraintStart_toEndOf=\"@+id/"+idImage+"\"\n" +
//                    "                            app:layout_constraintTop_toBottomOf=\"@+id/"+idName+"\" />\n" +
//                    "\n" +
//                    "                        <TextView\n" +
//                    "                            android:id=\"@+id/"+idInfoB+"\"\n" +
//                    "                            android:layout_width=\"wrap_content\"\n" +
//                    "                            android:layout_height=\"wrap_content\"\n" +
//                    "                            android:layout_marginBottom=\"20dp\"\n" +
//                    "                            android:text=\""+infoB+"\"\n" +
//                    "                            android:textColor=\"#F5F5F1\"\n" +
//                    "                            app:layout_constraintBottom_toBottomOf=\"parent\"\n" +
//                    "                            app:layout_constraintEnd_toEndOf=\"parent\"\n" +
//                    "                            app:layout_constraintHorizontal_bias=\"0.208\"\n" +
//                    "                            app:layout_constraintStart_toEndOf=\"@+id/"+idImage+"\"\n" +
//                    "                            app:layout_constraintTop_toBottomOf=\"@+id/"+idInfoA+"\" />\n" +
//                    "                    </androidx.constraintlayout.widget.ConstraintLayout>\n" +
//                    "\n" +
//                    "                </androidx.cardview.widget.CardView>\n";
//
//            // BELOW = one line to figure out
////            stylist1.appendChild(xmlText);
//        }
//    }


    public void btnSelectLocationOnClick(View v) {
        Intent mapIntent = new Intent(SearchActivity.this, MapsActivity.class);
        mapIntent.putExtra("email", userProfile.email);
        startActivity(mapIntent);
    }

    public void stylistProfileOnClick(View v) {
        // Create activity_profile Intent;
        Intent stylistProfileIntent = new Intent(SearchActivity.this, ProfileActivity.class);
        startActivity(stylistProfileIntent);
    }
}
