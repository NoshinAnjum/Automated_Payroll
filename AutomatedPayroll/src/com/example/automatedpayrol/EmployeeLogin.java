package com.example.automatedpayrol;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EmployeeLogin extends Activity implements OnClickListener {
	static String location,status;
	static int flag=0;
	public EditText username, userpass;
	private Button mSubmit;
	private TextView mRegister,t1;
	JSONArray output = null;
	static String user;
	static String status1;
	private ProgressDialog pDialog;
	int k;
	JSONParser jsonParser = new JSONParser();
	public static final String PLACE_NAME = "PLACENAME";
	private static final String LOGIN_URL = "http://kareeshoma.netai.net/MainProject/login.php";
    
  //testing from a real server:
    //private static final String LOGIN_URL = "http://www.yourdomain.com/webservice/login.php";
	 private static final String TAG_NAME="location";
	    private static final String TAG_PRODUCTS="output";
    //JSON element ids from repsonse of php script:
	    private static final String TAG_NAME2="status";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		//setup input fields
		username = (EditText)findViewById(R.id.loginuser);
		userpass = (EditText)findViewById(R.id.loginpass);
		
		//setup buttons
		mSubmit = (Button)findViewById(R.id.btnLogin);
		
		//register listeners
		mSubmit.setOnClickListener(this);
	}
	
	

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLogin:
			boolean validate=true;
			validate=validator();
			
			if(validate==true)
			{ 
			new AttemptLogin().execute();}
			break;
		default:
				break;
		}
	}
	private boolean validator() {
		if(Validation.requirefield(username)) return true;
		if(Validation.requirefield(userpass)) return true;
		
		return false;
		
		
		
	}
	class AttemptLogin extends AsyncTask<String, String, String> {
		
		boolean failure = false;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EmployeeLogin.this);
			pDialog.setMessage("Attempting login..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		
		@Override
		protected String doInBackground(String...args) {
			//Check for success tag
			int success;
			 user = username.getText().toString();
			String password = userpass.getText().toString();
			 try {
				 //Building Parameters
				 List<NameValuePair> params = new ArrayList<NameValuePair>();
				 params.add(new BasicNameValuePair("txtUName", user));
				 params.add(new BasicNameValuePair("txtPass", password));
				 
				 Log.d("request!", "starting");
				 // getting product details by making HTTP request
				 JSONObject json = jsonParser.makeHttpRequest (
					 LOGIN_URL, "POST", params);
				 
				 //check your log for json response
				 Log.d("Login attempt", json.toString());
				
				// json success tag
				 success = json.getInt(TAG_SUCCESS);
				 if(success == 1) {
					 Log.d("Login Successful!", json.toString());
					 flag = 1;
					 output= json.getJSONArray(TAG_PRODUCTS);
						//finish();
					for(int i =0; i < output.length() ;i++ ){
						JSONObject c = output.getJSONObject(i);
							//JSONObject c = output.getJSONObject(0);
							k=1;
							location = c.getString(TAG_NAME);
							status1=c.getString(TAG_NAME2);
							
						}
					
					 return json.getString(TAG_MESSAGE);
				 }else{
					 k=0;
					 Log.d("Login Falure!", json.getString(TAG_MESSAGE));
					 return json.getString(TAG_MESSAGE);
				 }
			 }catch (JSONException e) {
				 e.printStackTrace();
			 }
			 return null;
		}
	
		// After completing background task Dismiss the progress dialog
		
		protected void onPostExecute(String file_url) {
			//dismiss the dialog once product deleted
			pDialog.dismiss();
			if(k==1){
			 Intent intent = new Intent(EmployeeLogin.this, ImageUpload.class);
             //intent.putExtra(PLACE_NAME, location);
             finish();
             startActivity(intent);
			 }
			if (file_url != null) {
				Toast.makeText(EmployeeLogin.this, file_url, Toast.LENGTH_LONG).show();
            }
        }	
	}
}