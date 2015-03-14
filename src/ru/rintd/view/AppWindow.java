package ru.rintd.view;

import javax.swing.JFrame;

import ru.rintd.controller.Run;

public class AppWindow {

	private JFrame mainFrame;
	private String applicationTitle = "";

	public AppWindow() {

		mainFrame = new JFrame();

		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		return tmp;
	}

	/**
	 * добавляет статусное сообщение в заголовок программы типа:
	 * <b>"заголовок [ status ]"</b>
	 * 
	 * @param status статусное сообщение, вообще - люая строка
	 */
	public void setTitleStatus(String status) {
		mainFrame.setTitle(applicationTitle + " [" + status + "]");
	}

}
