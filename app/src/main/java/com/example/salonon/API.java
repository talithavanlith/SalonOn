package com.example.salonon;

public interface API {


    String createNewProfile(Profile profileToAddToDatabase);
    // Maybe change string array to a list of profile parameters.
    // Need to know what a profile should encapsulate first.
    // 
    /* Given an Profile object, adds the profile to the database. Returns a String to signify
    whether the adding to the database was successful or a failure. If the string is "Success"
    then it was successful. It not, the string is the error message to display */

    Profile loginToProfile(String emailAddress, String password);

    /* Given an email and password, returns the Profile information for a given account. */

    Profile[] stylistSearchForProfilesByLocation(String latitude, String longitude, int radius);

    Profile[] salonSearchForProfilesByLocation(String latitude, String longitude, int radius);

    Profile getClientProfile(int clientID);
    // Given a client id, return a Profile Object of the client's data.
}
