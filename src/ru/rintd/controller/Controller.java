package ru.rintd.controller;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.rintd.model.res.Model;
import ru.rintd.view.AppWindow;

/**
 * Контроллер. Принимает сигналы интерфейса, передает сигналы модели и снова
 * управляет интерфейсом
 * 
 * @author sheihar
 */

public class Controller {

	private AppWindow mainWindow;
	private Model model;

	private Dimension windowDimension = new Dimension(1000, 800);

	private static final Logger log = LogManager.getLogger(Controller.class
			.getName());

	public Controller() {

	}

	/**
	 * отобразить главное окно приложения
	 * 
	 * @return
	 */
	public int doShowMainWindow() {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				// инициализация окна
				log.info("Init main window...");
				mainWindow = new AppWindow(windowDimension);
				// инициализация ресурсов(модели)
				model = new Model();
				// настройка событий
				log.info("Setting actions...");
				configureActions();

			}
		});

		return 0;
	}

	/**
	 * инициализация всех Action, например, ActionListener's кнопок
	 */
	private void configureActions() {
		// кнопка открытия
		mainWindow.setOpenFileButtonActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// диалог
				showFileOpenDialog();

			}
		});

		mainWindow.setZoomInButtonActionLinsteber(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.zoomIn();

			}
		});

		mainWindow.setZoomOutButtonActionLinsteber(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.zoomOut();

			}
		});
		mainWindow.setZoomNULLButtonActionLinsteber(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.zoomDef();

			}
		});
	}

	/**
	 * отобразить диалог открытия файла
	 */
	private void showFileOpenDialog() {
		log.info("Load open file chooser...");
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter(
				"JSON file", "json");
		chooser.setFileFilter(fileNameExtensionFilter);
		int value = chooser.showOpenDialog(null);
		if (value == JFileChooser.APPROVE_OPTION) {
			log.info("File chooser return APPROVE_OPTION value");
			String filePath = chooser.getSelectedFile().getAbsolutePath();
			log.info("Load file PATH: " + filePath);
			model.setJsonString(filePath);
			// TODO: отобразить где-то название файла
			log.info("Start convert building to Polygons...");
			// log.info("LOCAL DIM:"+mainWindow.getBuildingPanelDimension());
			// mainWindow.setToDrawPolygons(model.getToDrawPolygons(mainWindow.getBuildingPanelDimension()),
			// mainWindow.getBuildingPanelDimension());
			mainWindow.setToDrawPolygons(model.getToDrawPolygons(),
					mainWindow.getBuildingPanelDimension());
			// mainWindow.init();
		}
	}

}
