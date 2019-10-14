package com.example.salonon;

import android.media.Image;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class APIImpl implements API {
    Network network = new Network();

    public String createNewProfile(Profile profileToAddToDatabase) {
        try {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("user", profileToAddToDatabase.emailAddress);
            parameters.put("first", profileToAddToDatabase.firstName);
            parameters.put("last", profileToAddToDatabase.lastName);
            parameters.put("pass", profileToAddToDatabase.password);
            if (profileToAddToDatabase.typeOfProfile.equalsIgnoreCase("stylist")) {
                parameters.put("isStylist", "TRUE");
                parameters.put("stylistBio", profileToAddToDatabase.bio);
            } else {
                parameters.put("isSylist", "FALSE");
//                parameters.put("stylistBio", "NONE");
            }
            if (profileToAddToDatabase.typeOfProfile.equalsIgnoreCase("salon")) {
                parameters.put("isSalon", "TRUE");
                parameters.put("salonBio", profileToAddToDatabase.bio);
//                TODO salon rate
            } else {
                parameters.put("isSalon", "FALSE");
                parameters.put("salonBio", "NONE");
            }
            String response = network.post(network.herokuTestURL + "createuser", parameters);
            JSONObject jsonStatus = new JSONObject(response);
            String status = (String) jsonStatus.get("status");
            return status;
        } catch (Exception e) {
            return null;
        }
    }
    // Maybe change string array to a list of activity_profile parameters.
    // Need to know what a activity_profile should encapsulate first.
    //
    /* Given an Profile object, adds the activity_profile to the database. Returns a String to signify
    whether the adding to the database was successful or a failure. If the string is "Success"
    then it was successful. It not, the string is the error message to display */

    public Profile loginToProfile(String emailAddress, String password) {
        try {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("user", emailAddress);
            parameters.put("pass", password);
            String response = network.post(network.herokuTestURL + "login", parameters);
            JSONObject json = new JSONObject(response);
            // TODO This isn't quite right yet
            String typeOfProfile = json.getString("isStylist") == "0" ? (int) json.get("isSalon") == 0 ? "Client" : "Salon" : "Stylist";
            String email = (String) json.get("email");
            String firstName = (String) json.get("first");
            String lastName = (String) json.get("last");
            String bio = json.get("stylistBio") == "null" ? (String) json.get("salonBio") : (String) json.get("stylistBio");
            Profile profile = new Profile(typeOfProfile, email, null, firstName, lastName, null, null, null, null, null, null, bio, null);
            return profile;
        } catch (Exception e) {
            return null;
        }

    }

    /* Given an email and password, returns the Profile information for a given account. */

    public Profile[] stylistSearchForProfilesByLocation(String latitude, String longitude, int radius) {
        return null;

    }

    public Profile[] salonSearchForProfilesByLocation(String latitude, String longitude, int radius) {
        return null;

    }

    public String getAmenityByID(int id) {
        try {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("id", String.valueOf(id));
            String response = network.post(network.herokuTestURL + "amenity-by-id", parameters);
            JSONObject jsonAmenity = new JSONObject(response);
            String name = (String) jsonAmenity.get("name");
            return name;
        } catch (Exception e) {
            return null;
        }
    }

    public Profile getClientProfile(int clientID) {
        try {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("clientID", String.valueOf(clientID));
            String response = network.post(network.herokuURL + "client-by-id", parameters);
            JSONObject jsonProfile = new JSONObject(response);
            String firstName = (String) jsonProfile.get("firstName");
            // TODO Get all of the values and create a activity_profile from them.
            return null;
        } catch (Exception e) {
            Exception thisException = e;
            return null;
        }
    }
    // Given a client id, return a Profile Object of the client's data.
}

