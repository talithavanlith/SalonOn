package com.example.salonon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AvailabilityActivity extends AppCompatActivity {

    private API api;
    private Profile userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availablity);

        // get extras from passed intent:
        Intent currentIntent = getIntent();
        Bundle bundle = currentIntent.getExtras();
        String email = bundle.getString("email");

        api = new API();
        userProfile = api.getClientProfile(email);

        if(userProfile == null) {
            Toast.makeText(this, "Failed to re-fetch profile", Toast.LENGTH_LONG).show();
        }

        loadStyleTEST();

    }

    private void loadStyleTEST(){
        //ACCESS THE ELEMENTS IN THE INFLATED VIEW (THIS IS WHERE WE EDIT THEM)
        ImageView image = this.findViewById(R.id.imgProfile);
        TextView name = this.findViewById(R.id.txtStylistName);

        TextView styleName = this.findViewById(R.id.txtStyle);

        TextView time = this.findViewById(R.id.txtTime);
        TextView price = this.findViewById(R.id.txtPrice);

        // set the details
        //todo: add image
        name.setText("Susie Kato");

        styleName.setText("Pixie cut");

        time.setText("2.0hrs");
        price.setText("$50.00");

    }

    public void requestButtonOnClick(View v) {
        // Create activity_profile Intent;
        Intent bookingIntent = new Intent(AvailabilityActivity.this, BookingsActivity.class);
        bookingIntent.putExtra("email", userProfile.email);
        // todo: below get the name of the style/stylist/date/time/notes
//        bookingIntent.putExtra("stylist", v.getStylistEmail());
//        bookingIntent.putExtra("style", v.getStyleID());
//        bookingIntent.putExtra("date", v.getDate());
//        bookingIntent.putExtra("time", v.getTime());
//        bookingIntent.putExtra("comment", v.getComment());
        startActivity(bookingIntent);

    }
}
