package ru.rintd.controller;

import java.awt.Dimension;
import java.awt.Font;
import java.util.prefs.Preferences;

public class AppPreferences {

	private Preferences preferences;

	public int windowWidth = 1000;
	public int windowHeight = 800;
	public boolean planGrid = true;
	public boolean showLabelsPlan = true;

	public String fontPlan = "Serif";
	public String fontType = "PLAIN";
	public int fontSize = 18;

	public AppPreferences() {
		preferences = Preferences.userNodeForPackage(AppPreferences.class);
		windowWidth = preferences.getInt("windowWidth", 1000);
		windowHeight = preferences.getInt("windowHeight", 800);
		planGrid = preferences.getBoolean("planGrid", true);
		planGrid = preferences.getBoolean("showLabelsPlan", true);

		fontPlan = preferences.get("fontPlan", "Serif");
		fontSize = preferences.getInt("fontSize", 18);
		fontType = preferences.get("fontType", "PLAIN");
	}

	public Dimension getWindowDim() {
		return new Dimension(windowWidth, windowHeight);
	}

	public void saveAll() {
		preferences.putInt("windowWidth", windowWidth);
		preferences.putInt("windowHeight", windowHeight);
		preferences.putBoolean("planGrid", planGrid);
		preferences.putBoolean("showLabelsPlan", showLabelsPlan);

		preferences.putInt("fontSize", fontSize);
		preferences.put("fontPlan", "Serif");
		preferences.put("fontType", fontType);
	}
}
