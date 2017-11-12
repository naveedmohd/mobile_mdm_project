package com.example.innovation_day;

import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.util.Log;
import android.widget.ArrayAdapter;

	public class ListviewAdapter extends BaseAdapter
	{
	    public ArrayList<HashMap> list;
	    Activity activity;

	    public ListviewAdapter(Activity activity, ArrayList<HashMap> list) {
	        super();
	        this.activity = activity;
	        this.list = list;
	    }

	    @Override
	    public int getCount() {
	        // TODO Auto-generated method stub
	        return list.size();
	    }

	    @Override
	    public Object getItem(int position) {
	        // TODO Auto-generated method stub
	        return list.get(position);
	    }

	    @Override
	    public long getItemId(int position) {
	        // TODO Auto-generated method stub
	        return 0;
	    }

/*
	    private class ViewHolder {
	           TextView txtFirst;
	           TextView txtSecond;
	           TextView txtThird;
	      }
*/

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        // TODO Auto-generated method stub
	    	TextView txtFirst;
	    	TextView txtSecond;
	    	TextView txtThird;

	    	if (convertView == null){
	        // TODO Auto-generated method stub
	                LayoutInflater inflater =  activity.getLayoutInflater();

	                    convertView = inflater.inflate(R.layout.unmerge_layout, null);
	    	}
	                    //holder = new ViewHolder();
	                    txtFirst = (TextView) convertView.findViewById(R.id.txt_rowid_object);
	                    txtSecond = (TextView) convertView.findViewById(R.id.txt_pkey_src_object);
	                    txtThird = (TextView) convertView.findViewById(R.id.txt_rowid_system);
	                    //convertView.setTag(holder);
	    	
	                String txt = txtFirst.toString();
	          
	                
	                Log.d("mytxt", txt);

	                HashMap map = list.get(position);
	                String myvar = map.get("ROWID_OBJECT").toString();
	                Log.d("myvar",myvar);
	                
	                txtFirst.setText(map.get("ROWID_OBJECT").toString());
	                txtSecond.setText(map.get("PKEY_SRC_OBJECT").toString());
	                txtThird.setText(map.get("ROWID_SYSTEM").toString());

	                

	            return convertView;

	    }

	}
