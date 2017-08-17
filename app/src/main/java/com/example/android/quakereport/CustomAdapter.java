package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by toshiba on 7/5/2017.
 *
 * Custom Adapter that will handle displaying earthquake information into the list view
 */

public class CustomAdapter extends ArrayAdapter<QuakeInfo> {

    public CustomAdapter(Context context, ArrayList<QuakeInfo> quakeData) {
        super(context,R.layout.custom_layout, quakeData);
    }


    //Return formatted date string (i.e. "July 07, 2017")
    private String formatDate(Date dateObject){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, YYYY");
        return  dateFormat.format(dateObject);
    }

    //Return formatted time string (i.e. "8:20 PM")
    private String formatTime(Date dateObject){
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }


    //Return offset location (i.e. "227 SE of")
    private String getOffsetLocation(String location){

        String locationObject = new String(location);
        String[] spiltString;
        String offsetLocation;

        if(locationObject.contains("of")){

            spiltString = locationObject.split("of",2);
            offsetLocation = spiltString[0]+ " of";
        }else{
            offsetLocation = "Near the";

        }

        return offsetLocation;


    }

    //Return primary location (i.e. "Saranggani, Philippines")
    private String getPrimaryLocation(String location){

        String locationObject = new String(location);
        String[] spiltString;
        String primaryLocation;

        if(locationObject.contains("of")){

            spiltString = locationObject.split("of",2);
            primaryLocation = spiltString[1];
        }else{
            primaryLocation = locationObject.toString();

        }

        return primaryLocation;


    }

    //Return formatted decimal (i.e. "0.00")
    private String formatMagnitude(double mag){

        DecimalFormat formatter = new DecimalFormat("0.0");
        String output = formatter.format(mag);

        return output;
    }

    //set the appropriate magnitude background color
    private int getMagnitudeColor(double magnitude){

        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor){

            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
            case 10:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;


        }

        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }




    //Override getView method to handle displaying data in the UI

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = LayoutInflater.from(getContext());

        View customView = mInflater.inflate(R.layout.custom_layout, parent,false);

        QuakeInfo singleItem = getItem(position);
        TextView magnitudeText = (TextView) customView.findViewById(R.id.magnitude_text_view);
        TextView offsetLocationText = (TextView) customView.findViewById(R.id.location_offset_text_view);
        TextView primaryLocationText = (TextView) customView.findViewById(R.id.primary_location_text_view);
        TextView dateText = (TextView) customView.findViewById(R.id.date_text_view);
        TextView timeText = (TextView) customView.findViewById(R.id.time_text_view);

        //display magnitude
        String magnitudeToDisplay = formatMagnitude(singleItem.getMagnitude());
        magnitudeText.setText(magnitudeToDisplay);

        //Set proper background color on the magnitude circle
        //Fetch the background from the TextView, which is a GradientDrawable
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeText.getBackground();

        //Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(singleItem.getMagnitude());

        //Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);


        //display location
        offsetLocationText.setText(getOffsetLocation(singleItem.getLocation()));
        primaryLocationText.setText(getPrimaryLocation(singleItem.getLocation()));


        //display date and time
        long timeMilliseconds = singleItem.getDate();
        Date dateObject = new Date(timeMilliseconds);

        String dateToDisplay = formatDate(dateObject);
        dateText.setText(dateToDisplay);

        String timeToDisplay = formatTime(dateObject);
        timeText.setText(timeToDisplay);


        return customView;

    }
}
