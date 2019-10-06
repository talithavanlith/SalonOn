package com.example.salonon;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class APIExample {
    private final String TAG = APIExample.class.getSimpleName();

    public void testGet() {
        try {
            // This is an asynchronous method to run the code.  See below.
            NetworkGet task = new NetworkGet();
            // TODO I don't know what parameters this function takes.  I've entered an empty string.
            String json = task.execute(" ").get();
            // The returned object is in the form of "testKey" : "testValue"
            JSONObject jsonObject = new JSONObject(json);
            // Access testValue by using its key
            String value = (String) jsonObject.get("testKey");
            print(value);
            } catch (Exception e) {
            print(e.getMessage());
        }
    }

    // EXAMPLE OF DEALING WITH ARRAY:
    //            JSONArray arr = obj.getJSONArray("subreddits");
    //            for (int i = 0; i < arr.length(); i++) {
    //                String s = (String) arr.get(i);
    //                print(s);
    //            }
    // TUTORIAL:
    // https://www.testingexcellence.com/how-to-parse-json-in-java/

    public void print(String message) {
        Log.i(TAG, message);
    }

    // Network code must be run asynchronously.  This AsyncTask technique might have bugs
    // For example, rotating the phone might crash it.  We can adjust this later
    class NetworkGet extends AsyncTask<String, Void, String> {

        private Exception exception;

        // Override method provided by asynctask.  This code is run upon NetworkGet.execute().
        protected String doInBackground(String... urls) {
            try {
                // Create a connection to the "test" endpoint on our server.
                URL url = new URL("https://salon-on-backend.herokuapp.com/test");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Content-Type", "application/json");
                // Read the content returned from that endpoint.
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                con.disconnect();
                String m = content.toString();
                // Return the string.
                return m;
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
}
