package com.example.salonon;

import androidx.annotation.Nullable;


public class Booking {
        Profile stylist;
        Profile client;
        Profile salon;
        String date;
        String time;
        int offerID;
        String style;
        String category;
        double price;
        double deposit;
        double duration;

        //status
        String clientConfirm;
        String stylistConfirm;
        String salonConfirm;

        //not implemented yet
        String clientComment;
        String stylistComment;
        String salonComment;

    public Booking(Profile client, offer offer, @Nullable Profile salon, String date, String time) {
        // set stylist information
        this.client = client;
        this.stylist = new API().getClientProfile(offer.stylistEmail);
        this.salon = salon;
        this.duration = offer.duration;
        this.style = offer.style;
        this.category = offer.category;
        this.date = date;
        this.time = time;
        this.offerID = offer.offerID;
    }

    public void addStylistComment(String comment){
        this.stylistComment = comment;
    }
    public void addSalonComment(String comment){
        this.salonComment = comment;
    }
    public void addClientComment(String comment){
        this.clientComment = comment;
    }


}
