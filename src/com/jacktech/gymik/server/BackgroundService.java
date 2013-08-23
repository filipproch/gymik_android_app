package com.jacktech.gymik.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class BackgroundService extends Service{

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

}
