package ru.rintd.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vividsolutions.jts.geom.Polygon;

import ru.rintd.controller.Run;
import ru.rintd.view.jtsView.JtPanel;

public class AppWindow {

	// оено приложения
	private JFrame mainFrame;
	// предыдущий заголовок
	private String applicationTitle = "";
	// логгер
	private static final Logger log = LogManager.getLogger(AppWindow.class
			.getName());

	private Dimension windowDimension;

	// панели
	private AppWindowButtonsJToolBar actionButtons;
	private BuildingPanel buildingPanel;

	public AppWindow(Dimension dimension) {

		this.windowDimension = dimension;
		log.info("Start intrface...");
		mainFrame = new JFrame();
		setDefaults();
		setButtonsPanel();
		setToDrawPolygons();
		mainFrame.setVisible(true);
	}

	/**
	 * настройки окна по-умолчанию
	 */
	public void setDefaults() {
		log.info("Set default params in window");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(windowDimension);
		this.setTitle(null);
		
	}

	/**
	 * Текст в заголовке окна типа: <b>"Run.APPLICATION_NAME title"</b>
	 * 
	 * @param title
	 *            заголовок, чаще название файла, при null сбрасывается до
	 *            названия программы
	 * @return предыдущая строка
	 */
	public String setTitle(String title) {
		String tmp = applicationTitle;
		if (title != null) {
			applicationTitle = Run.APPLICATION_NAME + " " + title;
		} else {
			applicationTitle = Run.APPLICATION_NAME;
		}
		mainFrame.setTitle(applicationTitle);
		log.info("Set new title: " + applicationTitle + ", old: " + tmp);
		return tmp;
	}

	/**
	 * добавляет статусное сообщение в заголовок программы типа:
	 * <b>"заголовок [ status ]"</b>
	 * 
	 * @param status
	 *            статусное сообщение, вообще - люая строка
	 */
	public void setTitleStatus(String status) {
		mainFrame.setTitle(applicationTitle + " [" + status + "]");
		log.info("Set new status: " + status);
	}

	/**
	 * Встаавить панель с кнопками сверху
	 */
	private void setButtonsPanel() {
		actionButtons = new AppWindowButtonsJToolBar();
		mainFrame.add(actionButtons, BorderLayout.NORTH);
	}

	
	/**
	 * задать, что делать при нажатии кнопки "Open" (открыть)
	 * 
	 * @param action
	 *            действие ActionListener
	 */
	public void setOpenFileButtonActionListener(ActionListener action) {
		this.actionButtons.openJButton.addActionListener(action);
	}

	/**
	 * задать, что делать при нажатии кнопки "Save" (сохранить)
	 * 
	 * @param action
	 *            действие ActionListener
	 */
	public void setSaveFileButtonActionListener(ActionListener action) {
		this.actionButtons.saveJButton.addActionListener(action);
	}
	
	public void setZoomInButtonActionLinsteber(ActionListener action) {
		this.actionButtons.zoomInJButton.addActionListener(action);
	}
	
	public void setZoomOutButtonActionLinsteber(ActionListener action) {
		this.actionButtons.zoomOutJButton.addActionListener(action);
	}
	
	public void setZoomNULLButtonActionLinsteber(ActionListener action) {
		this.actionButtons.zoom0JButton.addActionListener(action);
	}
	public void setPropertiesButtonActionListener(ActionListener actionListener){
		this.actionButtons.preferencesButton.addActionListener(actionListener);
	}
	
	public void setToDrawPolygons() {
		buildingPanel = new BuildingPanel();
		buildingPanel.setPreferredSize(windowDimension);
		mainFrame.add(buildingPanel, BorderLayout.CENTER);
	}

	/*
	 * public void setToDrawPolygons(ToDrawPolygons[] toDrawPolygons, Dimension
	 * dimension){ this.windowDimension = dimension; buildingPanel = new
	 * BuildingPanel(); buildingPanel.setPreferredSize(windowDimension);
	 * mainFrame.add(buildingPanel, BorderLayout.CENTER);
	 * //log.info("SET DIM:"+windowDimension); if (toDrawPolygons != null)
	 * buildingPanel.init(toDrawPolygons); else buildingPanel.init(); }
	 */
	
	public void setToDrawPolygons(Polygon[][] toDrawPolygons,
			Dimension dimension) {
		this.windowDimension = dimension;
		// log.info("SET DIM:"+windowDimension);
		if (toDrawPolygons != null) {
			buildingPanel.init(toDrawPolygons);
			log.info("Init draw polygons...");
		} else {
			buildingPanel.init();
			log.warn("No to draw polygons!");
		}
	}

	public Dimension getBuildingPanelDimension() {
		// log.info("DIM "+buildingPanel.getSize());
		return buildingPanel.getSize();
	}

	public void init() {
		buildingPanel.init();
	}
	
	public void zoomIn() {
		buildingPanel.zoomIn();
	}
	public void zoomOut() {
		buildingPanel.zoomOut();
	}
	public void zoomDef(){
		buildingPanel.zoomDef();
	}
	
	public JtPanel[] getJtPanel(){
		return buildingPanel.getJtPanel();
	}
	
	public void add(Component com, Object constrainst ){
		
		mainFrame.add(com, constrainst);
	}

	public void setWindowClosing(WindowListener listener){
		mainFrame.addWindowListener(listener);
	}
	
}

