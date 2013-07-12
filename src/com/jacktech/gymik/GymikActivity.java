package com.jacktech.gymik;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jacktech.gymik.Adapters.NewsAdapter;
import com.jacktech.gymik.Adapters.SuplovAdapter;

public class GymikActivity extends SherlockFragmentActivity {

	private DataWorker dw;
	private List<JSONObject> newsList;
	private boolean settingsOpen = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.Theme_Sherlock_Light_DarkActionBar);
		ActionBar actionBar = getSupportActionBar();
		ArrayAdapter<String> adapter = new NavigationAdapter(this,
		         R.layout.sherlock_spinner_dropdown_item, getResources().getStringArray(R.array.navigation));
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actionBar.setListNavigationCallbacks(adapter, new OnNavigationListener() {
			
			@Override
			public boolean onNavigationItemSelected(int itemPosition, long itemId) {
				updateScreen(itemPosition);
				return false;
			}
		});
		actionBar.setDisplayShowTitleEnabled(false);
		dw = new DataWorker(this);
		updateScreen(0);
	}
	
	public void updateScreen(int screen){
		switch(screen){
			case 1:
				setContentView(R.layout.rozvrh_layout);
				final Button po = (Button) findViewById(R.id.rozvrhPo);
				final Button ut = (Button) findViewById(R.id.rozvrhUt);
				final Button st = (Button) findViewById(R.id.rozvrhSt);
				final Button ct = (Button) findViewById(R.id.rozvrhCt);
				final Button pa = (Button) findViewById(R.id.rozvrhPa);
				po.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						updateRozvrh(0);
						po.setBackgroundColor(Color.parseColor("#555555"));
						ut.setBackgroundColor(Color.parseColor("#333333"));
						st.setBackgroundColor(Color.parseColor("#333333"));
						ct.setBackgroundColor(Color.parseColor("#333333"));
						pa.setBackgroundColor(Color.parseColor("#333333"));
					}
				});
				ut.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						updateRozvrh(1);
						po.setBackgroundColor(Color.parseColor("#333333"));
						ut.setBackgroundColor(Color.parseColor("#555555"));
						st.setBackgroundColor(Color.parseColor("#333333"));
						ct.setBackgroundColor(Color.parseColor("#333333"));
						pa.setBackgroundColor(Color.parseColor("#333333"));
					}
				});
				st.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						updateRozvrh(2);
						po.setBackgroundColor(Color.parseColor("#333333"));
						ut.setBackgroundColor(Color.parseColor("#333333"));
						st.setBackgroundColor(Color.parseColor("#555555"));
						ct.setBackgroundColor(Color.parseColor("#333333"));
						pa.setBackgroundColor(Color.parseColor("#333333"));
					}
				});
				ct.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						updateRozvrh(3);
						po.setBackgroundColor(Color.parseColor("#333333"));
						ut.setBackgroundColor(Color.parseColor("#333333"));
						st.setBackgroundColor(Color.parseColor("#333333"));
						ct.setBackgroundColor(Color.parseColor("#555555"));
						pa.setBackgroundColor(Color.parseColor("#333333"));
					}
				});
				pa.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						updateRozvrh(4);
						po.setBackgroundColor(Color.parseColor("#333333"));
						ut.setBackgroundColor(Color.parseColor("#333333"));
						st.setBackgroundColor(Color.parseColor("#333333"));
						ct.setBackgroundColor(Color.parseColor("#333333"));
						pa.setBackgroundColor(Color.parseColor("#555555"));
					}
				});
				break;
			case 0:
				setContentView(R.layout.activity_gymik);
				newsList = (List<JSONObject>)dw.getNews().get("news");
				NewsAdapter newsAdapter = new Adapters.NewsAdapter(this, R.layout.news_item, newsList);
				ListView newsLV = (ListView)findViewById(R.id.news_list);
				newsLV.setAdapter(newsAdapter);
				newsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View v,
							int pos, long arg3) {
						final JSONObject item = newsList.get(pos);
						View newsDialogView = LayoutInflater.from(GymikActivity.this).inflate(R.layout.news_dialog, null);
						TextView text = (TextView) newsDialogView.findViewById(R.id.newsDialogText);
						text.setText(Html.fromHtml((String)item.get("description")));
						text.setMovementMethod(LinkMovementMethod.getInstance());
						AlertDialog.Builder builder = new AlertDialog.Builder(GymikActivity.this);
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
				JSONArray suplovani = (JSONArray)dw.getSuplovani().get("data");
				ArrayList<JSONObject> suplovList = new ArrayList<JSONObject>();
				HashMap<Integer, String> suplovDays= new HashMap<Integer, String>();
				int k = 0;
				for(Object suplov : suplovani){
					if(suplov != null && ((JSONObject)suplov).get("data") != null){
						suplovDays.put(k, (String)((JSONObject)suplov).get("day"));
						suplovList.addAll(((JSONArray)((JSONObject)suplov).get("data")));
						k+=((JSONArray)((JSONObject)suplov).get("data")).size();
					}
				}
				SuplovAdapter suplov = new SuplovAdapter(GymikActivity.this, R.layout.suplov_item, suplovList, suplovDays);
				ListView suplovLV = (ListView) findViewById(R.id.supl_list);
				suplovLV.setAdapter(suplov);
				break;
			case 2:
				setContentView(R.layout.map_layout);
				final MapView map = (MapView) findViewById(R.id.map_view);
				final Button p0 = (Button) findViewById(R.id.patroSelect0);
				final Button p1 = (Button) findViewById(R.id.patroSelect1);
				final Button p2 = (Button) findViewById(R.id.patroSelect2);
				final Button p3 = (Button) findViewById(R.id.patroSelect3);
				p0.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						map.updateLevel(0);
						p0.setBackgroundColor(Color.parseColor("#555555"));
						p1.setBackgroundColor(Color.parseColor("#333333"));
						p2.setBackgroundColor(Color.parseColor("#333333"));
						p3.setBackgroundColor(Color.parseColor("#333333"));
					}
				});
				p1.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						map.updateLevel(1);
						p0.setBackgroundColor(Color.parseColor("#333333"));
						p1.setBackgroundColor(Color.parseColor("#555555"));
						p2.setBackgroundColor(Color.parseColor("#333333"));
						p3.setBackgroundColor(Color.parseColor("#333333"));
					}
				});
				p2.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						map.updateLevel(2);
						p0.setBackgroundColor(Color.parseColor("#333333"));
						p1.setBackgroundColor(Color.parseColor("#333333"));
						p2.setBackgroundColor(Color.parseColor("#555555"));
						p3.setBackgroundColor(Color.parseColor("#333333"));
					}
				});
				p3.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						map.updateLevel(3);
						p0.setBackgroundColor(Color.parseColor("#333333"));
						p1.setBackgroundColor(Color.parseColor("#333333"));
						p2.setBackgroundColor(Color.parseColor("#333333"));
						p3.setBackgroundColor(Color.parseColor("#555555"));
					}
				});
				break;
			case 3:
				setContentView(R.layout.bakalari_layout);
				WebView web = (WebView) findViewById(R.id.bakalariWebView);
				web.loadUrl("https://bakalari.mikulasske.cz/login.aspx");
				break;
			case 4:
				setContentView(R.layout.bakalari_layout);
				WebView web2 = (WebView) findViewById(R.id.bakalariWebView);
				web2.loadUrl("http://esf.mikulasske.cz/");
				break;
		}
	}
	
	protected void updateRozvrh(int i) {
		// TODO Auto-generated method stub
		
	}

	class NavigationAdapter extends ArrayAdapter<String>{
		
		public NavigationAdapter(Context context, int textViewResourceId,
				String[] objects) {
			super(context, textViewResourceId, objects);
		}

		@Override
		public View getView(int pos, View view, ViewGroup viewGroup) {
			TextView text = (TextView) super.getView(pos, view, viewGroup);
	        text.setTextColor(Color.WHITE);
	        return text;
	    }
		
		@Override
		public View getDropDownView(int pos, View view, ViewGroup viewGroup) {
			TextView text = (TextView) super.getView(pos, view, viewGroup);
	        text.setTextColor(Color.WHITE);
	        return text;
	    }	
	}
	
	@Override
	public void onResume(){
		super.onResume();
		if(settingsOpen){
			settingsOpen = false;
			updateScreen(getSupportActionBar().getSelectedNavigationIndex());
		}
	}
	
	public void openSettings(Activity a){
		settingsOpen = true;
		a.startActivity(new Intent(a, SettingsActivity.class));
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
			case R.id.action_settings:
				openSettings(this);
				return true;
		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.gymik, menu);
		return true;
	}

}
