package com.example.android.quakereport;


import android.content.Context;
import android.content.AsyncTaskLoader;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by toshiba on 7/23/2017.
 *
 * Loads a list of earthquakes by using an AsyncTask to perform
 * Network operation to the given URL.
 */

public class EarthquakeLoader extends AsyncTaskLoader<ArrayList<QuakeInfo>> {

    //Tag for log messages
    private static final String LOG_TAG = EarthquakeLoader.class.getName();

    //Query String URL
    private String mUrl;


    /**Construct a new EarthquakeLoader
    *@param context of the activity
    *@param url to load data from
    **/
    public EarthquakeLoader(Context context, String url) {
        super(context);

        mUrl = url;
    }

    //Override the onStartLoading() method to call forceLoad() to trigger the loadInBackground() method to execute
    @Override
    protected void onStartLoading() {

        Log.d(LOG_TAG,"onStartLoading is called ...");
        forceLoad();
    }

    //Task in the background thread
    @Override
    public ArrayList<QuakeInfo> loadInBackground() {

        Log.d(LOG_TAG, "loadInBackgroud is called ...");

        if(mUrl == null){
            return null;
        }


        Log.d(LOG_TAG,"fetchEarthquakeData is called ...");
        //Perform the network request, parse the response and extract a list of earthquakes
        ArrayList<QuakeInfo> earthquakes = QueryUtils.fetchEarthquakeData(mUrl);
        return earthquakes;
    }
}
