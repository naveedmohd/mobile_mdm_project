package com.example.innovation_day;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.AlertDialog;
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
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.TextView;
import android.widget.EditText;
import android.text.TextWatcher;
import android.text.Editable;
import android.util.SparseBooleanArray;
import android.view.View.OnClickListener;
import android.content.Intent;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.example.innovation_day.AddConsumer.CreateNewConsumer;

import android.app.AlertDialog;
import android.content.DialogInterface;


public class Get_Rowidobject_List extends Activity {

	// List view
	private ListView lv;

	// Listview Adapter
	ArrayAdapter<String> adapter;

	// Search EditText
	EditText inputSearch;
	
	JSONParser jParser = new JSONParser();
	JSONArray rowidobjects = null;

    // Listview Data
    ArrayList<String> rowid_object_array = new ArrayList<String>();
    HashMap<String, String> map = new HashMap<String, String>();
    
    
    String rid;
    ArrayList<HashMap<String, String>> rowidList;
    
    ProgressDialog pDialog;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ROWID = "rowid_object";
    private static final String TAG_ROWID_VALUE = "rowid_object";
    
    private String SOURCE_ROWID = "http://192.168.1.65/android_connect/get_source_rowid.php";
    private String url_merge_records = "http://192.168.1.65/android_connect/merge_records.php";
    
    //private String SOURCE_ROWID = "http://192.168.2.6/android_connect/get_source_rowid.php";
    //private String url_merge_records = "http://192.168.2.6/android_connect/merge_records.php";

    //private String SOURCE_ROWID = "http://10.1.220.44/android_connect/get_source_rowid.php";
    //private String url_merge_records = "http://10.1.220.44/android_connect/merge_records.php";

    
    //private String SOURCE_ROWID = "http://10.1.221.221/android_connect/get_source_rowid.php";
    //private String url_merge_records = "http://10.1.221.221/android_connect/merge_records.php";

    
    private static final String TAG_ROWIDS = "sourcerowid";
    
    private static final String SOURCE_ROWID_OBJECT = "source_rowid";
    private static final String TARGET_ROWID_OBJECT = "target_rowid";
    
