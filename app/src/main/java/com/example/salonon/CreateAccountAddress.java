package com.example.salonon;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreateAccountAddress extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account_address);
    }

    public void imageButtonOnClick(View v) {
        Toast.makeText(this, "Feature Not Implemented yet", Toast.LENGTH_LONG).show();
    }

    public void signUpButtonOnClick(View v) {
        // get extras from passed intent:
        Intent currentIntent = getIntent();
        Bundle bundle = currentIntent.getExtras();
        String email = bundle.getString("email");
        String accountType = bundle.getString("accountType");

        // get data from edit texts:
        EditText addressEditText = findViewById(R.id.addressEditText);
        EditText cityEditText = findViewById(R.id.cityEditText);
        EditText stateEditText = findViewById(R.id.stateEditText);
        EditText zipEditText = findViewById(R.id.zipEditText);

        //get address info
        String address = addressEditText.getText().toString();
        String city = cityEditText.getText().toString();
        String state = stateEditText.getText().toString();
        String zip = zipEditText.getText().toString();

        //add location to account
        new API().addLocation(email, address, city, state, zip);



        //START PROFILE EDIT FOR STYLIST
        if (accountType.equals("stylist")){
            Intent activateStylistIntent = new Intent(CreateAccountAddress.this, CreateAccountActivateStylist.class);
            activateStylistIntent.putExtra("email", email);
            startActivity(activateStylistIntent);
            finish();
        }  else

        //START PROFILE EDIT FOR SALON
        if (accountType.equals("salon")){
            Intent activateSalonIntent = new Intent(CreateAccountAddress.this, CreateAccountActivateSalon.class);
            activateSalonIntent.putExtra("email", email);
            startActivity(activateSalonIntent);
            finish();
        }

    }

    public void haveAccountTextViewOnClick(View view) {
        // Takes you back to sign in activity.
        Intent signInIntent = new Intent(CreateAccountAddress.this, LoginActivity.class);
        startActivity(signInIntent);
        finish();
    }
}
