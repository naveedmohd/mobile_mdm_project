package com.example.innovation_day;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.innovation_day.Get_Rowidobject_List.MergeRecords;

import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.util.SparseBooleanArray;
import android.view.View.OnClickListener;


import android.app.AlertDialog;
import android.content.DialogInterface;


public class ActivityUnmerge extends ListActivity{
    
    private ProgressDialog m_ProgressDialog = null; 
    private ArrayList<UnmergeRecord> m_unmergeRecord = null;
    private UnmergeAdapter m_adapter;
    private Runnable viewUnmergeRecords;

	JSONParser jParser = new JSONParser();
	JSONArray rowidobjects = null;
	ArrayList<String> rowid_object_array = new ArrayList<String>();
	HashMap<String, String> map = new HashMap<String, String>();

    //private String get_unmerge_records = "http://192.168.2.6/android_connect/get_to_be_unmerged.php";	    
    //private String url_unmerge_records = "http://192.168.2.6/android_connect/unmerge_records.php";
    
    //private String get_unmerge_records = "http://10.1.220.44/android_connect/get_to_be_unmerged.php";	    
    //private String url_unmerge_records = "http://10.1.220.44/android_connect/unmerge_records.php";

    
  private String get_unmerge_records = "http://192.168.1.65/android_connect/get_to_be_unmerged.php";
  private String url_unmerge_records = "http://192.168.1.65/android_connect/unmerge_records.php";
    
