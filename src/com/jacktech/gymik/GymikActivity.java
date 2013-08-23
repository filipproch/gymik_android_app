package com.jacktech.gymik;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jacktech.gymik.Adapters.NavigationAdapter;
import com.jacktech.gymik.fragments.AktualityFragment;
import com.jacktech.gymik.fragments.BakalariFragment;
import com.jacktech.gymik.fragments.MapFragment;
import com.jacktech.gymik.fragments.MoodleFragment;
import com.jacktech.gymik.fragments.RozvrhFragment;

public class GymikActivity extends AbstractActivity {

	private boolean settingsOpen = false;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private List<NavigationItem> mNavigationArray;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setTheme(R.style.Theme_Sherlock_Light_DarkActionBar);
		this.setContentView(R.layout.navigation_layout);
		
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mNavigationArray = loadNavigation();
        mDrawerList.setAdapter(new NavigationAdapter(this,R.layout.drawer_list_item, mNavigationArray));
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
	}
	
	private List<NavigationItem> loadNavigation() {
		ArrayList<NavigationItem> retList = new ArrayList<NavigationItem>();
		retList.add(new NavigationItem(R.drawable.ic_news, "Aktuality"));
		retList.add(new NavigationItem(R.drawable.ic_rozvrh,"Rozvrh"));
		retList.add(new NavigationItem(R.drawable.ic_map, "Mapa školy"));
		retList.add(new NavigationItem(0, "Bakaláři"));
		retList.add(new NavigationItem(0, "Moodle"));
		return retList;
	}

	public void updateScreen(int screen){
		Fragment fragment = null;
		switch(screen){
			case 1:
				fragment = new RozvrhFragment();
				break;
			case 0:
				fragment = new AktualityFragment();
				break;
			case 2:
				fragment = new MapFragment();
				break;
			case 3:
				fragment = new BakalariFragment();
				break;
			case 4:
				fragment = new MoodleFragment();
				break;
		}
		FragmentManager fragmentManager = getSupportFragmentManager();
	    fragmentManager.beginTransaction()
	                   .replace(R.id.content_frame, fragment)
	                   .commit();
	    mDrawerList.setItemChecked(screen, true);
        getSupportActionBar().setTitle(mNavigationArray.get(screen).text);
        mDrawerLayout.closeDrawer(mDrawerList);
	}

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

}
