package com.example.salonon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class BookingsActivity extends AppCompatActivity {

    String clientID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);

        Intent currentIntent = getIntent();
        Bundle bundle = currentIntent.getExtras();
        clientID = bundle.getString("email");
        API api = new API();

        //BOOKING OBJECTS TO FILL XML WITH
        Booking[] bookings = api.getClientBookings(clientID);
        LinearLayout insertPoint = (LinearLayout) findViewById(R.id.bookingslist);
        LayoutInflater inflater = getLayoutInflater();

        for (int i =0; i<bookings.length; i++){
            View v = inflater.inflate(R.layout.booking, insertPoint, false);
            TextView styleName = v.findViewById(R.id.stylename);
            TextView stylistName = v.findViewById(R.id.stylistname);
            TextView salonName = v.findViewById(R.id.salonname);
            Button bookingButton = v.findViewById(R.id.bookingstatus);
            bookingButton.setTag(bookings[i].bookingID);
            styleName.setText(bookings[i].style);
            Log.v("style", bookings[i].style);
            stylistName.setText("Stylist: "+bookings[i].stylist.first);
            salonName.setText("Salon: MySalon");
            insertPoint.addView(v);
        }
    }

    public void searchNavButton(View v){
        Intent intent = new Intent(BookingsActivity.this, SearchActivity.class);
        intent.putExtra("email", clientID);
        startActivity(intent);
        finish();
    }
    public void bookingStatusButton(View v){
        int bid = (int)v.getTag();
    }
}