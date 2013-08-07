package com.jacktech.gymik;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.actionbarsherlock.app.SherlockActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;

public class InstallActivity extends SherlockActivity{

	private Config config;
	private DataWorker dw;
	private int stage = 0;
	private ProgressBar downloadingClassesBar;
	private boolean firstInstall = true;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		if(getIntent().hasExtra("install"))
			firstInstall = false;
		setContentView(R.layout.install_layout);
		downloadingClassesBar = (ProgressBar) findViewById(R.id.progressBar1);
		dw = new DataWorker(this);
		config = new Config(dw.getConfig(), dw);
		new ClassListDownloader().execute();
	}
	
	class ClassListDownloader extends AsyncTask<Void, Void, JSONObject>{

		@Override
		protected JSONObject doInBackground(Void... params) {
			try{
				URL classListURL = new URL("http://gymik.jacktech.cz/class_parser.php");
				URLConnection connection = classListURL.openConnection();
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				StringBuilder data = new StringBuilder();
				String line;
				while((line = reader.readLine()) != null){
					data.append(line);
				}
				reader.close();
				JSONParser parser = new JSONParser();
				JSONObject classes = (JSONObject) parser.parse(data.toString());
				return classes;
			}catch(IOException e){
				Log.i("DEBUG", "Error while downloading class list > "+e.getLocalizedMessage());
				return null;
			} catch (ParseException e) {
				Log.i("DEBUG", "Error while parsing classes data > "+e.getLocalizedMessage()+", "+e.getPosition());
				return null;
			}
		}
		
		@Override
		protected void onPostExecute(final JSONObject data){
			if(data != null){
				stage = 1;
				setContentView(R.layout.install_layout);
				JSONArray classes = (JSONArray)data.get("data");
				downloadingClassesBar.setVisibility(View.INVISIBLE);
				final Spinner sp = (Spinner) findViewById(R.id.pickClassSpinner);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(InstallActivity.this,android.R.layout.simple_spinner_item,classes);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				sp.setAdapter(adapter);
				Button cont = (Button) findViewById(R.id.installButton);
				cont.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						config.updateConfig("schoolYear", data.get("year"));
						config.updateConfig("class", sp.getSelectedItem());
						downloadSuplovAndRozvrh();
					}
				});
			}else{
				showDownloadErrorDialog();
			}
		}
	}
	
	protected void downloadSuplovAndRozvrh() {
		stage = 2;
		setContentView(R.layout.install_download_layout);
		new SuplovRozvrhDownloader().execute();
	}
	
	private class SuplovRozvrhDownloader extends AsyncTask<Void, Void, Object[]>{

		@Override
		protected Object[] doInBackground(Void... params) {
			Object[] data = new Object[3];
			try{
				JSONParser parser = new JSONParser();
				//stahovani suplovani
				URL suplovUrl = new URL("http://gymik.jacktech.cz/suplov_parser.php?class="+config.getConfig("class"));
				URLConnection con1 = suplovUrl.openConnection();
				BufferedReader reader = new BufferedReader(new InputStreamReader(con1.getInputStream()));
				data[0] = (JSONObject) parser.parse(reader);
				//stahovani rozvrhu
				data[1] = null;//zatim neimplementovano
				//stahovani novinek
				RSSParser rssParser = new RSSParser("http://mikulasske.cz/index.php?option=com_content&view=featured&format=feed&type=rss");
				data[2] = rssParser.parse();
				reader.close();
				return data;
			}catch(IOException e){
				Log.i("DEBUG", e.getLocalizedMessage());
				return null;
			} catch (ParseException e) {
				Log.i("DEBUG", e.getLocalizedMessage()+"//"+e.getPosition());
				return null;
			}
		}
		
		@Override
		protected void onPostExecute(Object[] data){
			if(data != null){
				config.updateConfig("lastSuplov", System.currentTimeMillis()+"");
				config.writeConfig();
				dw.writeSuplovani((JSONObject)data[0]);
				//dw.writeRozvrh(data[1]);
				dw.writeNews((List<RSSFeed>)data[2]);
				if(firstInstall)
					startActivity(new Intent(InstallActivity.this, GymikActivity.class));
				else
					finish();
			}else{
				showDownloadErrorDialog();
			}
		}
		
	}
	
	@Override
	public void onBackPressed(){
		if(stage < 2){
			AlertDialog.Builder builder = new AlertDialog.Builder(InstallActivity.this);
			builder.setTitle(R.string.warning);
			builder.setCancelable(false);
			builder.setMessage(R.string.exitDialog1);
			builder.setPositiveButton(R.string.dialogOk, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.setNegativeButton(R.string.dialogExit, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					InstallActivity.this.finish();
				}
			});
			builder.create().show();
		}else{
			AlertDialog.Builder builder = new AlertDialog.Builder(InstallActivity.this);
			builder.setTitle(R.string.warning);
			builder.setCancelable(false);
			builder.setMessage(R.string.exitDialog2);
			builder.setPositiveButton(R.string.dialogOk, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
		}
	}

	public void showDownloadErrorDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(InstallActivity.this);
		builder.setTitle(R.string.warning);
		builder.setCancelable(false);
		builder.setMessage(R.string.noInternetDataDownloadFailed);
		builder.setPositiveButton(R.string.dialogExit, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				InstallActivity.this.finish();
			}
		});
		builder.create().show();
	}
	
}
