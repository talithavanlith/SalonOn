package com.example.salonon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    private API api;
    private Profile stylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent currentIntent = getIntent();
        Bundle bundle = currentIntent.getExtras();
        String email = bundle.getString("id");

        api = new API();

        Offer[] offers = api.getStylistOffers(email);
        stylist = api.getClientProfile(email);
        Log.v("offerLength", "" +offers.length);

        loadProfile(stylist, offers);
    }

    private void loadProfile(Profile stylist, Offer[] offers){
        //ACCESS THE ELEMENTS IN THE INFLATED VIEW (THIS IS WHERE WE EDIT THEM)
        ImageView image = this.findViewById(R.id.imgProfile);
        TextView name = this.findViewById(R.id.txtStylistName);
        TextView details = this.findViewById(R.id.txtDetails);

        TextView bio = this.findViewById(R.id.txtBio);

        RatingBar rateClean = this.findViewById(R.id.rateClean);
        RatingBar rateAccess = this.findViewById(R.id.rateAccess);
        RatingBar rateFriend = this.findViewById(R.id.rateFriend);
        RatingBar rateProfes = this.findViewById(R.id.rateProfes);

        TextView comment1 = this.findViewById(R.id.txtComment);
        TextView comment2 = this.findViewById(R.id.txtComment2);

        // set the details
        //todo: add image
        name.setText(stylist.first + " " + stylist.last);
        //todo: get miles away data from SearchActivity.java (maybe by creating a hash map)
        details.setText("5 miles away");
        bio.setText(stylist.stylistBio);

        //todo: access stylist's ratings and comments
//        rateClean.setRating(stylist.getCleanRating);
//        rateAccess.setRating(stylist.getAccessRating);
//        rateFriend.setRating(stylist.getFriendRating);
//        rateProfes.setRating(stylist.getProfesRating);

//        comment1.setText(stylist.getComment);
//        comment2.setText(stylist.getComment2);

        //todo: dynamically generate the styles available
        //*****DYNAMICALLY ADD search results to screen ******

        LinearLayout insertPoint = (LinearLayout) findViewById(R.id.styleOptions);
        LayoutInflater inflater = getLayoutInflater();

        //GET INFO FROM AMENITY LIST
        for (int i = 0; i < offers.length; i++) {

            //CREATE A INFLATED VIEW BY GIVING REFERENCE TO CHILD FILE (amenity.xml) AND FUTURE PARENT VIEW (insertPoint)
            View newView = inflater.inflate(R.layout.style, insertPoint, false);

            //ACCESS THE ELEMENTS IN THE INFLATED VIEW (THIS IS WHERE WE EDIT THEM)
            TextView styleName = newView.findViewById(R.id.txtStyleName);
            TextView time = newView.findViewById(R.id.txtTime);
            TextView price = newView.findViewById(R.id.txtPrice);
            ImageView styleImage = newView.findViewById(R.id.imgStyle);
//            name.setTag(offers[i].email);

            // set the details
            styleName.setText(offers[i].style);
            time.setText("" + offers[i].duration + "hrs");
            price.setText("$" + offers[i].price);
            //todo: add image

            //FINALLY, USE INSERT POINT TO ADD INFLATED VIEW.
            insertPoint.addView(newView);
        }

    }
    public void styleButtonOnClick(View v) {

        // Create activity_profile Intent;
        Intent styleInfoIntent = new Intent(ProfileActivity.this, StyleInfoActivity.class);
        // todo: below get the name of the style that is clicked (and maybe the stylist's email/id)
//        styleInfoIntent.putExtra("stylist", v.getStylistEmail());
//        styleInfoIntent.putExtra("style", v.getStyleID());
        startActivity(styleInfoIntent);
//        // Create activity_profile Intent;
//        String stylistID = (String) v.getTag();
//        Log.v("stylistID", stylistID);
//        Intent stylistProfileIntent = new Intent(SearchActivity.this, ProfileActivity.class);
//        stylistProfileIntent.putExtra("id", stylistID);
//        startActivity(stylistProfileIntent);
    }
}
