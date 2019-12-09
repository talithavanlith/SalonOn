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
    private Profile userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent currentIntent = getIntent();
        Bundle bundle = currentIntent.getExtras();
        String stylistID = bundle.getString("id");
        String email = bundle.getString("email");

        //get user profile
        api = new API();
        userProfile = api.getClientProfile(email);

        if(userProfile == null) {
            Toast.makeText(this, "Failed to re-fetch profile", Toast.LENGTH_LONG).show();
        }

        Offer[] offers = api.getStylistOffers(stylistID);
        stylist = api.getClientProfile(stylistID);
        Log.v("offerLength", "" + offers.length);

        loadProfile(offers);
    }

    private void loadProfile(Offer[] offers){
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
        details.setText("2 years experience - 0 miles away");
        bio.setText("Closure is the best programming language ever!");

        //todo: access stylist's ratings and comments
        double[] ratings = api.getAverageRatings(stylist.email);
        if(ratings != null){
            rateClean.setRating((float)ratings[0]);
            rateAccess.setRating((float)ratings[3]);
            rateFriend.setRating((float)ratings[1]);
            rateProfes.setRating((float)ratings[2]);

        }

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
            time.setText("" + offers[i].duration + " hrs");
            price.setText("$" + offers[i].price);
            newView.setTag(offers[i]);
            //todo: add image

            //FINALLY, USE INSERT POINT TO ADD INFLATED VIEW.
            insertPoint.addView(newView);
        }

    }
    public void styleButtonOnClick(View v) {

        // Create activity_profile Intent;
        Intent styleInfoIntent = new Intent(ProfileActivity.this, StyleInfoActivity.class);
        styleInfoIntent.putExtra("email", userProfile.email);
        styleInfoIntent.putExtra("stylist", stylist.email);
//        // Create activity_profile Intent;
        Offer offer = (Offer)(v.getTag());
        styleInfoIntent.putExtra("style", offer.style);
        styleInfoIntent.putExtra("duration", offer.duration);
        styleInfoIntent.putExtra("price", offer.price);
        styleInfoIntent.putExtra("offerID", offer.offerID);

        startActivity(styleInfoIntent);
    }
}
