package com.jacktech.gymik.server;

import java.util.Calendar;

import com.jacktech.gymik.Config;
import com.jacktech.gymik.DataWorker;
import com.jacktech.gymik.InitActivity;
import com.jacktech.gymik.R;
import com.jacktech.gymik.UpdateClass;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class BackgroundService extends Service{

	private boolean serviceRunning = true;
	private DataWorker dw;
	private Config config;
	private UpdateClass updateClass;
	private Handler h;
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public void onCreate(){
		super.onCreate();
		dw = new DataWorker(this);
		config = new Config(dw.getConfig(), dw);
		updateClass = new UpdateClass(dw, config, this);
		h = new Handler();
		new Thread(downloadThread).start();
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		serviceRunning = false;
	}
	
	private Runnable downloadThread = new Runnable() {
		
		@Override
		public void run() {
			Calendar c = Calendar.getInstance();
			while(serviceRunning){
				config.reloadConfig();
				if(config.getConfig("suplovAutoDownload").equals("true")){
					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis(Long.parseLong((String) config.getConfig("lastSuplov")));
					String timeDownload = (String) config.getConfig("suplovDownloadTime");
					if(cal.get(Calendar.DAY_OF_YEAR) != c.get(Calendar.DAY_OF_YEAR)){
						if(timeDownload.equals("midnight") && c.get(Calendar.HOUR_OF_DAY) == 0){
							updateClass.downloadSuplov();
							showSuplovDownloaded();
						}
						if(timeDownload.equals("morning") && c.get(Calendar.HOUR_OF_DAY) == 6){
							updateClass.downloadSuplov();
							showSuplovDownloaded();
						}
						if(timeDownload.equals("school") && c.get(Calendar.HOUR_OF_DAY) == 11){
							updateClass.downloadSuplov();
							showSuplovDownloaded();
						}
						if(timeDownload.equals("afternoon") && c.get(Calendar.HOUR_OF_DAY) == 16){
							updateClass.downloadSuplov();
							showSuplovDownloaded();
						}
					}
				}
				try {
					Thread.sleep(1000*60*15);
				} catch (InterruptedException e) {
				}
			}
		}
	};

	protected void showSuplovDownloaded() {
		h.post(new Runnable() {
			
			@Override
			public void run() {
				NotificationCompat.Builder mBuilder =
						new NotificationCompat.Builder(BackgroundService.this)
				        .setSmallIcon(R.drawable.ic_launcher)
				        .setContentTitle("Staženo aktuální suplování")
				        .setContentText("Právě bylo dokončeno stahování aktuálního suplování");
				Intent resultIntent = new Intent(BackgroundService.this, InitActivity.class);
				TaskStackBuilder taskStack = TaskStackBuilder.create(BackgroundService.this);
				taskStack.addParentStack(InitActivity.class);
				taskStack.addNextIntent(resultIntent);
				PendingIntent resultPendingIntent = taskStack.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
				mBuilder.setContentIntent(resultPendingIntent);
				NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				manager.notify(0, mBuilder.build());
			}
		});
	}

}
