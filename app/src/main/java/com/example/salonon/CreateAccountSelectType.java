package com.example.salonon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
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
        String email = bundle.getString("email");

        //USER IS CLIENT
        if (v == findViewById(R.id.client)){
            Intent addressIntent = new Intent(CreateAccountSelectType.this, SearchActivity.class);
            addressIntent.putExtra("email", email);
            startActivity(addressIntent);
            //USER IS STYLIST
        } else if (v== findViewById(R.id.stylist)){
            Intent addressIntent = new Intent(CreateAccountSelectType.this, CreateAccountAddress.class);
            addressIntent.putExtra("email", email);
            addressIntent.putExtra("accountType", "stylist");
            startActivity(addressIntent);

            //USER IS SALON
        } else {
            Intent addressIntent = new Intent(CreateAccountSelectType.this, CreateAccountAddress.class);
            addressIntent.putExtra("email", email);
            addressIntent.putExtra("accountType", "salon");
            startActivity(addressIntent);
        }
    }

}
