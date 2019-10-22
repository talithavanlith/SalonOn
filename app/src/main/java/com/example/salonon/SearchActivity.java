package com.example.salonon;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Login to account
        // get extras from passed intent:
        Intent currentIntent = getIntent();
        Bundle bundle = currentIntent.getExtras();
        String userType = bundle.getString("userType");
        // String name = bundle.getString("name");
        String email = bundle.getString("email");
        String password = bundle.getString("password");

        API api = new APIImpl();
        Profile userProfile = api.loginToProfile(email, password);
        if(userProfile != null) {
            Toast.makeText(this, "User Login Successful!", Toast.LENGTH_LONG).show();
        } else {
            //Toast.makeText(this, "Error, login unsuccessful", Toast.LENGTH_SHORT).show();
        }
        // add latitude and longitude of UNC CH. These values will eventually come from userProfile
        String latitude = "35.9049° N" ;
        String longitude = "79.0469° W";
        int radius = 10;

        // Display stylists in activity_search

        Profile[] arrayOfStylists = api.stylistSearchForProfilesByLocation(userProfile);
        if (arrayOfStylists != null) {
            fillSearchActivityWithData(arrayOfStylists);
        } else {
            // test
            // Test data
//            Toast.makeText(this, "Unable to get profile data from the server. Displaying local test data.", Toast.LENGTH_LONG).show();
            Image image = null;
            String creditCardNumber = "00000002";
            String bio = "I am a " + "stylist";
            Booking[] bookings = new Booking[1];
            String name = "test!!!";
            String phoneNumber = "999";
            String zipCode = "27599";
            Profile testProfile = makeProfile("Stylist", email, password, name, image,true,true, true, phoneNumber, creditCardNumber, bio, bookings, zipCode);
            Profile[] testProfiles = new Profile[1];
            testProfiles[0] = testProfile;
            fillSearchActivityWithData(testProfiles);
        }





    }

    private Profile makeProfile(String userType, String email, String password, String name, @Nullable Image image, Boolean shareContactInfoWhenBookingIsRequested, Boolean useMobileData, Boolean allowNotifications, String phoneNumber, String creditCardNumber, String bio, Booking[] arrayOfBookings, String zip) {
        String firstName = name.split(" ")[0];
        // String lastName = name.split(" ")[1];
        String lastName = firstName;
        Profile userProfile = new Profile(userType, email, password, firstName, lastName, image, shareContactInfoWhenBookingIsRequested, useMobileData, allowNotifications, phoneNumber, creditCardNumber, bio, arrayOfBookings, zip);
        return userProfile;
    }


    private void fillSearchActivityWithData(Profile [] profiles){
        // Get TextViews from view:
        TextView stylistNameTextView = findViewById(R.id.txtName1);
        TextView stylistInfoTextView = findViewById(R.id.txtInfo1);
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

            stylistNameTextView.setText(profiles[i].firstName);
            stylistInfoTextView.setText(profiles[i].typeOfProfile);
        }
    }

    public void btnSelectLocationOnClick(View v) {
        Intent mapIntent = new Intent(SearchActivity.this, MapActivity.class);

        startActivity(mapIntent);
    }

    public void stylistProfileOnClick(View v) {
        // Create activity_profile Intent;
        Intent stylistProfileIntent = new Intent(SearchActivity.this, ProfileActivity.class);
        startActivity(stylistProfileIntent);
    }
}
