package com.jacktech.gymik.widget;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.jacktech.gymik.Adapters;
import com.jacktech.gymik.DataWorker;
import com.jacktech.gymik.R;

public class SuplovViewsFactory implements RemoteViewsService.RemoteViewsFactory {

	private Context context = null;
	private int appWidgetId;
	private ArrayList<String> items;
	private DataWorker dw;

	public SuplovViewsFactory(Context context, Intent intent) {
	    this.context = context;
	    this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID);
	    this.dw = new DataWorker(context);
	    ArrayList<String> list = new ArrayList<String>();
	    JSONArray o = (JSONArray) dw.getSuplovani().get("data");
	    for(int i = 0;i<o.size();i++){
	    	JSONObject info = (JSONObject) o.get(i);
	    	String day = getDayFromDate((String) info.get("day"));
	    	JSONArray data = (JSONArray) info.get("data");
	    	for(int k = 0;k < data.size();k++){
	    		JSONObject supl = (JSONObject) data.get(k);
	    		list.add(day+", "+supl.get("predmet")+"("+supl.get("hodina")+"h), "+Adapters.getStatusString(supl.get("status")));
	    	}
	    }
	    items = list;
	}
	
	private String getDayFromDate(String object) {
		Calendar c = Calendar.getInstance();
		for(int i = -1;i<2;i++){
			String date = (c.get(Calendar.DAY_OF_MONTH)+i)+"."+(c.get(Calendar.MONTH+1)+"."+c.get(Calendar.YEAR));
			if(date.equals(object)){
				switch(i){
					case -1: return "VČERA";
					case 0: return "DNES";
					case 1: return "ZÍTRA";
				}
			}
		}
		String[] dateSplit = object.split("\\.");
		return dateSplit[0]+"."+dateSplit[1]+".";
	}
	
	@Override
	public void onCreate() {
	}
	  
	@Override
	public void onDestroy() {
	}
	
	@Override
	public int getCount() {
		return items.size();
	}
	
	@Override
	public RemoteViews getViewAt(int position) {
		RemoteViews row = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);
		row.setTextViewText(R.id.widget_list_text1, items.get(position));
		Intent i = new Intent();
		Bundle extras = new Bundle();
		
		extras.putString(SuplovWidgetProvider.EXTRA_DETAILS, items.get(position));
		i.putExtras(extras);
		//row.setOnClickFillInIntent(R.id.widget_list_text1, i);
		return row;
	}
	
	@Override
	public RemoteViews getLoadingView() {
		return null;
	}
	
	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public void onDataSetChanged() {
	}

}
