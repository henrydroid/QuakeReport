package com.example.android.quakereport;

/**
 * Created by toshiba on 7/6/2017.
 */

import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;


/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {



    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }



    //This method will be performed in background in the async task within the main activity
    public static ArrayList<QuakeInfo> fetchEarthquakeData(String urlString){

        //Force the background thread to sleep for 10 seconds
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String jsonResponse = "";

        //create URL object
        URL url = makeURL(urlString);

        //Perform HTTP request to the URL object and receive a JSON response back
        try {
            jsonResponse = makeHTTPRequest(url);
        }catch (IOException e){

            Log.e("QueryUtils","Problem retrieving the earthquake JSON results",e);
        }


        return extractEarthquakes(jsonResponse);


    }




    //method to build a url object
    private static URL makeURL(String s){

        URL url = null;
        try{
            url = new URL(s);

        }catch(MalformedURLException e){

            Log.e("QueryUtils", "Error creating a url object from the given url string",e);
        }

        return url;
    }


    //method to make an HTTP request and return a json response
    private static String makeHTTPRequest(URL url) throws IOException {
        String jsonResponse = "";

        //If the URL is null, then return early
        if(url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {

            //Form HTTP Request
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            //Send the HTTP Request
            urlConnection.connect();


            //Handle the HTTP response code
            int httpResponseCode = urlConnection.getResponseCode();

            //If the request was successful, receive the response and parse JSON to String
            if (httpResponseCode == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("QueryUtils", "Unsuccessful connection! HTTP response code: " + httpResponseCode);
            }

          } catch (IOException e) {
                Log.e("QueryUtils", "Error occurred in making an HTTP Request", e);

          } finally {
            if (urlConnection != null)
                urlConnection.disconnect();

            try {
                if (inputStream != null)
                    inputStream.close();

            }catch(IOException e) {
                Log.e("QueryUtils", "Error closing the input stream", e);
            }
        }

        return jsonResponse;
    }





    //helper method to convert the input stream result into string
    private static String readFromStream(InputStream inputStream) {

        StringBuilder output = new StringBuilder();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
        BufferedReader reader = new BufferedReader(inputStreamReader);

        String line;

        try {

            do {
                    line =  reader.readLine();
                    output.append(line);
            }while(line != null);

        }catch(IOException e){

            Log.e("QueryUtils", "Error converting input stream into a string",e);

        }

        return output.toString();


    }





    /**
     * Return a list of {@link QuakeInfo} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<QuakeInfo> extractEarthquakes(String s) {

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<QuakeInfo> earthquakes = new ArrayList<>();


        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.

            JSONObject rootJSONObject = new JSONObject(s);

            JSONArray featureArray = rootJSONObject.optJSONArray("features");


            for (int i=0; i<featureArray.length(); i++) {

                JSONObject featuresObject = featureArray.optJSONObject(i);

                JSONObject propertiesObject = featuresObject.optJSONObject("properties");

                double mag = propertiesObject.getDouble("mag");
                String place = propertiesObject.getString("place");
                long time = propertiesObject.getLong("time");
                String url = propertiesObject.getString("url");


                earthquakes.add(new QuakeInfo(mag, place, time,url));


            }



        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);

        }

        // Return the list of earthquakes
        return earthquakes;
    }

}
