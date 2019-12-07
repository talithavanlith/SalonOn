package com.example.salonon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
    }

    public void searchNavButton(View v){
        Intent intent = new Intent(BookingsActivity.this, SearchActivity.class);
        intent.putExtra("email", clientID);
        startActivity(intent);
        finish();
    }
}