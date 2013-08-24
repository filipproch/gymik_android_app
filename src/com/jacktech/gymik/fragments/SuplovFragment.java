package com.jacktech.gymik.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.jacktech.gymik.Adapters;
import com.jacktech.gymik.GymikActivity;
import com.jacktech.gymik.R;
import com.jacktech.gymik.Adapters.SuplovAdapter;

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
		return rootView;
	}
	
}
