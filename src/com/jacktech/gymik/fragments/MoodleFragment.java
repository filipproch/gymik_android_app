package com.jacktech.gymik.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.actionbarsherlock.app.SherlockFragment;
import com.jacktech.gymik.R;

public class MoodleFragment extends SherlockFragment{

	private View rootView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.moodle_layout,container, false);
		WebView web2 = (WebView) rootView.findViewById(R.id.moodleWebView);
		web2.loadUrl("http://esf.mikulasske.cz/");
		return rootView;
	}
	
}
