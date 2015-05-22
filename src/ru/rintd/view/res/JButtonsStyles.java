package ru.rintd.view.res;

import java.awt.Insets;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * используются иконки Gnome Project
 * 
 * @author sheihar
 */

public class JButtonsStyles {

	public static final int POINTER_ID = 0;
	public static final int OPEN_ID = 1;
	public static final int SAVE_ID = 2;
	public static final int UNDO_ID = 3;
	public static final int RENDO_ID = 4;
	public static final int DELETE_ID = 5;
	public static final int ZOOM_IN_ID = 6;
	public static final int ZOOM_ORIGINAL_ID = 7;
	public static final int ZOOM_OUT_ID = 8;

	// светофоры
	public static final int LIGHT_GREEN = 9;
	public static final int LIGHT_RED = 10;
	public static final int LIGHT_YELOW = 11;
	// указатели направления
	public static final int POINTER_GREEN = 12;
	public static final int POINTER_RED = 13;
	public static final int POINTER_YELOW = 14;
	// сенсоры
	public static final int SENSOR_GREEN = 15;
	public static final int SENSOR_RED = 16;
	public static final int SENSOR_YELOW = 17;
	// сервер
	public static final int SERVER_GREEN = 18;
	public static final int SERVER_RED = 19;
	public static final int SERVER_YELOW = 20;

	// настройки
	public static final int PREFERENCES_ID = 21;

	public static final int CORBA_BUTTON = 22;
	public static final int CORBA_ERROR = 23;
	public static final int CORBA_IDLE = 22;
	public static final int CORBA_OFFLINE = 24;
	public static final int CORBA_RECEIVE = 25;
	public static final int CORBA_TRANSMIT = 26;

	private final static String[] BUTTONS_NAMES = { "Pointer", "Open", "Save",
			"Undo", "Rendo", "Delete", "ZoomIn", "ZoomNull", "ZoomOut",
			"Light", "Light ERROR", "Light NULL", "Pointer", "Pointer ERROR",
			"Pointer NULL", "Sensor", "Sensor ERROR", "Sensor NULL", "Server",
			"Server ERROR", "Server NULL", "Preferences", "Connect to CORBA",
			"CORBA ERROR", "CORBA idle", "CORBA offline", "CORBA receive",
			"CORBA transmit"};

	private final static String[] BUTTONS_DESCRIPTIONS = { "Pointer", "Open",
			"Save", "Undo", "Rendo", "Delete", "ZoomIn", "ZoomNull", "ZoomOut",
			"Light", "Light ERROR", "Light NULL", "Pointer", "Pointer ERROR",
			"Pointer NULL", "Sensor", "Sensor ERROR", "Sensor NULL", "Server",
			"Server ERROR", "Server NULL", "Preferences", "Connect to CORBA",
			"CORBA ERROR", "CORBA idle", "CORBA offline", "CORBA receive",
			"CORBA transmit" };

	private final static String[] BUTTONS_FILE = { "pointer", "document-open",
			"document-save", "edit-redo-rtl", "edit-redo", "edit-delete",
			"zoom-in", "zoom-original", "zoom-out", "nodes/light_green",
			"nodes/light_red", "nodes/light_yelow", "nodes/pointer_green",
			"nodes/pointer_red", "nodes/pointer_yelow", "nodes/sensor_green",
			"nodes/sensor_red", "nodes/sensor_yelow", "nodes/server_green",
			"nodes/server_red", "nodes/server_yelow", "preferences-system",
			"network-idle", "network-error", "network-offline",
			"network-receive", "network-transmit" };

	// логгер
	private static final Logger log = LogManager.getLogger(JButtonsStyles.class
			.getName());

	/**
	 * возвращает кнопку по её id
	 * 
	 * @param type
	 *            используются встроенные константы типа *_ID
	 * @return кнопка
	 */
	public static JButton getButton(int type) {

		log.info("Generate button type " + type + " [" + BUTTONS_NAMES[type]
				+ " is " + BUTTONS_DESCRIPTIONS[type] + "]");
		JButton button = new JButton();

		button.setToolTipText(BUTTONS_DESCRIPTIONS[type]);
		URL img = JButtonsStyles.class.getResource("icons/"
				+ BUTTONS_FILE[type] + ".png");
		System.out.print(">" + BUTTONS_FILE[type]);
		button.setIcon(new ImageIcon(img));
		button.setOpaque(false);
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
		button.setMargin(new Insets(0, 0, 0, 0));

		return button;
	}

	public static ImageIcon getImg(int img) {
		return new ImageIcon(JButtonsStyles.class.getResource("icons/"
				+ BUTTONS_FILE[img] + ".png"));
	}

}
