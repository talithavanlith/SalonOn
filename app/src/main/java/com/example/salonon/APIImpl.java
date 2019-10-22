package com.example.salonon;

import android.media.Image;
import android.os.AsyncTask;

import org.json.JSONArray;
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

    public Profile jsonToProfile(JSONObject profile) {
        try {
            String typeOfProfile = (int) profile.get("isStylist") == 0 ? (int) profile.get("isSalon") == 0 ? "Client" : "Salon" : "Stylist";
            String email = (String) profile.get("email");
            String firstName = (String) profile.get("first");
            String lastName = (String) profile.get("last");
            String bio = profile.get("stylistBio") == "null" ? (String) profile.get("salonBio") : (String) profile.get("stylistBio");
            Profile newProfile = new Profile(typeOfProfile, email, null, firstName, lastName, null, null, null, null, null, null, bio, null, null);
            return newProfile;
        } catch (Exception e) {
            return null;
        }
    }

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
                parameters.put("isStylist", "FALSE");
                parameters.put("salonBio", "NULL");
            }
            String response = network.post(network.herokuURL + "createuser", parameters);
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
            String response = network.post(network.herokuURL + "login", parameters);
            JSONObject json = new JSONObject(response);
            // TODO This isn't quite right yet
            JSONObject profile = json.getJSONObject("profile");
            String typeOfProfile = (int) profile.get("isStylist") == 0 ? (int) profile.get("isSalon") == 0 ? "Client" : "Salon" : "Stylist";
            String email = (String) profile.get("email");
            String firstName = (String) profile.get("first");
            String lastName = (String) profile.get("last");
            String bio = profile.get("stylistBio") == "null" ? (String) profile.get("salonBio") : (String) profile.get("stylistBio");
            Profile newProfile = new Profile(typeOfProfile, email, null, firstName, lastName, null, null, null, null, null, null, bio, null, null);
            return newProfile;
        } catch (Exception e) {
            return null;
        }

    }

    /* Given an email and password, returns the Profile information for a given account. */
//todo profiles don't have zipcodes.  use mock data? pass the email to the server and have the server query the database?
    public Profile[] stylistSearchForProfilesByLocation(Profile profile) {
        try {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("zip", "27514");
            parameters.put("radius", "10");
            String response = network.post(network.herokuURL + "searchstylistslocation", parameters);
            JSONObject json = new JSONObject(response);
            JSONArray array = json.getJSONArray("profiles");
            Profile[] objects = new Profile[array.length()];
            for(int i=0;i<array.length();i++)
            {
                objects[i] = jsonToProfile(array.getJSONObject(i));
            }
            return objects;
        } catch (Exception e) {
            return null;
        }
    }

    public Profile[] salonSearchForProfilesByLocation(String latitude, String longitude, int radius) {
        return null;

    }

    // TODO Delete?
    public String getAmenityByID(int id) {
        try {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("id", String.valueOf(id));
            String response = network.post(network.herokuURL + "amenity-by-id", parameters);
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
            parameters.put("id", String.valueOf(clientID));
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

