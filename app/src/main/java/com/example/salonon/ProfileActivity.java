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

        Intent currentIntent = getIntent();
        Bundle bundle = currentIntent.getExtras();
        String email = bundle.getString("id");
        Offer[] offers = new API().getStylistOffers(email);
        Log.v("offerLength", "" +offers.length);
    }

    public void styleButtonOnClick(View v) {
        Toast.makeText(this, "", Toast.LENGTH_LONG).show();
    }
}
