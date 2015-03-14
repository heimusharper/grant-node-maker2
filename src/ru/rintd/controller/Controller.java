package ru.rintd.controller;

import ru.rintd.model.res.Model;
import ru.rintd.view.AppWindow;

/**
 * Контроллер. Принимает сигналы интерфейса, передает сигналы модели и снова
 * управляет интерфейсом
 * 
 * @author sheihar
 *
 */

public class Controller {

	private AppWindow mainWindow;
	private Model model;

	public Controller() {

	}

	public int doShowMainWindow() {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				
				mainWindow = new AppWindow();
				
			}
		});
		
		return 0;
	}

}
