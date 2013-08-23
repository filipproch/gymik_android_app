package com.jacktech.gymik;

import java.util.Calendar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.TextView;

public class InitActivity extends Activity{

	private TextView statusText;
	private DataWorker dw;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.init_layout);
		statusText = (TextView) findViewById(R.id.statusText);
		statusText.setText("Připravuji spuštění");
		dw = new DataWorker(this);
		if(dw.mExternalStorageAvailable && dw.mExternalStorageWriteable){
			if(dw.isFirstRun()){
				if(isOnline(this)){
					startActivity(new Intent(InitActivity.this,InstallActivity.class));
				}else{
					showNoConnectionDialog();
				}
			}else{
				Calendar c = Calendar.getInstance();
				String schoolYear = null;
				if(c.get(Calendar.MONTH)<9){
					schoolYear = String.valueOf(c.get(Calendar.YEAR)-1).substring(2)+"/"+String.valueOf(c.get(Calendar.YEAR)).substring(2);
				}else
					schoolYear = String.valueOf(c.get(Calendar.YEAR)).substring(2)+"/"+String.valueOf(c.get(Calendar.YEAR)+1).substring(2);
				Config config = new Config(dw.getConfig(),dw);
				if(config.getConfig("schoolYear").equals(schoolYear))
					startActivity(new Intent(this,GymikActivity.class));
				else{
					if(isOnline(this)){
						startActivity(new Intent(InitActivity.this,InstallActivity.class));
					}else{
						showNoConnectionDialog();
					}
				}
			}
		}else{
			showNoStorageDialog();
		}
	}
	
	private void showNoStorageDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(InitActivity.this);
		builder.setTitle(R.string.warning);
		builder.setMessage(R.string.noStorage);
		builder.setPositiveButton(R.string.dialogExit, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
            	InitActivity.this.finish();
			}
		});
		builder.create().show();
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
