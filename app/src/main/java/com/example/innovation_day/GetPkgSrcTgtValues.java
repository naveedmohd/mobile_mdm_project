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


public class GetPkgSrcTgtValues extends Activity implements OnItemSelectedListener {

	private Spinner spinnerPackage;
	private Spinner spinnerSrcrowid;
	private Spinner spinnerTgtrowid;

	// array list for spinner adapter
	
	private ArrayList<Package> packageList;
	private ArrayList<SourceRowid> srcrowidList;
	private ArrayList<TargetRowid> tgtrowidList;
	
	ProgressDialog pDialog;

	// Url to get all categories
	//private String URL_CATEGORIES = "http://10.2.115.84/android_connect/get_packages.php";
	//private String SOURCE_ROWID = "http://10.2.115.84/android_connect/get_source_rowid.php";
	//private String TARGET_ROWID = "http://10.2.115.84/android_connect/get_target_rowid.php";

	// Url to get all categories
	private String URL_CATEGORIES = "http://10.2.115.5/android_connect/get_packages.php";
	private String SOURCE_ROWID = "http://10.2.115.5/android_connect/get_source_rowid.php";
	private String TARGET_ROWID = "http://10.2.115.5/android_connect/get_target_rowid.php";

	//private String URL_CATEGORIES = "http://10.1.221.221/android_connect/get_packages.php";
	//private String SOURCE_ROWID = "http://10.1.221.221/android_connect/get_source_rowid.php";
	//private String TARGET_ROWID = "http://10.1.221.221/android_connect/get_target_rowid.php";


	private static final String PACKAGE_NAME = "package_name";
	private static final String SOURCEROWID_VALUE = "source_rowid";
	private static final String TARGETROWID_VALUE = "target_rowid";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pkg_src_tgt_list);

		spinnerPackage = (Spinner) findViewById(R.id.spinPackage);
		spinnerSrcrowid = (Spinner) findViewById(R.id.spinSourcerowid);
		spinnerTgtrowid = (Spinner) findViewById(R.id.spinTargetrowid);

		packageList = new ArrayList<Package>();
		srcrowidList = new ArrayList<SourceRowid>();
		tgtrowidList = new ArrayList<TargetRowid>();

		// spinner item select listener
		spinnerPackage.setOnItemSelectedListener(this);
		new GetPackages().execute();

		spinnerPackage.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
				//Toast.makeText( getApplicationContext(), parent.getItemAtPosition(position).toString() + " Selected" ,Toast.LENGTH_LONG).show();
				String package_name;			
				int count = spinnerPackage.getCount();
				int i;
				for(i=0; i<count; i++){
					package_name =spinnerPackage.getItemAtPosition(i).toString();
					Log.d("displa",package_name);
					Log.d("pkgname: ",parent.getItemAtPosition(position).toString());										
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) { }
		});

		spinnerSrcrowid.setOnItemSelectedListener(this);
		new GetSrcrowid().execute();

		spinnerSrcrowid.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
				//Toast.makeText( getApplicationContext(), parent.getItemAtPosition(position).toString() + " Selected" ,Toast.LENGTH_LONG).show();
				String source_rowid;			
				int count = spinnerSrcrowid.getCount();
				int i;
				for(i=0; i<count; i++){
					source_rowid =spinnerSrcrowid.getItemAtPosition(i).toString();
					Log.d("displa",source_rowid);
					Log.d("sourcerowid: ",parent.getItemAtPosition(position).toString());										
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) { }
		});		

		spinnerTgtrowid.setOnItemSelectedListener(this);
		new GetTgtrowid().execute();

		spinnerTgtrowid.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
				//Toast.makeText( getApplicationContext(), parent.getItemAtPosition(position).toString() + " Selected" ,Toast.LENGTH_LONG).show();
				String target_rowid;			
				int count = spinnerTgtrowid.getCount();
				int i;
				for(i=0; i<count; i++){
					target_rowid =spinnerTgtrowid.getItemAtPosition(i).toString();
					Log.d("displa",target_rowid);
					Log.d("target_rowid: ",parent.getItemAtPosition(position).toString());										
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


	//populate spinner with source rowidobjects
	private void populateSrcrowidSpinner() {
		List<String> lables = new ArrayList<String>();

		for (int i = 0; i < srcrowidList.size(); i++) {
			lables.add(srcrowidList.get(i).getName());
		}

		// Creating adapter for spinner
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, lables);

		// Drop down layout style - list view with radio button
		spinnerAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// attaching data adapter to spinner
		spinnerSrcrowid.setAdapter(spinnerAdapter);
	}

	//populate sipnner with target rowidobjects
	private void populateTgtrowidSpinner() {
		List<String> lables = new ArrayList<String>();

		for (int i = 0; i < tgtrowidList.size(); i++) {
			lables.add(tgtrowidList.get(i).getName());
		}

		// Creating adapter for spinner
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, lables);

		// Drop down layout style - list view with radio button
		spinnerAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// attaching data adapter to spinner
		spinnerTgtrowid.setAdapter(spinnerAdapter);
	}

	
	/**
	 * Async task to get all food categories
	 * */
	private class GetPackages extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(GetPkgSrcTgtValues.this);
			//pDialog.setMessage("Fetching Packages..");
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



	//Get source rowid
	private class GetSrcrowid extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(GetPkgSrcTgtValues.this);
			//pDialog.setMessage("Fetching SourceRowids..");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			ServiceHandler jsonParser = new ServiceHandler();
			String json = jsonParser.makeServiceCall(SOURCE_ROWID, ServiceHandler.GET);

			Log.e("Response: ", "> " + json);

			if (json != null) {
				try {
					JSONObject jsonObj = new JSONObject(json);
					if (jsonObj != null) {
						JSONArray sourceRid = jsonObj
								.getJSONArray("sourcerowid");

						for (int i = 0; i < sourceRid.length(); i++) {
							JSONObject srcRidObj = (JSONObject) sourceRid.get(i);
							SourceRowid srcId = new SourceRowid(srcRidObj.getString("rowid_object"));
							srcrowidList.add(srcId);
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
			populateSrcrowidSpinner();
		}

	}
	
	
	//Get targetrowid

	private class GetTgtrowid extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(GetPkgSrcTgtValues.this);
			//pDialog.setMessage("Fetching Target Rowids..");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			ServiceHandler jsonParser = new ServiceHandler();
			String json = jsonParser.makeServiceCall(TARGET_ROWID, ServiceHandler.GET);

			Log.e("Response: ", "> " + json);

			if (json != null) {
				try {
					JSONObject jsonObj = new JSONObject(json);
					if (jsonObj != null) {
						JSONArray targetRid = jsonObj
								.getJSONArray("targetrowid");

						for (int i = 0; i < targetRid.length(); i++) {
							JSONObject tgtRidObj = (JSONObject) targetRid.get(i);
							TargetRowid tgtId = new TargetRowid(tgtRidObj.getString("rowid_object"));
							tgtrowidList.add(tgtId);
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
			populateTgtrowidSpinner();
		}

	}
	
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		
		Log.d("status","inside onitemselected");
		Toast.makeText(
				getApplicationContext(),
						parent.getItemAtPosition(position).toString() + " Selected" ,
				Toast.LENGTH_LONG).show();


		
		
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}
}
