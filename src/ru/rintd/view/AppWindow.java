package ru.rintd.view;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.rintd.controller.Run;

public class AppWindow {

    // оено приложения
    private JFrame mainFrame;
    // предыдущий заголовок
    private String applicationTitle = "";
    // логгер
    private static final Logger log = LogManager.getLogger(AppWindow.class.getName());

    // панели
    private AppWindowButtonsJPane actionButtons;
    private BuildingPanel buildingPanel;

    public AppWindow() {

        log.info("Start intrface...");
        mainFrame = new JFrame();
        setDefaults();
        setButtonsPanel();
        setBuildingPanel(null);
        mainFrame.setVisible(true);
        buildingPanel.get
    }

    /**
     * настройки окна по-умолчанию
     */
    public void setDefaults() {
        log.info("Set default params in window");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1000, 800);
        this.setTitle(null);
    }

    /**
     * Текст в заголовке окна типа: <b>"Run.APPLICATION_NAME title"</b>
     * 
     * @param title заголовок, чаще название файла, при null сбрасывается до названия программы
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
     * добавляет статусное сообщение в заголовок программы типа: <b>"заголовок [ status ]"</b>
     * 
     * @param status статусное сообщение, вообще - люая строка
     */
    public void setTitleStatus(String status) {
        mainFrame.setTitle(applicationTitle + " [" + status + "]");
        log.info("Set new status: " + status);
    }

    /**
     * Встаавить панель с кнопками сверху
     */
    private void setButtonsPanel() {
        actionButtons = new AppWindowButtonsJPane();
        mainFrame.add(actionButtons, BorderLayout.WEST);
    }

    /**
     * задать, что делать при нажатии кнопки "Open" (открыть)
     * @param action действие ActionListener
     */
    public void setOpenFileButtonActionListener(ActionListener action) {
        this.actionButtons.openJButton.addActionListener(action);
    }
    
    /**
     * задать, что делать при нажатии кнопки "Save" (сохранить)
     * @param action  действие ActionListener
     */
    public void setSaveFileButtonActionListener(ActionListener action) {
        this.actionButtons.saveJButton.addActionListener(action);
    }
    /**
     * 
     */
    private void setBuildingPanel(ToDrawPolygons[] toDrawPolygons){
        buildingPanel = new BuildingPanel();
        if (toDrawPolygons != null)
            buildingPanel.init(toDrawPolygons);
        else
            buildingPanel.init();
        mainFrame.add(buildingPanel, BorderLayout.CENTER);
    }
}
