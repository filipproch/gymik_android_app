package com.jacktech.gymik.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.jacktech.gymik.R;

public class ZnamkyFragment extends SherlockFragment implements ZnamkyListFragment.IItemClick{

	private View rootView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View main = inflater.inflate(R.layout.znamky_layout, null);
		if(rootView.findViewById(R.id.znamky_list_container) != null){
			click(0);
		}	
		return main;
	}

	@Override
	public void click(int position) {
		ZnamkyListDetailFragment fragment = new ZnamkyListDetailFragment();
		getSherlockActivity().getSupportFragmentManager().beginTransaction().replace(R.id.znamky_list_container, fragment).commit();
	}
	
}
