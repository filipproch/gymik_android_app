package com.jacktech.gymik.fragments;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockFragment;
import com.jacktech.gymik.GymikActivity;
import com.jacktech.gymik.R;

public class NewsFragment extends SherlockFragment implements NewsListFragment.IItemClick{

	private View rootView;
	private NewsListDetailFragment newsFragment;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.news_layout,container, false);
		newsFragment = (NewsListDetailFragment) getSherlockActivity().getSupportFragmentManager().findFragmentById(R.id.item_list);
		if(rootView.findViewById(R.id.news_list_container) != null){
			NewsListFragment fragment = new NewsListFragment(this);
			getSherlockActivity().getSupportFragmentManager().beginTransaction().replace(R.id.news_list_container, fragment).commit();
		}	
		return rootView;
	}

	@Override
	public void click(int position) {
		newsFragment.getListView().smoothScrollToPosition(position);
	}
	
}
