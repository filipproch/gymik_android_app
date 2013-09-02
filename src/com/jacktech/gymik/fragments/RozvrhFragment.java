package com.jacktech.gymik.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.actionbarsherlock.app.SherlockFragment;
import com.jacktech.gymik.GymikActivity;
import com.jacktech.gymik.R;

public class RozvrhFragment extends SherlockFragment{

	private View rootView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.rozvrh_layout,container, false);
		GymikActivity ac = (GymikActivity) getActivity();
		WebView view = (WebView) rootView.findViewById(R.id.rozvrh_web_view);
		String rozvrh = ac.dw.getRozvrh();
		if(rozvrh != null)
			view.loadDataWithBaseURL(null, "<style>"+getString(R.string.rozvrhStyle)+"</style>"+rozvrh, "text/html", "utf-8", null);
		else
			view.loadDataWithBaseURL(null,getString(R.string.missingRozvrh), "text/html", "utf-8", null);
		return rootView;
	}
}
