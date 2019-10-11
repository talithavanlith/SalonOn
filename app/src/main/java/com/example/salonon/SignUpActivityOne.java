package com.example.salonon;

import android.content.Intent;
import android.os.Bundle;
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
        String userType = bundle.getString("userType");
        String name = bundle.getString("name");
        String email = bundle.getString("email");

        // get data from edit texts:
        EditText phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        EditText cityEditText = findViewById(R.id.cityEditText);
        EditText stateEditText = findViewById(R.id.stateEditText);
        EditText zipEditText = findViewById(R.id.zipEditText);

        String phoneNumber = phoneNumberEditText.getText().toString();
        String city = phoneNumberEditText.getText().toString();
        String state = phoneNumberEditText.getText().toString();
        String zip = phoneNumberEditText.getText().toString();

        // Do I pass this account info to Talitha's search intent or login to the profile here?
        // For now, don't sign up here.
        Toast.makeText(this, "Screen Not Implemented Yet", Toast.LENGTH_LONG).show();
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
