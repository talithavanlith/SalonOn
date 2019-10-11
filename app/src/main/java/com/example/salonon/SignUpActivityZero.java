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
        // Get userType from Intent;
        Intent currentIntent = getIntent();
        Bundle bundle = currentIntent.getExtras();
        String userType;
        if(bundle != null) {
            userType = bundle.getString("userType");
        } else {
            userType = null;
        }

        // Get Strings from edit texts:
        EditText nameEditText = findViewById(R.id.nameEditText);
        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        EditText confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);

        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        // Make sure passwords are not different from password and confirmPassword Strings
        if (password.compareTo(confirmPassword) != 0) {
            // passwords do not match!
            Toast.makeText(this, "Passwords Do Not Match! Please confirm your password.", Toast.LENGTH_LONG).show();
            return;
        }

        // else:
        // Go to next screen:
        Intent signUpContinueIntent = new Intent(SignUpActivityZero.this, SignUpActivityOne.class);
        // Add profile fields to intent:
        signUpContinueIntent.putExtra("userType", userType);
        signUpContinueIntent.putExtra("name", name);
        signUpContinueIntent.putExtra("email", email);
        // signUpContinueIntent.putExtra("password", password); // TODO Hash Password before I send it through to another Intent.
        startActivity(signUpContinueIntent);
        Toast.makeText(this, "Intent Not Implemented Yet", Toast.LENGTH_LONG).show(); // TODO Remove this Toast when intent has content.
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

        Intent signInIntent = new Intent(SignUpActivityZero.this, SignInActivity.class);
        signInIntent.putExtra("userType", userType);
        startActivity(signInIntent);
    }
}
