package com.example.salonon;

public class Offer {
    int offerID;
    String style;
    String category;
    double price;
    double deposit;
    double duration;
    String stylistEmail;

    public Offer(int offerID, String stylistEmail, String style, String category, double price, double deposit, double duration){
        this.offerID = offerID;
        this.style =style;
        this.category = category;
        this.price = price;
        this.deposit = deposit;
        this.duration = duration;
        this.stylistEmail = stylistEmail;
    }
}