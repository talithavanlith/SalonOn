package com.example.salonon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class CreateAccountActivateSalon extends AppCompatActivity {

    final int MY_PERMISSIONS_REQUEST =2;
    Bitmap photo = null;
    API api = new API();
    ArrayList<CheckBox> options = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_activate_salon);


        //*****DYNAMICALLY ADD AMENITIES VALUES TO XML******

        //Integer is amenity ID, String is amenity name
        Map<Integer, String> amenities = api.getAllAmentities();

        LinearLayout insertPoint = (LinearLayout) findViewById(R.id.amenitylist);
        LayoutInflater inflater = getLayoutInflater();

        //GET INFO FROM AMENITY LIST
        for (Map.Entry<Integer, String> entry : amenities.entrySet() ) {

            //CREATE A INFLATED VIEW BY GIVING REFERENCE TO CHILD FILE (amenity.xml) AND FUTURE PARENT VIEW (insertPoint)
            View v = inflater.inflate(R.layout.amenity, insertPoint, false);

            //ACCESS THE ELEMENTS IN THE INFLATED VIEW (THIS IS WHERE WE EDIT THEM)
            CheckBox check = v.findViewById(R.id.amencheck);
            TextView name = v.findViewById(R.id.amentext);
            check.setTag(entry.getKey());
            name.setText(entry.getValue());

            //KEEPING A REFERENCE TO CHECKBOXES
            options.add(check);

            //FINALLY, USE INSERT POINT TO ADD INFLATED VIEW.
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

    //GRABS USER INFO FROM VIEWS AND ATTEMPTS TO ACTIVATE SALON ACCOUNT AND UPLOAD PROFILE PHOTO
    public void doneButton(View v){
        //GET EMAIL AND BIO VIEW
        Intent currentIntent = getIntent();
        Bundle bundle = currentIntent.getExtras();
        String email = bundle.getString("email");
        EditText bioView = findViewById(R.id.bio);
        String bio = bioView.getText().toString();

        ArrayList<Integer> checked = new ArrayList<>();
        for (int i=0; i<options.size(); i++){
            if (options.get(i).isChecked()){
                checked.add((Integer) options.get(i).getTag());
            }
        }
        int[] amenities = new int[checked.size()];
        for (int i=0; i< amenities.length; i++){
            amenities[i] = checked.get(i);
        }

        boolean salonStatus = api.addSalon(email, bio, amenities);
        boolean photoStatus = api.addProfilePic(email, photo);

        if (salonStatus && photoStatus){
            Toast.makeText(this, "Salon account activated successfully", Toast.LENGTH_LONG).show();
            Intent searchIntent = new Intent(CreateAccountActivateSalon.this, BookingsActivity.class);
            searchIntent.putExtra("email", email);
            startActivity(searchIntent);
            finish();
        }
    }
}
