package ru.rintd.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vividsolutions.jts.geom.Polygon;

import ru.sheihar.JtPanel;

/**
 * @author sheihar
 */

public class BuildingPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6531110536272618222L;
	// логгер
	private static final Logger log = LogManager.getLogger(BuildingPanel.class
			.getName());
	private JTabbedPane tabbedPane;

	private Polygon[][] polygons;
	private JtPanel[] jtPanel;

	private double zoom = 1;
	private double dZoom = 0.2;

	public BuildingPanel() {
		super();

		this.setLayout(new BorderLayout());
		tabbedPane = new JTabbedPane();
		this.add(tabbedPane, BorderLayout.CENTER);

	}

	/**
	 * рисовка всего, что есть в toDraw
	 * 
	 * @param toDraw
	 */

	public void init(Polygon[][] toDraw) {
		clear();
		log.info("INIT building panel");
		polygons = toDraw;

		jtPanel = new JtPanel[polygons.length];
		int i = 0;
		for (Polygon[] polygons : toDraw) {
			jtPanel[i] = new JtPanel(polygons);
			i++;
		}

		for (i = 0; i < polygons.length; i++) {
			JScrollPane jScrollPane = new JScrollPane(jtPanel[i]);

			jScrollPane
					.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			jScrollPane
					.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			// log.info("DIM:"+this.getSize());
			// log.info("DIM PS:"+this.getPreferredSize());
			// log.info("DIM TAB:"+tabbedPane.getSize());
			Dimension dim = jScrollPane.getSize();
			// jScrollPane.setPreferredSize(dim);
			jtPanel[i].setPreferredSize(dim);
			tabbedPane.addTab("Level " + (i + 1), jScrollPane);

		}
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
	public void clear() {
		log.info("Clear building panel...");
		this.remove(tabbedPane);
		tabbedPane = new JTabbedPane();
		this.add(tabbedPane, BorderLayout.CENTER);
	}

	public void zoomIn() {
		if (zoom < 18) {
			zoom += dZoom;
			if (jtPanel != null)
				for (JtPanel jtPanel2 : jtPanel) {
					jtPanel2.setZoom(zoom);
				}
		}
		this.repaint();
	}

	public void zoomOut() {
		if (zoom >= 1) {
			zoom -= dZoom;
			if (jtPanel != null)
				for (JtPanel jtPanel2 : jtPanel) {
					jtPanel2.setZoom(zoom);
				}
		}
		this.repaint();
	}

	public void zoomDef() {
		zoom = 1;
		if (jtPanel != null)
			for (JtPanel jtPanel2 : jtPanel) {
				jtPanel2.setZoom(zoom);
			}
		this.repaint();
	}
}
