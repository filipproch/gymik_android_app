package com.jacktech.gymik.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.jacktech.gymik.R;

public class JidloFragment extends SherlockFragment implements JidloListFragment.IItemClick{

	private static View rootView;
	private JidloListDetailFragment jidloFragment;
	
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
			rootView = inflater.inflate(R.layout.jidlo_layout,container, false);
	    } catch (Exception e) {
	    }
		jidloFragment = (JidloListDetailFragment) getSherlockActivity().getSupportFragmentManager().findFragmentById(R.id.jidlo_item_list);
		if(rootView.findViewById(R.id.jidlo_list_container) != null){
			JidloListFragment fragment = new JidloListFragment(this);
			getSherlockActivity().getSupportFragmentManager().beginTransaction().replace(R.id.jidlo_list_container, fragment).commit();
		}	
		return rootView;
	}

	@Override
	public void click(int position) {
		jidloFragment.getListView().smoothScrollToPosition(position);
	}
	
}
