package com.jacktech.gymik.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
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
import com.jacktech.gymik.Adapters;
import com.jacktech.gymik.GymikActivity;
import com.jacktech.gymik.R;
import com.jacktech.gymik.Adapters.NewsAdapter;
import com.jacktech.gymik.Adapters.SuplovAdapter;

public class AktualityFragment extends SherlockFragment{

	private View rootView;
	private List<JSONObject> newsList;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.news_layout,container, false);
		GymikActivity ac = (GymikActivity) getActivity();
		newsList = (List<JSONObject>)ac.dw.getNews().get("news");
		NewsAdapter newsAdapter = new Adapters.NewsAdapter(getActivity(), R.layout.news_item, newsList);
		ListView newsLV = (ListView)rootView.findViewById(R.id.news_list);
		newsLV.setAdapter(newsAdapter);
		newsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v,
					int pos, long arg3) {
				final JSONObject item = newsList.get(pos);
				View newsDialogView = LayoutInflater.from(getActivity()).inflate(R.layout.news_dialog, null);
				TextView text = (TextView) newsDialogView.findViewById(R.id.newsDialogText);
				text.setText(Html.fromHtml((String)item.get("description")));
				text.setMovementMethod(LinkMovementMethod.getInstance());
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle((String)item.get("title"));
				builder.setView(newsDialogView);
				builder.setPositiveButton(R.string.dialogClose, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				builder.setNegativeButton(R.string.dialogLink, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent i = new Intent(Intent.ACTION_VIEW);
						i.setData(Uri.parse((String)item.get("link")));
						startActivity(i);
					}
				});
				builder.create().show();
			}
		});
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
		suplovLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int pos, long arg3) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				JSONObject supl = suplovList.get(pos);
				View main = getActivity().getLayoutInflater().inflate(R.layout.suplov_dialog, null);
				((TextView)main.findViewById(R.id.sDialogHodina)).setText((String) supl.get("hodina")+"h");
				((TextView)main.findViewById(R.id.sDialogPredmet)).setText((String) supl.get("predmet"));
				((TextView)main.findViewById(R.id.sDialogStav)).setText(Adapters.getStatusString(supl.get("status")));
				((TextView)main.findViewById(R.id.sDialogSkupina)).setText((String) supl.get("skupina"));
				((TextView)main.findViewById(R.id.sDialogMistnost)).setText((String) supl.get("mistnost"));
				((TextView)main.findViewById(R.id.sDialogNahrazuje)).setText((String)supl.get("nahrazuje"));
				builder.setView(main);
				builder.setTitle(supl.get("predmet")+" - "+Adapters.getStatusString(supl.get("status")));
				builder.setPositiveButton(R.string.dialogClose, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				builder.show();
			}
		});
		return rootView;
	}
	
}
