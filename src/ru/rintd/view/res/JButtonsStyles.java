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

    private final static String[] BUTTONS_NAMES = { "Pointer", "Open", "Save", "Undo", "Rendo", "Delete", "ZoomIn",
            "ZoomNull", "ZoomOut" };

    private final static String[] BUTTONS_DESCRIPTIONS = { "Pointer", "Open", "Save", "Undo", "Rendo", "Delete",
            "ZoomIn", "ZoomNull", "ZoomOut" };

    private final static String[] BUTTONS_FILE = { "pointer", "document-open", "document-save", "edit-redo-rtl",
            "edit-redo", "edit-delete", "zoom-in", "zoom-original", "zoom-out" };

    // логгер
    private static final Logger log = LogManager.getLogger(JButtonsStyles.class.getName());

    /**
     * возвращает кнопку по её id
     * 
     * @param type используются встроенные константы типа *_ID
     * @return кнопка
     */
    public static JButton getButton(int type) {

        log.info("Generate button type "+type+" ["+BUTTONS_NAMES[type]+" is "+BUTTONS_DESCRIPTIONS[type]+"]");
        JButton button = new JButton();

        button.setToolTipText(BUTTONS_DESCRIPTIONS[type]);
        URL img = JButtonsStyles.class.getResource("icons/" + BUTTONS_FILE[type] + ".png");
        System.out.print(">"+BUTTONS_FILE[type] );
        button.setIcon(new ImageIcon(img));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        
        return button;
    }

}