package ru.rintd.controller;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.rintd.json2grid.BuildElement;
import ru.rintd.json2grid.Node;
import ru.rintd.model.res.AddNodeSomeAction;
import ru.rintd.model.res.Model;
import ru.rintd.model.res.SomeActionS;
import ru.rintd.view.AppWindow;
import ru.rintd.view.MultiPanel;
import ru.rintd.view.PropertiesFrame;
import ru.rintd.view.jtsView.JtPanel;

/**
 * Контроллер. Принимает сигналы интерфейса, передает сигналы модели и снова
 * управляет интерфейсом
 * 
 * @author sheihar
 */

public class Controller {

	// главное окно приложения
	private AppWindow mainWindow;
	// панель информации справа
	private MultiPanel multiPanel;
	// модель данных
	private Model model;
	// настройки приложения
	public static AppPreferences appPreferences;
	// окно настроек приложения
	private PropertiesFrame props;
	// Система событий
	private SomeActionS actionS;
	// нструмент
	private int instrument = 0;
	
	private final int NO_REACTION = 0;
	private final int ADD_SENSOR = 1;
	private final int DELETE = -1;
	private final int ADD_LIGHT = 2;
	private final int ADD_POINTER = 3;
	private final int ADD_SERVER = 4;

	// размеры изображения
	private Dimension windowDimension;

	private static final Logger log = LogManager.getLogger(Controller.class
			.getName());

	public Controller() {
		appPreferences = new AppPreferences();
		windowDimension = appPreferences.getWindowDim();
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
				setOnCloseWindow();
				// инициализация ресурсов(модели)
				model = new Model();
				// инициализация событий
				actionS = new SomeActionS(model);
				// настройка событий
				log.info("Setting actions...");
				props = new PropertiesFrame(appPreferences);
				props.setVisible(false);
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

		// масштаб ++
		mainWindow.setZoomInButtonActionLinsteber(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.zoomIn();

			}
		});

		// масштаб --
		mainWindow.setZoomOutButtonActionLinsteber(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.zoomOut();

			}
		});
		// масштаб 00
		mainWindow.setZoomNULLButtonActionLinsteber(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.zoomDef();

			}
		});
		// кнопка открытия настроек
		mainWindow.setPropertiesButtonActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				props.setVisible(true);

			}
		});
		
		mainWindow.setAddSensorButtonActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				instrument = ADD_SENSOR;
				
			}
		});

	}

	/**
	 * настройки после инициализации здания
	 */
	private void configureActionsAfter() {

		// клик по области здания и сама его инициализация
		JtPanel[] jtPanels = mainWindow.getJtPanel();
		int i = 0;
		for (JtPanel jtPanel : jtPanels) {
			final int is = i;
			jtPanel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {

					// поиск элемента
					BuildElement buildElement = jtPanel.getXYelement(e.getX(),
							e.getY());
					// System.out.println(">"+buildElement.Id);
					if (buildElement != null) {
						// вне здания - вывод информации о здании
						multiPanel.setBuildElement(buildElement);
					} else {
						// попали в элемент - вывод инфы о элементе
						multiPanel.setBuilding(model.getBuilding());
						if (instrument == ADD_SENSOR){
							// добавление сенсора
							// TODO: преобразовать координаты и подставить вместо 0 0
							Node node = new Node(buildElement.Id, ADD_SENSOR, 0, 0);
							actionS.push(new AddNodeSomeAction(node, is));
						}
					}
					super.mouseClicked(e);

				}
			});
			i++;
		}
	}

	/**
	 * правая информационно-настроечная панель
	 */
	private void setMultiPanel() {
		multiPanel = new MultiPanel();
		mainWindow.add(multiPanel, BorderLayout.EAST);
	}

	/**
	 * вот это портянка
	 */
	private void setOnCloseWindow() {
		mainWindow.setWindowClosing(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosing(WindowEvent e) {
				// вывод окна подтверждения выхода
				String[] opStrings = { "Yes", "No" };
				int n = JOptionPane.showOptionDialog(e.getWindow(),
						"Close window?", "Close", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, opStrings,
						opStrings[0]);
				if (n == 0) {
					// скрываем окно
					e.getWindow().setVisible(false);
					// сохраняем настройки
					appPreferences.windowWidth = e.getWindow().getWidth();
					appPreferences.windowHeight = e.getWindow().getHeight();
					appPreferences.saveAll();
					// закрываем приложение
					System.exit(0);
				} else {
					// TODO: отмена закрытия
				}

			}

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub

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
			setMultiPanel();
			configureActionsAfter();
		}
	}

}
