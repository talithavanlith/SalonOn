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
    private String herokuURL = "https://salon-on-backend.herokuapp.com/";
    private String herokuTestURL = "https://salon-on.herokuapp.com/";
    public String createNewProfile(Profile profileToAddToDatabase) {
        return null;
    }
    // Maybe change string array to a list of profile parameters.
    // Need to know what a profile should encapsulate first.
    //
    /* Given an Profile object, adds the profile to the database. Returns a String to signify
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
            NetworkPost task = new NetworkPost();
            // Place the relevant parameters; in this case, "clientID" and their values (which must be strings)
            task.parameters.put("clientID", String.valueOf(clientID));
            // Execute the post request and receive the result from the server
            String result = task.execute(herokuTestURL + "test").get();
            // Convert the result from the server to a json object. This represents a profile.
            JSONObject jsonObject = new JSONObject(result);
            // Access firstName by using its key
            String firstName = (String) jsonObject.get("firstName");
            // Attach firstName to Profile.
            // TODO return profile.
            return null;
        } catch (Exception e) {
            Exception thisException = e;
            return null;
        }
    }
    // Given a client id, return a Profile Object of the client's data.

    // Network code must be run asynchronously.  This AsyncTask technique might have bugs
    // For example, rotating the phone might crash it.  We can adjust this later
    class NetworkGet extends AsyncTask<String, Void, String> {

        private Exception exception;

        // Override method provided by asynctask.  This code is run upon NetworkGet.execute().
        protected String doInBackground(String... urls) {
            try {
                // Create a connection to the "test" endpoint on our server.
                URL url = new URL(urls[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Content-Type", "application/json");

                // Read the content returned from that endpoint.
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                con.disconnect();
                // Return the string.
                return content.toString();
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        // TODO this might be useful, but so far I'm not using it.
        protected void onPostExecute(String string) {
            // TODO: check this.exception
            // TODO: do something with the feed
//            print(string);
        }

    }

    class NetworkPost extends AsyncTask<String, Void, String> {

        private Exception exception;
        public Map<String, String> parameters = new HashMap<>();

        // Override method provided by asynctask.  This code is run upon NetworkGet.execute().
        protected String doInBackground(String... urls) {
            try {
                // Create a connection to the "test" endpoint on our server.
                URL url = new URL(urls[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
//                con.setRequestProperty("Content-Type", "application/json");

//                Map<String, String> parameters = new HashMap<>();
//                parameters.put("param1", "val");

                con.setDoOutput(true);
                DataOutputStream out = new DataOutputStream(con.getOutputStream());
                out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
                out.flush();
                out.close();

                // Read the content returned from that endpoint.
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                con.disconnect();
                // Return the string.
                return content.toString();
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        // TODO this might be useful, but so far I'm not using it.
        protected void onPostExecute(String string) {
            // TODO: check this.exception
            // TODO: do something with the feed
//            print(string);
        }

    }

    public static class ParameterStringBuilder {
        public static String getParamsString(Map<String, String> params)
                throws UnsupportedEncodingException {
            StringBuilder result = new StringBuilder();

            result.append("{");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                result.append("\"");
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("\"");
                result.append(":");
                result.append("\"");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                result.append("\"");
                result.append(",");
            }

            String resultString = result.toString();
            return resultString.length() > 0
                    ? resultString.substring(0, resultString.length() - 1) + "}"
                    : resultString + "}";
        }
    }
}

