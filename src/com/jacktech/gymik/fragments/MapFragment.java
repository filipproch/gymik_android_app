package com.jacktech.gymik.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockFragment;
import com.jacktech.gymik.AbstractActivity;
import com.jacktech.gymik.MapView;
import com.jacktech.gymik.R;

public class MapFragment extends SherlockFragment{

private View rootView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.map_layout,container, false);
		AbstractActivity parent = (AbstractActivity) getActivity();
		final MapView map = (MapView) rootView.findViewById(R.id.map_view);
		map.setShowColors(Boolean.parseBoolean((String) parent.config.getConfig("showMapColors")));
		final Button p0 = (Button) rootView.findViewById(R.id.patroSelect0);
		final Button p1 = (Button) rootView.findViewById(R.id.patroSelect1);
		final Button p2 = (Button) rootView.findViewById(R.id.patroSelect2);
		final Button p3 = (Button) rootView.findViewById(R.id.patroSelect3);
		p0.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				map.updateLevel(0);
				p0.setBackgroundColor(Color.parseColor("#555555"));
				p1.setBackgroundColor(Color.parseColor("#333333"));
				p2.setBackgroundColor(Color.parseColor("#333333"));
				p3.setBackgroundColor(Color.parseColor("#333333"));
			}
		});
		p1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				map.updateLevel(1);
				p0.setBackgroundColor(Color.parseColor("#333333"));
				p1.setBackgroundColor(Color.parseColor("#555555"));
				p2.setBackgroundColor(Color.parseColor("#333333"));
				p3.setBackgroundColor(Color.parseColor("#333333"));
			}
		});
		p2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				map.updateLevel(2);
				p0.setBackgroundColor(Color.parseColor("#333333"));
				p1.setBackgroundColor(Color.parseColor("#333333"));
				p2.setBackgroundColor(Color.parseColor("#555555"));
				p3.setBackgroundColor(Color.parseColor("#333333"));
			}
		});
		p3.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				map.updateLevel(3);
				p0.setBackgroundColor(Color.parseColor("#333333"));
				p1.setBackgroundColor(Color.parseColor("#333333"));
				p2.setBackgroundColor(Color.parseColor("#333333"));
				p3.setBackgroundColor(Color.parseColor("#555555"));
			}
		});
		return rootView;
	}
	
}
