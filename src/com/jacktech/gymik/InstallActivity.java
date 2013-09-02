package com.jacktech.gymik;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.actionbarsherlock.app.SherlockActivity;
import com.google.analytics.tracking.android.EasyTracker;

public class InstallActivity extends SherlockActivity{

	private Config config;
	private DataWorker dw;
	private int stage = 0;
	private ProgressBar downloadingClassesBar;
	private boolean firstInstall = true;
	private String className =null;
	private Handler h;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		if(getIntent().hasExtra("install"))
			firstInstall = false;
		setContentView(R.layout.install_layout);
		dw = new DataWorker(this);
		config = new Config(dw.getConfig(), dw);
		h = new Handler();
		stage = 1;
		ArrayList<String> classes = new ArrayList<String>();
		for(int i = 1;i<8;i++){
			for(int j = 0;j<5;j++){
				classes.add(i+"."+getChar(j));
			}
		}
		final Spinner sp = (Spinner) findViewById(R.id.pickClassSpinner);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(InstallActivity.this,android.R.layout.simple_spinner_item,classes);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp.setAdapter(adapter);
		Button customClass = (Button) findViewById(R.id.customClassName);
		customClass.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				h.post(new Runnable() {
					
					@Override
					public void run() {
						AlertDialog.Builder builder = new AlertDialog.Builder(InstallActivity.this);
						final EditText input = new EditText(InstallActivity.this);
						LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						        LinearLayout.LayoutParams.MATCH_PARENT,
						        LinearLayout.LayoutParams.MATCH_PARENT);
						input.setLayoutParams(lp);
						builder.setView(input);
						builder.setTitle("Zadejte název třídy");
						builder.setMessage("Název musí odpovídat označení na suplování, jinak aplikace nebude fungovat správně");
						builder.setPositiveButton(R.string.dialogOk, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								className = input.getText().toString().toUpperCase();
								dialog.dismiss();
							}
						});
						builder.setNegativeButton(R.string.dialogClose, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
						builder.show();
					}
				});
			}
		});
		Button cont = (Button) findViewById(R.id.installButton);
		cont.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Calendar c = Calendar.getInstance();
				String schoolYear;
				if(c.get(Calendar.MONTH)<9){
					schoolYear = String.valueOf(c.get(Calendar.YEAR)-1).substring(2)+"/"+String.valueOf(c.get(Calendar.YEAR)).substring(2);
				}else
					schoolYear = String.valueOf(c.get(Calendar.YEAR)).substring(2)+"/"+String.valueOf(c.get(Calendar.YEAR)+1).substring(2);
				config.updateConfig("schoolYear", schoolYear);
				if(className == null)
					config.updateConfig("class", sp.getSelectedItem());
				else
					config.updateConfig("class", className);
				downloadSuplovAndRozvrh();
			}
		});
	}
	
	private char getChar(int j) {
		switch (j) {
			case 0:
				return 'A';
			case 1:
				return 'B';
			case 2:
				return 'C';
			case 3:
				return 'D';
			case 4:
				return 'E';
		}
		return '-';
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
				reader.close();
				//stahovani rozvrhu nebude tady
				//stahovani map
				for(int i = 0;i<4;i++){
					URL mapUrl = new URL("http://gymik.jacktech.cz/genmap.php?floor="+i+"&output=json");
					con1 = mapUrl.openConnection();
					reader = new BufferedReader(new InputStreamReader(con1.getInputStream()));
					dw.writeMap((JSONObject)parser.parse(reader), i);
					reader.close();
				}
				//stahovani jidelnicku
				suplovUrl = new URL("http://gymik.jacktech.cz/jidlo_parser.php");
				con1 = suplovUrl.openConnection();
				reader = new BufferedReader(new InputStreamReader(con1.getInputStream()));
				data[1] = (JSONObject) parser.parse(reader);
				reader.close();
				//stahovani novinek
				RSSParser rssParser = new RSSParser("http://mikulasske.cz/index.php?option=com_content&view=featured&format=feed&type=rss");
				data[2] = rssParser.parse();
				return data;
			}catch(IOException e){
				return null;
			} catch (Exception e) {
				return null;
			}
		}
		
		@Override
		protected void onPostExecute(Object[] data){
			if(data != null){
				config.updateConfig("lastSuplov", System.currentTimeMillis()+"");
				config.writeConfig();
				dw.writeSuplovani((JSONObject)data[0]);
				dw.writeJidlo((JSONObject)data[1]);
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
