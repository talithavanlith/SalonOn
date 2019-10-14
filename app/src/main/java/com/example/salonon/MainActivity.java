package com.example.salonon;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        APIImpl api = new APIImpl();
//        api.getClientProfile(1);
//        String s = api.getAmenityByID(1);
//        Profile p = new Profile("Salon","testemail6@gmail.com","testPassword","firstname","lastname",null,null,null,null,null,null,"I'm good",null);
//        api.createNewProfile(p);
        api.loginToProfile("testemail6@gmail.com", "testPassword");
        setContentView(R.layout.activity_main);

    }

    public void clientButtonOnClick(View view) {
        Log.v("SalonOn", "I am a Client");
        Intent signInIntent = new Intent(MainActivity.this, SignInActivity.class);
        String userType = "Client";
        signInIntent.putExtra("userType", userType);
        startActivity(signInIntent);
    }

    public void stylistButtonOnClick(View view) {
        Log.v("SalonOn", "I am a Stylist");
        Intent signInIntent = new Intent(MainActivity.this, SignInActivity.class);
        String userType = "Stylist";
        signInIntent.putExtra("userType", userType);
        startActivity(signInIntent);
    }

    public void salonOwnerButtonOnClick(View view) {
        Log.v("SalonOn", "I am a Salon Owner");
        Intent signInIntent = new Intent(MainActivity.this, SignInActivity.class);
        String userType = "Salon Owner";
        signInIntent.putExtra("userType", userType);
        startActivity(signInIntent);
    }


}
