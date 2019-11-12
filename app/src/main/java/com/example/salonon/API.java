package com.example.salonon;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class API {
    Network network = new Network();

    public String test(){
        return "test";
    }

    //CREATE USER
    public boolean createNewProfile(Profile profile, String password) {
        try {
            //data code
            Map<String, String> parameters = new HashMap<>();
            parameters.put("user", profile.email);
            parameters.put("first", profile.first);
            parameters.put("last", profile.last);
            parameters.put("pass", password);
            if (profile.isStylist){
                parameters.put("isStylist", "TRUE");
            } else {
                parameters.put("isStylist", "FALSE");
            }
            if (profile.isSalon){
                parameters.put("isSalon", "TRUE");
            } else {
                parameters.put("isSalon", "FALSE");
            }
            parameters.put("stylistBio", profile.stylistBio);
            parameters.put("salonBio", profile.salonBio);
            parameters.put("salonRate", "0.0");

            //request code
            String response = network.post(network.herokuURL + "createuser", parameters);
            Log.v("createNewProfile", "response from server is: " + response);
            JSONObject jsonStatus = new JSONObject(response);
            boolean status = (boolean) jsonStatus.get("status");
            return status;
        } catch (Exception e) {
            e.getStackTrace();
            Log.v("API error", "Failed to create new account" + e);
            return false;
        }
    }
    // Maybe change string array to a list of activity_profile parameters.
    // Need to know what a activity_profile should encapsulate first.

    //LOGIN
    public Profile login(String emailAddress, String password) {
        try {
            Map<String, String> parameters = new HashMap<>();
            //make request
            parameters.put("user", emailAddress);
            parameters.put("pass", password);
            String response = network.post(network.herokuURL + "login", parameters);
            JSONObject json = new JSONObject(response);
            JSONObject profile = json.getJSONObject("profile");

            return jsonToProfile(profile);
        } catch (Exception e) {
            Log.v("API error", "Failed to get login confirmation");
            return null;
        }
    }

    //Todo no zip
    public Profile[] searchStylistByLocation(String address, String city, String state, String postalCode, String radius) {
        try {
            //request code
            Map<String, String> parameters = new HashMap<>();
            parameters.put("addr", address);
            parameters.put("city", city);
            parameters.put("state", state);
            parameters.put("zip", postalCode);
            parameters.put("radius", radius);
            String response = network.post(network.herokuURL + "searchstylistslocation", parameters);
            JSONObject json = new JSONObject(response);

            //gets an array of ALL profiles
            JSONArray array = json.getJSONArray("profiles");

            //data code
            Profile[] objects = new Profile[array.length()];
            for(int i=0; i<array.length(); i++) {
                objects[i] = jsonToProfile(array.getJSONObject(i));
            }
            return objects;
        } catch (Exception e) {
            Log.v("API error", "Failed to get stylists by location "+e);
            return null;
        }
    }

    public String updateProfilePhoto() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("id", "thomas@mail.com");
        parameters.put("photo", "photo");
//        parameters.put("state", state);
//        parameters.put("zip", postalCode);
//        parameters.put("radius", radius);
        String response = network.post(network.herokuURL + "update-profile-photo", parameters);
//        JSONObject json = new JSONObject(response);
//
//        //gets an array of ALL profiles
//        JSONArray array = json.getJSONArray("profiles");
//
//        //data code
//        Profile[] objects = new Profile[array.length()];
//        for(int i=0; i<array.length(); i++) {
//            objects[i] = jsonToProfile(array.getJSONObject(i));
//        }
        return response;
    }

    //FUNCTION TO GET PROFILE FROM JSON
    public Profile jsonToProfile(JSONObject profile) {
        try {

            String email = (String) profile.get("email");
            String first = (String) profile.get("first");
            String last = (String) profile.get("last");
            String stylistBio = (String) profile.get("stylistBio");
            String salonBio = (String) profile.get("salonBio");

            double salonRate;
            try {
                salonRate = (double) (profile.get("salonRate"));
            }catch(Exception e){
                salonRate = new Double((int)profile.get("salonRate"));
            }

            //get booleans from strings
            boolean isStylist = false;
            boolean isSalon = false;
            int resultStylist = (int) profile.get("isStylist");
            if (resultStylist == 1){
                isStylist = true;
            }
            int resultSalon = (int) profile.get("isStylist");
            if (resultSalon == 1){
                isSalon = true;
            }

            Profile newProfile = new Profile(email, first, last, null, isStylist, isSalon, stylistBio, salonBio, salonRate);
            return newProfile;
        } catch (Exception e) {
            Log.v("API error", "Failed to convert json to profile " + e);
            return null;
        }
    }

    public Profile[] searchSalonByLocation(String latitude, String longitude, int radius) {
        return null;
    }

    //GET PROFILE (WILL EVENTUALLY USE TOKEN INSTEAD OF EMAIL)
    public Profile getClientProfile(String clientID) {
        try {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("id", clientID);
            String response = network.post(network.herokuURL + "client-by-id", parameters);
            JSONObject profile = new JSONObject(response);
            return jsonToProfile(profile);
        } catch (Exception e) {
            Log.v("API error", "Failed to get profile from id "+e);
            return null;
        }
    }

    //ADD STYLIST TO ACCOUNT
    public boolean addStylist(String clientID, String bio, Offer[] offers){
        try{
            Map<String, String> parameters = new HashMap<>();
            parameters.put("id", clientID);
            parameters.put("bio", bio);
            parameters.put("styles", offerArrayToString(offers));
            String response = network.post(network.herokuURL + "add-stylist", parameters);
            JSONObject result = new JSONObject(response);
            boolean status = (boolean) result.get("status");
            return status;
        } catch (Exception e){
            Log.v("API error", "Failed to activate stylist account "+e);
            return false;
        }

    }
    public Booking getClientBookings(Profile profile){
        return null;
    }

    public Offer[] getStylistOffers(Profile profile){
        return null;
    }
    public boolean createBooking(Booking booking){
        return false;
    }



    //2 FUNCTIONS TO TURN OFFER OBJECTS INTO STRINGS TO BE PASSED TO PARAMETERS MAP
    public String offerArrayToString(Offer[] offers){
        String[] jsonStrings = new String[offers.length];
        for (int i=0; i< offers.length; i++){
            jsonStrings[i] = offerToJsonString(offers[i]);
        }
        String string = "{\"styleArray\": "+ Arrays.toString(jsonStrings)+"}";
        return string;
    }
    public String offerToJsonString(Offer offer){
        return "{\"id\":"+offer.styleID+", \"price\": "+offer.price+", \"deposit\": "+offer.deposit+", \"duration\": "+offer.duration+"}";
    }

}

