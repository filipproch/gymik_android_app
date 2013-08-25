package com.jacktech.gymik.fragments;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.jacktech.gymik.GymikActivity;

public class ZnamkyListFragment extends SherlockListFragment{

private ZnamkyFragment frag;
	
	public ZnamkyListFragment(ZnamkyFragment newsFragment) {
		this.frag = newsFragment;
	}
	
	public ZnamkyListFragment(){
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onListItemClick(ListView lv, View v, int pos, long id){
		frag.click(pos);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		GymikActivity ac = (GymikActivity) getActivity();
		getListView().setBackgroundColor(Color.WHITE);
	}
	
	public interface IItemClick{
		public void click(int position);
	}
	
	private ArrayList<String> parseArrayList(List<JSONObject> list) {
		return null;
	}
	
}
