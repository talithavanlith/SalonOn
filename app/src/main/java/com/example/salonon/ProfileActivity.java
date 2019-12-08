package com.example.salonon;

import android.content.Intent;
import android.media.Rating;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private API api;
    private Profile userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        // get extras from passed intent:
        Intent currentIntent = getIntent();
        Bundle bundle = currentIntent.getExtras();
        String email = bundle.getString("id");
        Offer[] offers = new API().getStylistOffers(email);
        Log.v("offerLength", "" +offers.length);

        api = new API();
        userProfile = api.getClientProfile(email);

        if(userProfile == null) {
            Toast.makeText(this, "Failed to re-fetch profile", Toast.LENGTH_LONG).show();
        }

        //todo: get required profile from intent V
//        String stylistEmail = bundle.getString("stylist");
        //todo: get intended profile from database
//        Profile stylist = api.getStylistProfile(stylistEmail);

        //load profile with information
        loadProfileTEST();
    }



    private void loadProfileTEST(){
        //ACCESS THE ELEMENTS IN THE INFLATED VIEW (THIS IS WHERE WE EDIT THEM)
        ImageView image = this.findViewById(R.id.imgProfile);
        TextView name = this.findViewById(R.id.txtStylistName);
        TextView details = this.findViewById(R.id.txtDetails);

        TextView specStyle1 = this.findViewById(R.id.txtStyle1);
        TextView specStyle2 = this.findViewById(R.id.txtStyle2);
        TextView specStyle3 = this.findViewById(R.id.txtStyle3);

        RatingBar rateClean = this.findViewById(R.id.rateClean);
        RatingBar rateAccess = this.findViewById(R.id.rateAccess);
        RatingBar rateFriend = this.findViewById(R.id.rateFriend);
        RatingBar rateProfes = this.findViewById(R.id.rateProfes);

        TextView comment1 = this.findViewById(R.id.txtComment);
        TextView comment2 = this.findViewById(R.id.txtComment2);

        // set the details
        //todo: add image
        name.setText("Susie Kato");
        details.setText("10 years experience - 5 miles away");

        specStyle1.setText("Pixie cut");
        specStyle2.setText("long hair");
        specStyle3.setText("mid-length hair");

        rateClean.setRating(5);
        rateAccess.setRating(5);
        rateFriend.setRating(5);
        rateProfes.setRating(5);

        comment1.setText("\"brilliant\" - Jennie");
        comment2.setText("\"couldn't be better but smells funny\" - Eric");

        //todo: dynamically generate the styles available
        //*****DYNAMICALLY ADD search results to screen ******

        LinearLayout insertPoint = (LinearLayout) findViewById(R.id.styleOptions);
        LayoutInflater inflater = getLayoutInflater();

        // **** get all the styles that the stylist does from the database

        //temp values
        String[] styles = new String[]{"Pixie-cut", "Mid-Length", "Long Hair"};
        Double[] times = new Double[]{2.0, 3.5, 1.5};
        Double[] prices = new Double[]{50.00, 35.00, 70.00};


        //GET INFO FROM AMENITY LIST
        for (int i = 0; i < styles.length; i++) {

            //CREATE A INFLATED VIEW BY GIVING REFERENCE TO CHILD FILE (amenity.xml) AND FUTURE PARENT VIEW (insertPoint)
            View newView = inflater.inflate(R.layout.style, insertPoint, false);

            //ACCESS THE ELEMENTS IN THE INFLATED VIEW (THIS IS WHERE WE EDIT THEM)
            TextView styleName = newView.findViewById(R.id.txtStyleName);
            TextView time = newView.findViewById(R.id.txtTime);
            TextView price = newView.findViewById(R.id.txtPrice);
            ImageView styleImage = newView.findViewById(R.id.imgStyle);

            // set the details
            styleName.setText(styles[i]);
            time.setText(times[i] + "hr");
            price.setText("$" + prices[i]);
            //todo: add image

            //FINALLY, USE INSERT POINT TO ADD INFLATED VIEW.
            insertPoint.addView(newView);
        }


    }

    private void loadProfile(Profile stylist, View v){
        //ACCESS THE ELEMENTS IN THE INFLATED VIEW (THIS IS WHERE WE EDIT THEM)
        ImageView image = this.findViewById(R.id.imgProfile);
        TextView name = v.findViewById(R.id.txtStylistName);
        TextView details = v.findViewById(R.id.txtDetails);

        TextView specStyle1 = v.findViewById(R.id.txtStyle1);
        TextView specStyle2 = v.findViewById(R.id.txtStyle2);
        TextView specStyle3 = v.findViewById(R.id.txtStyle3);

        RatingBar rateClean = v.findViewById(R.id.rateClean);
        RatingBar rateAccess = v.findViewById(R.id.rateAccess);
        RatingBar rateFriend = v.findViewById(R.id.rateFriend);
        RatingBar rateProfes = v.findViewById(R.id.rateProfes);

        TextView comment1 = v.findViewById(R.id.txtComment);
        TextView comment2 = v.findViewById(R.id.txtComment2);

        // set the details
        //todo: add image
        name.setText(stylist.first + " " + stylist.last);
        details.setText(stylist.stylistBio);

//        specStyle1.setText(stylist.style1);
//        specStyle2.setText(stylist.style2);
//        specStyle3.setText(stylist.style3);

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

        // **** get all the styles that the stylist does from the database
//        Styles[] styles = stylist.getStyles();

        //GET INFO FROM AMENITY LIST
//        for (int i = 0; i < styles.length; i++) {
//
//            //CREATE A INFLATED VIEW BY GIVING REFERENCE TO CHILD FILE (amenity.xml) AND FUTURE PARENT VIEW (insertPoint)
//            View newView = inflater.inflate(R.layout.style, insertPoint, false);
//
//            //ACCESS THE ELEMENTS IN THE INFLATED VIEW (THIS IS WHERE WE EDIT THEM)
//            TextView styleName = newView.findViewById(R.id.txtStyleName);
//            TextView time = newView.findViewById(R.id.txtTime);
//            TextView price = newView.findViewById(R.id.txtPrice);
//            ImageView styleImage = newView.findViewById(R.id.imgStyle);
//
//            // set the details
//            styleName.setText(styles[i].name);
//            time.setText(styles[i].time);
//            price.setText("$" + styles[i].price);
//            //todo: add image
//
//            //FINALLY, USE INSERT POINT TO ADD INFLATED VIEW.
//            insertPoint.addView(newView);
//        }

    }

    public void styleButtonOnClick(View v) {
        // Create activity_profile Intent;
        Intent styleInfoIntent = new Intent(ProfileActivity.this, StyleInfoActivity.class);
        styleInfoIntent.putExtra("email", userProfile.email);
        // todo: below get the name of the style that is clicked (and maybe the stylist's email/id)
//        styleInfoIntent.putExtra("stylist", v.getStylistEmail());
//        styleInfoIntent.putExtra("style", v.getStyleID());
        startActivity(styleInfoIntent);
    }
}
