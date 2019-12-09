package com.example.salonon;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
            infoA.setText(profiles[i].stylistBio);
            infoB.setText("for now we'll say hello");
            //todo: add image

            //FINALLY, USE INSERT POINT TO ADD INFLATED VIEW.
            insertPoint.addView(v);
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
        stylistProfileIntent.putExtra("email", userProfile.email);
        startActivity(stylistProfileIntent);
    }
}