    private static final String TAG_SUCCESS = "success";
    String pkey_item;
    String system_item;
    String pkey_select;
    String system_select;
    boolean checked;
    private static final String PKEY_SRC_OBJECT = "pkey_src_object";
    private static final String ROWID_SYSTEM = "rowid_system";

    
    ArrayList<String> myArray;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.u_list_item);
        m_unmergeRecord = new ArrayList<UnmergeRecord>();
        
        
        viewUnmergeRecords = new Runnable(){
            @Override
            public void run() {
                getUnmergeRecords();
            }
        };
        
        
        Thread thread =  new Thread(null, viewUnmergeRecords, "MagentoBackground");
        thread.start();
        m_ProgressDialog = ProgressDialog.show(ActivityUnmerge.this,    
              "Please wait...", "Retrieving data ...", true);

        this.m_adapter = new UnmergeAdapter(this, R.layout.unmerge_layout, m_unmergeRecord);
        setListAdapter(this.m_adapter);

        
        ListView lv = getListView();
        
        //on itemclick listener
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				// getting values from selected ListItem
				pkey_item = ((TextView) view.findViewById(R.id.txt_pkey_src_object)).getText().toString();
				Log.d("pkey chosen:",pkey_item);

				system_item = ((TextView) view.findViewById(R.id.txt_rowid_system)).getText().toString();
				Log.d("system chosen:",system_item);

				
			}
		});
		
		//unmerge button listener

		Button btnUnMergeCandidates = (Button) findViewById(R.id.btnUnMergeCandidates);
		

		btnUnMergeCandidates.setOnClickListener(new OnClickListener() {
        	@Override        	   

        	public void onClick(View v) {
        		ListView lv = getListView();
        		
        		//TextView bt = (TextView) v.findViewById(R.id.txt_pkey_src_object);
        		//Log.d("what is" , bt.getText().toString());
        		
        		//String pkey1 = ((TextView) v.findViewById(R.id.txt_pkey_src_object)).getText().toString();
				//Log.d("which pkey is this:",pkey1);
				
        		lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                //Bundle extras = new Bundle();
                //myArray = new ArrayList<String>();
                SparseBooleanArray sp = lv.getCheckedItemPositions();

              

                for(int i=0;i<lv.getAdapter().getCount();i++){             	
               	
                	UnmergeRecord o = m_unmergeRecord.get(i);
                	Log.d("varpkey ",o.getPkeySourceObject().toString());
                	pkey_select = o.getPkeySourceObject().toString();
                	system_select = o.getRowidSystem().toString();
                	
                	if ( 
                		pkey_item.equals("PKEY_SRC_OBJECT : "+pkey_select) 
                		&& system_item.equals("ROWID_SYSTEM    : "+system_select)
                		){               		
                		
                		
        				AlertDialog.Builder alertDialog = new AlertDialog.Builder(ActivityUnmerge.this);

        				// Setting Dialog Title
        				alertDialog.setTitle("Unmerge Alert");

        				// Setting Dialog Message
        				alertDialog.setMessage("Are you sure you want to unmerge this record?");

        				// Setting Icon to Dialog
        				//alertDialog.setIcon(R.drawable.tick);

        				
        				alertDialog.setPositiveButton("YES",
        						new DialogInterface.OnClickListener() {
        							public void onClick(DialogInterface dialog,int which) {
        								// Write your code here to execute after dialog
        								//Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();

        								new UnMergeRecords().execute();

        								//Toast.makeText(getApplicationContext(), "Records Merged Successfully", Toast.LENGTH_SHORT).show();

        								Intent j = new Intent(ActivityUnmerge.this, AndroidTabLayoutActivity.class);
        				    			startActivity(j);
        								
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

        							
                        //new UnMergeRecords().execute();

                    	//Intent j = new Intent(ActivityUnmerge.this, AndroidTabLayoutActivity.class);
            			//startActivity(j);
                		
                	}
                	

                	//String tst = getListView().getAdapter().getItem((int)getListView().getCheckItemIds()[i]).toString();
                	//Log.d("what is test:",tst);
                	
                	/*
                	if(sp.get(i)){
                		
                	
                		String mypkey = lv.getCheckItemIds().toString();
                		Log.d("mypkey chosen on button:",mypkey);
                		
                		String pkey = lv.getAdapter().getItem(i).toString();

        				Log.d("pkey chosen on button:",pkey);

                	}*/
                }

                //new MergeRecords().execute();
            	//Intent i = new Intent(Get_Rowidobject_List.this, AndroidTabLayoutActivity.class);
    			//startActivity(i);
        	}
        });

		
		
		
    }
    
    
    
    private Runnable returnRes = new Runnable() {

        @Override
        public void run() {
            if(m_unmergeRecord != null && m_unmergeRecord.size() > 0){
                m_adapter.notifyDataSetChanged();
                for(int i=0;i<m_unmergeRecord.size();i++)
                m_adapter.add(m_unmergeRecord.get(i));
            }
            m_ProgressDialog.dismiss();
            m_adapter.notifyDataSetChanged();
        }
    };
    
    /*
    private void getUnmergeRecords(){
        try{
        	m_unmergeRecord = new ArrayList<UnmergeRecord>();
        	UnmergeRecord o1 = new UnmergeRecord();
            o1.setRowidObject("SF services");
            o1.setPkeySourceObject("Pending");
            o1.setRowidSystem("abc");
            UnmergeRecord o2 = new UnmergeRecord();
            o2.setRowidObject("SF -New");
            o2.setPkeySourceObject("ACtive");
            o2.setRowidSystem("xyz");
            m_unmergeRecord.add(o1);
            m_unmergeRecord.add(o2);
            //Thread.sleep(5000);
            //Log.d("ARRAY", ""+ m_orders.size());
          } catch (Exception e) { 
            Log.e("BACKGROUND_PROC", e.getMessage());
          }
          runOnUiThread(returnRes);
      }
      */
    
    private void getUnmergeRecords(){

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		JSONObject json = jParser.makeHttpRequest(get_unmerge_records, "GET", params);

		m_unmergeRecord = new ArrayList<UnmergeRecord>();

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
			
				UnmergeRecord ur = new UnmergeRecord();


				Log.d("adding myrowid is::::",rowid);
				
				map.put("ROWID_OBJECT", rowid);
				map.put("PKEY_SRC_OBJECT", pkey);
				map.put("ROWID_SYSTEM", rowidsys);

				ur.setRowidObject(rowid);
				ur.setPkeySourceObject(pkey);
				ur.setRowidSystem(rowidsys);
				
				m_unmergeRecord.add(ur);
			
				
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		runOnUiThread(returnRes);
		
    }
        
    private class ViewHolder {
        TextView tt;
        TextView bt;
        TextView xt;
   }
      
        
    private class UnmergeAdapter extends ArrayAdapter<UnmergeRecord> {

        private ArrayList<UnmergeRecord> items;

        public UnmergeAdapter(Context context, int textViewResourceId, ArrayList<UnmergeRecord> items) {
                super(context, textViewResourceId, items);
                this.items = items;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                ViewHolder holder;
                
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.unmerge_layout, null);
                    holder = new ViewHolder();
                    holder.tt = (TextView) v.findViewById(R.id.txt_rowid_object);
                    holder.bt = (TextView) v.findViewById(R.id.txt_pkey_src_object);
                    holder.xt = (TextView) v.findViewById(R.id.txt_rowid_system);
                    v.setTag(holder);
                    
                }else{
                	holder = (ViewHolder) convertView.getTag();
                }
                
                UnmergeRecord o = items.get(position);
                
                Log.d("what is :", o.getPkeySourceObject());
                
                holder.tt.setText("ROWID_OBJECT    : "+ o.getRowidObject());
                holder.bt.setText("PKEY_SRC_OBJECT : "+ o.getPkeySourceObject());
                holder.xt.setText("ROWID_SYSTEM    : "+ o.getRowidSystem());
                
                


                /*if (o != null) {
                        TextView tt = (TextView) v.findViewById(R.id.txt_rowid_object);
                        TextView bt = (TextView) v.findViewById(R.id.txt_pkey_src_object);
                        TextView xt = (TextView) v.findViewById(R.id.txt_rowid_system);
                        
                        if (tt != null) {
                              tt.setText("RowidObject: "+o.getRowidObject());                            }
                        if(bt != null){
                              bt.setText("PkeySrcObject: "+ o.getPkeySourceObject());
                        }
                        if(xt != null){
                            xt.setText("RowidSystem: "+ o.getRowidSystem());
                      }
                }
                */
                return v;
        }
}
    
    
    class UnMergeRecords extends AsyncTask<String,String,String>{
        
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			m_ProgressDialog = new ProgressDialog(ActivityUnmerge.this);
			m_ProgressDialog.setMessage("Unmerging Record...");
			m_ProgressDialog.setIndeterminate(false);
			m_ProgressDialog.setCancelable(true);
			m_ProgressDialog.show();
		}
		
		protected String doInBackground(String... args) {


		
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			Log.d("pkey1",pkey_select);
			Log.d("sys1",system_select);
			params.add(new BasicNameValuePair(PKEY_SRC_OBJECT, pkey_select));
			params.add(new BasicNameValuePair(ROWID_SYSTEM, system_select));

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
			m_ProgressDialog.dismiss();
		}

    	
    }

    
}