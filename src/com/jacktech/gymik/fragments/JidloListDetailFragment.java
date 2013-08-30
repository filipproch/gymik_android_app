package com.jacktech.gymik.fragments;

import java.util.List;

import org.json.simple.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockListFragment;
import com.jacktech.gymik.Adapters;
import com.jacktech.gymik.Adapters.JidloAdapter;
import com.jacktech.gymik.GymikActivity;
import com.jacktech.gymik.R;
import com.jacktech.gymik.Adapters.NewsAdapter;

public class JidloListDetailFragment extends SherlockListFragment{

	private View rootView;
	private List<JSONObject> jidloList;
	private boolean isTablet = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		GymikActivity ac = (GymikActivity) getActivity();
		jidloList = (List<JSONObject>)ac.dw.getJidlo().get("data");
		JidloAdapter jidloAdapter = new Adapters.JidloAdapter(getActivity(), R.layout.jidlo_item, jidloList);
		setListAdapter(jidloAdapter);
		getListView().setDivider(getResources().getDrawable(R.drawable.transperent_color));
		getListView().setSelector(getResources().getDrawable(R.drawable.transperent_color));
	}
	
	@Override
	public void onListItemClick(ListView lv, View v, int pos, long id){
		super.onListItemClick(lv, v, pos, id);
	}
	
}
