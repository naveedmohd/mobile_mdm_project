package com.example.innovation_day;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.app.TabActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

public class AddConsumer extends Activity {

	// Progress Dialog
	private ProgressDialog pDialog;

	JSONParser jsonParser = new JSONParser();
	EditText inputConsumerId;
	EditText inputSuffix;
	EditText inputFname;
	EditText inputMname;
	EditText inputLname;
	EditText inputGender;
	EditText inputTelephone;
	EditText inputDob;
	EditText inputObjectId;
	String pkg;

	// url to create new product
	private static String url_create_consumer = "http://10.2.115.5/android_connect/create_consumer.php";
	//private static String url_create_consumer = "http://192.168.2.6/android_connect/create_consumer.php";
	//private static String url_create_consumer = "http://10.1.220.44/android_connect/create_consumer.php";
	//private static String url_create_consumer = "http://10.1.221.221/android_connect/create_consumer.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_consumer);
		
        //Intent in = new Intent(this, HeaderTabLayout.class);
        //startActivity(in);
       
   
		// Edit Text
		inputConsumerId 	= (EditText) findViewById(R.id.inputConsumerId);
		inputSuffix 	= (EditText) findViewById(R.id.inputSuffix);
		inputFname 		= (EditText) findViewById(R.id.inputFname);
		inputMname 		= (EditText) findViewById(R.id.inputMname);
		inputLname 		= (EditText) findViewById(R.id.inputLname);
		inputGender 	= (EditText) findViewById(R.id.inputGender);
		inputTelephone 	= (EditText) findViewById(R.id.inputTelephone);
		inputDob 		= (EditText) findViewById(R.id.inputDob);
		inputObjectId 	= (EditText) findViewById(R.id.inputObjectId);

		// Create button
		Button btnCreateConsumer = (Button) findViewById(R.id.btnCreateConsumer);
		Button btnHome = (Button) findViewById(R.id.btnHome);


		// button click event
		btnCreateConsumer.setOnClickListener(new View.OnClickListener() {


			@Override
			public void onClick(View view) {
				
				
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddConsumer.this);

				// Setting Dialog Title
				alertDialog.setTitle("Create Alert");

				// Setting Dialog Message
				alertDialog.setMessage("Are you sure you want to create consumer?");

				// Setting Icon to Dialog
				//alertDialog.setIcon(R.drawable.tick);

				
				alertDialog.setPositiveButton("YES",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int which) {
								// Write your code here to execute after dialog
								//Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();

								new CreateNewConsumer().execute();
								
								Toast.makeText(getApplicationContext(), "Consumer Record created successfully", Toast.LENGTH_SHORT).show();
							}
						});

				// Setting Negative "NO" Button
				alertDialog.setNegativeButton("NO",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,	int which) {
								// Write your code here to execute after dialog
								Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
								dialog.cancel();
							}
						});
				
				
				
							// Showing Alert Message
							alertDialog.show();

				
				//new CreateNewConsumer().execute();

			
				
				
				
				
			}
		});
		
		
		// button click event
		btnHome.setOnClickListener(new View.OnClickListener() {


			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(), AndroidTabLayoutActivity.class);
				startActivity(i);


			}
		});
		
	}

	/**
	 * Background Async Task to Create new product
	 * */
	class CreateNewConsumer extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(AddConsumer.this);
			pDialog.setMessage("Creating Consumer..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Creating Consumer
		 * */
		protected String doInBackground(String... args) {
			String consumerid	= inputConsumerId.getText().toString();
			String suffix 		= inputSuffix.getText().toString();
			String firstname 	= inputFname.getText().toString();
			String middlename 	= inputMname.getText().toString();
			String lastname 	= inputLname.getText().toString();
			String gender 		= inputGender.getText().toString();
			String telephone 	= inputTelephone.getText().toString();
			String dob 			= inputDob.getText().toString();
			String objectid		= inputObjectId.getText().toString();

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("consumerid", consumerid));
			params.add(new BasicNameValuePair("suffix", suffix));
			params.add(new BasicNameValuePair("firstname", firstname));
			params.add(new BasicNameValuePair("middlename", middlename));
			params.add(new BasicNameValuePair("lastname", lastname));
			params.add(new BasicNameValuePair("gender", gender));
			params.add(new BasicNameValuePair("telephone", telephone));
			params.add(new BasicNameValuePair("dob", dob));
			params.add(new BasicNameValuePair("objectid", objectid));

			// getting JSON Object
			// Note that create product url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(url_create_consumer,
					"POST", params);

			// check log cat fro response
			Log.d("Create Response", json.toString());

			// check for success tag
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// successfully created product
					

								
					Intent i = new Intent(getApplicationContext(), AddConsumer.class);
					startActivity(i);

					// closing this screen
					finish();
				} else {
					// failed to create product
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done


			
			pDialog.dismiss();
		}

	}
}
