package ru.rintd.view;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * @author sheihar
 *
 */

public class BuildingPanel extends JPanel{


    // логгер
    private static final Logger log = LogManager.getLogger(BuildingPanel.class.getName());
    private JTabbedPane tabbedPane;
    private ToDrawPolygons[] toDrawPolygons;
    
    public BuildingPanel(){
        super();
        tabbedPane = new JTabbedPane();
        
        this.setLayout(new BorderLayout());
        this.add(tabbedPane, BorderLayout.CENTER);
        
    }
    
    /**
     * рисовка всего, что есть в toDraw
     * @param toDraw
     */
    public void init(ToDrawPolygons[] toDraw){
        clear();
        toDrawPolygons = toDraw;
    }
    
    /**
     * рисовка по-умолчанию, тобишь ничего
     */
    public void init() {
        clear();
    }
    
    /**
     * очистка экрана
     */
    public void clear(){
        tabbedPane = new JTabbedPane();
    }
}
