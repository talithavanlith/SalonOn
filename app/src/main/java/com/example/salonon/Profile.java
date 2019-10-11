package com.example.salonon;

import android.media.Image;

import androidx.annotation.Nullable;

public class Profile {
    // Encapsulated the data a Profile should hold, including:
    String typeOfProfile; // Whether the activity_profile is for a Client, Stylist, or Salon Owner
    String emailAddress; // Email address
    String password; // password
    String firstName; // First Name
    String lastName; // Last Name
    @Nullable Image profilePicture; // Profile Picture
    // In app settings. Including:
        Boolean shareContactInfoWhenBookingIsRequested; // Share Contact Info when booking is requested Y/N
        Boolean useMobileData; // Use Mobile Data Y/N
        Boolean allowNotifications; // Allow Notifications Y/N
    String phoneNumber; // Contact Phone Number
    String creditCardNumber; // Payment Info
    String bio; // Bio
    Booking [] arrayOfBookings;
    // -----
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
    //----
    // Stylist Profile will need:
    // Working Location
    // Gallery of saved pictures of work.
    // List of services the stylist performs as well as the prices for each service and the time to complete for each service
    // Years of Experience as a Stylist
    // Verified Status
    // Calendly Information
    // Current/Previous Booking info, including:
        // The name of the Client
        // Profile pic of the Client
        // Pointer to the Client's Profile
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
    // Aggregate Review Scores from all reviews
    // Reviews of Stylist. Reviews include:
        // Review Score
        // Review write up
        // Name of reviewer
        // Profile picture of reviewer
        // Date of appointment being reviewed
        // Time of appointment being reviewed
        // Service of appointment being reviewed


    // ----
    // Salon Owner Profile will need:
    // Address
    // Equipment available for use
    // Calendly Information
    // Current/Previous Booking info, including:
        // The name of the Client
        // Profile pic of the Client
        // Pointer to the Client's Profile
        // The name of the Stylist
        // Pointer to the Stylist's Profile
        // The date of the appointment
        // The time of the appointment
        // Status of the booking. Statuses include:
            // Pending
            // Completed
            // Canceled
        // Comments on the Booking.
        // Requested service of the booking.
        // Time to complete the service of the booking
        // Aggregate Review Scores from all reviews
        // Reviews of Salon. Reviews include:
        // Review Score in each of the following categories:
            // Cleanliness
            // Friendliness
            // Professionalism
            // Accessibility
        // Review write up
        // Name of reviewer
        // Profile picture of reviewer
        // Date of appointment being reviewed
        // Time of appointment being reviewed
        // Service of appointment being reviewed


    public Profile(String typeOfProfile, String emailAddress, String password, String firstName, String lastName, Image profilePicture, Boolean shareContactInfoWhenBookingIsRequested, Boolean useMobileData, Boolean allowNotifications, String phoneNumber, String creditCardNumber, String bio, Booking[] arrayOfBookings) {
        this.typeOfProfile = typeOfProfile;
        this.emailAddress = emailAddress;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        if (profilePicture != null) {
            this.profilePicture = profilePicture;
        }
        this.shareContactInfoWhenBookingIsRequested = shareContactInfoWhenBookingIsRequested;
        this.useMobileData = useMobileData;
        this.allowNotifications = allowNotifications;
        this.phoneNumber = phoneNumber;
        this.creditCardNumber = creditCardNumber;
        this.bio = bio;
        this.arrayOfBookings = arrayOfBookings;

    }

}
