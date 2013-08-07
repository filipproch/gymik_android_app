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

	public void showDownloadError(String what) {
		Toast.makeText(context, "Stahování "+what+" selhalo", Toast.LENGTH_LONG).show();
	} 
	
	public void showDownloadSuccesfull(String what) {
		Toast.makeText(context, "Stahování "+what+" dokončeno", Toast.LENGTH_LONG).show();
	} 
	
}
