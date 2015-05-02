package ru.rintd.controller;

import java.awt.Dimension;
import java.util.prefs.Preferences;

/**
 * Настройки приложения
 * 
 * @author sheihar
 *
 */

public class AppPreferences {

	/**
	 * настройки
	 */
	private Preferences preferences;

	// ширина окна
	public int windowWidth = 1000;
	// высота окна
	public int windowHeight = 800;
	// выводить ли метровую сетку на изображении
	public boolean planGrid = true;
	// покзывать ли названия на комнатах и дверях
	public boolean showLabelsPlan = true;

	// касательно шрифта названий на элементах
	// шрифт названий
	public String fontPlan = "Serif";
	// начертание
	public String fontType = "PLAIN";
	// размер
	public int fontSize = 18;

	/**
	 * инициализация стандартных настроек при первом запуске или загрузка, если
	 * они есть
	 */
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

	/**
	 * получит размеры окна из настроек
	 * @return размеры
	 */
	public Dimension getWindowDim() {
		return new Dimension(windowWidth, windowHeight);
	}

	/**
	 * сохранить все настройки
	 */
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
