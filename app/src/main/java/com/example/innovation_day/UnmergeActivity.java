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
import android.widget.Button;
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
import android.util.SparseBooleanArray;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.widget.BaseAdapter;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;


public class UnmergeActivity extends ListActivity {


	public ArrayList<HashMap> list;
	
	// List view
	private ListView ulv;

	// Listview u_adapter
	ArrayAdapter<String> u_adapter;

	// Search EditText
	EditText u_inputSearch;
	
	JSONParser jParser = new JSONParser();
	JSONArray rowidobjects = null;



    // Listview Data
    ArrayList<String> rowid_object_array = new ArrayList<String>();
    HashMap<String, String> map = new HashMap<String, String>();
    
   
    ProgressDialog pDialog;

    private static final String TAG_SUCCESS = "success";
    
    //private String url_unmerge_records = "http://10.2.115.84/android_connect/get_to_be_unmerged.php";
    private String url_unmerge_records = "http://192.168.1.65/android_connect/get_to_be_unmerged.php";
    //private String url_merge_records = "http://192.168.2.5/android_connect/unmerge_records.php";
    
   
    private static final String SOURCE_ROWID_OBJECT = "source_rowid";
    private static final String TARGET_ROWID_OBJECT = "target_rowid";
    
	// ArrayList for Listview
	ArrayList<String> myArray;
	String src = "";
	String tgt = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.unmerge_layout);
        setContentView(R.layout.u_list_item);
        
        Button btnUnMrgCandidates = (Button) findViewById(R.id.btnUnMergeCandidates);
        
        ulv = (ListView) findViewById(android.R.id.list);
        //u_inputSearch = (EditText) findViewById(R.id.u_inputSearch);

        //new LoadRowidValues().execute();
        populateList();
        
        //SimpleAdapter v_adapter = new SimpleAdapter(UnmergeActivity.this, list, R.layout.unmerge_layout,new String[] {"Rowid_Object", "Pkey_Src_Object", "Rowid_System"}, new int[] {R.id.txt_rowid_object, R.id.txt_pkey_src_object, R.id.txt_rowid_system});
