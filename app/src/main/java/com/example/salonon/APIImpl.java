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
        return null;
    }
    // Maybe change string array to a list of activity_profile parameters.
    // Need to know what a activity_profile should encapsulate first.
    //
    /* Given an Profile object, adds the activity_profile to the database. Returns a String to signify
    whether the adding to the database was successful or a failure. If the string is "Success"
    then it was successful. It not, the string is the error message to display */

    public Profile loginToProfile(String emailAddress, String password) {
        return null;

    }

    /* Given an email and password, returns the Profile information for a given account. */

    public Profile[] stylistSearchForProfilesByLocation(String latitude, String longitude, int radius) {
        return null;

    }

    public Profile[] salonSearchForProfilesByLocation(String latitude, String longitude, int radius) {
        return null;

    }

    public Profile getClientProfile(int clientID) {
        try {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("clientID", String.valueOf(clientID));
            String response = network.post(network.herokuTestURL + "test", parameters);
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

