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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
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
import android.widget.Spinner;
import android.widget.TextView;

public class InitActivity extends Activity{

	private TextView statusText;
	private DataWorker dw;
	private int stage = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.init_layout);
		statusText = (TextView) findViewById(R.id.statusText);
		statusText.setText("Připravuji spuštění");
		dw = new DataWorker(this);
		if(dw.isFirstRun()){
			if(isOnline(this)){
				startActivity(new Intent(InitActivity.this,InstallActivity.class));
			}else{
				showNoConnectionDialog();
			}
		}else{
			startActivity(new Intent(this,GymikActivity.class));
		}
	}
	
	public static boolean isOnline(Activity activity) {
		ConnectivityManager connectivityManager = (ConnectivityManager)
		activity.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = null;
		if (connectivityManager != null) {
			networkInfo = connectivityManager.getActiveNetworkInfo();
		}
		return networkInfo == null ? false : networkInfo.isConnected();
    }
	
	private void showNoConnectionDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(InitActivity.this);
		builder.setTitle(R.string.warning);
		builder.setMessage(R.string.noInternet);
		builder.setNegativeButton(R.string.dialogExit, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				InitActivity.this.finish();
			}
		});
		builder.setPositiveButton(R.string.dialogSettings, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
            	InitActivity.this.finish();
			}
		});
		builder.create().show();
	}
	
}
