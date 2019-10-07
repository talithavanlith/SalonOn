package com.example.salonon;

import android.media.Image;

import androidx.annotation.Nullable;

public class Booking {
    enum BookingStatus {
        PENDING, COMPLETED, CANCELED
    }
    // Current/Previous Booking info, including:
        String stylistName; // The name of the Stylist
        Image stylistProfilePicture; // Profile pic of the Stylist
        Profile stylistProfile; // Pointer to the Stylist's Profile
        String clientName; // The name of the Client
        Image clientProfilePicture; // Profile pic of the Client
        Profile clientProfile; // Pointer to the Client's Profile
        String salonName; // The name of the Salon
        String salonAddress; // The address of the Salon
        String salonProfilePicture; // Salon profile Picture.
        Profile salonProfile; // Pointer to the Salon's Profile
        String dateOfAppointment; // The date of the appointment
        String timeOfAppointment; // The time of the appointment
        BookingStatus bookingStatus; // Status of the booking. Statuses include:
            // Pending
            // Completed
            // Canceled
        String clientCommentsOnBooking; // Comments on the Booking.
        String stylistCommentsOnBooking;
        String salonCommentsOnBooking;

        String requestedService; // Requested service of the booking.
        String timeToCompleteService; // Time to complete the service of the booking

    public Booking(@Nullable Profile clientProfile, @Nullable Profile stylistProfile, @Nullable Profile salonProfile) {
        if(clientProfile != null) {
            // set client information

        }
        if(stylistProfile != null) {
            // set stylist information
        }
        if(salonProfile != null) {
            // set salon information
        }

    }


}
