package com.jacktech.gymik;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;
import com.jacktech.gymik.Adapters.NavigationAdapter;
import com.jacktech.gymik.fragments.MapFragment;
import com.jacktech.gymik.fragments.MoodleFragment;
import com.jacktech.gymik.fragments.NewsFragment;
import com.jacktech.gymik.fragments.RozvrhFragment;
import com.jacktech.gymik.fragments.SuplovFragment;
import com.jacktech.gymik.fragments.ZnamkyFragment;
import com.jacktech.gymik.server.BackgroundService;

public class GymikActivity extends AbstractActivity {

	private boolean settingsOpen = false;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private List<NavigationItem> mNavigationArray;
	private Tracker analyticsTracker;
	private int beforeSelected = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setTheme(R.style.Theme_Sherlock_Light_DarkActionBar);
		this.setContentView(R.layout.navigation_layout);
		
		EasyTracker.getInstance().setContext(this);
		analyticsTracker = EasyTracker.getTracker();
		
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mNavigationArray = loadNavigation();
        mDrawerList.setAdapter(new NavigationAdapter(this,mNavigationArray));
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				updateScreen(pos);
			}
		});
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        
        ActionBarDrawerToggle abDrawerToggle = new ActionBarDrawerToggle(this,
        		mDrawerLayout, 
        		R.drawable.ic_drawer,
        		R.string.drawer_open, R.string.drawer_close){
        	public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(abDrawerToggle);
        
		dw = new DataWorker(this);
		config = new Config(dw.getConfig(),dw);
		updateScreen(0);
		
		if(!backgroundServiceRunning())
			startService(new Intent(this, BackgroundService.class));
	}
	
	private boolean backgroundServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (BackgroundService.class.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}

	private List<NavigationItem> loadNavigation() {
		ArrayList<NavigationItem> retList = new ArrayList<NavigationItem>();
		retList.add(new NavigationItem(R.drawable.ic_news, "Suplování"));
		retList.add(new NavigationItem(R.drawable.ic_rozvrh,"Rozvrh"));
		retList.add(new NavigationItem(R.drawable.ic_map, "Mapa školy"));
		retList.add(new NavigationItem(0, "Novinky"));
		retList.add(new NavigationItem(0, "Moodle"));
		retList.add(new NavigationItem());
		retList.add(new NavigationItem(0, "Moje známky"));
		return retList;
	}

	public void updateScreen(int screen){
		Fragment fragment = null;
		switch(screen){
			case 1:
				fragment = new RozvrhFragment();
				analyticsTracker.sendEvent("menu_item", "click", "rozvrh", 1L);
				break;
			case 0:
				fragment = new SuplovFragment();
				analyticsTracker.sendEvent("menu_item", "click", "suplov", 1L);
				break;
			case 2:
				fragment = new MapFragment();
				analyticsTracker.sendEvent("menu_item", "click", "mapa", 1L);
				break;
			case 3:
				fragment = new NewsFragment();
				analyticsTracker.sendEvent("menu_item", "click", "news", 1L);
				break;
			case 6:
				fragment = new ZnamkyFragment();
				analyticsTracker.sendEvent("menu_item", "click", "moje_znamky", 1L);
				return;
			case 4:
				fragment = new MoodleFragment();
				//startMoodleApp();
				analyticsTracker.sendEvent("menu_item", "click", "moodle", 1L);
				break;
			default:
				return;
		}
		FragmentManager fragmentManager = getSupportFragmentManager();
	    fragmentManager.beginTransaction()
	                   .replace(R.id.content_frame, fragment)
	                   .commit();
	    mDrawerList.setItemChecked(screen, true);
        getSupportActionBar().setTitle(mNavigationArray.get(screen).text);
        mDrawerLayout.closeDrawer(mDrawerList);
        beforeSelected = screen;
	}
	
	/*private void startMoodleApp(){
		boolean isInstalled = false;
		
		PackageManager pm = getPackageManager();
		try{
			pm.getPackageInfo("com.moodle.moodlemobile", PackageManager.GET_ACTIVITIES);
			isInstalled = true;
		}catch(Exception e){
			isInstalled = false;
		}
		
		if(isInstalled){
			Intent i = pm.getLaunchIntentForPackage("com.moodle.moodlemobile");
			startActivity(i);
		}else{
			Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.moodle.moodlemobile"));
			startActivity(i);
		}
	}*/

	@Override
	public void onResume(){
		super.onResume();
		if(settingsOpen){
			settingsOpen = false;
			config.reloadConfig();
		}
	}
	
	public void openSettings(Activity a){
		settingsOpen = true;
		a.startActivity(new Intent(a, SettingsActivity.class));
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
			case android.R.id.home:
				if(mDrawerLayout.isDrawerOpen(mDrawerList))
					mDrawerLayout.closeDrawer(mDrawerList);
				else
					mDrawerLayout.openDrawer(mDrawerList);
				break;
			case R.id.action_settings:
				openSettings(this);
				return true;
		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.gymik, menu);
		return true;
	}
	
	@Override
	public void onStart() {
		super.onStart();
	    EasyTracker.getInstance().activityStart(this);
	}

	@Override
	public void onStop() {
		super.onStop();
	    EasyTracker.getInstance().activityStop(this);
	}

}
