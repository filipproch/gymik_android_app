package com.jacktech.gymik;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class AbstractActivity extends SherlockFragmentActivity{
	public DataWorker dw;
	public Config config;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dw = new DataWorker(this);
		config = new Config(dw.getConfig(), dw);
	}
}
