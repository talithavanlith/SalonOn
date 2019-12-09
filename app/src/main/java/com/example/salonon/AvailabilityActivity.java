package com.example.salonon;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AvailabilityActivity extends AppCompatActivity {

    private API api;
    private Profile stylist;
    private String offerID;
    private String style;
    private Double time;
    private Double price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availablity);

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

        loadInfoTEST();
    }

    private void loadInfoTEST(){
        //ACCESS THE ELEMENTS IN THE INFLATED VIEW (THIS IS WHERE WE EDIT THEM)
        ImageView image = this.findViewById(R.id.imgProfile);
        TextView name = this.findViewById(R.id.txtStylistName);

        TextView styleName = this.findViewById(R.id.txtStyle);

        TextView timeView = this.findViewById(R.id.txtTime);
        TextView priceView = this.findViewById(R.id.txtPrice);

        // set the details
        //todo: add image
        name.setText(stylist.first + " " + stylist.last);

        styleName.setText(style);

        timeView.setText(""+ time +" hrs");
        priceView.setText("$" + price);

    }
}
