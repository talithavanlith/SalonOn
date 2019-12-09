package com.example.salonon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class CreateAccountActivateStylist extends AppCompatActivity {

    final int MY_PERMISSIONS_REQUEST =2;
    Bitmap photo = null;
    API api = new API();
    ArrayList<CheckBox> options = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_activate_stylist);

        JSONArray hairstyles = api.getAllHairstyles();

        LinearLayout insertPoint = (LinearLayout) findViewById(R.id.stylelist);
        LayoutInflater inflater = getLayoutInflater();

        for (int i=0; i< hairstyles.length(); i++){
            View v = inflater.inflate(R.layout.hairstyle, insertPoint, false);
            CheckBox check = v.findViewById(R.id.stylecheck);
            TextView name = v.findViewById(R.id.stylename);
            try {
                int hid = hairstyles.getJSONObject(i).getInt("hid");
                String cat = hairstyles.getJSONObject(i).getString("category");
                String styleName = hairstyles.getJSONObject(i).getString("styleName");
                check.setId(hid);
                name.setText(styleName);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            options.add(check);
            insertPoint.addView(v);
        }

    }

    //USER SELECTS PHOTO FROM NEW INTENT
    public void photoButton(View v){
        //check for permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST);
        } else {
            getphoto();
        }
    }

    //ASK FOR PHOTOS PERMISSION
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    getphoto();
                } else {
                    // permission denied
                    // tough noodles
                }
                return;
            }
        }
    }

    //START PHOTO SELECT INTENT
    public void getphoto() {
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");  // only components of type image are selected
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        startActivityForResult(intent,1);

    }

    //RESULT OF PHOTO INTENT
    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode==1) {
            Uri selectedImage = data.getData();
            ImageButton view = findViewById(R.id.profilepic);
            view.setImageURI(selectedImage);

            //convert uri to bitmap
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                photo = bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //GRABS USER INFO FROM VIEWS AND ATTEMPTS TO ACTIVATE STYLIST ACCOUNT AND UPLOAD PROFILE PHOTO
    public void doneButton(View v){
        //GET EMAIL AND BIO VIEW
        Intent currentIntent = getIntent();
        Bundle bundle = currentIntent.getExtras();
        String email = bundle.getString("email");
        EditText bioView = findViewById(R.id.bio);
        String bio = bioView.getText().toString();

        ArrayList<Offer> offers = new ArrayList<>();
        for (int i=0; i<options.size(); i++){
            CheckBox check = options.get(i);
            if (check.isChecked()){
                int hid = check.getId();
                LinearLayout parent = (LinearLayout)check.getParent();
                EditText priceText = parent.findViewWithTag("styleprice");
                double price = Double.parseDouble(priceText.getText().toString());
                EditText durationText = parent.findViewWithTag("styleduration");
                double duration = Double.parseDouble(durationText.getText().toString());
                Log.v("price", ""+price);
                Log.v("duration", ""+duration);
                offers.add(new Offer (hid, price, 0, duration));

            }
        }
        Offer[] offerArray = new Offer[offers.size()];
        for (int i=0; i< offers.size(); i++){
            offerArray[i] = offers.get(i);
        }


        //add the offers and bio info to account
        if (api.addStylist(email, bio, offerArray)){
            //api.addProfilePic(email, photo);
            Toast.makeText(this, "Stylist account activated successfully", Toast.LENGTH_LONG).show();
            Log.v("Add-stylist", "success");
            Intent searchIntent = new Intent(CreateAccountActivateStylist.this, BookingsActivity.class);
            searchIntent.putExtra("email", email);
            startActivity(searchIntent);
            finish();
        } else{
            Toast.makeText(this, "Error: Stylist account not activated", Toast.LENGTH_LONG).show();
            Log.v("Add-stylist", "failed");
        }
    }
}
