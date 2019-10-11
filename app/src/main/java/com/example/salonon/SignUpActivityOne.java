package com.example.salonon;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
        String userType = bundle.getString("userType");
        String name = bundle.getString("name");
        String email = bundle.getString("email");
        String password = bundle.getString("password");

        // get data from edit texts:
        EditText phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        EditText cityEditText = findViewById(R.id.cityEditText);
        EditText stateEditText = findViewById(R.id.stateEditText);
        EditText zipEditText = findViewById(R.id.zipEditText);

        String phoneNumber = phoneNumberEditText.getText().toString();
        String city = cityEditText.getText().toString();
        String state = stateEditText.getText().toString();
        String zip = zipEditText.getText().toString();

        // Do I pass this account info to Talitha's activity_search intent or login to the profile here?
        // For now, don't sign up here.
        Toast.makeText(this, "Account Creation Successful!", Toast.LENGTH_LONG).show();

        // create account
        API api = new APIImpl();

        Image image = null;
        String creditCardNumber = "00000002";
        String bio = "I am a " + userType;
        Booking[] bookings = new Booking[1];
        Profile userProfile = makeProfile(userType, email, password, name, image,true, true, true, phoneNumber, creditCardNumber, bio, bookings);

        api.createNewProfile(userProfile);

        // Now that profile is created, go to search intent:
        Intent searchIntent = new Intent(SignUpActivityOne.this, SearchActivity.class);
        searchIntent.putExtra("email", email);
        searchIntent.putExtra("password", password);

        startActivity(searchIntent);

    }

    private Profile makeProfile(String userType, String email, String password, String name, @Nullable Image image, Boolean shareContactInfoWhenBookingIsRequested, Boolean useMobileData, Boolean allowNotifications, String phoneNumber, String creditCardNumber, String bio, Booking[] arrayOfBookings) {
        String firstName = name.split(" ")[0];
        // String lastName = name.split(" ")[1];
        String lastName = firstName;
        Profile userProfile = new Profile(userType, email, password, firstName, lastName, image, shareContactInfoWhenBookingIsRequested, useMobileData, allowNotifications, phoneNumber, creditCardNumber, bio, arrayOfBookings);
        return userProfile;
    }

    public void haveAccountTextViewOnClick(View view) {
        // Takes you back to sign in activity.

        // Get userType from Intent;
        Intent currentIntent = getIntent();
        Bundle bundle = currentIntent.getExtras();
        String userType;
        if(bundle != null) {
            userType = bundle.getString("userType");
        } else {
            userType = null;
        }

        Intent signInIntent = new Intent(SignUpActivityOne.this, SignInActivity.class);
        signInIntent.putExtra("userType", userType);
        startActivity(signInIntent);
    }
}
