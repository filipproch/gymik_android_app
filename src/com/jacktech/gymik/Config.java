package com.jacktech.gymik;

import org.json.simple.JSONObject;

public class Config {

	private JSONObject config;
	private DataWorker dataWorker;
	
	public Config(JSONObject config, DataWorker dataWorker){
		this.config = config;
		this.dataWorker = dataWorker;
		if(Integer.parseInt((String) this.getConfig("configVersion")) != DataWorker.configVersion){
			this.config = dataWorker.updateConfig(config);
			dataWorker.writeConfig(config);
		}
	}
	
	public Config(DataWorker dataWorker){
		this.dataWorker = dataWorker;
		this.config = dataWorker.getDefaultConfig();
	}
	
	@SuppressWarnings("unchecked")
	public void putConfig(Object config, Object value){
		this.config.put(config, value);
	}
	
	public Object getConfig(Object config){
		return this.config.get(config);
	}
	
	@SuppressWarnings("unchecked")
	public void updateConfig(Object config, Object value){
		this.config.remove(config);
		this.config.put(config, value);
	}
	
	public void writeConfig(){
		dataWorker.writeConfig(config);
	}

	public void reloadConfig() {
		this.config = dataWorker.getConfig();
	}
}
