package ru.rintd.view.drawableObjects;

import java.awt.Polygon;

public class DrawableObject extends Polygon{

    private int level = 0;
    /**
     * тип объекта
     */
    private String buildingType;

    public void double2int(double minXjson, double maxXjson, double minYjson, double maxYjson, int windWidth,
            int windHeight, int zoom) {
        
    }

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
    
}
