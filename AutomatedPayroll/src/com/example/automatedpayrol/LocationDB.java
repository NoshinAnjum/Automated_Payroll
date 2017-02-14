package com.example.automatedpayrol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class LocationDB extends Activity {
    Button addressButton;
    TextView addressTV;
    TextView latLongTV;
    //String lat,lon;
    public static String lat = "lat";
    public static String lon = "lon";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String address =  EmployeeLogin.location;
       
        GeocodingLocation locationAddress = new GeocodingLocation();
        locationAddress.getAddressFromLocation(address,
                getApplicationContext(), new GeocoderHandler());
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    String [] splitArray =  locationAddress.split("\n");
                    //lat = new Double(splitArray[0]);
                    //lon = new Double(splitArray[1]);
		            lat = splitArray[0];
                    lon = splitArray[1];
                    //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + lat + "\nLong: " + lon, Toast.LENGTH_LONG).show();    
                   

                    Intent myintent = new Intent(LocationDB.this, AndroidGPSTrackingActivity.class);
                    myintent.putExtra("LatValue",lat);
		            myintent.putExtra("LongValue",lon);
                    startActivity(myintent);
                    break;
                default:
                    locationAddress = null;
            }
            
        }
    }
}