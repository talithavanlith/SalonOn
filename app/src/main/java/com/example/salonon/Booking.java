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
        Image salonProfilePicture; // Salon activity_profile Picture.
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

    public Booking(Profile clientProfile, Profile stylistProfile, @Nullable Profile salonProfile, String dateOfAppointment, String timeOfAppointment, @Nullable String clientCommentsOnBooking, @Nullable String stylistCommentsOnBooking, @Nullable String salonCommentsOnBooking, String requestedService) {
        // set stylist information
        this.stylistName = stylistProfile.firstName + stylistProfile.lastName;
        this.stylistProfilePicture = stylistProfile.profilePicture;
        this.stylistProfile = stylistProfile;
        this.timeToCompleteService = null; // TODO Implement a stylist Profile that extends Profile which has a timeToCompleteService for each service offered.

        // set client information
        this.clientName = clientProfile.firstName + clientProfile.lastName;
        this.clientProfilePicture = clientProfile.profilePicture;
        this.clientProfile = clientProfile;


        if(salonProfile != null) {
            // set salon information
            this.salonName = null; //TODO implement a Salon Profile that extends Profile with a salonName field.
            this.salonAddress = null; // TODO implement a Salon Profile that extends Profile with a salonAddress field.
            this.salonProfilePicture = salonProfile.profilePicture;
            this.salonProfile = salonProfile;
        }

        // General, appointment specific information:
        this.bookingStatus = BookingStatus.PENDING; // Booking is Pending when first initialized.

        if(dateOfAppointment != null) {
            this.dateOfAppointment = dateOfAppointment;
        } else {
            this.dateOfAppointment = "January 1st, 1970";
        }

        if(timeOfAppointment != null) {
            this.timeOfAppointment = timeOfAppointment;
        } else {
            this.timeOfAppointment = "12:00";
        }

        if (clientCommentsOnBooking != null) {
            this.clientCommentsOnBooking = clientCommentsOnBooking;
        } else {
            this.clientCommentsOnBooking = "No Comment";
        }

        if (stylistCommentsOnBooking != null) {
            this.stylistCommentsOnBooking = stylistCommentsOnBooking;
        } else {
            this.stylistCommentsOnBooking = "No Comment";
        }

        if(salonCommentsOnBooking != null) {
            this.salonCommentsOnBooking = salonCommentsOnBooking;
        } else {
            this.salonCommentsOnBooking = "No Comment";
        }


    }

    public void addSalonProfileToBooking(Profile salonProfile, @Nullable String salonCommentsOnBooking) {
        // set salon information
        this.salonName = null; //TODO implement a Salon Profile that extends Profile with a salonName field.
        this.salonAddress = null; // TODO implement a Salon Profile that extends Profile with a salonAddress field.
        this.salonProfilePicture = salonProfile.profilePicture;
        this.salonProfile = salonProfile;

        if(salonCommentsOnBooking != null) {
            this.salonCommentsOnBooking = salonCommentsOnBooking;
        } else {
            this.salonCommentsOnBooking = "No Comment";
        }
    }


}
