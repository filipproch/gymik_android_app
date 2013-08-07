package com.jacktech.gymik;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import android.app.Activity;
import android.os.Environment;

public class DataWorker {
	public static final int configVersion = 2;
	private static String CONFIG_FILE_NAME = "config.data";
	private static String NEWS_FILE_NAME = "news.data";
	private static String ROZVRH_FILE_NAME = "rozvrh.data";
	private static String SUPLOV_FILE_NAME = "suplov.data";
	private Activity activity;

	private boolean mExternalStorageAvailable = false;
	private boolean mExternalStorageWriteable = false;
	
	public DataWorker(Activity activity){
		this.activity = activity;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
		    mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
		    mExternalStorageAvailable = true;
		    mExternalStorageWriteable = false;
		} else {
		    mExternalStorageAvailable = mExternalStorageWriteable = false;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void writeNews(List<RSSFeed> data){
		if(mExternalStorageWriteable){
			JSONObject datax = new JSONObject();
			List<JSONObject> newsList = new ArrayList<JSONObject>();
			for(RSSFeed feed : data){
				JSONObject news = new JSONObject();
				news.put("title", feed.getTitle());
				news.put("description", feed.getDescription());
				news.put("link", feed.getLink());
				news.put("date", feed.getPubDate());
				newsList.add(news);
			}
			datax.put("news", newsList);
			File dataDir = activity.getExternalFilesDir(null);
			try {
				FileWriter writer = new FileWriter(dataDir.getAbsolutePath()+"/"+NEWS_FILE_NAME);
				datax.writeJSONString(writer);
				writer.close();
			} catch (IOException e) {
			}
		}
	}
	
	public void writeRozvrh(JSONObject rozvrh){
		if(mExternalStorageWriteable){
			File dataDir = activity.getExternalFilesDir(null);
			try {
				FileWriter writer = new FileWriter(dataDir.getAbsolutePath()+"/"+ROZVRH_FILE_NAME);
				rozvrh.writeJSONString(writer);
				writer.close();
			} catch (IOException e) {
			}
		}
	}
	
	public void writeSuplovani(JSONObject suplov){
		if(mExternalStorageWriteable){
			File dataDir = activity.getExternalFilesDir(null);
			try {
				FileWriter writer = new FileWriter(dataDir.getAbsolutePath()+"/"+SUPLOV_FILE_NAME);
				suplov.writeJSONString(writer);
				writer.close();
			} catch (IOException e) {
			}
		}
	}
	
	public void writeConfig(JSONObject config){
		if(mExternalStorageWriteable){
			File dataDir = activity.getExternalFilesDir(null);
			try {
				FileWriter writer = new FileWriter(dataDir.getAbsolutePath()+"/"+CONFIG_FILE_NAME);
				config.writeJSONString(writer);
				writer.close();
			} catch (IOException e) {
			}
		}
	}
	
	public JSONObject getNews(){
		if(mExternalStorageAvailable){
			File dataDir = activity.getExternalFilesDir(null);
			try {
				BufferedReader reader = new BufferedReader(new FileReader(dataDir.getAbsolutePath()+"/"+NEWS_FILE_NAME));
				JSONParser parser = new JSONParser();
				JSONObject data = (JSONObject) parser.parse(reader);
				reader.close();
				return data;
			} catch (IOException e) {
				return null;
			} catch (ParseException e) {
				return null;
			}
			
		}else{
			return null;
		}
	}
	
	public JSONObject getRozvrh(){
		if(mExternalStorageAvailable){
			File dataDir = activity.getExternalFilesDir(null);
			try {
				BufferedReader reader = new BufferedReader(new FileReader(dataDir.getAbsolutePath()+"/"+ROZVRH_FILE_NAME));
				JSONParser parser = new JSONParser();
				JSONObject data = (JSONObject) parser.parse(reader);
				reader.close();
				return data;
			} catch (IOException e) {
				return null;
			} catch (ParseException e) {
				return null;
			}
			
		}else{
			return null;
		}
	}
	
	public JSONObject getSuplovani(){
		if(mExternalStorageAvailable){
			File dataDir = activity.getExternalFilesDir(null);
			try {
				BufferedReader reader = new BufferedReader(new FileReader(dataDir.getAbsolutePath()+"/"+SUPLOV_FILE_NAME));
				JSONParser parser = new JSONParser();
				JSONObject data = (JSONObject) parser.parse(reader);
				reader.close();
				return data;
			} catch (IOException e) {
				return null;
			} catch (ParseException e) {
				return null;
			}
			
		}else{
			return null;
		}
	}
	
	public JSONObject getConfig(){
		if(mExternalStorageAvailable){
			File dataDir = activity.getExternalFilesDir(null);
			try {
				BufferedReader reader = new BufferedReader(new FileReader(dataDir.getAbsolutePath()+"/"+CONFIG_FILE_NAME));
				JSONParser parser = new JSONParser();
				JSONObject data = (JSONObject) parser.parse(reader);
				reader.close();
				return data;
			} catch (IOException e) {
				writeConfig(getDefaultConfig());
				return getDefaultConfig();
			} catch (ParseException e) {
				return null;
			}
			
		}else{
			return null;
		}
	}
	
	private static final String[][] defaultConfigValues = new String[][]{{"configVersion",""+configVersion},{"class","-"},{"schoolYear","-"},{"bakUser","-"},{"bakPsw","-"},{"lastSuplov","-"},{"showMapColors","true"}};
	
	@SuppressWarnings("unchecked")
	public JSONObject getDefaultConfig(){
		JSONObject config = new JSONObject();
		for(String[] conf : defaultConfigValues)
			config.put(conf[0], conf[1]);
		return config;
	}

	public boolean isFirstRun() {
		if(!new File(activity.getExternalFilesDir(null).getAbsolutePath()+"/"+CONFIG_FILE_NAME).exists())
			return true;
		return false;
	}

	public JSONObject updateConfig(JSONObject config) {
		for(String[] conf : defaultConfigValues)
			if(config.get(conf[0]) == null)
				config.put(conf[0], conf[1]);
		return config;
	}
	
}
