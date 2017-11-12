package com.example.innovation_day;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.ListActivity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.Intent;
import android.widget.TextView;
import android.widget.EditText;
import android.text.TextWatcher;
import android.text.Editable;

public class GetPkgSrcTgtList extends ListActivity implements OnItemSelectedListener {
	
	private ListView lv;
	
	// Listview Adapter
	ArrayAdapter<String> adapter;
	
	// Spinner Adapter
	ArrayAdapter<String> spinnerAdapter;
		
	JSONParser jParser = new JSONParser();
	JSONArray rowidobjects = null;

	private Spinner spinnerPackage;

	// Search EditText
	EditText inputSearch;
	String rid;
	String ridvalue[];
	
	// array list for spinner adapter
	private ArrayList<Package> packageList;
	ArrayList<HashMap<String, String>> rowidList;

	private static final String TAG_SUCCESS = "success";
	private static final String TAG_ROWIDS = "sourcerowid";
	private static final String TAG_ROWID = "rowid_object";
	private static final String TAG_ROWID_VALUE = "rowid_object";
	private static final String PACKAGE_NAME = "package_name";

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


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pkg_src_tgt_list_view);

		spinnerPackage = (Spinner) findViewById(R.id.spinPackage);

		packageList = new ArrayList<Package>();
		rowidList = new ArrayList<HashMap<String, String>>();

		
		// spinner item select listener
	/*
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
					Log.d("pkgname:",package_name);
					Log.d("pkgdetails: ",parent.getItemAtPosition(position).toString());
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) { }
		});
		
		*/
		//load rowidobjects

		
		new LoadRowidValues().execute();

		
		Log.d("rowidvalue:" , ridvalue[0]);
		//lv = getListView();
		lv = (ListView) findViewById(R.id.list_view);
		inputSearch = (EditText) findViewById(R.id.inputSearch);
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.ridvalue, ridvalue);
        lv.setAdapter(adapter);

        
        inputSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
				// When user changed the Text
				GetPkgSrcTgtList.this.adapter.getFilter().filter(cs);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
			}
		});
        
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				String rid = ((TextView) view.findViewById(R.id.ridvalue)).getText().toString();
					Log.d("rid is:", rid);
				
			}
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
		spinnerAdapter = new ArrayAdapter<String>(this,
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
			pDialog = new ProgressDialog(GetPkgSrcTgtList.this);
			pDialog.setMessage("Fetching Packages..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
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
		Toast.makeText(
				getApplicationContext(),
						parent.getItemAtPosition(position).toString() + " Selected" ,
				Toast.LENGTH_LONG).show();





	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}
	
	
	

	class LoadRowidValues extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
	
		
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(GetPkgSrcTgtList.this);
			pDialog.setMessage("Loading rowids. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();	
		}
		

		/**
		 * getting All products from url
		 * */
		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// getting JSON string from URL
			
			Log.d("running","in background");
			JSONObject json = jParser.makeHttpRequest(SOURCE_ROWID, "GET", params);
			
			// Check your log cat for JSON reponse
			Log.d("All rowids: ", json.toString());

			try {
				// Checking for SUCCESS TAG
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// products found
					// Getting Array of rowids
					rowidobjects = json.getJSONArray(TAG_ROWIDS);

					// looping through All Products
					for (int i = 0; i < rowidobjects.length(); i++) {
						JSONObject c = rowidobjects.getJSONObject(i);

						// Storing each json item in variable
						rid = c.getString(TAG_ROWID);
						ridvalue[i] = c.getString(TAG_ROWID_VALUE);
						

						// creating new HashMap
						HashMap<String, String> map = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						map.put(TAG_ROWID, rid);
						map.put(TAG_ROWID_VALUE, ridvalue[i]);

						// adding HashList to ArrayList
						rowidList.add(map);
					}
				} else {
					// no products found
					Log.d("No rowids found: ", json.toString());
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
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed JSON data into ListView
					 * */
					ListAdapter adapter = new SimpleAdapter(
							GetPkgSrcTgtList.this, rowidList,
							R.layout.list_item, new String[] { TAG_ROWID,
									TAG_ROWID_VALUE},
							new int[] { R.id.ridvalue, R.id.ridvalue });
					// updating listview
					setListAdapter(adapter);
				}
			});

		}

	}	
}
