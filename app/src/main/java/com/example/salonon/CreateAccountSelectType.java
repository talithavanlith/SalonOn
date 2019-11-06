package com.example.salonon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class CreateAccountSelectType extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account_type);
    }
    public void accountTypeButton(View v){
        Intent currentIntent = getIntent();
        Bundle bundle = currentIntent.getExtras();
        String first = bundle.getString("first");
        String last = bundle.getString("last");
        String email = bundle.getString("email");
        String password = bundle.getString("password");

        if (v == findViewById(R.id.client)){
            Image image = null;
            Profile userProfile = new Profile(email, first,last, image,false, false, "none", "none", 0);
            if(new API().createNewProfile(userProfile, password)) {
                Toast.makeText(this, "Account created successfully", Toast.LENGTH_LONG).show();
                Log.v("success", "Account created successfully");

                //start search intent
                Intent searchIntent = new Intent(CreateAccountSelectType.this, SearchActivity.class);
                searchIntent.putExtra("email", email);
                startActivity(searchIntent);
                finish();
            } else {
                Toast.makeText(this, "Error, account not created.", Toast.LENGTH_LONG).show();
                Log.v("Profile", "Failed to Create User Profile!!!");
            }
        } else if (v== findViewById(R.id.stylist)){
            Intent addressIntent = new Intent(CreateAccountSelectType.this, CreateAccountAddress.class);
            addressIntent.putExtra("first", first);
            addressIntent.putExtra("last", last);
            addressIntent.putExtra("email", email);
            addressIntent.putExtra("password", password);
            startActivity(addressIntent);
        } else {
            //salon
        }
    }

}
