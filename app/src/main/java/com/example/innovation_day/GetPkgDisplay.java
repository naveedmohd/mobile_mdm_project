package com.example.innovation_day;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.Intent;


public class GetPkgDisplay extends Activity implements OnItemSelectedListener {

	private Spinner spinnerPackage;
	// array list for spinner adapter
	private ArrayList<Package> packageList;
	ProgressDialog pDialog;

	// Url to get all categories
	//private String URL_CATEGORIES = "http://10.2.115.84/android_connect/get_packages.php";
	private String URL_CATEGORIES = "http://192.168.1.65/android_connect/get_packages.php";
	//private String URL_CATEGORIES = "http://10.1.220.44/android_connect/get_packages.php";
	//private String URL_CATEGORIES = "http://10.1.221.221/android_connect/get_packages.php";

	private static final String PACKAGE_NAME = "package_name";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.package_main);

		spinnerPackage = (Spinner) findViewById(R.id.spinPackage);

		packageList = new ArrayList<Package>();

		// spinner item select listener
		spinnerPackage.setOnItemSelectedListener(this);

		new GetPackages().execute();


		spinnerPackage.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
				//Toast.makeText( getApplicationContext(), parent.getItemAtPosition(position).toString() + " Selected" ,Toast.LENGTH_LONG).show();

				String package_name;
				Intent in;
				
				int count = spinnerPackage.getCount();
				int i;

				for(i=0; i<count; i++){

					package_name =spinnerPackage.getItemAtPosition(i).toString();
					Log.d("displa",package_name);

					Log.d("pkgname: ",parent.getItemAtPosition(position).toString());
					
					if (parent.getItemAtPosition(position).toString().equals("CONSUMER_PKG") ){
					
						Log.d("am i here,", "pls tell me");
				
						in = new Intent(getApplicationContext(),AddConsumer.class);
					
						in.putExtra(PACKAGE_NAME, parent.getItemAtPosition(position).toString());
					
						startActivityForResult(in,100);
						

					}

					break;

					
				}


				



			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) { }

		});


	}

	/**
	 * Adding spinner data
	 * */
	private void populateSpinner() {
		List<String> lables = new ArrayList<String>();

		for (int i = 0; i < packageList.size(); i++) {
			lables.add(packageList.get(i).getName());
		}

		// Creating adapter for spinner
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, lables);

		// Drop down layout style - list view with radio button
		spinnerAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// attaching data adapter to spinner
		spinnerPackage.setAdapter(spinnerAdapter);
	}

	/**
	 * Async task to get all food categories
	 * */
	private class GetPackages extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(GetPkgDisplay.this);
			pDialog.setMessage("Fetching Packages..");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			ServiceHandler jsonParser = new ServiceHandler();
			String json = jsonParser.makeServiceCall(URL_CATEGORIES, ServiceHandler.GET);

			Log.e("Response: ", "> " + json);

			if (json != null) {
				try {
					JSONObject jsonObj = new JSONObject(json);
					if (jsonObj != null) {
						JSONArray packages = jsonObj
								.getJSONArray("packages");

						for (int i = 0; i < packages.length(); i++) {
							JSONObject pkgObj = (JSONObject) packages.get(i);
							Package pkg = new Package(pkgObj.getString("table_name"));
							packageList.add(pkg);
						}
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

			} else {
				Log.e("JSON Data", "Didn't receive any data from server!");
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (pDialog.isShowing())
				pDialog.dismiss();
			populateSpinner();
		}

	}




	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		
		Log.d("status","inside onitemselected");
		/*Toast.makeText(
				getApplicationContext(),
						parent.getItemAtPosition(position).toString() + " Selected" ,
				Toast.LENGTH_LONG).show();
		*/

		
		
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}
	
	 public void startDialog(View v) {
	        Intent intent = new Intent(GetPkgDisplay.this, DialogActivity.class);
	        startActivity(intent);
	    }
}
