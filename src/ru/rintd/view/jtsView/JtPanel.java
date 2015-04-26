package ru.rintd.view.jtsView;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;

import ru.rintd.controller.Controller;
import ru.rintd.json2grid.BuildElement;

import com.vividsolutions.jts.awt.PointShapeFactory;
import com.vividsolutions.jts.awt.ShapeWriter;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.TopologyException;

public class JtPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8270609114688328489L;

	/**
	 * Массив полигонов
	 */
	public Polygon[] polygons;
	/**
	 * для быстрого доступа
	 */
	public HashMap<String, Polygon> hashMapPolys = new HashMap<String, Polygon>();
	/**
	 * буферы дверей
	 */
	private ArrayList<Geometry> buffers = new ArrayList<Geometry>();
	/**
	 * перевод Geometry в Shape2D
	 */
	ShapeWriter shapeWriter;
	/**
	 * аффинные преобразования
	 */
	Viewport viewport;
	/**
	 * максимумы и минимумы введенной геометрии
	 */
	double maxX = 0;
	double maxY = 0;
	double minX = 0;
	double minY = 0;
	/**
	 * масштаб изображения чтобы втиснить в окно
	 */
	double scaler = 1;
	/**
	 * глобальный мастаб
	 */
	double zoom = 1;

	/**
	 * 
	 * @param p
	 *            геометрия
	 */
	public JtPanel(Polygon[] p) {

		this.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		shapeWriter = new ShapeWriter();
		this.polygons = p;
		this.setBackground(Color.WHITE);
		for (int i = 0; i < p.length; i++) {
			Shape shape = shapeWriter.toShape(p[i]);
			maxX = Math.max(maxX, shape.getBounds2D().getMaxX());
			maxY = Math.max(maxY, shape.getBounds2D().getMaxY());
			minX = Math.min(minX, shape.getBounds2D().getMinX());
			minY = Math.min(minY, shape.getBounds2D().getMinY());
		}
		for (int i = 0; i < p.length; i++) {
			hashMapPolys.put(((BuildElement) p[i].getUserData()).Id, p[i]);
		}
		genDoorBuffers();

	}

	public JtPanel() {
		super();
		this.setBackground(Color.WHITE);
	}

	private void genDoorBuffers() {
		for (int i = 0; i < polygons.length; i++) {
			BuildElement buildElement = (BuildElement) polygons[i]
					.getUserData();
			if (isDoor(buildElement.Sign)) {
				if (buildElement.Output.length == 2) {
					Polygon room0 = hashMapPolys.get(buildElement.Output[0]);
					Polygon room1 = hashMapPolys.get(buildElement.Output[1]);

					if (room0 != null && room1 != null) {
						Geometry buff = polygons[i].buffer(0.2);
						try {

							Geometry pol1 = buff.intersection(room0);
							Geometry pol2 = buff.intersection(room1);
							buffers.add(pol1);
							buffers.add(pol2);
						} catch (TopologyException e) {
							System.out.println(">>>>>>>>>>>>>>>"
									+ buildElement.Id);
						}
					}
				}
				if (buildElement.Output.length == 1) {
					Polygon room0 = hashMapPolys.get(buildElement.Output[0]);
					try {

						Geometry buff = polygons[i].buffer(10.0);

						Geometry pol1 = buff.intersection(room0);
						buffers.add(pol1);
					} catch (TopologyException e) {
						System.out.println(">>>>>>>>>>>>>>>" + buildElement.Id);
					}

				}

			}
		}
	}

	public void paint(Graphics g) {
		if (polygons != null) {
			// получаем масштаб
			setscale();
			ArrayList<Point> centroids = new ArrayList<Point>();
			ArrayList<String> ids = new ArrayList<String>();
			// определяем преобразования
			viewport = new Viewport(this.getSize(), scaler * zoom);
			// определяем конвертер по точкам
			shapeWriter = new ShapeWriter(this.viewport,
					new PointShapeFactory.Point());
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setStroke(new BasicStroke(2));
			for (Polygon polygon : polygons) {
				BuildElement buildElement = (BuildElement) polygon
						.getUserData();

				Shape shape = shapeWriter.toShape(polygon);
				
				g2d.setPaint(getColor(buildElement.Sign));
				if (buildElement.Id
						.substring(buildElement.Id.length() - 5,
								buildElement.Id.length() - 1).equals("52fa")
						| buildElement.Id
								.substring(buildElement.Id.length() - 5,
										buildElement.Id.length() - 1)
								.equals("0076")
						| buildElement.Id
								.substring(buildElement.Id.length() - 5,
										buildElement.Id.length() - 1)
								.equals("c1c1")
						| buildElement.Id
								.substring(buildElement.Id.length() - 5,
										buildElement.Id.length() - 1)
								.equals("ec8d")
						| buildElement.Id
								.substring(buildElement.Id.length() - 5,
										buildElement.Id.length() - 1)
								.equals("920e")) {
					g2d.setColor(Color.GREEN);
				}
				g2d.fill(shape);

				for (int i = 0; i < polygon.getNumInteriorRing(); i++) {
					g2d.setPaint(Color.WHITE);
					Shape shapeEx = shapeWriter.toShape(polygon
							.getInteriorRingN(i));
					g2d.fill(shapeEx);
				}

				if (Controller.appPreferences.showLabelsPlan) {

					try {
						Point p = polygon.getCentroid();
						p = polygon.getInteriorPoint();
						centroids.add(p);
						ids.add(buildElement.Id);
						// System.out.println("fill " + shape.getBounds2D());
					} catch (TopologyException e) {
						System.out.println(">>>>>>>>>>>>>>>" + buildElement.Id);
					}
				}

			}
			g2d.setColor(Color.PINK);
			for (int i = 0; i < buffers.size(); i++) {
				Shape shape = shapeWriter.toShape(buffers.get(i));
				g2d.fill(shape);
			}

			if (Controller.appPreferences.showLabelsPlan) {
				g2d.setFont(new Font(Controller.appPreferences.fontPlan,
						getFontType(Controller.appPreferences.fontType),
						Controller.appPreferences.fontSize));
				g2d.setColor(Color.BLACK);
				for (int i = 0; i < centroids.size(); i++) {
					Point point = centroids.get(i);
					Shape s = shapeWriter.toShape(point);
					if (ids.get(i)
							.substring(ids.get(i).length() - 5,
									ids.get(i).length() - 1).equals("52fa")
							| ids.get(i)
									.substring(ids.get(i).length() - 5,
											ids.get(i).length() - 1)
									.equals("0076")
							| ids.get(i)
									.substring(ids.get(i).length() - 5,
											ids.get(i).length() - 1)
									.equals("c1c1")
							| ids.get(i)
									.substring(ids.get(i).length() - 5,
											ids.get(i).length() - 1)
									.equals("ec8d")
							| ids.get(i)
									.substring(ids.get(i).length() - 5,
											ids.get(i).length() - 1)
									.equals("920e")) {
						g2d.setColor(Color.GREEN);
					}
					g2d.drawString(
							ids.get(i).substring(ids.get(i).length() - 5,
									ids.get(i).length() - 1), s.getBounds().x,
							s.getBounds().y);
					g2d.setColor(Color.BLACK);
				}

			}

			// сетка
			if (Controller.appPreferences.planGrid) {
				float[] dashl = { 5, 5 };
				BasicStroke pen = new BasicStroke(1, BasicStroke.CAP_ROUND,
						BasicStroke.JOIN_BEVEL, 10, dashl, 0);

				g2d.setStroke(pen);
				g2d.setColor(new Color(0f, 0f, 0f, 0.5f));
				for (int i = 0; i < this.getHeight() / (scaler * zoom); i++) {
					g2d.drawLine(0, (int) (i * (scaler * zoom)),
							this.getWidth(), (int) (i * (scaler * zoom)));
				}
				for (int i = 0; i < this.getWidth() / (scaler * zoom); i++) {
					g2d.drawLine((int) (i * (scaler * zoom)), 0,
							(int) (i * (scaler * zoom)), this.getHeight());
				}
			}
		}
	}

	private int getFontType(String s) {
		switch (s) {
		case "PLAIN":
			return Font.PLAIN;
		case "BOLD":
			return Font.BOLD;
		case "ITALIC":
			return Font.ITALIC;

		default:
			return Font.PLAIN;
		}
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
		return new Color(255, 0, 255);
	}

	private boolean isDoor(String sign) {
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
			return true;
		case "Door":
			return true;
		}
		return false;
	}

	private void setscale() {
		int h = this.getSize().height;
		int w = this.getSize().width;
		// широкий
		if (maxX - minX > maxY - minY) {
			// по ширине
			if ((maxX - minX) / (maxY - minY) <= w / h) {
				// в широком
				scaler = h / (maxY - minY);
			} else {
				// в высоком
				scaler = w / (maxX - minX);
			}
		} else {
			// высокий
			if ((maxX - minX) / (maxY - minY) <= w / h) {
				// в широком
				scaler = h / (maxY - minY);
			} else {
				// в высоком
				scaler = w / (maxX - minX);
			}
		}
	}

	public void setMouseClickAction(MouseAdapter event) {

		this.addMouseListener(event);

	}

	public BuildElement getXYelement(int x, int y) {

		setscale();
		// System.out.println(scaler);
		viewport = new Viewport(this.getSize(), scaler * zoom);
		shapeWriter = new ShapeWriter(this.viewport,
				new PointShapeFactory.Point());
		for (Polygon polygon : polygons) {
			Shape shape = shapeWriter.toShape(polygon);
			Point2D point2d = new Point2D.Double(x, y);
			if (shape.contains(point2d)) {

				return (BuildElement) polygon.getUserData();
			}

		}

		return null;

	}

	public double getZoom() {
		return zoom;
	}

	public void setZoom(double zoom) {
		this.zoom = zoom;
	}

}