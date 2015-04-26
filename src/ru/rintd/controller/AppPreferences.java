package ru.rintd.controller;

import java.awt.Dimension;
import java.util.prefs.Preferences;

public class AppPreferences {

	private Preferences preferences;
	
	public int windowWidth = 1000;
	public int windowHeight = 800;
	public boolean planGrid = true;
	
	public AppPreferences(){
		preferences = Preferences.userNodeForPackage(AppPreferences.class);
		windowWidth = preferences.getInt("windowWidth", 1000);
		windowHeight = preferences.getInt("windowHeight", 800);
		planGrid = preferences.getBoolean("planGrid", true);
	}
	
	public Dimension getWindowDim() {
		return new Dimension(windowWidth, windowHeight);
	}
	
	public void saveAll(){
		preferences.putInt("windowWidth", windowWidth);
		preferences.putInt("windowHeight", windowHeight);
		preferences.putBoolean("planGrid", planGrid);
	}
}
