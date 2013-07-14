package com.jacktech.gymik;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONObject;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class Adapters {

	public static class NewsAdapter extends ArrayAdapter<JSONObject>{

		private List<JSONObject> items;
		
		public NewsAdapter(Context context, int textViewResourceId, List<JSONObject> list) {
			super(context, textViewResourceId, list);
			this.items = list;
		}
		
		@Override
		public View getView(int position, View convertedView, ViewGroup group){
			View conv = convertedView;
			if(conv == null){
				LayoutInflater inflater = LayoutInflater.from(this.getContext());
				conv = inflater.inflate(R.layout.news_item, null);
			}
			
			JSONObject newsElement = items.get(position);
			if(newsElement != null){
				TextView title = (TextView) conv.findViewById(R.id.news_item_title);
				TextView text = (TextView) conv.findViewById(R.id.news_item_text);
				title.setText((String)newsElement.get("title"));
				String textData = (String)newsElement.get("description");
				textData.replace("<div class=\"feed-description\">", "");
				textData.replace("</div>", "");
				Spanned data = Html.fromHtml(textData);
				text.setText(data);
			}
			return conv;	
		}
	}
	
	public static class SuplovAdapter extends ArrayAdapter<JSONObject>{

		private List<JSONObject> data;
		private HashMap<Integer, String> days;
		
		public SuplovAdapter(Context context, int textViewResourceId,
				List<JSONObject> objects,HashMap<Integer,String> days) {
			super(context, textViewResourceId, objects);
			this.data = objects;
			this.days = days;
			String day = "";
			for(int i = 0;i<data.size();i++){
				if(days.get(i) != null)
					day = days.get(i);
				else
					days.put(i, day);
			}
		}
		
		@Override
		public View getView(int pos,View v, ViewGroup group){
			
			if(v == null){
				v = LayoutInflater.from(getContext()).inflate(R.layout.suplov_item, null);
			}
			if(data.get(pos)!=null){
				String dayId = getDayFromDate(days.get(pos));
				JSONObject item = data.get(pos);
				TextView day = (TextView) v.findViewById(R.id.suplov_item_day);
				TextView detail = (TextView) v.findViewById(R.id.suplov_item_detail);
				day.setText(dayId);
				if(item != null){
					String text = (String)item.get("hodina")+"h - "+getStatusString(item.get("status"));
					detail.setText(text);
				}else{
					detail.setText("CHYBA");
				}
			}
			return v;
		}

		private String getStatusString(Object object) {
			if(object instanceof Long){
				switch(((Long)object).intValue()){
					case 0: return "suplovaná";
					case 1: return "odpadá";
					case 2: return "změna";
					case 3: return "zrušena";
					case 4: return "exkurze";
					case 5: return "jiná akce";
					case 6: return "spojí";
					case 7: return "výlet";
					case 8: return "přesun";
				}
			}
			return null;
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
		
	}
	
}
