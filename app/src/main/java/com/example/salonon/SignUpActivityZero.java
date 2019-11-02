package com.example.salonon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivityZero extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_zero);
    }

    public void continueButtonOnClick(View view) {
        // Get Strings from edit texts:
        EditText nameEditText = findViewById(R.id.firstNameEditText);
        EditText lastNameEditText = findViewById(R.id.lastNameEditText);
        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        EditText confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);

        String first = nameEditText.getText().toString();
        String last = lastNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        // Check matching passwords
        if (password.compareTo(confirmPassword) != 0) {
            Toast.makeText(this, "Passwords Do Not Match!", Toast.LENGTH_LONG).show();
            return;
        }

        // Go to next screen:
        Intent signUpContinueIntent = new Intent(SignUpActivityZero.this, SignUpActivityOne.class);
        // Add profile fields to next intent:
        signUpContinueIntent.putExtra("first", first);
        signUpContinueIntent.putExtra("last", last);
        signUpContinueIntent.putExtra("email", email);
        signUpContinueIntent.putExtra("password", password);
        startActivity(signUpContinueIntent);
        finish();
    }

    public void haveAccountTextViewOnClick(View view) {
        // Takes you back to sign in activity.
        Intent signInIntent = new Intent(SignUpActivityZero.this, MainActivity.class);
        startActivity(signInIntent);
        finish();
    }
}
