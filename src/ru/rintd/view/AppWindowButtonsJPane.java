package ru.rintd.view;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.rintd.view.res.JButtonsStyles;

public class AppWindowButtonsJPane extends JPanel {

    // выбранный инструмент
    private int type = 0;
    // логгер
    private static final Logger log = LogManager.getLogger(AppWindowButtonsJPane.class.getName());

    // кнопки
    // управление
    // указатель
    JButton pointerJButton = JButtonsStyles.getButton(JButtonsStyles.POINTER_ID);
    // открыть
    JButton openJButton = JButtonsStyles.getButton(JButtonsStyles.OPEN_ID);
    // сохранить
    JButton saveJButton = JButtonsStyles.getButton(JButtonsStyles.SAVE_ID);
    
    //
    
    public AppWindowButtonsJPane() {
        
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.add(pointerJButton);
        this.add(openJButton);
        this.add(saveJButton);
        
    }

}
