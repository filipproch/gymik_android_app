package com.jacktech.gymik;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class UpdateClass {

	private DataWorker dw;
	private Config config;
	private Context context;
	
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
	
	private class SuplovDownloader extends AsyncTask<Void, Void, Object[]>{

		@Override
		protected Object[] doInBackground(Void... params) {
			Object[] data = new Object[3];
			try{
				JSONParser parser = new JSONParser();
				//stahovani suplovani
				URL suplovUrl = new URL("http://gymik.jacktech.cz/suplov_parser.php?class="+config.getConfig("class"));
				URLConnection con1 = suplovUrl.openConnection();
				BufferedReader reader = new BufferedReader(new InputStreamReader(con1.getInputStream()));
				data[0] = (JSONObject) parser.parse(reader);
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
		protected void onPostExecute(Object[] data){
			if(data != null){
				config.updateConfig("lastSuplov", System.currentTimeMillis()+"");
				config.writeConfig();
				dw.writeSuplovani((JSONObject)data[0]);
				showDownloadSuccesfull("suplování");
			}else{
				showDownloadError("suplování");
			}
		}
		
	}
	
	private class JidloDownloader extends AsyncTask<Void, Void, Object[]>{

		@Override
		protected Object[] doInBackground(Void... params) {
			Object[] data = new Object[3];
			try{
				JSONParser parser = new JSONParser();
				//stahovani suplovani
				URL suplovUrl = new URL("http://gymik.jacktech.cz/jidlo_parser.php");
				URLConnection con1 = suplovUrl.openConnection();
				BufferedReader reader = new BufferedReader(new InputStreamReader(con1.getInputStream()));
				data[0] = (JSONObject) parser.parse(reader);
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
		protected void onPostExecute(Object[] data){
			if(data != null){
				dw.writeJidlo((JSONObject)data[0]);
				showDownloadSuccesfull("jídlo");
			}else{
				showDownloadError("jídlo");
			}
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
		}
		
	}

	public void showDownloadError(String what) {
		Toast.makeText(context, "Stahování "+what+" selhalo", Toast.LENGTH_LONG).show();
	} 
	
	public void showDownloadSuccesfull(String what) {
		Toast.makeText(context, "Stahování "+what+" dokončeno", Toast.LENGTH_LONG).show();
	}
	
}
