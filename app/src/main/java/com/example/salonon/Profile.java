package com.example.salonon;

import android.graphics.Bitmap;
import android.media.Image;

public class Profile {
    String email;
    String first;
    String last;
    boolean isStylist;
    boolean isSalon;
    String salonBio;
    String stylistBio;
    double salonRate;
    Bitmap image;
    String[] addresses;
    int[] zips; //indexes match address
    Offer[] offers; //null if not a stylist
    double distance;

    public Profile(String email, String first, String last, Bitmap image, boolean isStylist, boolean isSalon,
                   String salonBio, String stylistBio, double salonRate){
        this.email = email;
        this.first = first;
        this.last = last;
        this.isStylist = isStylist;
        this.isSalon = isSalon;
        this.salonRate = salonRate;
        this.salonBio = "none";
        this.stylistBio = "none";

    }
}