//        mSimple = new SimpleAdapter(this,list,R.layout.unmerge_layout,new String[] {"Rowid_Object","Pkey_Src_Object","Rowid_System"},new Int[] {R.id.txt_rowid_object,R.id.txt_pkey_src_object,R.id.txt_rowid_system});
        
        
        ListviewAdapter u_adapter = new ListviewAdapter(UnmergeActivity.this, list);
        
        
        // Adding items to listview
        //u_adapter = new ArrayAdapter<String>(this, R.layout.u_list_item, R.id.u_ridvalue, rowid_object_array);
               
        ulv.setAdapter(u_adapter);
        
        /**
         * Enabling Search Filter
         * */
        /*
        u_inputSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
				// When user changed the Text
				UnmergeActivity.this.u_adapter.getFilter().filter(cs);
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
               
        */
        
       //ulv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        
               
        btnUnMrgCandidates.setOnClickListener(new OnClickListener() {
        	@Override        	   
        		public void onClick(View v) {
                //Intent mrgIntent = new Intent(UnmergeActivity.this,MergeRecordsActivity.class);
                Bundle extras = new Bundle();
                myArray = new ArrayList<String>();
                SparseBooleanArray sp = ulv.getCheckedItemPositions();
                TextView tv;                
                int j;
                for(int i=0;i<ulv.getAdapter().getCount();i++){             	
                	
                	if(sp.get(i)){
                		
                		if (src.isEmpty() && tgt.isEmpty()){
                			src = ulv.getAdapter().getItem(i).toString();
                			//map.put("source", ulv.getAdapter().getItem(i).toString());                			
                		}else{
                			tgt=ulv.getAdapter().getItem(i).toString();
                			//map.put("target", ulv.getAdapter().getItem(i).toString());

                		}                		               		               		
                	}
                }

                
                //src = map.get("source");
                //tgt = map.get("target");
                
                //Log.d("source is:" ,src); 
                //Log.d("target is:" , tgt);
               
                	
                new UnMergeRecords().execute();
        		
        	   }
        	


        });

        
        
        ulv.setOnItemClickListener(new OnItemClickListener() {
        	
        	@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
        		
        		String rid = ((TextView) view.findViewById(R.id.txt_rowid_object)).getText().toString();
        		Log.d("select",rid);
        		
        	}
        	
        

		});
        

        
        
    }
    
    
    
    class UnMergeRecords extends AsyncTask<String,String,String>{
    
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(UnmergeActivity.this);
			pDialog.setMessage("Merging Records...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		
		protected String doInBackground(String... args) {


			Log.d("in merge source:", src);
			Log.d("in merge target:", tgt);
			
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			params.add(new BasicNameValuePair(SOURCE_ROWID_OBJECT, src));
			params.add(new BasicNameValuePair(TARGET_ROWID_OBJECT, tgt));

			// sending modified data through http request
			// Notice that update product url accepts POST method
			JSONObject json = jParser.makeHttpRequest(url_unmerge_records,
					"POST", params);

			// check json success tag
			try {
				int success = json.getInt(TAG_SUCCESS);
				
				if (success == 1) {
					// successfully updated
					Intent i = getIntent();
					// send result code 100 to notify about product update
					setResult(100, i);
					finish();
				} else {
					// failed to update product
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}
        	
		
		
		
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once product uupdated
			pDialog.dismiss();
		}

    	
    }
    
    private void populateList(){
    	
    	list = new ArrayList<HashMap>();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		JSONObject json = jParser.makeHttpRequest(url_unmerge_records, "GET", params);
		
		String jsonstr = json.toString();
		// Check your log cat for JSON reponse
		Log.d("All unmerge rowids: ", jsonstr);

		
		try {
			// Checking for SUCCESS TAG
			int success = json.getInt(TAG_SUCCESS);

			rowidobjects = json.getJSONArray("sourcerowid");
			//String myrowid = json.getJSONArray("sourcerowid").toString();

			//String vrowid[] = null;
			for(int i=0;i<rowidobjects.length();i++){
		
				JSONObject c = rowidobjects.getJSONObject(i);
				String rowid = c.getString("rowid_object");
				String pkey = c.getString("pkey_src_object");
				String rowidsys = c.getString("rowid_system");
				
				Log.d("myrowid is::::",rowid);
				Log.d("myrowid is::::",pkey);
				Log.d("myrowid is::::",rowidsys);
				
				rowid_object_array.add(c.getString("rowid_object"));
				rowid_object_array.add(c.getString("pkey_src_object"));
				rowid_object_array.add(c.getString("rowid_system"));

				Log.d("adding myrowid is::::",rowid);
				
				map.put("ROWID_OBJECT", rowid);
				map.put("PKEY_SRC_OBJECT", pkey);
				map.put("ROWID_SYSTEM", rowidsys);

				
				list.add(map);
			
				
				Log.d("added myrowid is::::",rowid);
				
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		
    }

	class LoadRowidValues extends AsyncTask<String, String, String> {


		/**
		 * getting All rowidobjects from url
		 * */
		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// getting JSON string from URL
			
			Log.d("running","in background");
			JSONObject json = jParser.makeHttpRequest(url_unmerge_records, "GET", params);
			
			String jsonstr = json.toString();
			// Check your log cat for JSON reponse
			Log.d("All unmerge rowids: ", jsonstr);

			try {
				// Checking for SUCCESS TAG
				int success = json.getInt(TAG_SUCCESS);

				rowidobjects = json.getJSONArray("sourcerowid");
				//String myrowid = json.getJSONArray("sourcerowid").toString();

				//String vrowid[] = null;
				for(int i=0;i<rowidobjects.length();i++){
			
					JSONObject c = rowidobjects.getJSONObject(i);
					String rowid = c.getString("rowid_object");
					String pkey = c.getString("pkey_src_object");
					String rowidsys = c.getString("rowid_system");
					
					Log.d("myrowid is::::",rowid);
					Log.d("myrowid is::::",pkey);
					Log.d("myrowid is::::",rowidsys);
					
					rowid_object_array.add(c.getString("rowid_object"));
					rowid_object_array.add(c.getString("pkey_src_object"));
					rowid_object_array.add(c.getString("rowid_system"));

					Log.d("adding myrowid is::::",rowid);
					
					map.put("ROWID_OBJECT", rowid);
					map.put("PKEY_SRC_OBJECT", pkey);
					map.put("ROWID_SYSTEM", rowidsys);

					
					list.add(map);
					
					Log.d("added myrowid is::::",rowid);
					
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}



	}	    
    
	
	
	

}
