package com.jacktech.gymik.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.actionbarsherlock.app.SherlockFragment;
import com.jacktech.gymik.R;

public class NewsFragment extends SherlockFragment implements NewsListFragment.IItemClick{

	private static View rootView;
	private NewsListDetailFragment newsFragment;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		if (rootView != null) {
	        ViewGroup parent = (ViewGroup) rootView.getParent();
	        if (parent != null)
	            parent.removeView(rootView);
	    }
		try {
			rootView = inflater.inflate(R.layout.news_layout,container, false);
	    } catch (Exception e) {
	    }
		newsFragment = (NewsListDetailFragment) getSherlockActivity().getSupportFragmentManager().findFragmentById(R.id.news_item_list);
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
