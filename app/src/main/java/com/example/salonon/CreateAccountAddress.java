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
        String first = bundle.getString("first");
        String last = bundle.getString("last");
        String email = bundle.getString("email");
        String password = bundle.getString("password");
        String accountType = bundle.getString("accountType");

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



        //start search intent
        Intent activateStylistIntent = new Intent(CreateAccountAddress.this, CreateAccountActivateStylist.class);
        activateStylistIntent.putExtra("email", email);
        startActivity(activateStylistIntent);
        finish();

    }

    public void haveAccountTextViewOnClick(View view) {
        // Takes you back to sign in activity.
        Intent signInIntent = new Intent(CreateAccountAddress.this, LoginActivity.class);
        startActivity(signInIntent);
        finish();
    }
}
