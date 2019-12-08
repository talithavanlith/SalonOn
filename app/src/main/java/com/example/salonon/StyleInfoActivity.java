package com.example.salonon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class StyleInfoActivity extends AppCompatActivity {

    private API api;
    private Profile userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style_info);

        // get extras from passed intent:
        Intent currentIntent = getIntent();
        Bundle bundle = currentIntent.getExtras();
        String email = bundle.getString("email");

        api = new API();
        userProfile = api.getClientProfile(email);

        if(userProfile == null) {
            Toast.makeText(this, "Failed to re-fetch profile", Toast.LENGTH_LONG).show();
        }

        loadStyleTEST();

    }

    private void loadStyleTEST(){
        //ACCESS THE ELEMENTS IN THE INFLATED VIEW (THIS IS WHERE WE EDIT THEM)
        ImageView image = this.findViewById(R.id.imgProfile);
        TextView name = this.findViewById(R.id.txtStylistName);
        TextView details = this.findViewById(R.id.txtDetails);

        TextView styleName = this.findViewById(R.id.txtImagesOfWork2);

        //todo: update slideshow with images of the stylist's work

        TextView time = this.findViewById(R.id.txtTime);
        TextView price = this.findViewById(R.id.txtPrice);

        // set the details
        //todo: add image
        name.setText("Susie Kato");
        details.setText("10 years experience - 5 miles away");

        styleName.setText("Pixie cut");

        time.setText("2.0hrs");
        price.setText("$50.00");

    }

    public void bookNowButtonOnClick(View v) {
        // Create activity_profile Intent;
        Intent styleInfoIntent = new Intent(StyleInfoActivity.this, AvailabilityActivity.class);
        styleInfoIntent.putExtra("email", userProfile.email);
        // todo: below get the name of the style that is clicked (and maybe the stylist's email/id)
//        styleInfoIntent.putExtra("stylist", v.getStylistEmail());
//        styleInfoIntent.putExtra("style", v.getStyleID());
        startActivity(styleInfoIntent);

    }
}
