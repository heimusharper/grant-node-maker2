package ru.rintd.view;

import javax.swing.JFrame;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.rintd.controller.Run;

public class AppWindow {

	private JFrame mainFrame;
	private String applicationTitle = "";
	private static final Logger log = LogManager.getLogger(AppWindow.class
			.getName());

	public AppWindow() {

		log.info("Start intrface...");
		mainFrame = new JFrame();
		setDefaults();

		mainFrame.setVisible(true);
	}

	public void setDefaults() {
		log.info("Set default params in window");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(1000, 800);
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

}
