package com.example.salonon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

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

        API api = new API();
        Profile userProfile = api.login(email, password);
        if (userProfile != null) {
            Toast.makeText(this, "Login Successful.", Toast.LENGTH_LONG).show();
            Log.v("SalonOn", "Login successful");

            // TODO make Profile Serializable or Parsable
            //for now, passing account id (email)
            Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
            searchIntent.putExtra("email", email);
            startActivity(searchIntent);
            finish();
        } else {
            Toast.makeText(this, "Sorry, this email or password is invalid. Please try again.", Toast.LENGTH_LONG).show();
        }
    }

    public void newUserOnClick(View view) {
        Intent signUpIntent = new Intent(MainActivity.this, SignUpActivityZero.class);
        startActivity(signUpIntent);
    }
}
