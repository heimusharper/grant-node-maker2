package ru.rintd.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.rintd.json2grid.Node;
import ru.rintd.model.res.Model;
import ru.rintd.view.jtsView.JtPanel;

import com.vividsolutions.jts.geom.Polygon;


/**
 * панель с планом здания
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
	/**
	 * поэтажность
	 */
	private JTabbedPane tabbedPane;
	/**
	 * необходимые полигоны [этаж][полигон]
	 */
	private Polygon[][] polygons;
	/**
	 * этажи
	 */
	private JtPanel[] jtPanel;
	/**
	 * масштаб
	 */
	private double zoom = 1;
	/**
	 * шаг масштабирования
	 */
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

	public void init(Polygon[][] toDraw, ArrayList<ArrayList<Node>> nodes, Model m) {
		clear();
		log.info("INIT building panel");
		polygons = toDraw;

		jtPanel = new JtPanel[polygons.length];
		int i = 0;
		for (Polygon[] polygons : toDraw) {
			jtPanel[i] = new JtPanel(polygons, m, i);
			i++;
		}

		for (i = 0; i < polygons.length; i++) {
			final int exi = i;
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					JScrollPane jScrollPane = new JScrollPane(jtPanel[exi]);

					jScrollPane
							.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
					jScrollPane
							.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
					jScrollPane.setWheelScrollingEnabled(true);
					jtPanel[exi].jScrollPane = jScrollPane;
					/*
					 * log.info("DIM:"+this.getSize());
					 * log.info("DIM PS:"+this.getPreferredSize());
					 * log.info("DIM TAB:"+tabbedPane.getSize());
					 */
					// jScrollPane.setViewportBorder(BorderFactory.createLineBorder(Color.BLUE));
					Dimension dim = getSize();
					dim.setSize(dim.getWidth() * 2, dim.getHeight() * 2);
					jtPanel[exi].setPreferredSize(dim);
					//jtPanel[exi].setNodes(nodes.get(exi));
					tabbedPane.addTab("Level " + (exi + 1), jScrollPane);
					
				}
			});

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
					// jtPanel2.setZoom(zoom);
					jtPanel2.setPreferredSize(new Dimension((int) (jtPanel2
							.getWidth() * (1 + dZoom)), (int) (jtPanel2
							.getHeight() * (1 + dZoom))));
				}
		}
		this.repaint();
	}

	public void zoomOut() {
		if (zoom >= 1) {
			zoom -= dZoom;
			if (jtPanel != null)
				for (JtPanel jtPanel2 : jtPanel) {
					// jtPanel2.setZoom(zoom);
					jtPanel2.setPreferredSize(new Dimension((int) (jtPanel2
							.getWidth() * (1 - dZoom)), (int) (jtPanel2
							.getHeight() * (1 - dZoom))));
				}
		}
		this.repaint();
	}

	public void zoomDef() {
		zoom = 1;
		if (jtPanel != null)
			if (zoom != 1)
				do {
					if (zoom != 1) {
						for (JtPanel jtPanel2 : jtPanel) {
							jtPanel2.setPreferredSize(new Dimension(
									(int) (jtPanel2.getWidth() * (1 - dZoom)),
									(int) (jtPanel2.getHeight() * (1 - dZoom))));
						}
						zoom -= dZoom;
					}
				} while (zoom > 1);

		this.repaint();
	}

	public JtPanel[] getJtPanel() {
		return jtPanel;
	}

	
	
}
