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
import com.jacktech.gymik.GymikActivity;
import com.jacktech.gymik.R;
import com.jacktech.gymik.Adapters.NewsAdapter;

public class NewsListDetailFragment extends SherlockListFragment{

	private View rootView;
	private List<JSONObject> newsList;
	private boolean isTablet = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		GymikActivity ac = (GymikActivity) getActivity();
		newsList = (List<JSONObject>)ac.dw.getNews().get("news");
		NewsAdapter newsAdapter = new Adapters.NewsAdapter(getActivity(), R.layout.news_item, newsList);
		setListAdapter(newsAdapter);
	}
	
	@Override
	public void onListItemClick(ListView lv, View v, int pos, long id){
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
	
}
