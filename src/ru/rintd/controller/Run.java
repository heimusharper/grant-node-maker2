package ru.rintd.controller;

/**
 * Загрузчик приложения, инициализирует контроллер и запускает главное окно
 * 
 * @author sheihar
 *
 */

public class Run {
	
	public static final String APPLICATION_NAME = "NodeMaker2";

	private static Controller controller = null;

	public static void main(String[] args) {

		controller = new Controller();

		controller.doShowMainWindow();
		/*
		 * javax.swing.SwingUtilities.invokeLater(new Runnable() {
		 * 
		 * @Override public void run() { controller.doShowMainWindow(); } });
		 */
	}
}
