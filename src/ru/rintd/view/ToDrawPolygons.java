package ru.rintd.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.rintd.json2grid.BuildElement;
import ru.rintd.json2grid.Building;
import ru.rintd.json2grid.Building.InternLevel;
import ru.rintd.view.drawableObjects.CustomPolygon;

/**
 * по сути - один этаж здания со всем фаршем в нем находящимся
 * 
 * @author sheihar
 */
public class ToDrawPolygons extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6751047521759577331L;

	/**
	 * этаж
	 */
	private int level = 0;

	/**
	 * данные этажа из Building
	 */
	private InternLevel internLevel;

	/**
	 * полигоны, расфасованные по типу.
	 */
	private ArrayList<ArrayList<CustomPolygon>> objects;

	/**
	 * карта полигонов, введена для быстрого поиска
	 */
	private HashMap<String, CustomPolygon> objectsMap = new HashMap<String, CustomPolygon>();

	/**
	 * максимальные и минимальные значения в json файле
	 */
	private double maxXjson = 0;
	private double minXjson = 0;
	private double maxYjson = 0;
	private double minYjson = 0;

	/**
	 * подстраивание изображениия в окно для отображения сразу всего изображения
	 */
	private int adjustmentTo = 1;
	// возможные значения
	/**
	 * по высоте
	 */
	public static final int ADJUST_TO_HEIGHT = 0;
	/**
	 * по ширине
	 */
	public static final int ADJUST_TO_WIDTH = 1;

	/**
	 * масштаб
	 */
	private double zoom = 1;

	/**
	 * размеры окна
	 */
	private Dimension windowDim = new Dimension();

	/**
	 * логгирование
	 */
	private static final Logger log = LogManager.getLogger(ToDrawPolygons.class
			.getName());


	public ToDrawPolygons(){
		// TODO: не работает
		EmptyBorder eb = new EmptyBorder(new Insets(10, 100, 10, 10));
		this.setBorder(eb);
	}
	
	public void setBuilding(Building building, int level) {
		log.info("Convert Level " + level + ", elements "
				+ building.Level[level].BuildElement.length);
		this.level = level;
		this.setPreferredSize(windowDim);
		// log.info("DIM setB:"+windowDim);
		internLevel = building.Level[level];
		levelToPolygons();
	}

	/**
	 * преобразование этажа json в полигоны
	 */
	private void levelToPolygons() {

		maximin();
		
		if ((maxXjson - minXjson) > (maxYjson - minYjson))
			adjustmentTo = ADJUST_TO_WIDTH;
		else
			adjustmentTo = ADJUST_TO_HEIGHT;

		objects = new ArrayList<ArrayList<CustomPolygon>>();
		for (int i = 0; i < 7; i++) {
			objects.add(new ArrayList<CustomPolygon>());
		}
		for (int i = 0; i < internLevel.BuildElement.length; i++) {
			BuildElement buildElement = internLevel.BuildElement[i];

			for (int rings = 0; rings < buildElement.XY.length; rings++) {
				CustomPolygon polygon = new CustomPolygon(buildElement.Id,
						buildElement.Sign, getLayer(buildElement.Sign),
						buildElement.Output);

				for (int points = 0; points < buildElement.XY[rings].length; points++) {

					double x = buildElement.XY[rings][points][0] - minXjson;
					double y = buildElement.XY[rings][points][1] - minXjson;

					polygon.addPoint(x, y);
				}

				objects.get(getLayer(buildElement.Sign)).add(polygon);
				objectsMap.put(buildElement.Id, polygon);
			}
		}
		// log.debug("HashMap elems:"+objectsMap.size());

	}

	/**
	 * рисование компоненты
	 * 
	 * @param g
	 *            гафика
	 */
	public void paint(Graphics g) {
		super.paintComponent(g);

		polygonstSyzeRevalidate(minXjson, maxXjson, minYjson, maxYjson,
				windowDim.width, windowDim.height, zoom);

		if (internLevel != null) {
			log.info("Level " + level + " draw..");
			int i = 0;
			for (ArrayList<CustomPolygon> arrayList : objects) {
				log.info("Draw in type " + i + " size " + arrayList.size());
				for (CustomPolygon drawableObject : arrayList) {
					// TODO: оптимизировать! убрать этот расчет в самое начало и
					// изменять координаты помножением
					// drawableObject.double2int(minXjson, maxXjson, minYjson,
					// maxYjson, windowDim.width, windowDim.height, 1);
					if (i == 6) {
						g.setColor(getColor(((CustomPolygon) drawableObject)
								.getBuildingType()));

						g.fillPolygon(drawableObject);
					} else {
						g.setColor(getColor(((CustomPolygon) drawableObject)
								.getBuildingType()));

						g.fillPolygon(drawableObject);
					}
				}
				i++;
			}
		} else {
			log.warn("Empty level! level " + level);
			// TODO: ошибка - пустой этаж!
		}
	}

	private void polygonstSyzeRevalidate(double minXjson, double maxXjson,
			double minYjson, double maxYjson, int windowDimWidth,
			int windowDimHeight, double zoom) {

		if (internLevel != null) {
			for (ArrayList<CustomPolygon> arrayList : objects) {
				for (CustomPolygon drawableObject : arrayList) {
					drawableObject.double2int(minXjson, maxXjson, minYjson,
							maxYjson, windowDimWidth, windowDimHeight, adjustmentTo, zoom);
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

	public static boolean isDoor(String sign) {
		switch (sign) {
		case "Room":
			return false;
		case "Staircase":
			return false;
		case "Outside":
			return true;
		case "DoorWayOut":
			return true;
		case "DoorWayInt":
			return true;
		case "DoorWay":
			return true;
		case "Door":
			return false;
		}
		return false;

	}

	public static boolean isDoor(int sign) {
		switch (sign) {
		case 0:
			return false;
		case 4:
			return false;
		case 5:
			return true;
		case 2:
			return true;
		case 1:
			return true;
		case 3:
			return true;
		case 6:
			return false;
		}
		return false;
	}

	public static boolean isDoorIn(int sign) {
		switch (sign) {
		case 0:
			return false;
		case 4:
			return false;
		case 5:
			return false;
		case 2:
			return false;
		case 1:
			return true;
		case 3:
			return false;
		case 6:
			return false;
		}
		return false;
	}

	public static boolean isDoorIn(String sign) {
		switch (sign) {
		case "Room":
			return false;
		case "Staircase":
			return false;
		case "Outside":
			return false;
		case "DoorWayOut":
			return false;
		case "DoorWayInt":
			return true;
		case "DoorWay":
			return false;
		case "Door":
			return false;
		}
		return false;

	}

	private Color getColor(String sign) {
		switch (sign) {
		case "Room":
			return new Color(160, 160, 160);
		case "Staircase":
			return new Color(130, 130, 255);
		case "Outside":
			return new Color(255, 255, 255);
		case "DoorWayOut":
			return new Color(100, 190, 70);
		case "DoorWayInt":
			return new Color(190, 70, 70);
		case "DoorWay":
			return new Color(70, 100, 190);
		case "Door":
			return Color.ORANGE;
		}
		return new Color(255, 255, 255);
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
						maxXjson = Math.max(maxXjson,
								internLevel.BuildElement[j].XY[k][m][0]);
						minXjson = Math.min(minXjson,
								internLevel.BuildElement[j].XY[k][m][0]);
						maxYjson = Math.max(maxYjson,
								internLevel.BuildElement[j].XY[k][m][1]);
						minYjson = Math.min(minYjson,
								internLevel.BuildElement[j].XY[k][m][1]);
					}
				}
			}
		}
		log.info("Matched XYs: X[" + minXjson + ";" + maxXjson + "], Y["
				+ minYjson + ";" + maxYjson + "]");
		/*
		 * maxXjson += get20perc(maxXjson); maxYjson += get20perc(maxYjson);
		 * minXjson -= get20perc(maxXjson); minYjson -= get20perc(maxYjson);
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

	public ArrayList<ArrayList<CustomPolygon>> getObjects() {
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

	public double getZoom() {
		return zoom;
	}

	public void setZoom(double zoom) {
		this.zoom = zoom;
		
		CustomPolygon p = new CustomPolygon("", "Room", this.level, new String[0]);
		double[] xs = {minXjson, maxXjson};
		double[] ys = {minYjson, maxYjson};
		p.setXpointsf(xs);
		p.setYpointsf(ys);
		p.setNpointsf(2);
		
		p.double2int(minXjson, maxXjson, minYjson,
				maxYjson, windowDim.width, windowDim.height, adjustmentTo, zoom);
		
		this.setPreferredSize(new Dimension( p.xpoints[1], p.ypoints[1]));
		this.setSize(new Dimension( p.xpoints[1], p.ypoints[1]));
		
		log.debug("DIMs: "+p.xpoints[1]+";"+p.ypoints[1]);
	}
	
	

}
