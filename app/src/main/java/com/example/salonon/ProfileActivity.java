package com.example.salonon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Offer[] offers = new API().getStylistOffers("susie@mail.com");
        Log.v("offerLength", "" +offers.length);
    }

    public void styleButtonOnClick(View v) {
        Toast.makeText(this, "", Toast.LENGTH_LONG).show();
    }
}
