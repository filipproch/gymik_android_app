package com.jacktech.gymik;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.jacktech.gymik.bakalari.Predmet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Adapters {

	public static class NavigationAdapter extends BaseAdapter{

		private List<NavigationItem> items;
		private Context context;
		private LayoutInflater layoutInflater;
		
		public NavigationAdapter(Context context, List<NavigationItem> list) {
			this.items = list;
			this.context = context;
			layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
        public int getItemViewType(int position) {
            return items.get(position).delimiter ? 1 : 0;
        }
		
		@Override
        public int getViewTypeCount() {
            return 2;
        }
		
		@Override
        public int getCount() {
            return items.size();
        }
 
        @Override
        public NavigationItem getItem(int position) {
            return items.get(position);
        }
 
        @Override
        public long getItemId(int position) {
            return position;
        }
		
		@Override
		public View getView(int position, View convertedView, ViewGroup group){
			View conv = convertedView;
			if(conv == null){
				if(items.get(position).delimiter){
					conv = layoutInflater.inflate(R.layout.drawer_list_delimiter, null);
				}else{
					conv = layoutInflater.inflate(R.layout.drawer_list_item, null);
				}
			}
			
			NavigationItem listElement = items.get(position);
			if(listElement != null && !listElement.delimiter){
				TextView t = (TextView)conv;
				t.setText(listElement.text);
				t.setCompoundDrawablesWithIntrinsicBounds(listElement.drawable, 0, 0, 0);
			}
			return conv;	
		}
	}
	
	public static class ZnamkyPredmetyAdapter extends BaseAdapter{

		private List<Predmet> items;
		private Context context;
		private LayoutInflater layoutInflater;
		
		public ZnamkyPredmetyAdapter(Context context, List<Predmet> list) {
			this.items = list;
			this.context = context;
			layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
        public int getCount() {
            return items.size();
        }
 
        @Override
        public Predmet getItem(int position) {
            return items.get(position);
        }
 
        @Override
        public long getItemId(int position) {
            return position;
        }
		
		@Override
		public View getView(int position, View convertedView, ViewGroup group){
			View conv = convertedView;
			if(conv == null){
				conv = layoutInflater.inflate(R.layout.znamky_predmet_menu_item, null);
			}
			
			Predmet listElement = items.get(position);
			if(listElement != null){
				//t.setText(listElement.text);
				//t.setCompoundDrawablesWithIntrinsicBounds(listElement.drawable, 0, 0, 0);
			}
			return conv;	
		}
	}
	
	public static class NewsAdapter extends ArrayAdapter<JSONObject>{

		private List<JSONObject> items;
		private ImageGetter imgGetter = new ImageGetter(){

			@Override
			public Drawable getDrawable(String source) {
				/*try {
					return new ImageLoader(getContext()).execute(source).get();
				} catch (InterruptedException e) {
					Log.e("Adapters.ImageGetter", "InterruptedException, image loader failed");
				} catch (ExecutionException e) {
					Log.e("Adapters.ImageGetter", "ExecutionException, image loader failed");
				}*/
				return null;
			}
			
		};
		
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
				if(textData.length() > 160){
					int i = 160;
					while(i < textData.length()-1){
						if(textData.charAt(i) == ' ')
							break;
						i++;
					}
					textData = textData.substring(0,i);
					}
				Spanned data = Html.fromHtml(textData, imgGetter, null);
				text.setText(data);
			}
			return conv;	
		}
	}
	
	public static class JidloAdapter extends ArrayAdapter<JSONObject>{

		private List<JSONObject> items;
		private ImageGetter imgGetter = new ImageGetter(){

			@Override
			public Drawable getDrawable(String source) {
				/*try {
					return new ImageLoader(getContext()).execute(source).get();
				} catch (InterruptedException e) {
					Log.e("Adapters.ImageGetter", "InterruptedException, image loader failed");
				} catch (ExecutionException e) {
					Log.e("Adapters.ImageGetter", "ExecutionException, image loader failed");
				}*/
				return null;
			}
			
		};
		
		public JidloAdapter(Context context, int textViewResourceId, List<JSONObject> list) {
			super(context, textViewResourceId, list);
			this.items = list;
		}
		
		@Override
		public View getView(int position, View convertedView, ViewGroup group){
			View conv = convertedView;
			boolean createdView = false;
			if(conv == null){
				LayoutInflater inflater = LayoutInflater.from(this.getContext());
				conv = inflater.inflate(R.layout.jidlo_item, null);
				createdView = true;
			}
			
			if(items != null){
				JSONObject jidloElement = items.get(position);
				if(jidloElement != null){
					TextView day = (TextView) conv.findViewById(R.id.jidlo_item_day);
					TextView polevka = (TextView) conv.findViewById(R.id.jidlo_item_polevka);
					TextView menu1 = (TextView) conv.findViewById(R.id.jidlo_item_menu1);
					TextView menu2 = (TextView) conv.findViewById(R.id.jidlo_item_menu2);
					TextView menu3 = (TextView) conv.findViewById(R.id.jidlo_item_menu3);
					TextView desert = (TextView) conv.findViewById(R.id.jidlo_item_desert);
					
					day.setText((CharSequence) jidloElement.get("day"));
					JSONArray dat = (JSONArray) jidloElement.get("data");
					for(Object o : dat){
						JSONObject ob = (JSONObject) o;
						String typ = (String) ob.get("typ");
						String name = (String) ob.get("name");
						if(polevka != null && typ.contains("Polévka"))
							polevka.setText(typ+" - "+name);
						if(menu1 != null && typ.contains("Menu 1"))
							menu1.setText(typ+" - "+name);
						if(menu2 != null && typ.contains("Menu 2"))
							menu2.setText(typ+" - "+name);
						if(menu3 != null && typ.contains("Menu 3"))
							menu3.setText(typ+" - "+name);
						if(desert != null && typ.contains("Zákusek"))
							desert.setText(typ+" - "+name);
					}
					if(polevka != null && polevka.length() <= 0 && createdView)
						((LinearLayout)polevka.getParent()).removeView(polevka);
					if(menu1 != null && menu1.length() <= 0 && createdView)
						((LinearLayout)menu1.getParent()).removeView(menu1);
					if(menu2 != null && menu2.length() <= 0 && createdView)
						((LinearLayout)menu2.getParent()).removeView(menu2);
					if(menu3 != null && menu3.length() <= 0 && createdView)
						((LinearLayout)menu3.getParent()).removeView(menu3);
					if(desert != null && desert.length() <= 0 && createdView)
						((LinearLayout)desert.getParent()).removeView(desert);
				}
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
			boolean createdView = false;
			if(v == null){
				v = LayoutInflater.from(getContext()).inflate(R.layout.suplov_item, null);
				createdView = true;
			}
			if(data.get(pos)!=null){
				String dayId = getDayFromDate(days.get(pos));
				JSONObject item = data.get(pos);
				TextView day = (TextView) v.findViewById(R.id.suplov_item_day);
				TextView detail = (TextView) v.findViewById(R.id.suplov_item_detail);
				TextView skupina = (TextView) v.findViewById(R.id.suplov_item_skupina);
				TextView mistnost = (TextView) v.findViewById(R.id.suplov_item_mistnost);
				TextView nahrazuje = (TextView) v.findViewById(R.id.suplov_item_nahrazuje);
				day.setText(dayId);
				if(item != null){
					String predmet = "";
					if(item.containsKey("predmet") && ((String)item.get("predmet")).length() > 0){
						predmet = "("+item.get("predmet")+")";
					}
					String text = (String)item.get("hodina")+"h"+predmet+" - "+getStatusString(item.get("status"));
					detail.setText(text);
					if(skupina != null && item.containsKey("skupina") && ((String)item.get("skupina")).length() > 0){
						skupina.setText("Skupina: "+item.get("skupina"));
					}else
						if(createdView)
							((LinearLayout)skupina.getParent()).removeView(skupina);
					
					if(mistnost != null && item.containsKey("mistnost") && ((String)item.get("mistnost")).length() > 0)
						mistnost.setText("Mistnost: "+item.get("mistnost"));
					else
						if(createdView)
							((LinearLayout)mistnost.getParent()).removeView(mistnost);
					
					if(nahrazuje != null && item.containsKey("nahrazuje") && ((String)item.get("nahrazuje")).length() > 0)
						nahrazuje.setText("Učitel: "+item.get("nahrazuje"));
					else
						if(createdView)
							((LinearLayout)nahrazuje.getParent()).removeView(nahrazuje);
				}else{
					detail.setText("CHYBA");
				}
			}
			return v;
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
	
	private static class ImageLoader extends AsyncTask<String, Void, Drawable>{

		private Context context;
		
		public ImageLoader(Context c){
			this.context = c;
		}
		
		@Override
		protected Drawable doInBackground(String... params) {
			try {
				URL aURL = new URL(params[0]);
				URLConnection conn = aURL.openConnection();
				conn.connect();
		        InputStream is = conn.getInputStream();
		        BufferedInputStream bis = new BufferedInputStream(is);
		        Bitmap bm = BitmapFactory.decodeStream(bis);
		        bis.close();
		        is.close();

		        Drawable d =new BitmapDrawable(context.getResources(),bm);
		        return d;
	        } catch (IOException e) {
		        Log.e("DEBUGTAG", "Remote Image Exception", e);
	        }
			return null;
		}
		
	}
	
	public static String getStatusString(Object object) {
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
}
