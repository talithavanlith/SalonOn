package com.example.salonon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AvailabilityActivity extends AppCompatActivity {

    private API api;
    private Profile stylist;
    private int offerID;
    private String style;
    private Double time;
    private Double price;
    private String requestedDate;
    private String requestedTime;
    private String comments;
    private Profile userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availablity);

        // get extras from passed intent:
        Intent currentIntent = getIntent();
        Bundle bundle = currentIntent.getExtras();
        String stylistID = bundle.getString("stylist");
        offerID = bundle.getInt("offerID");
        style = bundle.getString("style");
        time = bundle.getDouble("duration");
        price = bundle.getDouble("price");
        String email = bundle.getString("email");

        api = new API();
        userProfile = api.getClientProfile(email);

        if(userProfile == null) {
            Toast.makeText(this, "Failed to re-fetch profile", Toast.LENGTH_LONG).show();
        }

        stylist = api.getClientProfile(stylistID);

        loadInfo();
    }

    private void loadInfo(){
        //ACCESS THE ELEMENTS IN THE INFLATED VIEW (THIS IS WHERE WE EDIT THEM)
        ImageView image = this.findViewById(R.id.imgProfile);
        TextView name = this.findViewById(R.id.txtStylistName);

        TextView styleName = this.findViewById(R.id.txtStyle);

        TextView timeView = this.findViewById(R.id.txtTime);
        TextView priceView = this.findViewById(R.id.txtPrice);

        // set the details
        //todo: add image
        name.setText(stylist.first + " " + stylist.last);

        styleName.setText(style);

        timeView.setText(""+ time +" hrs");
        priceView.setText("$" + price);

    }

    public void requestButtonOnClick(View view) {
        //ACCESS THE ELEMENTS IN THE INFLATED VIEW (THIS IS WHERE WE EDIT THEM)

        EditText hourInput = findViewById(R.id.hourEditText);
        EditText minuteInput = findViewById(R.id.minuteEditText);
        EditText ampmInput = findViewById(R.id.ampmEditText);
        EditText dateInput = findViewById(R.id.dateEditText);
        EditText commentInput = findViewById(R.id.commentEditText);

        Integer hourIN = Integer.parseInt(hourInput.getText().toString());
        Integer minuteIN = Integer.parseInt(minuteInput.getText().toString());
        String ampmIN = ampmInput.getText().toString();
        String dateIN = dateInput.getText().toString();
        String commentIN = commentInput.getText().toString();

        if(hourIN > 12 || hourIN < 1){
            Toast.makeText(this, "Please enter an hour value between 1 and 12", Toast.LENGTH_LONG).show();
            return;
        }

        if(minuteIN > 59 || minuteIN < 0){
            Toast.makeText(this, "Please enter a minute value between 0 and 60", Toast.LENGTH_LONG).show();
            return;
        }

        if(!(ampmIN.equals("am") || ampmIN.equals("pm") || ampmIN.equals("AM") || ampmIN.equals("PM"))){
            Toast.makeText(this, "Please enter either am/pm into the am/pm field", Toast.LENGTH_LONG).show();
            return;
        }

        requestedTime = hourIN + ":" + minuteIN + " " + ampmIN;

        if(checkDate(dateIN) != ""){
            //checkEmail will return a string explaining why the email is incorrect if it is
            Toast.makeText(this, checkDate(dateIN), Toast.LENGTH_LONG).show();
            return;
        }

        comments = commentIN;

        //todo: create booking on clientID
        //start account type intent
        Intent bookingIntent = new Intent(AvailabilityActivity.this, BookingsActivity.class);
        bookingIntent.putExtra("stylist", stylist.email);
        bookingIntent.putExtra("time", requestedTime);
        bookingIntent.putExtra("date", requestedDate);
        bookingIntent.putExtra("comment", comments);
        bookingIntent.putExtra("offerID", offerID);
        bookingIntent.putExtra("email", userProfile.email);
        startActivity(bookingIntent);

    }

    private String checkDate(String dateToCheck){

        // SEPARATE DATE **************
        char separator = 'n';
        int[] count = new int[2];

        // check separator
        for (int i = 0; i < dateToCheck.length(); i++) {
            char ch = dateToCheck.charAt(i);

            if(ch == '-' || ch == '/'){

                // if separator isn't specified yet
                if(separator == 'n'){
                    separator = ch;
                    count[0] = i;

                    // if separators are the same and there
                    // have only been one instance so far
                }else if(separator == ch && count[1] == 0){
                    count[1] = i;

                }else{
                    return "INVALID DATE: Incorrect separators.";
                }

            }
        }

        // if there are not exactly 2 separators
        if(count[1] == 0){
            return "INVALID DATE: Incorrect date format.";
        }

        String month = dateToCheck.substring(0, count[0]);
        String day = dateToCheck.substring(count[0] + 1, count[1]);
        String year = dateToCheck.substring(count[1] + 1);

        // VALIDIFY YEAR ****************

        int yearInt;

        if(year.matches("[0-9]+") && (year.length() == 2 || year.length() == 4)){
            yearInt = Integer.parseInt(year);
        }else{
            return "INVALID DATE: Year is not a four or two digit number.";
        }

        if (yearInt < 50){
            yearInt = 2000 + yearInt;
        }

        if(yearInt > 2022 || yearInt < 2019){
            return "INVALID DATE: Year out of range.";
        }

        year = Integer.toString(yearInt);


        // VALIDIFY MONTH ******************

        if (month.length() == 1){
            month = '0' + month;

        }else if (month.length() == 3){

            Character l1 = new Character(month.charAt(0));
            Character l2 = new Character(month.charAt(1));
            Character l3 = new Character(month.charAt(2));

            //checking if all lower case, all upper case or if it's capitalized
            if((Character.isUpperCase(l1) && Character.isUpperCase(l2) && Character.isUpperCase(l3))
                    || (Character.isLowerCase(l1) && Character.isLowerCase(l2) && Character.isLowerCase(l3))
                    || (Character.isUpperCase(l1) && Character.isLowerCase(l2) && Character.isLowerCase(l3))){

                char m1 = Character.toUpperCase(l1);
                char m2 = Character.toLowerCase(l2);
                char m3 = Character.toLowerCase(l3);

                month = Character.toString(m1) + Character.toString(m2) + Character.toString(m3);

            }else{
                return "INVALID DATE: Incorrect month format.";
            }

        }else if (month.length() != 2){
            return "INVALID DATE: Incorrect month format.";
        }

        // check values are valid and make them uniform
        switch (month) {
            case "01": case "Jan":
                month = "January";
                break;
            case "02": case "Feb":
                month = "Feb";
                break;
            case "03": case "Mar":
                month = "Mar";
                break;
            case "04": case "Apr":
                month = "Apr";
                break;
            case "05": case "May":
                month = "May";
                break;
            case "06": case "Jun":
                month = "Jun";
                break;
            case "07": case "Jul":
                month = "Jul";
                break;
            case "08": case "Aug":
                month = "Aug";
                break;
            case "09": case "Sep":
                month = "Sep";
                break;
            case "10": case "Oct":
                month = "Oct";
                break;
            case "11": case "Nov":
                month = "Nov";
                break;
            case "12": case "Dec":
                month = "Dec";
                break;
            default:
                System.out.println(dateToCheck + " - ");
                return "INVALID DATE: Month provided does not exist.";
        }

        // VALIDIFY DATE *****************
        int dayInt;

        if(day.matches("[0-9]+") && (day.length() <= 2)){
            dayInt = Integer.parseInt(day);
        }else{
            return "INVALID DATE: Day provided must be a 1/2 digit number";
        }

        if(month == "Sep" || month == "Apr" || month == "Jun" || month == "Nov"){

            if(dayInt > 30 || dayInt < 1){
                return "INVALID DATE: Day provided does not exist.";
            }
        }else if(month == "Feb"){
            Integer intY = Integer.parseInt(year);

            //if it is a leap year
            if(intY % 4 == 0 && !(intY % 100 == 0 && intY % 400 != 0)){
                if(dayInt > 29 || dayInt < 1){
                    return "INVALID DATE: Day provided does not exist.";
                }
                // if its not a leap year
            }else if(dayInt > 28 || dayInt < 1){
                return "INVALID DATE: Day provided does not exist.";
            }

        }else if(dayInt > 31 || dayInt < 1){
            return "INVALID DATE: Day provided does not exist.";
        }

        if(dayInt < 10){
            day = "0" + Integer.toString(dayInt);
        }else{
            day = Integer.toString(dayInt);
        }


        requestedDate = month + " " + day + " " + year;
        return "";
    }
}
