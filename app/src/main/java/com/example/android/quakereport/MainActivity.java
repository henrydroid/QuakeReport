package com.example.android.quakereport;


import android.app.LoaderManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<QuakeInfo>> {

    //Define the activity tag for log messages
    private static final String LOG_TAG = MainActivity.class.getName();

    //URL for earthquake data from the USGS
    private static String USGS_URL_STRING =  "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=5&limit=20";

    //Adapter for the list of earthquakes
    private CustomAdapter adapter;

    //Define layout views
    private ListView list;
    private TextView emptyView;
    private ProgressBar progressIndicator;

    //Define internet connectivity variable
    private boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "Main Activity onCreate() is called ...");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


       //Find a reference to the ListView in the layout
        list = (ListView) findViewById(R.id.list_view);

        //Find a reference to the TextView in case of no earthquake data available
        emptyView = (TextView) findViewById(R.id.empty_text_view);

        //Find a reference to the ProgressBar indicator
        progressIndicator = (ProgressBar) findViewById(R.id.loading_spinneer);

        //Create a new adapter that takes an empty list of earthquakes a inputs
        adapter = new CustomAdapter(this, new ArrayList<QuakeInfo>());

        //Set the adapter on the ListView so the list can be populated in the user interface
        list.setAdapter(adapter);

        //Determine if there is an internet connection
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                              activeNetwork.isConnectedOrConnecting();


        //Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected earthquake
        list.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        QuakeInfo quakeInfo = adapter.getItem(position);
                        String url = quakeInfo.getUrl();

                        //Open url in the browser
                        try {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(browserIntent);

                            Toast.makeText(getApplicationContext(), url, Toast.LENGTH_SHORT).show();

                        }catch ( ActivityNotFoundException e ){

                            Toast.makeText(getApplicationContext(),"No application can handle this request",Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                }
        );



        //Initialize the loader
        Log.d(LOG_TAG,"initLoader is called ...");
        getLoaderManager().initLoader(0,null,this);


    }

    @Override
    public Loader<ArrayList<QuakeInfo>> onCreateLoader(int id, Bundle args) {

        //Create a new loader for the given URL
        Log.d(LOG_TAG,"onCreateLoader is called ...");
        return new EarthquakeLoader(MainActivity.this,USGS_URL_STRING);

    }

    @Override
    public void onLoadFinished(Loader<ArrayList<QuakeInfo>> loader, ArrayList<QuakeInfo> quakeInfos) {

        Log.d(LOG_TAG,"onLoadFinishedLoader is called ...");

        //Set the progress bar visibility to invisible to show earthquake data
        progressIndicator.setVisibility(View.GONE);

        //Clear the adapter of the previous earthquake data
        adapter.clear();

        //If there is a valid list of Earthquakes, then add them to the adapters's data set.
        //This will trigger the ListView to update
        if(quakeInfos != null && !quakeInfos.isEmpty()){

            adapter.addAll(quakeInfos);

        }else if(!isConnected){
            emptyView.setText("No internet connection.");
            emptyView.setVisibility(View.VISIBLE);
            Log.d(LOG_TAG, "Not connected to internet");
        }
        else {
           list.setEmptyView(emptyView);
            emptyView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<QuakeInfo>> loader) {

        Log.d(LOG_TAG, "onLoaderReset is called ...");

        //Loader reset, so we can clear out our existing data
        adapter.clear();
    }






}
