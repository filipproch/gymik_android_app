package com.jacktech.gymik.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockFragment;
import com.jacktech.gymik.GymikActivity;
import com.jacktech.gymik.R;

public class RozvrhFragment extends SherlockFragment{

	private View rootView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	public void updateRozvrh(int i) {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.rozvrh_layout,container, false);
		final Button po = (Button) rootView.findViewById(R.id.rozvrhPo);
		final Button ut = (Button) rootView.findViewById(R.id.rozvrhUt);
		final Button st = (Button) rootView.findViewById(R.id.rozvrhSt);
		final Button ct = (Button) rootView.findViewById(R.id.rozvrhCt);
		final Button pa = (Button) rootView.findViewById(R.id.rozvrhPa);
		po.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				updateRozvrh(0);
				po.setBackgroundColor(Color.parseColor("#555555"));
				ut.setBackgroundColor(Color.parseColor("#333333"));
				st.setBackgroundColor(Color.parseColor("#333333"));
				ct.setBackgroundColor(Color.parseColor("#333333"));
				pa.setBackgroundColor(Color.parseColor("#333333"));
			}
		});
		ut.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				updateRozvrh(1);
				po.setBackgroundColor(Color.parseColor("#333333"));
				ut.setBackgroundColor(Color.parseColor("#555555"));
				st.setBackgroundColor(Color.parseColor("#333333"));
				ct.setBackgroundColor(Color.parseColor("#333333"));
				pa.setBackgroundColor(Color.parseColor("#333333"));
			}
		});
		st.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				updateRozvrh(2);
				po.setBackgroundColor(Color.parseColor("#333333"));
				ut.setBackgroundColor(Color.parseColor("#333333"));
				st.setBackgroundColor(Color.parseColor("#555555"));
				ct.setBackgroundColor(Color.parseColor("#333333"));
				pa.setBackgroundColor(Color.parseColor("#333333"));
			}
		});
		ct.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				updateRozvrh(3);
				po.setBackgroundColor(Color.parseColor("#333333"));
				ut.setBackgroundColor(Color.parseColor("#333333"));
				st.setBackgroundColor(Color.parseColor("#333333"));
				ct.setBackgroundColor(Color.parseColor("#555555"));
				pa.setBackgroundColor(Color.parseColor("#333333"));
			}
		});
		pa.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				updateRozvrh(4);
				po.setBackgroundColor(Color.parseColor("#333333"));
				ut.setBackgroundColor(Color.parseColor("#333333"));
				st.setBackgroundColor(Color.parseColor("#333333"));
				ct.setBackgroundColor(Color.parseColor("#333333"));
				pa.setBackgroundColor(Color.parseColor("#555555"));
			}
		});
		return rootView;
	}
}