	// ArrayList for Listview
	ArrayList<HashMap<String, String>> productList;
	ArrayList<String> myArray;
	String src = "";
	String tgt = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rowidobject);
        
        Button btnMrgCandidates = (Button) findViewById(R.id.btnMergeCandidates);

        new LoadRowidValues().execute();
        Log.d("count of rowid:", "got count" );
        
        lv = (ListView) findViewById(R.id.list_view);
        inputSearch = (EditText) findViewById(R.id.inputSearch);

        Log.d("count of rowid:", "adding adapter" );

        // Adding items to listview
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.ridvalue, rowid_object_array);
        
        Log.d("count of rowid:", adapter.toString() );
        
        lv.setAdapter(adapter);
        
        Log.d("count of rowid:", "Adapter set" );

        /**
         * Enabling Search Filter
         * */
        inputSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
				// When user changed the Text
				Get_Rowidobject_List.this.adapter.getFilter().filter(cs);
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
               
        
        
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        
                
        btnMrgCandidates.setOnClickListener(new OnClickListener() {
        	@Override        	   
        		public void onClick(View v) {
                //Intent mrgIntent = new Intent(Get_Rowidobject_List.this,MergeRecordsActivity.class);
                Bundle extras = new Bundle();
                myArray = new ArrayList<String>();
                SparseBooleanArray sp = lv.getCheckedItemPositions();
                

                
                TextView tv;                
                int j;
                for(int i=0;i<lv.getAdapter().getCount();i++){             	

                    Log.d("chjecked merge :", ""+sp.get(i));
                    
                	if(sp.get(i)){
                		
                		if (src.isEmpty() && tgt.isEmpty()){
                			src = lv.getAdapter().getItem(i).toString();
                			map.put("source", lv.getAdapter().getItem(i).toString());
                			//lv.setSelected(true);
                		}else{
                			tgt=lv.getAdapter().getItem(i).toString();
                			map.put("target", lv.getAdapter().getItem(i).toString());
                			//lv.setSelected(true);

                		}                		               		               		
                	}
                }

                
                src = map.get("source");
                tgt = map.get("target");
                
                Log.d("source is:" ,src); 
                Log.d("target is:" , tgt);
               
                	
				
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(Get_Rowidobject_List.this);

				// Setting Dialog Title
				alertDialog.setTitle("Merge Alert");

				// Setting Dialog Message
				alertDialog.setMessage("Are you sure you want to merge records?");

				// Setting Icon to Dialog
				//alertDialog.setIcon(R.drawable.tick);

				
				alertDialog.setPositiveButton("YES",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int which) {
								// Write your code here to execute after dialog
								//Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();

				                new MergeRecords().execute();

								Toast.makeText(getApplicationContext(), "Records Merged Successfully", Toast.LENGTH_SHORT).show();

				            	Intent i = new Intent(Get_Rowidobject_List.this, AndroidTabLayoutActivity.class);
				    			startActivity(i);
								
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
                

        		/*
        	     StringBuffer str = new StringBuffer();
        	     View myview;
        	     TextView tv;
        	     for(int i=0;i<sp.size();i++){
        	      if(sp.valueAt(i)==true){
        	    	  
        	    	  if (src.isEmpty()){
        	    		  myview = lv.getChildAt(i);
        	    		 tv  = (TextView) myview.findViewById(R.id.ridvalue);
        	    	  }else{
        	    		  myview = lv.getChildAt(i+1);
        	    		  tv  = (TextView) myview.findViewById(R.id.ridvalue);
        	      }
        	    	  

        	    	  if (src.isEmpty()){
        	    	  //if (src.equals("") || (src==null)){
        	    		  src = tv.getText().toString();	
        	    		  Log.d("source string is:", src);
        	    		  extras.putString(SOURCE_ROWID_OBJECT, src);
        	    	  }
        	    	  
        	    	  if ( (src.isEmpty()) || (tgt.isEmpty()) ){
        	    	  //if (    (!src.equals("") || src!=null)  && (tgt.equals("") || tgt == null) )  {
        	    		  tgt = tv.getText().toString();
        	    		  Log.d("target string is:", tgt);
        	    		  extras.putString(TARGET_ROWID_OBJECT, tgt);
        	    	  }
        	    	  
        	    	  mrgIntent.putExtras(extras);
        	    	  //mrgIntent.putExtra(SOURCE_ROWID_OBJECT, s);        	    	  
        	    	  //mrgIntent.putExtra(TARGET_ROWID_OBJECT, s);
        	    	  //String s = lv.getChildAt(i).getContentDescription().toString();
        	       //String s = ((TextView) lv.getChildAt(i)).getText().toString();
        	       str = str.append(" "+src + " " + tgt);
        	      }
        	     }   
        	    //Toast.makeText(Get_Rowidobject_List.this, "Selected items are "+str.toString(), Toast.LENGTH_LONG).show();
        	     String xyz=mrgIntent.getStringExtra(SOURCE_ROWID_OBJECT);
        	     String xyz1=mrgIntent.getStringExtra(TARGET_ROWID_OBJECT);
        	     Log.d("before",xyz + ' ' + xyz1);
        	    startActivityForResult(mrgIntent,100);
        	    */
        	   }
        	
            //mergeRecords(map);



        });


        
        
        lv.setOnItemClickListener(new OnItemClickListener() {
        	
        	@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
        		
        		String rid = ((TextView) view.findViewById(R.id.ridvalue)).getText().toString();
        		Log.d("am i selecting",rid);
        		
        	}
        	
        

		});
        

        
        
    }
    
    
    
    class MergeRecords extends AsyncTask<String,String,String>{
    
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Get_Rowidobject_List.this);
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
			JSONObject json = jParser.makeHttpRequest(url_merge_records,
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
    

	class LoadRowidValues extends AsyncTask<String, String, String> {


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

				rowidobjects = json.getJSONArray("sourcerowid");
				//String myrowid = json.getJSONArray("sourcerowid").toString();

				//String vrowid[] = null;
				for(int i=0;i<rowidobjects.length();i++){
					JSONObject c = rowidobjects.getJSONObject(i);
					Log.d("jsonrowid:", c.getString("rowid_object"));
					rowid_object_array.add(c.getString("rowid_object"));

				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			
			return null;
		}



	}	    
    

}
