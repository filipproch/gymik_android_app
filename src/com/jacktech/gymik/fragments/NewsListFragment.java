package com.jacktech.gymik.fragments;


import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockListFragment;
import com.jacktech.gymik.GymikActivity;
import com.jacktech.gymik.R;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class NewsListFragment extends SherlockListFragment {

	private NewsFragment frag;
	
	public NewsListFragment(NewsFragment newsFragment) {
		this.frag = newsFragment;
	}
	
	public NewsListFragment(){
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
		final ArrayList<String> newsList = parseArrayList((List<JSONObject>)ac.dw.getNews().get("news"));
		ArrayAdapter<String> newsAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, newsList);
		setListAdapter(newsAdapter);
		getListView().setBackgroundColor(Color.WHITE);
	}
	
	public interface IItemClick{
		public void click(int position);
	}
	
	private ArrayList<String> parseArrayList(List<JSONObject> list) {
		ArrayList<String> retList = new ArrayList<String>();
		for(JSONObject o : list){
			String sS = (String) o.get("title");
			if(sS.length() > 20)
				sS = sS.substring(0, 20)+"...";
			retList.add(sS);
		}
		return retList;
	}
	
}
