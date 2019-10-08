package com.example.salonon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
    }

    public void submitButtonOnClick(View view) {
        // get email and password from edit text. Call Login with that.
        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        API api = new APIImpl();
        Profile userProfile = api.loginToProfile(email, password);
        if (userProfile != null) {
            // Now Open up the home screen intent and pass the user profile.
            Toast.makeText(this, "Login Successful.", Toast.LENGTH_LONG).show();
            Log.v("SalonOn", "Opening home screen of user");
        } else {
            Toast.makeText(this, "Sorry, this email or password is invalid. Please try again.", Toast.LENGTH_LONG).show();
        }

    }

    public void newUserOnClick(View view) {
        Intent currentIntent = getIntent();
        Bundle bundle = currentIntent.getExtras();
        String userType;
        if(bundle != null) {
            userType = bundle.getString("userType");
        } else {
            userType = null;
        }
        // Now I can pass userType to sign in if we need it.
        Intent signUpIntent = new Intent(SignInActivity.this, SignUpActivity.class);
        signUpIntent.putExtra("userType", userType);
        startActivity(signUpIntent);
    }
}
