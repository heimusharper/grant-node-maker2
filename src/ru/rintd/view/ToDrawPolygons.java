package ru.rintd.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

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

    public void setBuilding(Building building, int level) {
        this.level = level;
        this.setPreferredSize(new Dimension(1000, 800));
        internLevel = building.Level[level];
        levelToPolygons();
    }

    /**
     * преобразование этажа json в полигоны
     */
    private void levelToPolygons() {

        objects = new ArrayList<ArrayList<DrawableObject>>();
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
            }
        }
        maximin();

    }

    /**
     * рисование компоненты
     * 
     * @param g гафика
     */
    public void paintCmponent(Graphics g) {
        super.paintComponent(g);

        if (internLevel != null) {
            for (ArrayList<DrawableObject> arrayList : objects) {
                for (DrawableObject drawableObject : arrayList) {
                    drawableObject.double2int(minXjson, maxXjson, minYjson, maxYjson, this.getWidth(),
                            this.getHeight(), 1);
                    g.drawPolygon(drawableObject);
                }
            }
        } else {
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
        /*
         * maxXjson += get20perc(maxXjson); maxYjson += get20perc(maxYjson); minXjson -= get20perc(maxXjson); minYjson
         * -= get20perc(maxYjson);
         */
    }
}
