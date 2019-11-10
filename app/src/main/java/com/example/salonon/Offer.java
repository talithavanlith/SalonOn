package com.example.salonon;

public class Offer {
    int offerID;
    int styleID;
    String style;
    String category;
    double price;
    double deposit;
    double duration;
    String stylistEmail;

    //CONSTRUCTOR FOR BEING RETURNED FROM API
    public Offer(int offerID, int styleID, String stylistEmail, String style, String category, double price, double deposit, double duration){
        this.offerID = offerID;
        this.style =style;
        this.category = category;
        this.price = price;
        this.deposit = deposit;
        this.duration = duration;
        this.stylistEmail = stylistEmail;
    }

    //CONSTRUCTOR FOR BEING CREATED BY USER
    public Offer(int styleID, double price, double deposit, double duration){
        this.styleID = styleID;
        this.price = price;
        this.deposit = deposit;
        this.duration = duration;
    }
}
