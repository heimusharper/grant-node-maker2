package ru.rintd.view.drawableObjects;

import java.awt.Polygon;

public class DrawableObject extends Polygon{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1295272471733840500L;
	private int level = 0;
    /**
     * тип объекта
     */
    //private String buildingType;

    public void double2int(double minXjson, double maxXjson, double minYjson, double maxYjson, int windWidth,
            int windHeight, double zoom) {
        
    }

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + level;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DrawableObject other = (DrawableObject) obj;
		if (level != other.level)
			return false;
		return true;
	}
    
}
