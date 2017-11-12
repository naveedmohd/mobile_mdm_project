package com.example.innovation_day;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.content.Intent;

public class AndroidTabLayoutActivity extends TabActivity {
    /** Called when the activity is first created. */
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        TabHost tabHost = getTabHost();

        // Tab for DataManager
        TabSpec dataspec = tabHost.newTabSpec("DataManager");
        dataspec.setIndicator("DataManager", getResources().getDrawable(R.drawable.edit));
        Intent datamgrIntent = new Intent(this, GetPkgDisplay.class);
        dataspec.setContent(datamgrIntent);


        // Tab for MergeManager
        TabSpec mergespec = tabHost.newTabSpec("MergeManager");
        // setting Title and Icon for the Tab
        mergespec.setIndicator("MergeManager", getResources().getDrawable(R.drawable.merge));
        Intent mergemgrIntent = new Intent(this, Get_Rowidobject_List.class);
        mergespec.setContent(mergemgrIntent);

        
        // Tab for MergeManager
        TabSpec unmergespec = tabHost.newTabSpec("UnmergeManager");
        // setting Title and Icon for the Tab
        unmergespec.setIndicator("UnmergeManager", getResources().getDrawable(R.drawable.unmerge));
        Intent unmergeIntent = new Intent(this, ActivityUnmerge.class);
        unmergespec.setContent(unmergeIntent);

        

        // Adding all TabSpec to TabHost
        tabHost.addTab(dataspec); // Adding photos tab
        tabHost.addTab(mergespec); // Adding songs tab
        tabHost.addTab(unmergespec); // Adding songs tab


        
        
    }
}