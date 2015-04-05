package ru.rintd.view;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import ru.rintd.view.res.JButtonsStyles;

public class AppWindowButtonsJPane extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4679491453627123935L;
	// выбранный инструмент
    //private int type = 0;
    // логгер
    //private static final Logger log = LogManager.getLogger(AppWindowButtonsJPane.class.getName());

    // кнопки
    // управление
    // указатель
    JButton pointerJButton = JButtonsStyles.getButton(JButtonsStyles.POINTER_ID);
    // открыть
    JButton openJButton = JButtonsStyles.getButton(JButtonsStyles.OPEN_ID);
    // сохранить
    JButton saveJButton = JButtonsStyles.getButton(JButtonsStyles.SAVE_ID);
    //отменить
    JButton undoJButton = JButtonsStyles.getButton(JButtonsStyles.UNDO_ID);
    // возвратить
    JButton rendoJButton = JButtonsStyles.getButton(JButtonsStyles.RENDO_ID);
    // удалить
    JButton deleteJButton = JButtonsStyles.getButton(JButtonsStyles.DELETE_ID);
    // zoom +
    JButton zoomInJButton = JButtonsStyles.getButton(JButtonsStyles.ZOOM_IN_ID);
    // zoom 0
    JButton zoom0JButton = JButtonsStyles.getButton(JButtonsStyles.ZOOM_ORIGINAL_ID);
    // zoom -
    JButton zoomOutJButton = JButtonsStyles.getButton(JButtonsStyles.ZOOM_OUT_ID);
    // TODO: остальные кнопки
    
    //
    
    public AppWindowButtonsJPane() {
        
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.add(pointerJButton);
        this.add(openJButton);
        this.add(saveJButton);
        this.add(undoJButton);
        this.add(rendoJButton);
        this.add(deleteJButton);
        this.add(zoomInJButton);
        this.add(zoom0JButton);
        this.add(zoomOutJButton);
        
    }

}
