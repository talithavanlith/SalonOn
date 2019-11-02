package com.example.salonon;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivityOne extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_one);
    }

    public void imageButtonOnClick(View v) {
        Toast.makeText(this, "Feature Not Implemented yet", Toast.LENGTH_LONG).show();
    }

    public void signUpButtonOnClick(View v) {
        // get extras from passed intent:
        Intent currentIntent = getIntent();
        Bundle bundle = currentIntent.getExtras();
        String first = bundle.getString("first");
        String last = bundle.getString("last");
        String email = bundle.getString("email");
        String password = bundle.getString("password");

        // get data from edit texts:
        EditText addressEditText = findViewById(R.id.addressEditText);
        EditText cityEditText = findViewById(R.id.cityEditText);
        EditText stateEditText = findViewById(R.id.stateEditText);
        EditText zipEditText = findViewById(R.id.zipEditText);

        //currently doing nothing with this info
        String address = addressEditText.getText().toString();
        String city = cityEditText.getText().toString();
        String state = stateEditText.getText().toString();
        String zip = zipEditText.getText().toString();

        // create account
        API api = new API();
        Image image = null;
        Profile userProfile = new Profile(email, first,last, image,false, false, "none", "none", 0);

        if(api.createNewProfile(userProfile, password)) {
            Toast.makeText(this, "Account created successfully", Toast.LENGTH_LONG).show();
            Log.v("success", "Account created successfully");

            //start search intent
            Intent searchIntent = new Intent(SignUpActivityOne.this, SearchActivity.class);
            searchIntent.putExtra("email", email);
            startActivity(searchIntent);
            finish();
        } else {
            Toast.makeText(this, "Error, account not created.", Toast.LENGTH_LONG).show();
            Log.v("Profile", "Failed to Create User Profile!!!");
        }

    }

    public void haveAccountTextViewOnClick(View view) {
        // Takes you back to sign in activity.
        Intent signInIntent = new Intent(SignUpActivityOne.this, MainActivity.class);
        startActivity(signInIntent);
        finish();
    }
}
