package com.example.salonon;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
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
        email = email.toLowerCase();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        if(checkEmail(email) != ""){
            //checkEmail will return a string explaining why the email is incorrect if it is
            Toast.makeText(this, checkEmail(email), Toast.LENGTH_LONG).show();
            return;
        }

        //CHECK IS USER ALREADY EXISTS
        if (new API().getClientProfile(email) != null){
            Toast.makeText(this, "Email already exists", Toast.LENGTH_LONG).show();
            return;

        }

        // CHECK MATCHING PASSWORDS
        if (password.compareTo(confirmPassword) != 0) {
            Toast.makeText(this, "Passwords Do Not Match!", Toast.LENGTH_LONG).show();
            return;
        }

        //ATTEMPT CREATE ACCOUNT
        Bitmap image = null;
        Profile userProfile = new Profile(email, first,last, image,false, false, "none", "none", 0);
        if(new API().createNewProfile(userProfile, password)) {
            Toast.makeText(this, "Account created successfully", Toast.LENGTH_LONG).show();
            Log.v("success", "Account created successfully");

            //start account type intent
            Intent accountTypeIntent = new Intent(CreateAccountActivity.this, CreateAccountSelectType.class);
            accountTypeIntent.putExtra("email", email);
            startActivity(accountTypeIntent);
            finish();
        } else {
            Toast.makeText(this, "Error, account not created.", Toast.LENGTH_LONG).show();
            Log.v("Profile", "Failed to Create User Profile!!!");
        }
    }

    //ALREADY HAVE ACCOUNT
    public void haveAccountTextViewOnClick(View view) {
        // Takes you back to sign in activity.
        Intent signInIntent = new Intent(CreateAccountActivity.this, LoginActivity.class);
        startActivity(signInIntent);
        finish();
    }

    //HELPER EMAIL FUNCTIONS
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
        //check if the mailbox is correct
        if(checkMailbox(email) != "") {
            return checkMailbox(email);
        }
        // check if the domain name is correct
        if(checkDomain(email) != "") {
            return checkDomain(email);
        }
        return "";
    }


    public static String checkMailbox(String e) {
        int i = e.indexOf("@");
        String mailBox = e.substring(0,i);
        int lastChar = mailBox.length() -1;

        if(mailBox.matches("[A-Za-z0-9\\.\\-\\_]*")){

            //check that first value isn't .-_
            if(mailBox.charAt(0) == '.' || mailBox.charAt(0) == '-' || mailBox.charAt(0) == '_'){
                return "Mailbox cannot start with a symbol";
            }

            //check that last value isn't .-_
            if(mailBox.charAt(lastChar) == '.' || mailBox.charAt(lastChar) == '-' || mailBox.charAt(lastChar) == '_'){
                return "Mailbox cannot end with a symbol";
            }

            //check for double ._- in a row
            for(int j =0; j <= lastChar; j++){
                char ch = mailBox.charAt(j);
                if( ch == '.' || ch == '-' || ch == '_'){
                    char nextCh = mailBox.charAt(j+1);
                    if(nextCh == '.' || nextCh == '-' || nextCh == '_'){
                        return "Mailbox cannot have more than one symbol in a row";
                    }
                }
            }
            return "";
        }
        return "Mailbox contains invalid symbols";
    }

    public static String checkDomain(String e) {
        int i = e.indexOf("@");
        String domain = e.substring(i+1);
        if(domain.startsWith("[")&& e.endsWith("]")){
            return "";
        }
        if(domain.matches("[A-Za-z0-9\\.]*")){
            //check for .. or first char being a dot
            if(domain.contains("..") || domain.charAt(0) == '.'){
                return "Domain cannot start with a . or have ..";
            }
            return "";
        }
        return "Domain contains invalid symbols";
    }
}
