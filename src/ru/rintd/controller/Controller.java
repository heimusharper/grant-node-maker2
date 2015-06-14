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
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;

import com.vividsolutions.jts.geom.Coordinate;

import ru.rintd.controller.network.Connector;
import ru.rintd.json2grid.BuildElement;
import ru.rintd.json2grid.Building;
import ru.rintd.json2grid.Json2Grid;
import ru.rintd.json2grid.Node;
import ru.rintd.model.res.AddNodeSomeAction;
import ru.rintd.model.res.Model;
import ru.rintd.model.res.SomeActionS;
import ru.rintd.view.AppWindow;
import ru.rintd.view.CorbaConnectorFrame;
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
	// инструмент
	private int instrument = 0;

	private final int NO_REACTION = 0;
	private final int ADD_SENSOR = 1;
	private final int DELETE = -1;
	private final int ADD_LIGHT = 2;
	private final int ADD_POINTER = 3;
	private final int ADD_SERVER = 4;

	private Connector connector;

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
				configureActions();

				try {
					connector = new Connector(null, "127.0.0.1:38011");
					connector.Add_Node("111111");
					/*
					 * Runtime.getRuntime().addShutdownHook( new Thread(new
					 * Runnable() { public void run() { connector.unregister();
					 * } }));
					 */
				} catch (InvalidName e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (AdapterInactive e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ServantNotActive e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (WrongPolicy e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		return 0;
	}

	/**
	 * инициализация всех Action, например, ActionListener's кнопок
	 */
	private void configureActions() {

		Controller t = this;
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

				props = new PropertiesFrame(appPreferences, t);

			}
		});

		mainWindow.setAddSensorButtonActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				instrument = ADD_SENSOR;

			}
		});

		mainWindow.setAddLightButtonActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				instrument = ADD_LIGHT;

			}
		});
		mainWindow.setAddPointerButtonActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				instrument = ADD_POINTER;

			}
		});
		mainWindow.setAddServerButtonActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				instrument = ADD_SERVER;

			}
		});

		mainWindow.setPointButtonActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				instrument = NO_REACTION;

			}
		});
		mainWindow.setDeleteButtonActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				instrument = DELETE;

			}
		});
		mainWindow.setSaveButtonActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				showFileSaveDialog();

			}
		});
		mainWindow.setCorbaButtonActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				CorbaConnectorFrame connectorFrame = new CorbaConnectorFrame();
				connectorFrame.setConnectAction(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						connectToCorba(connectorFrame.getAddresField()
								.getText(), Integer.parseInt(connectorFrame
								.getPortField().getText()));
						connectorFrame.dispose();
					}
				});
				connectorFrame.setSize(new Dimension(200, 100));
				connectorFrame.setVisible(true);

			}
		});

		mainWindow.setUndoButtonActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				actionS.back();
				repaintWindow();
			}
		});

		mainWindow.setRendoButtonActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				actionS.forvard();
				repaintWindow();
			}
		});
	}

	private void connectToCorba(String addres, int port) {
		// TODO: connecting to cloud!
	}

	public void repaintWindow() {
		mainWindow.repaint();
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

					log.info("Click to [" + e.getX() + "; " + e.getY() + "]");
					// поиск узла
					Node n = jtPanel.getXYNode(e.getX(), e.getY());
					if (n != null) {
						log.info("Select Node", n.toString());
						if (n.uid != null) {
							multiPanel.setNode(n, model.getNodeInfo().get(n));
						} else
							multiPanel.setNode(n, null);
					} else {

						// поиск элемента
						BuildElement buildElement = jtPanel.getXYelement(
								e.getX(), e.getY());
						// System.out.println(">"+buildElement.Id);
						if (buildElement == null) {
							log.info("Outside building!");
							// вне здания - вывод информации о здании
							multiPanel.setBuilding(model.getBuilding());
						} else {
							log.info("Inside the building!");
							// попали в элемент - вывод инфы о элементе
							multiPanel.setBuildElement(buildElement);
							if ((instrument == ADD_SENSOR && !JtPanel
									.isDoor(buildElement.Sign))
									|| (instrument == ADD_LIGHT && JtPanel
											.isDoor(buildElement.Sign))
									|| (instrument == ADD_POINTER && !JtPanel
											.isDoor(buildElement.Sign))
									|| (instrument == ADD_SERVER && !JtPanel
											.isDoor(buildElement.Sign))) {
								log.info("Add new Node...");
								// добавление сенсора
								Coordinate coord = jtPanel.getPoint(e
										.getPoint());
								log.info("Converted coordinates ["
										+ e.getPoint().x + "; "
										+ e.getPoint().y + "] -> [" + coord.x
										+ "; " + coord.y + "]");
								multiPanel.setClick(coord);
								Node node = new Node(buildElement.Id,
										instrument, coord.x, coord.y);
								log.info("Node: " + node + " push");
								actionS.push(new AddNodeSomeAction(node, is));
								multiPanel.setNodeTree(model.getNodes());
								jtPanel.repaint();
								// jtPanel.setNodes(model.getNodesLevel(is));
							}
						}
					}
					super.mouseClicked(e);

				}
			});
			i++;
		}

		multiPanel.setNodeTree(model.getNodes());
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
				log.info("Open window close dialog...");
				String[] opStrings = { "Yes", "No" };
				int n = JOptionPane.showOptionDialog(e.getWindow(),
						"Close window?", "Close", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, opStrings,
						opStrings[0]);
				if (n == 0) {
					log.info("Closing...");
					// скрываем окно
					e.getWindow().setVisible(false);
					// сохраняем настройки
					log.info("Saving preferences...");
					appPreferences.windowWidth = e.getWindow().getWidth();
					appPreferences.windowHeight = e.getWindow().getHeight();
					appPreferences.saveAll();
					// закрываем приложение
					log.info("Exit");
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
			mainWindow.setToDrawPolygons(
					mainWindow.getBuildingPanelDimension(), model);
			// mainWindow.init();
			log.info("Set multi panel...");
			setMultiPanel();
			log.info("Set actions to building panel...");
			configureActionsAfter();
			// model.initNodes(model.getBuilding().Level.length);
		}
	}

	/**
	 * диалог сохранения файла
	 */
	private void showFileSaveDialog() {
		log.info("Save file...");
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter(
				"JSON file", "json");
		chooser.setFileFilter(fileNameExtensionFilter);
		int value = chooser.showSaveDialog(null);
		if (value == JFileChooser.APPROVE_OPTION) {
			String filePath = chooser.getSelectedFile().getAbsolutePath();
			log.info("Save file to " + filePath + " ...");
			Building b = model.getBuildingToSave();
			Json2Grid.saveVMjson(filePath, b);
			log.info("Save done.");
		}
	}

}
