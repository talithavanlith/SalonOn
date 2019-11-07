package com.example.salonon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreateAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);
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

        if(checkEmail(email) != ""){
            //checkEmail will return a string explaining why the email is incorrect if it is
            Toast.makeText(this, checkEmail(email), Toast.LENGTH_LONG).show();
            return;
        }

        // Check matching passwords
        if (password.compareTo(confirmPassword) != 0) {
            Toast.makeText(this, "Passwords Do Not Match!", Toast.LENGTH_LONG).show();
            return;
        }

        // Go to next screen:
        Intent signUpContinueIntent = new Intent(CreateAccountActivity.this, CreateAccountSelectType.class);
        signUpContinueIntent.putExtra("first", first);
        signUpContinueIntent.putExtra("last", last);
        signUpContinueIntent.putExtra("email", email);
        signUpContinueIntent.putExtra("password", password);
        startActivity(signUpContinueIntent);
        finish();
    }

    public String checkEmail(String email){

        //check to see if it contains an @ symbol
        if(!email.contains("@")){
            return "Your email address needs an @ symbol";
        }

        //check to see if the extension is right (might want to add some more extensions or take this out idk)
        String[] extensions = {".co.nz", ".com.au", ".co.ca", ".com", ".co.us", ".co.uk", ".net", ".unc.edu"};
        Boolean extenRight = false;
        for(String ex: extensions){
            if(email.endsWith(ex)){
                extenRight = true;
            }
        }

        //check if the extension is an IP address
        if(email.contains("@[") && email.endsWith("]")){
            int i = email.indexOf("[");
            String ip = email.substring(i+1, email.length()-1);
            //regex from mdma at StackOverflow
            if(!(ip.matches("(([0-1]?[0-9]{1,2}\\.)|(2[0-4][0-9]\\.)|(25[0-5]\\.)){3}(([0-1]?[0-9]{1,2})|(2[0-4][0-9])|(25[0-5]))"))){

                return "Your email extension is incorrect";
            }
        }else if(!extenRight){
            return "Your email extension is incorrect";
        }

        return "";
    }

    public void haveAccountTextViewOnClick(View view) {
        // Takes you back to sign in activity.
        Intent signInIntent = new Intent(CreateAccountActivity.this, LoginActivity.class);
        startActivity(signInIntent);
        finish();
    }
}
