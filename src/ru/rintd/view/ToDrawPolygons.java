package ru.rintd.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.rintd.json2grid.BuildElement;
import ru.rintd.json2grid.Building;
import ru.rintd.json2grid.Building.InternLevel;
import ru.rintd.view.drawableObjects.CustomPolygon;
import ru.rintd.view.drawableObjects.DrawableObject;

/**
 * по сути - один этаж здания со всем фаршем в нем находящимся
 * 
 * @author sheihar
 */
public class ToDrawPolygons extends JPanel {

    private int level = 0;

    private InternLevel internLevel;

    private ArrayList<ArrayList<DrawableObject>> objects;

    private double maxXjson = 0;
    private double minXjson = 0;
    private double maxYjson = 0;
    private double minYjson = 0;

    private Dimension windowDim = new Dimension();

    private static final Logger log = LogManager.getLogger(ToDrawPolygons.class.getName());

    public void setBuilding(Building building, int level) {
        log.info("Convert Level " + level + ", elements " + building.Level[level].BuildElement.length);
        this.level = level;
        this.setPreferredSize(windowDim);
        //log.info("DIM setB:"+windowDim);
        internLevel = building.Level[level];
        levelToPolygons();
    }

    /**
     * преобразование этажа json в полигоны
     */
    private void levelToPolygons() {

        objects = new ArrayList<ArrayList<DrawableObject>>();
        for (int i = 0; i < 7; i++){
        	objects.add(new ArrayList<DrawableObject>());
        }
        for (int i = 0; i < internLevel.BuildElement.length; i++) {
            BuildElement buildElement = internLevel.BuildElement[i];

            for (int rings = 0; rings < buildElement.XY.length; rings++) {
                CustomPolygon polygon = new CustomPolygon(buildElement.Id, buildElement.Sign,
                        getLayer(buildElement.Sign));

                for (int points = 0; points < buildElement.XY[rings].length; points++) {

                    double x = buildElement.XY[rings][points][0];
                    double y = buildElement.XY[rings][points][1];

                    polygon.addPoint(x, y);
                }
                
                objects.get(getLayer(buildElement.Sign)).add(polygon);
            }
        }
        maximin();

    }

    /**
     * рисование компоненты
     * 
     * @param g гафика
     */
    public void paint(Graphics g) {
        super.paintComponent(g);

        if (internLevel != null) {
            log.info("Level " + level + " draw..");
            for (ArrayList<DrawableObject> arrayList : objects) {
                for (DrawableObject drawableObject : arrayList) {
                    drawableObject.double2int(minXjson, maxXjson, minYjson, maxYjson, windowDim.width,
                            windowDim.height, 1);
                    g.setColor(getColor(((CustomPolygon)drawableObject).getBuildingType()));
                    g.fillPolygon(drawableObject);
                    
                }
            }
        } else {
            log.warn("Empty level! level " + level);
            // TODO: ошибка - пустой этаж!
        }
    }

    /**
     * тип объекта в слой
     * 
     * @param sign
     */
    private int getLayer(String sign) {
        switch (sign) {
        case "Room":
            return 0;
        case "Staircase":
            return 4;
        case "Outside":
            return 5;
        case "DoorWayOut":
            return 2;
        case "DoorWayInt":
            return 1;
        case "DoorWay":
            return 3;
        case "Door":
            return 6;
        }
        return 6;
    }

    private Color getColor(String sign) {
        switch (sign) {
        case "Room":
            return new Color(160,160,160);
        case "Staircase":
            return new Color(130,130,255);
        case "Outside":
            return new Color(255,255,255);
        case "DoorWayOut":
            return new Color(100,190,70);
        case "DoorWayInt":
            return new Color(190,70,70);
        case "DoorWay":
            return new Color(70,100,190);
        case "Door":
            return new Color(70,100,190);
        }
        return new Color(255,255,255);
    }
    
    /**
     * вычисляет максимальные и минимальные координаты json
     */
    public void maximin() {
        boolean is = false;
        for (int j = 0; j < internLevel.BuildElement.length; j++) {
            for (int k = 0; k < internLevel.BuildElement[j].XY.length; k++) {
                for (int m = 0; m < internLevel.BuildElement[j].XY[k].length; m++) {
                    if (!is) {
                        maxXjson = internLevel.BuildElement[j].XY[k][m][0];
                        maxYjson = internLevel.BuildElement[j].XY[k][m][1];
                        minXjson = internLevel.BuildElement[j].XY[k][m][0];
                        minYjson = internLevel.BuildElement[j].XY[k][m][1];
                        is = true;
                    } else {
                        maxXjson = Math.max(maxXjson, internLevel.BuildElement[j].XY[k][m][0]);
                        minXjson = Math.min(minXjson, internLevel.BuildElement[j].XY[k][m][0]);
                        maxYjson = Math.max(maxYjson, internLevel.BuildElement[j].XY[k][m][1]);
                        minYjson = Math.min(minYjson, internLevel.BuildElement[j].XY[k][m][1]);
                    }
                }
            }
        }
        log.info("Matched XYs: X[" + minXjson + ";" + maxXjson + "], Y[" + minYjson + ";" + maxYjson + "]");
        /*
         * maxXjson += get20perc(maxXjson); maxYjson += get20perc(maxYjson); minXjson -= get20perc(maxXjson); minYjson
         * -= get20perc(maxYjson);
         */
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public InternLevel getInternLevel() {
        return internLevel;
    }

    public void setInternLevel(InternLevel internLevel) {
        this.internLevel = internLevel;
    }

    public Dimension getWindowDim() {
        return windowDim;
    }

    public void setWindowDim(Dimension windowDim) {
        this.windowDim = windowDim;
    }

    public ArrayList<ArrayList<DrawableObject>> getObjects() {
        return objects;
    }

    public double getMaxXjson() {
        return maxXjson;
    }

    public double getMinXjson() {
        return minXjson;
    }

    public double getMaxYjson() {
        return maxYjson;
    }

    public double getMinYjson() {
        return minYjson;
    }
    
    
}
