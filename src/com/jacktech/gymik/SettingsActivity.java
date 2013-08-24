package com.jacktech.gymik;

import java.util.Calendar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.view.Gravity;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.google.analytics.tracking.android.EasyTracker;

public class SettingsActivity extends SherlockPreferenceActivity{

	private Config config;
	private UpdateClass updater;
	
	@Override
	public void onCreate(Bundle savedInstanceBundle){
		super.onCreate(savedInstanceBundle);
		addPreferencesFromResource(R.layout.settings_layout);
		
		DataWorker dw = new DataWorker(this);
		config = new Config(dw.getConfig(),dw);
		updater = new UpdateClass(dw, config, this);
		
		Preference aboutPreference = (Preference) findPreference("aboutApp");
		aboutPreference.setOnPreferenceClickListener(new OnPreferenceClickListener(){

			public boolean onPreferenceClick(Preference p) {
				Calendar c = Calendar.getInstance();
				int year = c.get(Calendar.YEAR);
				PackageInfo pInfo;
				String version;
				try {
					pInfo = SettingsActivity.this.getPackageManager().getPackageInfo(SettingsActivity.this.getPackageName(), 0);
					version = pInfo.versionName;
				} catch (NameNotFoundException e) {
					pInfo = null;
					version = "";
				}
				
				AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
		        builder.setMessage(SettingsActivity.this.getString(R.string.app_name)+" v"+version+" ©"+year+"\nvytvořil Filip Procházka")
		               .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		                   public void onClick(DialogInterface dialog, int id) {
		                       dialog.dismiss();
		                   }
		               });
		        AlertDialog dialog = builder.show();
		        TextView messageText = (TextView)dialog.findViewById(android.R.id.message);
		        messageText.setGravity(Gravity.CENTER);
		        dialog.show();
				return false;
			}
			
		});
		
		Preference authorWeb = (Preference) findPreference("authorWeb");
		authorWeb.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				String url = "http://gymik.jacktech.cz/";
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
				return true;
			}
		});
		
		Preference changeClass = (Preference) findPreference("changeClass");
		changeClass.setTitle("Změnit třídu ("+config.getConfig("class")+")");
		changeClass.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				config.updateConfig("class", "-");
				SettingsActivity.this.finish();
				Intent i = new Intent(SettingsActivity.this,InstallActivity.class);
				i.putExtra("install", false);
				startActivity(i);
				return true;
			}
		});
		
		Preference updateSuplov = (Preference) findPreference("updateSuplov");
		updateSuplov.setTitle("Aktualizovat suplování (naposledy "+UtilityClass.getDateReadable((String) config.getConfig("lastSuplov"))+")");
		updateSuplov.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				updater.downloadSuplov();
				return true;
			}
		});
		
		Preference updateMap = (Preference) findPreference("updateMap");
		updateMap.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				updater.downloadMap();
				return true;
			}
		});
		
		CheckBoxPreference mapShowColors = (CheckBoxPreference) findPreference("mapShowColors");
		mapShowColors.setChecked(Boolean.parseBoolean((String) config.getConfig("showMapColors")));
		mapShowColors.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				config.updateConfig("showMapColors", newValue.toString());
				return true;
			}
		});
		
		CheckBoxPreference updateSuplovAuto = (CheckBoxPreference) findPreference("updateSuplovAuto");
		updateSuplovAuto.setChecked(Boolean.parseBoolean((String) config.getConfig("suplovAutoDownload")));
		updateSuplovAuto.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				config.updateConfig("suplovAutoDownload", newValue.toString());
				return true;
			}
		});
		
		final ListPreference updateSuplovTime = (ListPreference) findPreference("updateSuplovTime");
		updateSuplovTime.setTitle("Doba stahování ("+getDayTimeName((String) config.getConfig("suplovDownloadTime"))+")");
		updateSuplovTime.setDefaultValue(config.getConfig("suplovDownloadTime"));
		updateSuplovTime.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				updateSuplovTime.setTitle("Doba stahování ("+getDayTimeName((String) newValue)+")");
				config.updateConfig("suplovDownloadTime", newValue);
				return true;
			}
		});
	}
	
	@Override
	public void onPause(){
		super.onPause();
		config.writeConfig();
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
	
	private String getDayTimeName(String value){
		if(value.equals("midnight"))
			return "Půlnoc";
		if(value.equals("school"))
			return "Kolem 11";
		if(value.equals("morning"))
			return "Ráno";
		if(value.equals("afternoon"))
			return "Odpoledne";
		return "";
	}
	
}
