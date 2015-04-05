package ru.rintd.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Double;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

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

	/**
	 * 
	 */
	private static final long serialVersionUID = 6751047521759577331L;

	private int level = 0;

	private InternLevel internLevel;

	private ArrayList<ArrayList<DrawableObject>> objects;

	private HashMap<String, DrawableObject> objectsMap = new HashMap<String, DrawableObject>();

	private double maxXjson = 0;
	private double minXjson = 0;
	private double maxYjson = 0;
	private double minYjson = 0;

	private double zoom = 1;

	private Dimension windowDim = new Dimension();

	private static final Logger log = LogManager.getLogger(ToDrawPolygons.class
			.getName());

	class Line2DCustom {
		int x1 = 0;
		int y1 = 0;
		int x2 = 0;
		int y2 = 0;

		public Line2DCustom(int x1, int y1, int x2, int y2) {
			super();
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
		}

		public boolean itersection(Polygon p) {
			if (p.contains(x1, y1) || p.contains(x2, y2))
				return true;
			return false;
		}

		/*
		 * public boolean inside(Polygon p) { if (p.contains(x1, y1) &
		 * p.contains(x2, y2)) return true; return false; }
		 */

		public boolean inside(Polygon p1, Polygon p2) {
			if (p1.contains(x1, y1) & p2.contains(x2, y2))
				return true;
			if (p2.contains(x1, y1) & p1.contains(x2, y2))
				return true;
			return false;
		}

		public CustomPolygon getDebugRectangleCenter(String buildingId,
				int layer, String[] ouput, int size) {
			CustomPolygon returned = new CustomPolygon(buildingId, "Door",
					layer, ouput);
			returned.addPoint((int) ((x1 + x2) / 2), (int) ((y1 + y2) / 2));
			returned.addPoint((int) ((x1 + x2) / 2) + size,
					(int) ((y1 + y2) / 2));
			returned.addPoint((int) ((x1 + x2) / 2), (int) ((y1 + y2) / 2)
					+ size);
			returned.addPoint((int) ((x1 + x2) / 2), (int) ((y1 + y2) / 2));
			return returned;
		}

		public CustomPolygon getDebugRectangleCenter(String buildingId,
				int layer, String[] ouput, double size, double px1, double py1,
				double px2, double py2) {
			CustomPolygon returned = new CustomPolygon(buildingId, "Door",
					layer, ouput);
			returned.addPoint(((px1 + px2) / 2), ((py1 + py2) / 2));
			returned.addPoint(((px1 + px2) / 2) + size, ((py1 + py2) / 2));
			returned.addPoint(((px1 + px2) / 2), ((py1 + py2) / 2) + size);
			returned.addPoint(((px1 + px2) / 2), ((py1 + py2) / 2));
			return returned;
		}

		public Line2D.Double getLine2D() {
			return new Line2D.Double(x1, y1, x2, y2);
		}

		public boolean isIntersection(Polygon poly) {
			Line2D.Double line = getLine2D();
			final PathIterator polyIt = poly.getPathIterator(null); // Getting
																	// an
																	// iterator
																	// along the
																	// polygon
																	// path
			final double[] coords = new double[6]; // Double array with length 6
													// needed by iterator
			final double[] firstCoords = new double[2]; // First point (needed
														// for closing polygon
														// path)
			final double[] lastCoords = new double[2]; // Previously visited
														// point
			final Set<Point2D> intersections = new HashSet<Point2D>(); // List
																		// to
																		// hold
																		// found
																		// intersections
			polyIt.currentSegment(firstCoords); // Getting the first coordinate
												// pair
			lastCoords[0] = firstCoords[0]; // Priming the previous coordinate
											// pair
			lastCoords[1] = firstCoords[1];
			polyIt.next();
			while (!polyIt.isDone()) {
				final int type = polyIt.currentSegment(coords);
				switch (type) {
				case PathIterator.SEG_LINETO: {
					final Line2D.Double currentLine = new Line2D.Double(
							lastCoords[0], lastCoords[1], coords[0], coords[1]);
					if (currentLine.intersectsLine(line))
						intersections.add(getIntersection(currentLine, line));
					lastCoords[0] = coords[0];
					lastCoords[1] = coords[1];
					break;
				}
				case PathIterator.SEG_CLOSE: {
					final Line2D.Double currentLine = new Line2D.Double(
							coords[0], coords[1], firstCoords[0],
							firstCoords[1]);
					if (currentLine.intersectsLine(line))
						intersections.add(getIntersection(currentLine, line));
					break;
				}
				default: {
					try {
						throw new Exception(
								"Unsupported PathIterator segment type.");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				}
				polyIt.next();
			}
			if (intersections.size() != 0)
				return true;
			return false;
		}

		public Point2D getIntersection(Line2D.Double line1, Line2D.Double line2) {

			final double x1, y1, x2, y2, x3, y3, x4, y4;
			x1 = line1.x1;
			y1 = line1.y1;
			x2 = line1.x2;
			y2 = line1.y2;
			x3 = line2.x1;
			y3 = line2.y1;
			x4 = line2.x2;
			y4 = line2.y2;
			final double x = ((x2 - x1) * (x3 * y4 - x4 * y3) - (x4 - x3)
					* (x1 * y2 - x2 * y1))
					/ ((x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4));
			final double y = ((y3 - y4) * (x1 * y2 - x2 * y1) - (y1 - y2)
					* (x3 * y4 - x4 * y3))
					/ ((x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4));

			return new Point2D.Double(x, y);

		}

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
		

		objects = new ArrayList<ArrayList<DrawableObject>>();
		for (int i = 0; i < 7; i++) {
			objects.add(new ArrayList<DrawableObject>());
		}
		for (int i = 0; i < internLevel.BuildElement.length; i++) {
			BuildElement buildElement = internLevel.BuildElement[i];

			for (int rings = 0; rings < buildElement.XY.length; rings++) {
				CustomPolygon polygon = new CustomPolygon(buildElement.Id,
						buildElement.Sign, getLayer(buildElement.Sign),
						buildElement.Output);

				for (int points = 0; points < buildElement.XY[rings].length; points++) {

					double x = buildElement.XY[rings][points][0]-minXjson;
					double y = buildElement.XY[rings][points][1]-minXjson;

					polygon.addPoint(x, y);
				}

				objects.get(getLayer(buildElement.Sign)).add(polygon);
				objectsMap.put(buildElement.Id, polygon);
			}
		}
		// log.debug("HashMap elems:"+objectsMap.size());

		maxXjson -= minXjson;
		maxYjson -= minYjson;
		minXjson -= minXjson;
		minYjson -= minYjson;
		

		maxXjson += 2;
		maxYjson += 2;
		minXjson -= 2;
		minYjson -= 2;
		
		// создание полигонов для расстановки светофоров у дверей.
		int i = 0;

		ArrayList<DrawableObject> doorsPoly = new ArrayList<DrawableObject>();

		// даные для расчетов
		polygonstSyzeRevalidate(minXjson, maxXjson, minYjson, maxYjson,
				windowDim.width, windowDim.height, zoom);

		// TODO: Очень опасная часть! возможны сножественные NullPointerExeption
		for (ArrayList<DrawableObject> drawableObjects : objects) {
			// проходим двери
			if (isDoor(i) & isDoorIn(i)) {
				for (DrawableObject door : drawableObjects) {
					// это дверь.
					DrawableObject room1 = null;
					DrawableObject room2 = null;
					String[] outpt = ((CustomPolygon) door).getOutput();
					int j = 0;
					/*
					 * for (ArrayList<DrawableObject> drawableTypes : objects) {
					 * if (!isDoor(j)) for (DrawableObject room : drawableTypes)
					 * { if
					 * (((CustomPolygon)room).getBuildingId().equals(outpt[0])){
					 * room1 = room; } if
					 * (((CustomPolygon)room).getBuildingId().equals(outpt[1])){
					 * room2 = room; } } j++; }
					 */
					room1 = objectsMap.get(outpt[0]);
					room2 = objectsMap.get(outpt[1]);
					log.debug("Output: " + outpt[0] + "; " + outpt[1]);
					if (room1 != null & room2 != null) {
						CustomPolygon pol1 = null;
						CustomPolygon pol2 = null;

						// пары точек
						// line1to2 - линия от 1 до 2 точки
						Line2DCustom line1to2 = new Line2DCustom(
								door.xpoints[0], door.ypoints[0],
								door.xpoints[1], door.xpoints[1]);
						Line2DCustom line2to3 = new Line2DCustom(
								door.xpoints[1], door.ypoints[1],
								door.xpoints[2], door.xpoints[2]);
						Line2DCustom line3to4 = new Line2DCustom(
								door.xpoints[2], door.ypoints[2],
								door.xpoints[3], door.xpoints[3]);
						Line2DCustom line4to1 = new Line2DCustom(
								door.xpoints[3], door.ypoints[3],
								door.xpoints[4], door.xpoints[4]);

						// перебираем случаи.
						// когда 2 точки принадлежат одому, одна другому, а
						// третья
						// висит
						// идеальный случай - частный, по отношению к этому
						// line1to2 или line3to4 находится в помещении, значит,
						// на
						// нем и нужно рисовать область(ставить в середину
						// светофор!)
						/*
						 * if (line1to2.inside(room1) | line3to4.inside(room1) |
						 * line1to2.inside(room2) | line3to4.inside(room2)) {
						 * pol1 = line1to2.getDebugRectangleCenter(
						 * ((CustomPolygon) door).getBuildingId(), level, outpt,
						 * 0.7, ((CustomPolygon) door).xpointsf[0],
						 * ((CustomPolygon) door).ypointsf[0], ((CustomPolygon)
						 * door).xpointsf[1], ((CustomPolygon)
						 * door).ypointsf[1]); pol2 =
						 * line3to4.getDebugRectangleCenter( ((CustomPolygon)
						 * door).getBuildingId(), level, outpt, 0.7,
						 * ((CustomPolygon) door).xpointsf[2], ((CustomPolygon)
						 * door).ypointsf[2], ((CustomPolygon)
						 * door).xpointsf[3], ((CustomPolygon)
						 * door).ypointsf[3]); } if (line2to3.inside(room1) |
						 * line4to1.inside(room1) | line2to3.inside(room2) |
						 * line4to1.inside(room2)) { pol1 =
						 * line2to3.getDebugRectangleCenter( ((CustomPolygon)
						 * door).getBuildingId(), level, outpt, 0.7,
						 * ((CustomPolygon) door).xpointsf[1], ((CustomPolygon)
						 * door).ypointsf[1], ((CustomPolygon)
						 * door).xpointsf[2], ((CustomPolygon)
						 * door).ypointsf[2]); pol2 =
						 * line4to1.getDebugRectangleCenter( ((CustomPolygon)
						 * door).getBuildingId(), level, outpt, 0.7,
						 * ((CustomPolygon) door).xpointsf[3], ((CustomPolygon)
						 * door).ypointsf[3], ((CustomPolygon)
						 * door).xpointsf[0], ((CustomPolygon)
						 * door).ypointsf[0]);
						 * 
						 * }
						 */

						// if (line1to2.inside(room1, room2)
						// | line3to4.inside(room1, room2)) {
						// pol1 = line1to2.getDebugRectangleCenter(
						// ((CustomPolygon) door).getBuildingId(),
						// level, outpt, 0.7,
						// ((CustomPolygon) door).xpointsf[0],
						// ((CustomPolygon) door).ypointsf[0],
						// ((CustomPolygon) door).xpointsf[1],
						// ((CustomPolygon) door).ypointsf[1]);
						// pol2 = line3to4.getDebugRectangleCenter(
						// ((CustomPolygon) door).getBuildingId(),
						// level, outpt, 0.7,
						// ((CustomPolygon) door).xpointsf[2],
						// ((CustomPolygon) door).ypointsf[2],
						// ((CustomPolygon) door).xpointsf[3],
						// ((CustomPolygon) door).ypointsf[3]);
						// }
						// if (line2to3.inside(room1, room2)
						// | line4to1.inside(room1, room2)) {
						// pol1 = line2to3.getDebugRectangleCenter(
						// ((CustomPolygon) door).getBuildingId(),
						// level, outpt, 0.7,
						// ((CustomPolygon) door).xpointsf[1],
						// ((CustomPolygon) door).ypointsf[1],
						// ((CustomPolygon) door).xpointsf[2],
						// ((CustomPolygon) door).ypointsf[2]);
						// pol2 = line4to1.getDebugRectangleCenter(
						// ((CustomPolygon) door).getBuildingId(),
						// level, outpt, 0.7,
						// ((CustomPolygon) door).xpointsf[3],
						// ((CustomPolygon) door).ypointsf[3],
						// ((CustomPolygon) door).xpointsf[0],
						// ((CustomPolygon) door).ypointsf[0]);
						//
						// }

						if ((line1to2.isIntersection(room1) & line1to2
								.isIntersection(room2))
								| (line3to4.isIntersection(room1) & line3to4
										.isIntersection(room2))) {
							pol1 = line2to3.getDebugRectangleCenter(
									((CustomPolygon) door).getBuildingId(),
									level, outpt, 0.7,
									((CustomPolygon) door).xpointsf[1],
									((CustomPolygon) door).ypointsf[1],
									((CustomPolygon) door).xpointsf[2],
									((CustomPolygon) door).ypointsf[2]);
							pol2 = line4to1.getDebugRectangleCenter(
									((CustomPolygon) door).getBuildingId(),
									level, outpt, 0.7,
									((CustomPolygon) door).xpointsf[3],
									((CustomPolygon) door).ypointsf[3],
									((CustomPolygon) door).xpointsf[0],
									((CustomPolygon) door).ypointsf[0]);
						}
						if ((line2to3.isIntersection(room1) & line2to3
								.isIntersection(room2))
								| (line4to1.isIntersection(room1) & line4to1
										.isIntersection(room2))) {
							
							pol1 = line1to2.getDebugRectangleCenter(
									((CustomPolygon) door).getBuildingId(),
									level, outpt, 0.7,
									((CustomPolygon) door).xpointsf[0],
									((CustomPolygon) door).ypointsf[0],
									((CustomPolygon) door).xpointsf[1],
									((CustomPolygon) door).ypointsf[1]);
							pol2 = line3to4.getDebugRectangleCenter(
									((CustomPolygon) door).getBuildingId(),
									level, outpt, 0.7,
									((CustomPolygon) door).xpointsf[2],
									((CustomPolygon) door).ypointsf[2],
									((CustomPolygon) door).xpointsf[3],
									((CustomPolygon) door).ypointsf[3]);

						}

						if (pol1 != null & pol2 != null) {
							objects.get(6).add(pol1);
							objects.get(6).add(pol2);
						} else {

							log.debug("NULL!");
						}
					} else {
						log.debug("False!");
					}

				}
			} else if (isDoor(i)) {

			}

			i++;
		}

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
			for (ArrayList<DrawableObject> arrayList : objects) {
				log.info("Draw in type " + i + " size " + arrayList.size());
				for (DrawableObject drawableObject : arrayList) {
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
			int i = 0;
			for (ArrayList<DrawableObject> arrayList : objects) {
				for (DrawableObject drawableObject : arrayList) {
					// TODO: оптимизировать! убрать этот расчет в самое начало и
					// изменять координаты помножением
					// if (i != 6) {
					drawableObject.double2int(minXjson, maxXjson, minYjson,
							maxYjson, windowDimWidth, windowDimHeight, zoom);
					// }
				}
				i++;
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
