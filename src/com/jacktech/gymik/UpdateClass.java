package com.jacktech.gymik;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.jacktech.gymik.bakalari.Predmet;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class UpdateClass {

	private DataWorker dw;
	private Config config;
	private Context context;
	private OnCompletitionListener onComplete = null;
	
	public interface OnCompletitionListener{
		public void onComplete(boolean success);
	}
	
	public void setOnCompletitionListener(OnCompletitionListener listener){
		this.onComplete = listener;
	}
	
	public UpdateClass(DataWorker dw, Config config, Context context){
		this.dw = dw;
		this.config = config;
		this.context = context;
	}
	
	public void downloadSuplov(){
		new SuplovDownloader().execute();
	}
	
	public void downloadMap() {
		new MapDownloader().execute();
	}
	
	public void downloadJidlo() {
		new JidloDownloader().execute();	
	}
	
	public void downloadBakalari(){
		new ZnamkyDownloader().execute();
	}
	
	public void downloadNews() {
		new NewsDownloader().execute();
	}
	
	private class SuplovDownloader extends AsyncTask<Void, Void, JSONObject>{

		@Override
		protected JSONObject doInBackground(Void... params) {
			JSONObject data;
			try{
				JSONParser parser = new JSONParser();
				//stahovani suplovani
				URL suplovUrl = new URL("http://gymik.jacktech.cz/suplov_parser.php?class="+config.getConfig("class"));
				URLConnection con1 = suplovUrl.openConnection();
				BufferedReader reader = new BufferedReader(new InputStreamReader(con1.getInputStream()));
				data = (JSONObject) parser.parse(reader);
				reader.close();
				return data;
			}catch(IOException e){
				Log.i("DEBUG", e.getLocalizedMessage());
				return null;
			} catch (ParseException e) {
				Log.i("DEBUG", e.getLocalizedMessage()+"//"+e.getPosition());
				return null;
			}
		}
		
		@Override
		protected void onPostExecute(JSONObject data){
			if(data != null){
				config.updateConfig("lastSuplov", System.currentTimeMillis()+"");
				config.writeConfig();
				dw.writeSuplovani(data);
				if(onComplete != null)
					onComplete.onComplete(true);
				showDownloadSuccesfull("suplování");
			}else{
				if(onComplete != null)
					onComplete.onComplete(false);
				showDownloadError("suplování");
			}
		}
		
	}
	
	private class NewsDownloader extends AsyncTask<Void, Void, Boolean>{

		@Override
		protected Boolean doInBackground(Void... params) {
			try{
				//stahovani novinek
				URL suplovUrl = new URL("http://gymik.jacktech.cz/suplov_parser.php?class="+config.getConfig("class"));
				URLConnection con1 = suplovUrl.openConnection();
				BufferedReader reader = new BufferedReader(new InputStreamReader(con1.getInputStream()));
				reader.close();
				return true;
			}catch(IOException e){
				Log.i("NewsDownloader.doInBackground", "IOException: "+e.getLocalizedMessage());
				return null;
			}
		}
		
		@Override
		protected void onPostExecute(Boolean data){
			if(data){
				showDownloadSuccesfull("suplování");
			}else{
				showDownloadError("suplování");
			}
			if(onComplete != null)
				onComplete.onComplete(data);
		}
		
	}
	
	private class JidloDownloader extends AsyncTask<Void, Void, JSONObject>{

		@Override
		protected JSONObject doInBackground(Void... params) {
			JSONObject data;
			try{
				JSONParser parser = new JSONParser();
				//stahovani suplovani
				URL suplovUrl = new URL("http://gymik.jacktech.cz/jidlo_parser.php");
				URLConnection con1 = suplovUrl.openConnection();
				BufferedReader reader = new BufferedReader(new InputStreamReader(con1.getInputStream()));
				data = (JSONObject) parser.parse(reader);
				reader.close();
				return data;
			}catch(IOException e){
				Log.i("DEBUG", e.getLocalizedMessage());
				return null;
			} catch (ParseException e) {
				Log.i("DEBUG", e.getLocalizedMessage()+"//"+e.getPosition());
				return null;
			}
		}
		
		@Override
		protected void onPostExecute(JSONObject data){
			if(data != null){
				dw.writeJidlo(data);
				showDownloadSuccesfull("jídlo");
				if(onComplete != null)
					onComplete.onComplete(true);
			}else{
				showDownloadError("jídlo");
				if(onComplete != null)
					onComplete.onComplete(false);
			}
		}
		
	}
	
	private class ZnamkyDownloader extends AsyncTask<Void, Void, Boolean>{

		private PageLoader pageLoader;
		
		private String getRozvrhPage() throws IOException{
			Calendar c = Calendar.getInstance();
			int day = c.get(Calendar.DAY_OF_WEEK);
			String blankZnamky = pageLoader.getPage("https://bakalari.mikulasske.cz/prehled.aspx?s=6");
		    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		    if(day == Calendar.SATURDAY || day == Calendar.SUNDAY)
		    	nameValuePairs.add(new BasicNameValuePair("ctl00$cphmain$radiorozvrh", "rozvrh na p��t� t�den"));
		    nameValuePairs.add(new BasicNameValuePair("ctl00$cphmain$Flyrozvrh$checkucitel", "on"));
		    nameValuePairs.add(new BasicNameValuePair("ctl00$cphmain$Flyrozvrh$checkskupina", "on"));
		    nameValuePairs.add(new BasicNameValuePair("ctl00$cphmain$Flyrozvrh$Checkmistnost", "on" ));
			nameValuePairs.add(new BasicNameValuePair("__VIEWSTATE", PageLoader.getViewState(blankZnamky)));
			nameValuePairs.add(new BasicNameValuePair("__EVENTVALIDATION", PageLoader.getEventValidation(blankZnamky)));
	        
			return pageLoader.getPage("https://bakalari.mikulasske.cz/prehled.aspx?s=6", nameValuePairs);
		}
		
		private String getZnamkyPage() throws IOException{
			String blankZnamky = pageLoader.getPage("https://bakalari.mikulasske.cz/prehled.aspx?s=2");
		    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		    nameValuePairs.add(new BasicNameValuePair("ctl00$cphmain$Flyout2$Checktypy", "on"));
		    nameValuePairs.add(new BasicNameValuePair("ctl00$cphmain$Flyout2$Checkdatumy", "on"));
			nameValuePairs.add(new BasicNameValuePair("ctl00$cphmain$Checkdetail", "on"));
			nameValuePairs.add(new BasicNameValuePair("__VIEWSTATE", PageLoader.getViewState(blankZnamky)));
			nameValuePairs.add(new BasicNameValuePair("__EVENTVALIDATION", PageLoader.getEventValidation(blankZnamky)));
			return pageLoader.getPage("https://bakalari.mikulasske.cz/prehled.aspx?s=2", nameValuePairs);
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			JSONObject data = new JSONObject();
			try{
				String bakUser = (String) config.getConfig("bakUser").toString();
				String bakPsw = (String) config.getConfig("bakPsw").toString();
				if(!bakUser.equals("-") && !bakPsw.equals("-")){
					pageLoader = new PageLoader();
					String blankPage = pageLoader.getPage("https://bakalari.mikulasske.cz/login.aspx");
				    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			        nameValuePairs.add(new BasicNameValuePair("ctl00$cphmain$TextBoxjmeno", bakUser));
			        nameValuePairs.add(new BasicNameValuePair("ctl00$cphmain$TextBoxHeslo", bakPsw));
			        nameValuePairs.add(new BasicNameValuePair("ctl00$cphmain$ButtonPrihlas", ""));
			        nameValuePairs.add(new BasicNameValuePair("__VIEWSTATE",PageLoader.getViewState(blankPage)));
			        nameValuePairs.add(new BasicNameValuePair("__EVENTVALIDATION",PageLoader.getEventValidation(blankPage)));
			        
			        String page = pageLoader.getPage("https://bakalari.mikulasske.cz/login.aspx", nameValuePairs);
			        
			        data = new JSONObject();
			        if(page.contains("cphmain_LabelChyba")){
			        	data.put("error", "badAuth");
			        	dw.writeRozvrh("error/auth");
			        	dw.writeZnamky(data);
			        	return false;
			        }else{
			        	String znamkyPage = getZnamkyPage();
			        	String rozvrhPage = getRozvrhPage();
			        	dw.writeRozvrh(parseRozvrh(rozvrhPage));
			        	dw.writeSuplovani(parseZnamky(znamkyPage));
			        	return true;
			        }
				}
				return false;
			}catch(IOException e){
				Log.i("DEBUG", e.getLocalizedMessage());
				return false;
			}
		}
		
		private String parseRozvrh(String page){
			if(page != null){
				Elements tableRozvrh = Jsoup.parse(page).getElementsByClass("rozbunka");
				return "<table cellspacing=\"0\" cellpadding=\"0\" class=\"rozbunka\">"+tableRozvrh.get(0).html()+"</table>";
			}else{
				return "error/getPage";
			}
		}
		
		private JSONObject parseZnamky(String page){
			JSONObject o = new JSONObject();
			
			if(page != null){
				//parsovani znamek
				ArrayList<Predmet> predmety = new ArrayList<Predmet>();
				//Document doc = Jsoup.parse(page);
				
				//ulozeni do JSON formatu
				o.put("error", "notFinished");
			}else{
				o.put("error", "getPage");
			}
			return o;
		}
		
		@Override
		protected void onPostExecute(Boolean data){
			if(data){
				showDownloadSuccesfull("známky");
			}else{
				showDownloadError("známky");
			}
			if(onComplete != null)
				onComplete.onComplete(data);
		}
		
	}
	
	private class MapDownloader extends AsyncTask<Void, Void, Boolean>{

		@Override
		protected Boolean doInBackground(Void... params) {
			try{
				JSONParser parser = new JSONParser();
				for(int i = 0;i<4;i++){
					URL mapUrl = new URL("http://gymik.jacktech.cz/genmap.php?floor="+i+"&output=json");
					URLConnection con1 = mapUrl.openConnection();
					BufferedReader reader = new BufferedReader(new InputStreamReader(con1.getInputStream()));
					dw.writeMap((JSONObject)parser.parse(reader), i);
					reader.close();
				}
				return true;
			}catch(IOException e){
				Log.i("DEBUG", e.getLocalizedMessage());
				return false;
			} catch (ParseException e) {
				Log.i("DEBUG", e.getLocalizedMessage()+"//"+e.getPosition());
				return false;
			}
		}
		
		@Override
		protected void onPostExecute(Boolean data){
			if(data != false){
				showDownloadSuccesfull("map");
			}else{
				showDownloadError("map");
			}
			if(onComplete != null)
				onComplete.onComplete(data);
		}
		
	}

	public void showDownloadError(String what) {
		//Toast.makeText(context, "Stahování "+what+" selhalo", Toast.LENGTH_LONG).show();
	} 

	public void showDownloadSuccesfull(String what) {
		//Toast.makeText(context, "Stahování "+what+" dokončeno", Toast.LENGTH_LONG).show();
	}
	
	public String getPage(String url) throws IOException{
		URL suplovUrl = new URL("http://gymik.jacktech.cz/jidlo_parser.php");
		URLConnection con1 = suplovUrl.openConnection();
		BufferedReader reader = new BufferedReader(new InputStreamReader(con1.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String s;
		while((s = reader.readLine()) != null)
			sb.append(s);
		return sb.toString();
	}

}
