package com.example.android.quakereport;

/**
 * Created by toshiba on 7/5/2017.
 *
 *
 * Class that will hold information for a single earthquake
 */

public class QuakeInfo {


    //define properties for a single earthquake
    private double magnitude;

    private String location;

    private long date;

    private String url;

    //QuakeInfo constructor
    QuakeInfo(double m, String l, long d, String u){

        magnitude = m;
        location = l;
        date = d;
        url = u;
    }

    //accessor methods

    public double getMagnitude(){return magnitude;}
    public String getLocation(){return location;}
    public long getDate(){return date;}
    public String getUrl(){return url;}


}
