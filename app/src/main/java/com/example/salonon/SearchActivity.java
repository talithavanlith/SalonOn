package com.example.salonon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SearchActivity extends AppCompatActivity {

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

        // Display stylists in activity_search
        Profile[] arrayOfStylists = api.searchStylistByLocation(userProfile);
        if (arrayOfStylists != null) {
            fillSearchActivityWithData(arrayOfStylists);
        } else {
            Toast.makeText(this, "Failed to get stylists by location", Toast.LENGTH_LONG).show();
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
        Intent mapIntent = new Intent(SearchActivity.this, MapActivity.class);
        startActivity(mapIntent);
    }

    public void stylistProfileOnClick(View v) {
        // Create activity_profile Intent;
        Intent stylistProfileIntent = new Intent(SearchActivity.this, ProfileActivity.class);
        startActivity(stylistProfileIntent);
    }
}
