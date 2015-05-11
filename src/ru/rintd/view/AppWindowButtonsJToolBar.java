package ru.rintd.view;

import javax.swing.JButton;
import javax.swing.JToolBar;

import ru.rintd.view.res.JButtonsStyles;

public class AppWindowButtonsJToolBar extends JToolBar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4679491453627123935L;
	// выбранный инструмент
	// private int type = 0;
	// логгер
	// private static final Logger log =
	// LogManager.getLogger(AppWindowButtonsJPane.class.getName());

	// кнопки
	// управление
	// указатель
	JButton pointerJButton = JButtonsStyles
			.getButton(JButtonsStyles.POINTER_ID);
	// открыть
	JButton openJButton = JButtonsStyles.getButton(JButtonsStyles.OPEN_ID);
	// сохранить
	JButton saveJButton = JButtonsStyles.getButton(JButtonsStyles.SAVE_ID);
	// отменить
	JButton undoJButton = JButtonsStyles.getButton(JButtonsStyles.UNDO_ID);
	// возвратить
	JButton rendoJButton = JButtonsStyles.getButton(JButtonsStyles.RENDO_ID);
	// удалить
	JButton deleteJButton = JButtonsStyles.getButton(JButtonsStyles.DELETE_ID);
	// zoom +
	JButton zoomInJButton = JButtonsStyles.getButton(JButtonsStyles.ZOOM_IN_ID);
	// zoom 0
	JButton zoom0JButton = JButtonsStyles
			.getButton(JButtonsStyles.ZOOM_ORIGINAL_ID);
	// zoom -
	JButton zoomOutJButton = JButtonsStyles
			.getButton(JButtonsStyles.ZOOM_OUT_ID);
	// сенсоры
	JButton sensorAddJButton = JButtonsStyles
			.getButton(JButtonsStyles.SENSOR_YELOW);
	// светофоры
	JButton lightAddJButton = JButtonsStyles
			.getButton(JButtonsStyles.LIGHT_YELOW);
	// указатели
	JButton pointersAddJButton = JButtonsStyles
			.getButton(JButtonsStyles.POINTER_YELOW);
	// сервер
	JButton serverAddJButton = JButtonsStyles
			.getButton(JButtonsStyles.SERVER_YELOW);
	// настройки
	JButton preferencesButton = JButtonsStyles
			.getButton(JButtonsStyles.PREFERENCES_ID);

	// TODO: остальные кнопки

	//

	public AppWindowButtonsJToolBar() {

		// this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(pointerJButton);
		this.add(openJButton);
		this.add(saveJButton);
		saveJButton.setEnabled(false);
		this.add(undoJButton);
		undoJButton.setEnabled(false);
		this.add(rendoJButton);
		rendoJButton.setEnabled(false);
		this.add(deleteJButton);
		deleteJButton.setEnabled(false);
		this.add(zoomInJButton);
		zoomInJButton.setEnabled(false);
		this.add(zoom0JButton);
		zoom0JButton.setEnabled(false);
		this.add(zoomOutJButton);
		zoomOutJButton.setEnabled(false);
		this.add(preferencesButton);
		preferencesButton.setEnabled(false);
		this.addSeparator();
		this.add(sensorAddJButton);
		sensorAddJButton.setEnabled(false);
		this.add(lightAddJButton);
		lightAddJButton.setEnabled(false);
		this.add(pointersAddJButton);
		pointersAddJButton.setEnabled(false);
		this.add(serverAddJButton);
		serverAddJButton.setEnabled(false);

	}

	public void fileIsOpened() {
		saveJButton.setEnabled(true);
		undoJButton.setEnabled(true);
		rendoJButton.setEnabled(true);
		deleteJButton.setEnabled(true);
		zoomInJButton.setEnabled(true);
		zoom0JButton.setEnabled(true);
		zoomOutJButton.setEnabled(true);
		sensorAddJButton.setEnabled(true);
		lightAddJButton.setEnabled(true);
		pointersAddJButton.setEnabled(true);
		serverAddJButton.setEnabled(true);
		preferencesButton.setEnabled(true);
	}

}
