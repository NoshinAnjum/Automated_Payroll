package com.example.automatedpayrol;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
 
public class AndroidGPSTrackingActivity extends Activity {
    
	private ProgressDialog pDialog;
	static int success;
    JSONArray output = null;
    JSONParser jParser2 = new JSONParser();
    static String status1;
    private static final String LOGIN_URL = "http://kareeshoma.netai.net/practise/location.php";
    private static final String TAG_NAME="status";
    private static final String TAG_PRODUCTS="output";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

JSONArray products = null;
	
	String s1,s2;
    Double latVal,longVal,latitude,longitude,distance;
	
    Button btnShowLocation;
     
    // GPSTracker class
    CurrentLocation gps;
     
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_current);
        
         
        Intent intent = getIntent(); 
        
            
            s1 = intent.getStringExtra("LatValue");
            s2 = intent.getStringExtra("LongValue");
            latVal =  new Double(s1);
            longVal = new Double(s2);
            
        btnShowLocation = (Button) findViewById(R.id.btnShowLocation);
         
        // show location button click event
        btnShowLocation.setOnClickListener(new View.OnClickListener() {
             
            @Override
            public void onClick(View arg0) {        
                // create class object
                gps = new CurrentLocation(AndroidGPSTrackingActivity.this);
 
                // check if GPS enabled     
                if(gps.canGetLocation()){
                     
                     latitude = gps.getLatitude();
                     longitude = gps.getLongitude();
                     Location selected_location=new Location("locationA");
                     selected_location.setLatitude(latVal);
                     selected_location.setLongitude(longVal);
                 Location near_locations=new Location("locationA");
                     near_locations.setLatitude(latitude);
                     near_locations.setLongitude(longitude);

                 distance=(double) selected_location.distanceTo(near_locations);
                    Geocoder gcd = new Geocoder(AndroidGPSTrackingActivity.this, Locale.getDefault());
List<Address> addresses = null;
try {
	addresses = gcd.getFromLocation(latitude, longitude, 1);
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
if (addresses.size() > 0) 
    if(distance>=0 && distance<1000)
    {
    	new AsyncEmployeeDetails().execute();
    }
                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }
                 
            }
        });
    }
    class AsyncEmployeeDetails extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AndroidGPSTrackingActivity.this);
            pDialog.setMessage("Adding location history...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
    	
    	@Override
    	protected String doInBackground(String... args) {
    		// TODO Auto-generated method stub
    		 // Check for success tag
            
            String vol_name= EmployeeLogin.user;
            //Double latitude = AndroidGPSTrackingActivity.latitude;
            //Double longitude = AndroidGPSTrackingActivity.longitude;
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = df.format(c.getTime());
            //Toast.makeText(getApplicationContext(), formattedDate, Toast.LENGTH_SHORT);
            
            try {
                // Building Parameters
        
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("txtvol",vol_name));
                params.add(new BasicNameValuePair("txtemail", Double.toString(latitude)));
                
                params.add(new BasicNameValuePair("remark", Double.toString(longitude)));
                params.add(new BasicNameValuePair("bag",formattedDate ));
                params.add(new BasicNameValuePair("find",Integer.toString(1)));
                Log.d("request!", "starting");
                
                //Posting user data to script 
                JSONObject json = jParser2.makeHttpRequest(
                       LOGIN_URL, "POST", params);

              
                Log.d("Login attempt", json.toString());

                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                	Log.d("User Created!", json.toString());              	
                	finish();
                	return json.getString(TAG_MESSAGE);
                }else{
                	Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                	return json.getString(TAG_MESSAGE);
                	
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
    		
    	}
    	
        protected void onPostExecute(String file_url) {
       
            pDialog.dismiss();
            if(success == 1)
            {
            	Intent intent = new Intent(AndroidGPSTrackingActivity.this, EmployeeHome.class);
                finish();
                startActivity(intent);
            }
            if (file_url != null){
            	Toast.makeText(AndroidGPSTrackingActivity.this, file_url, Toast.LENGTH_LONG).show();
            }

        }
    	
    }
    
     
}

