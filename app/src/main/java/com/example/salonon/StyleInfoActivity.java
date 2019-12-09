package com.example.salonon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class StyleInfoActivity extends AppCompatActivity {

    private API api;
    private Profile stylist;
    private String offerID;
    private String style;
    private Double time;
    private Double price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style_info);

        // get extras from passed intent:
        Intent currentIntent = getIntent();
        Bundle bundle = currentIntent.getExtras();
        String email = bundle.getString("stylist");
        offerID = bundle.getString("offerID");
        style = bundle.getString("style");
        time = bundle.getDouble("duration");
        price = bundle.getDouble("price");

        api = new API();

        stylist = api.getClientProfile(email);

        loadStyle(style, time, price);

    }

    private void loadStyle(String style, Double time, Double price){
        //ACCESS THE ELEMENTS IN THE INFLATED VIEW (THIS IS WHERE WE EDIT THEM)
        ImageView image = this.findViewById(R.id.imgProfile);
        TextView name = this.findViewById(R.id.txtStylistName);
        TextView details = this.findViewById(R.id.txtDetails);

        TextView styleView = this.findViewById(R.id.txtImagesOfWork2);

        //todo: update slideshow with images of the stylist's work

        TextView timeView = this.findViewById(R.id.txtTime);
        TextView priceView = this.findViewById(R.id.txtPrice);

        // set the details
        //todo: add image
        name.setText(stylist.first + " " + stylist.last);
        details.setText("10 years experience - 5 miles away");

        styleView.setText(style);

        timeView.setText("" + time + " hrs");
        priceView.setText("$" + price);

    }

    public void bookNowButtonOnClick(View v) {

        // Create activity_profile Intent;
        Intent styleInfoIntent = new Intent(StyleInfoActivity.this, AvailabilityActivity.class);
        // todo: below get the name of the style that is clicked (and maybe the stylist's email/id)
        styleInfoIntent.putExtra("stylist", stylist.email);
//        // Create activity_profile Intent;
        styleInfoIntent.putExtra("style", style);
        styleInfoIntent.putExtra("duration", time);
        styleInfoIntent.putExtra("price", price);
        styleInfoIntent.putExtra("offerID", offerID);

        startActivity(styleInfoIntent);

    }
}
