package com.example.salonon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class CreateAccountActivateStylist extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_activate_stylist);

    }
    public void doneButton(View v){
        Intent currentIntent = getIntent();
        Bundle bundle = currentIntent.getExtras();
        String email = bundle.getString("email");
        EditText bioView = (EditText)findViewById(R.id.bio);
        String bio = bioView.getText().toString();

        Offer[] offers = new Offer[0];
        CheckBox checkBox = (CheckBox)findViewById(R.id.style1);
        if(checkBox.isChecked()){
            offers = new Offer[1];
            EditText priceBox = (EditText)findViewById(R.id.style1price);
            EditText depositBox = (EditText)findViewById(R.id.style1deposit);
            EditText durationBox = (EditText)findViewById(R.id.style1duration);

            String price = priceBox.getText().toString();
            String deposit = priceBox.getText().toString();
            String duration = priceBox.getText().toString();

            offers[0] = new Offer(1, Integer.parseInt(price), Integer.parseInt(deposit), Integer.parseInt(duration));
        }
        if (new API().addStylist(email, bio, offers)){
            Toast.makeText(this, "Stylist account activated successfully", Toast.LENGTH_LONG).show();
            Log.v("Add-stylist", "success");
            Intent searchIntent = new Intent(CreateAccountActivateStylist.this, SearchActivity.class);
            searchIntent.putExtra("email", email);
            startActivity(searchIntent);
            finish();
        } else{
            Toast.makeText(this, "Error: Stylist account not activated", Toast.LENGTH_LONG).show();
            Log.v("Add-stylist", "failed");
        }
    }
}
