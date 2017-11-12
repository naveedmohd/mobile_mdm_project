package com.example.innovation_day;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class HeaderTabLayout extends TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        TabHost tabHost = getTabHost();

        // Tab for DataManager
        TabSpec dataspec = tabHost.newTabSpec("DataManager");
        dataspec.setIndicator("DataManager", getResources().getDrawable(R.drawable.edit));
        //Intent datamgrIntent = new Intent(this, PhotosActivity.class);
        //dataspec.setContent(datamgrIntent);


        // Tab for MergeManager
        TabSpec mergespec = tabHost.newTabSpec("MergeManager");
        // setting Title and Icon for the Tab
        mergespec.setIndicator("MergeManager", getResources().getDrawable(R.drawable.merge));
        //Intent mergemgrIntent = new Intent(this, SongsActivity.class);
        //mergespec.setContent(mergemgrIntent);

        // Tab for Videos
        TabSpec videospec = tabHost.newTabSpec("Videos");
        videospec.setIndicator("Videos", getResources().getDrawable(R.drawable.icon_videos_tab));
        //Intent videosIntent = new Intent(this, VideosActivity.class);
        //videospec.setContent(videosIntent);

        // Adding all TabSpec to TabHost
        tabHost.addTab(dataspec); // Adding photos tab
        tabHost.addTab(mergespec); // Adding songs tab
        tabHost.addTab(videospec); // Adding videos tab


    }
}