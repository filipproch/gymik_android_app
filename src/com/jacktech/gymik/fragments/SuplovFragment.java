package com.jacktech.gymik.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.jacktech.gymik.Adapters.SuplovAdapter;
import com.jacktech.gymik.GymikActivity;
import com.jacktech.gymik.R;

public class SuplovFragment extends SherlockFragment{

	private View rootView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.suplov_layout,container, false);
		GymikActivity ac = (GymikActivity) getActivity();
		JSONArray suplovani = (JSONArray)ac.dw.getSuplovani().get("data");
		if(suplovani == null || suplovani.size() <= 0 || (suplovani.size() == 1 && ((JSONObject)suplovani.get(0)).get("data") == null)){
			TextView tx = new TextView(ac);
			if(suplovani == null)
				tx.setText("Chybí data");
			else
				tx.setText("Žádné změny");
			tx.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			tx.setGravity(Gravity.CENTER);
			tx.setTextSize(24f);
			tx.setPadding(0, 15, 0, 0);
			((RelativeLayout)rootView).removeAllViews();
			((RelativeLayout)rootView).addView(tx);
		}else{
			final ArrayList<JSONObject> suplovList = new ArrayList<JSONObject>();
			HashMap<Integer, String> suplovDays= new HashMap<Integer, String>();
			int k = 0;
			for(Object suplov : suplovani){
				if(suplov != null && ((JSONObject)suplov).get("data") != null){
					suplovDays.put(k, (String)((JSONObject)suplov).get("day"));
					suplovList.addAll(((JSONArray)((JSONObject)suplov).get("data")));
					k+=((JSONArray)((JSONObject)suplov).get("data")).size();
				}
			}
			SuplovAdapter suplov = new SuplovAdapter(getActivity(), R.layout.suplov_item, suplovList, suplovDays);
			ListView suplovLV = (ListView) rootView.findViewById(R.id.suplov_list_view);
			suplovLV.setAdapter(suplov);
		}
		return rootView;
	}
	
}
