package com.example.salonon;

import androidx.annotation.Nullable;


public class Booking {
        int bookingID;
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
        boolean clientConfirm;
        boolean stylistConfirm;
        boolean salonConfirm;

        //not implemented yet
        String clientComment;
        String stylistComment;
        String salonComment;

        //CONSTRUCTOR FROM OBJECTS (USER)
    public Booking(Profile client, Offer offer, @Nullable Profile salon, String date, String time) {
        // set stylist information
        API api = new API();
        this.client = client;
        this.stylist = api.getClientProfile(offer.stylistEmail);
        this.salon = salon;
        this.duration = offer.duration;
        this.style = offer.style;
        this.category = offer.category;
        this.date = date;
        this.time = time;
    }

    //CONSTRUCTOR FROM JSON DATA (API)
    public Booking(int bid, String client, String stylist, String salon, String styleName, String category, double price, double duration,
                   String date, String time, boolean clientConfirm, boolean stylistConfirm, boolean salonConfirm) {
        // set stylist information
        API api = new API();
        this.client = api.getClientProfile(client);
        this.stylist = api.getClientProfile(stylist);
        this.salon = api.getClientProfile(salon);
        this.duration = duration;
        this.style = styleName;
        this.category = category;
        this.date = date;
        this.time = time;
        this.price = price;
        this.duration = duration;
        this.clientConfirm = clientConfirm;
        this.stylistConfirm = stylistConfirm;
        this.salonConfirm = salonConfirm;
        this.bookingID = bid;
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
