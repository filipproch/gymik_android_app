package com.jacktech.gymik;

import org.json.simple.JSONObject;

public class Config {

	private JSONObject config;
	private DataWorker dataWorker;
	
	public Config(JSONObject config, DataWorker dataWorker){
		this.config = config;
		this.dataWorker = dataWorker;
	}
	
	public Config(DataWorker dataWorker){
		this.dataWorker = dataWorker;
		this.config = dataWorker.getDefaultConfig();
	}
	
	public void putConfig(Object config, Object value){
		this.config.put(config, value);
	}
	
	public Object getConfig(Object config){
		return this.config.get(config);
	}
	
	public void updateConfig(Object config, Object value){
		this.config.remove(config);
		this.config.put(config, value);
	}
	
	public void writeConfig(){
		dataWorker.writeConfig(config);
	}
}
