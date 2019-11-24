package com.example.salonon;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;

public class API {

    //CREATE USER
    public boolean createNewProfile(Profile profile, String password) {
        try {
            //data code
            HttpRequest request = new HttpRequest("post",  "creatuser");
            request.queryValues.put("user", profile.email);
            request.queryValues.put("first", profile.first);
            request.queryValues.put("last", profile.last);
            request.queryValues.put("pass", password);
            if (profile.isStylist){
                request.queryValues.put("isStylist", "TRUE");
            } else {
                request.queryValues.put("isStylist", "FALSE");
            }
            if (profile.isSalon){
                request.queryValues.put("isSalon", "TRUE");
            } else {
                request.queryValues.put("isSalon", "FALSE");
            }
            request.queryValues.put("stylistBio", profile.stylistBio);
            request.queryValues.put("salonBio", profile.salonBio);
            request.queryValues.put("salonRate", "0.0");
            String response = request.send();

            //result code
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

    //LOGIN
    public Profile login(String emailAddress, String password) {
        try {
            HttpRequest request = new HttpRequest("post",  "login");
            request.queryValues.put("user", emailAddress);
            request.queryValues.put("pass", password);
            String response = request.send();

            JSONObject json = new JSONObject(response);
            JSONObject profile = json.getJSONObject("profile");

            return jsonToProfile(profile);
        } catch (Exception e) {
            Log.v("API error", "Failed to get login confirmation");
            return null;
        }
    }

    //GET STYLISTS FROM ADDRESS
    public Profile[] searchStylistByLocation(String address, String city, String state, String postalCode, String radius) {
        try {
            //request code
            HttpRequest request = new HttpRequest("post",  "searchstylistslocation");
            request.queryValues.put("addr", address);
            request.queryValues.put("city", city);
            request.queryValues.put("state", state);
            request.queryValues.put("zip", postalCode);
            request.queryValues.put("radius", radius);
            String response = request.send();
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

    //FUNCTION TO GET PROFILE FROM JSON
    public Profile jsonToProfile(JSONObject profile) {
        try {
            String email = (String) profile.get("email");
            String first = (String) profile.get("first");
            String last = (String) profile.get("last");
            String stylistBio = (String) profile.get("stylistBio");
            String salonBio = (String) profile.get("salonBio");

            //Handle salonRate as double or int
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
            HttpRequest request = new HttpRequest("post",  "client-by-id");
            request.queryValues.put("id", clientID);
            String response = request.send();

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
            HttpRequest request = new HttpRequest("post",  "update-profile-photo");
            request.queryValues.put("id", clientID);
            request.queryValues.put("bio", bio);
            request.queryValues.put("styles", offerArrayToString(offers));
            String response = request.send();
            JSONObject result = new JSONObject(response);
            boolean status = (boolean) result.get("status");
            return status;
        } catch (Exception e){
            Log.v("API error", "Failed to activate stylist account "+e);
            return false;
        }

    }

    //2 FUNCTIONS TO TURN OFFER OBJECTS INTO JSON STRINGS TO BE PASSED TO PARAMETERS MAP
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


    //ADD PROFILE PIC TO ACCOUNT
    public boolean addProfilePic(String clientID, Bitmap bitmap){
        String encodedImage = bitmapToBase64(bitmap);
    //Make request
        HttpRequest request = new HttpRequest("post",  "update-profile-photo");
        request.bodyValues.put("id", clientID);
        request.bodyValues.put("photo", encodedImage);
        String response = request.send();

        //Handle response as json
        try{
            JSONObject json = new JSONObject(response);
            JSONObject status = json.getJSONObject("status");
            if (status.equals(true)){
                return true;
            } else {
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    //CONVERT BITMAP IMAGE TO STRING FOR HTTP REQUESTS
    public String bitmapToBase64(Bitmap bitmap){

        //CONVERT BITMAP To BYTE ARRAY
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = "";

        //CONVERT BYTE ARRAY TO BASE64 STRING
        try {
            encodedImage= URLEncoder.encode(Base64.encodeToString(byteArray, Base64.DEFAULT), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.v("photostring", encodedImage);
        return encodedImage;
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
}

