package com.jacktech.gymik.bakalari;

import java.util.ArrayList;

import org.json.simple.JSONArray;

public class Predmet {

	public ArrayList<Znamka> znamky;
	public String name;
	public String ucitel;
	
	public Predmet(String name, String ucitel){
		this.name = name;
		this.ucitel = ucitel;
		znamky = new ArrayList<Znamka>();
	}
	
	public void addZnamka(Znamka z){
		znamky.add(z);
	}
	
	public void loadZnamky(JSONArray stored){
		
	}
	
}
