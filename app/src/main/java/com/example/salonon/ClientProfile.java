package com.example.salonon;

import android.media.Image;

public class ClientProfile extends Profile {

    // Client Profile will need:

    // Answers to the sign up questions provided by Ms. Kee.
    // Current/Previous Booking info, including:
        // The name of the Stylist
        // Profile pic of the Stylist
        // Pointer to the Stylist's Profile
        // The address of the Salon
        // Pointer to the Salon's Profile
        // The date of the appointment
        // The time of the appointment
        // Status of the booking. Statuses include:
            // Pending
            // Completed
            // Canceled
        // Comments on the Booking.
        // Requested service of the booking.
        // Time to complete the service of the booking


    public ClientProfile (String typeOfProfile, String emailAddress, String password, String firstName, String lastName, Image profilePicture, Boolean shareContactInfoWhenBookingIsRequested, Boolean useMobileData, Boolean allowNotifications, String phoneNumber, String creditCardNumber, String bio, Booking[] arrayOfBookings) {
        super(typeOfProfile, emailAddress, password, firstName, lastName, profilePicture, shareContactInfoWhenBookingIsRequested, useMobileData, allowNotifications, phoneNumber, creditCardNumber, bio, arrayOfBookings);
    }
}
