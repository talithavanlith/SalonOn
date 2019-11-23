package com.example.salonon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CreateAccountActivateStylist extends AppCompatActivity {

    final int MY_PERMISSIONS_REQUEST =2;
    Bitmap photo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_activate_stylist);

    }
    public void photoButton(View v){
        //check for permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST);
        } else {
            getphoto();
        }
    }

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
    public void getphoto() {
        //Create an Intent with action as ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent
        startActivityForResult(intent,1);

    }
    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK && requestCode==1) {
            //data.getData returns the content URI for the selected Image
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
    public void doneButton(View v){
        Intent currentIntent = getIntent();
        Bundle bundle = currentIntent.getExtras();
        String email = bundle.getString("email");
        EditText bioView = (EditText)findViewById(R.id.bio);
        String bio = bioView.getText().toString();

        Offer[] offers = new Offer[0];
        CheckBox checkBox = (CheckBox)findViewById(R.id.style1);

        //adds the checked styles to offers, currently only checking for first one.
        if(checkBox.isChecked()){
            offers = new Offer[1];
            EditText priceBox = (EditText)findViewById(R.id.style1price);
            EditText depositBox = (EditText)findViewById(R.id.style1deposit);
            EditText durationBox = (EditText)findViewById(R.id.style1duration);

            String price = priceBox.getText().toString();
            String deposit = depositBox.getText().toString();
            String duration = durationBox.getText().toString();

            offers[0] = new Offer(1, Integer.parseInt(price), Integer.parseInt(deposit), Integer.parseInt(duration));
        }

        //add the offers and bio info to account
        if (new API().addStylist(email, bio, offers)){
            new API().addProfilePic(email,photo);

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
