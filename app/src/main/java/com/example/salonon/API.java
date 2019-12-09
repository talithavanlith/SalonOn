package com.example.salonon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.JsonReader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Response;

public class API {

    //CREATE USER
    public boolean createNewProfile(Profile profile, String password) {
        try {
            //data code
            HttpRequest request = new HttpRequest("post",  "createuser");
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
            double salonRate = profile.getDouble("salonRate");

            int isStylistInt = profile.getInt("isStylist");
            boolean isStylist = false;
            if(isStylistInt == 1){
                isStylist = true;
            }
            int isSalonInt = profile.getInt("isSalon");
            boolean isSalon = false;
            if(isSalonInt == 1){
                isSalon = true;
            }
            double distance =0;
            if(profile.has("distance")){
                distance = profile.getDouble("distance");
            }

            Profile newProfile = new Profile(email, first, last, null, isStylist, isSalon, stylistBio, salonBio, salonRate);
            newProfile.distance = distance;
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
            JSONObject json = new JSONObject(response);
            if (json.getBoolean("status")){
                return jsonToProfile(json.getJSONObject("results"));
            } else{
                return null;
            }
        } catch (Exception e) {
            Log.v("API error", "Failed to get profile from id "+e);
            return null;
        }
    }

    //ADD STYLIST TO ACCOUNT
    public boolean addStylist(String clientID, String bio, Offer[] offers){
        try{
            HttpRequest request = new HttpRequest("post",  "add-stylist");
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

    public Offer jsonToOffer(JSONObject json){
        try {
            String stylist, stylename, category;
            double price, deposit, duration;
            int offerID, styleID;
            stylist = json.getString("stylist");
            stylename = json.getString("styleName");
            category = json.getString("category");
            price = json.getDouble("price");
            deposit = json.getDouble("deposit");
            duration = json.getDouble("duration");
            offerID = json.getInt("offerID");
            styleID = json.getInt("hid");
            return new Offer(offerID, styleID, stylist, stylename, category, price, deposit, duration);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
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
            boolean status = json.getBoolean("status");
            if (status){
                Log.v("API Success", "Profile photo added successfully");
                return true;
            } else {
                Log.v("API Error", "Profile photo return false");
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.v("API Error", "Profile photo failed");
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

    public Bitmap getProfilePic(String clientID){
        HttpRequest request = new HttpRequest("post", "get-profile-photo" );
        request.queryValues.put("id", clientID);
        String response = request.send();
        try {
            JSONObject json = new JSONObject(response);
            if(json.getBoolean("status")){
                String base64 = json.getString("results");
                if(base64.equals("null")){
                    return null;
                }
                byte[] decodedByteArray = Base64.decode(base64, Base64.NO_WRAP);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
                return decodedBitmap;
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }


    //ADD LOCATION TO ACCOUNT IN DB
    public Boolean addLocation(String id, String address, String city, String state, String zip){
        HttpRequest request = new HttpRequest("post",  "add-location");
        request.queryValues.put("id", id);
        request.queryValues.put("addr", address);
        request.queryValues.put("state", state);
        request.queryValues.put("city", city);
        request.queryValues.put("zip", zip);
        String response = request.send();

        //handle response
        try{
            JSONObject json = new JSONObject(response);
            boolean status = json.getBoolean("status");
            if (status){
                return true;
            }
            Log.v("API Error", "addLocation returned false");
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addSalon(String email, String bio, int[] amens){
        HttpRequest request = new HttpRequest("post",  "add-salon");

        request.queryValues.put("id", email);
        request.queryValues.put("bio", bio);
        request.queryValues.put("amenities", Arrays.toString(amens));
        String response = request.send();
        try {
            JSONObject json = new JSONObject(response);
            JSONObject status = json.getJSONObject("status");
            if (status.equals(true)){
                Log.v("API Success", "Salon activated successfully");
                return true;
            }
            Log.v("API Error", "addSalon returned false");
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    //GET ALL AMENITIES FOR SALON SIGNUP
    public Map<Integer, String> getAllAmentities(){
        HttpRequest request = new HttpRequest("post",  "get-amenities");
        String response = request.send();

        //CONVERT JSON ARRAY TO HASHMAP
        Map<Integer, String> amenities = new HashMap<>();
        try {
            JSONArray amens = new JSONObject(response).getJSONArray("results");
            for (int i=0; i<amens.length(); i++){
                int id = amens.getJSONObject(i).getInt("aid");
                String name = amens.getJSONObject(i).getString("name");
                amenities.put(id, name);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return amenities;
    }

    public JSONArray getAllHairstyles(){
        HttpRequest request = new HttpRequest("post",  "get-styles");
        String response = request.send();
        try {
            JSONArray styles = new JSONObject(response).getJSONArray("results");
            return styles;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }



    public Booking[] getClientBookings(String clientID){
        HttpRequest request = new HttpRequest("post",  "get-client-bookings");
        request.queryValues.put("id", clientID);
        String response = request.send();
        try {
            JSONObject json = new JSONObject(response);
            if(json.getBoolean("status")){
                JSONArray jsonBookings = new JSONObject(response).getJSONArray("results");
                Booking[] bookings = new Booking[jsonBookings.length()];

                for(int i=0; i<bookings.length; i++){
                    bookings[i] = jsonToBooking(jsonBookings.getJSONObject(i));
                }
                return bookings;
            } else {
                Log.v("API Error", "Get Client Booking returned false");
                return null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public double[] getAverageRatings(String stylistID){
        HttpRequest request = new HttpRequest("post", "get-rating");
        request.queryValues.put("id", stylistID);
        String response = request.send();
        try {
            JSONObject json = new JSONObject(response);
            if(json.getBoolean("status")){
                JSONObject averages = json.getJSONObject("results");
                double[] aves = new double[4];
                aves[0] = averages.getDouble("clean");
                aves[1] = averages.getDouble("friend");
                aves[2] = averages.getDouble("pro");
                aves[3] = averages.getDouble("access");
                return aves;
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return  null;
        }
    }


    public Booking jsonToBooking(JSONObject json){
        String client, stylist, salon, styleName, category, bookDate, bookTime = null;
        double price, duration;
        boolean clientConfirm = false, stylistConfirm = false, salonConfirm = false;
        try {
            client = json.getString("client");
            stylist = json.getString("stylist");
            salon = json.getString("salon");
            styleName = json.getString("styleName");
            category = json.getString("category");
            bookDate = json.getString("bookDate");
            bookTime = json.getString("bookTime");
            price = json.getDouble("price");
            duration = json.getDouble("duration");
            int clientConfirmInt = json.getInt("clientConfirm");
            if(clientConfirmInt == 1){
                clientConfirm = true;
            }
            int stylistConfirmInt = json.getInt("stylistConfirm");
            if(stylistConfirmInt == 1){
                stylistConfirm = true;
            }
            int salonConfirmInt = json.getInt("salonConfirm");
            if(salonConfirmInt == 1){
                salonConfirm = true;
            }
            int bid = json.getInt("bid");


            return new Booking(bid,client,stylist, salon, styleName, category, price, duration,
                    bookDate, bookTime, clientConfirm, stylistConfirm, salonConfirm );
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Offer[] getStylistOffers(String stylistID){
        HttpRequest request = new HttpRequest("post",  "get-stylist-offers");
        request.queryValues.put("id", stylistID);
        String response = request.send();
        try {
            JSONObject json = new JSONObject(response);
            if(json.getBoolean("status")) {
                JSONArray styles = json.getJSONArray("results");
                Offer[] offers = new Offer[styles.length()];
                for (int i=0; i< styles.length(); i++){
                    offers[i] = jsonToOffer(styles.getJSONObject(i));
                }
                return offers;
            } else {
                Log.v("API Error", "Get stylist offers returned false");
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    public boolean createBooking(String client, int offerID, String date, String time){
        HttpRequest request = new HttpRequest("post", "create-booking");
        request.queryValues.put("client", client);
        request.queryValues.put("offer", Integer.toString(offerID));
        request.queryValues.put("date", date);
        request.queryValues.put("time", time);
        String response = request.send();
        try {
            return new JSONObject(response).getBoolean("status");
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }
}

