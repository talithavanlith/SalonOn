package com.example.salonon;

import android.os.AsyncTask;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import okhttp3.*;


//CLASS FOR ACCEPTING REQUEST PARAMETERS, TYPE, AND STARTING ASYNC TASKS
 public class HttpRequest {

     //FIELDS
     String type; //POST OR GET
     String path;
     public Map<String, String> queryValues = new HashMap<>();
     public Map<String, String> bodyValues = new HashMap<>();

     //CONSTRUCTOR
     public HttpRequest(String type, String path){
         this.type = type;
         this.path = path;
     }

     //EXECUTES A REQUEST
    public String send(){
         if (type.equals("post")){
             try {
                 return new PostRequest().execute(path).get().body().string();
             } catch (Exception e) {
                 e.printStackTrace();
             }
         } else {
             //for handling GET
         }
        return null;
    }

     //POST REQUEST ASYNC TASK (ACCEPTS URL AND USES PARENT CLASS FOR OTHER PARAMS)
     private class PostRequest extends AsyncTask<String, Void, Response>{
         OkHttpClient client = new OkHttpClient();
         @Override
         protected Response doInBackground(String... strings) {

             //BUILD THE URL HERE WITH QUERY PARAM MAP
             HttpUrl.Builder urlBuilder = new HttpUrl.Builder()
                     .scheme("https")
                     .host("salon-on-backend.herokuapp.com")
                     .addPathSegment(path);
                    for (Map.Entry<String, String> entry : queryValues.entrySet() ) {
                        urlBuilder.addQueryParameter( entry.getKey(), entry.getValue() );
                    }
                    HttpUrl httpUrl = urlBuilder.build();

             //BUILD BODY HERE FORM BODYVALUES MAP
             FormBody.Builder bodyBuilder = new FormBody.Builder();
                    for (Map.Entry<String, String> entry : bodyValues.entrySet() ) {
                        bodyBuilder.add( entry.getKey(), entry.getValue() );
                    }
                    RequestBody formBody = bodyBuilder.build();
             //CREATE AND SEND REQUEST
             Request request = new Request.Builder()
                     .url(httpUrl)
                     .post(formBody)
                     .build();
             try {
                 Response response = client.newCall(request).execute();
                 // Do something with the response.
                 return response;
             } catch (IOException e) {
                 e.printStackTrace();
                 return null;
             }
         }
     }

    //GET REQUEST ASYNC TASK (ACCEPTS URL AND USES PARENT CLASS FOR OTHER PARAMS)
     private class GetRequest extends AsyncTask<String, Void, Response>{

         @Override
         protected Response doInBackground(String... strings) {
             return null;
         }
     }

 }
