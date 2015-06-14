package ru.rintd.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Загрузчик приложения, инициализирует контроллер и запускает главное окно
 * 
 * @author sheihar
 */

public class Run {

    public static final String APPLICATION_NAME = "NodeMaker2";
    private static final Logger log = LogManager.getLogger(Run.class.getName());

    private static Controller controller = null;

    public static void main(String[] args) {

        log.info("Application start...");
        controller = new Controller();

        
        controller.doShowMainWindow();
        /*
         * javax.swing.SwingUtilities.invokeLater(new Runnable() {
         * @Override public void run() { controller.doShowMainWindow(); } });
         */
    }
}
