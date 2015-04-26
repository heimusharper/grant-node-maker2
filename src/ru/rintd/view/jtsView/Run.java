package ru.rintd.view.jtsView;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.vividsolutions.jts.geom.Polygon;

import ru.rintd.json2grid.Building;
import ru.rintd.json2grid.Building.InternLevel;
import ru.rintd.json2grid.GridTransformation;
import ru.rintd.json2grid.Json2Grid;

public class Run {

	public static String FILE = "";

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter(
						"JSON file", "json");
				chooser.setFileFilter(fileNameExtensionFilter);
				int value = chooser.showOpenDialog(null);
				if (value == JFileChooser.APPROVE_OPTION) {
					FILE = chooser.getSelectedFile().getAbsolutePath();
				}

				Building building = new Building();
				try {
					building = Json2Grid.getStructure(FILE);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Polygon[][] geo = GridTransformation.getJts(building);
				JFrame mainFrame = new JFrame();
				mainFrame.setSize(new Dimension(500, 500));
				JTabbedPane tp = new JTabbedPane();
				System.out.println(mainFrame.getSize());
				for (int i = 0; i < geo.length; i++) {
					if (geo[i] != null) {
						JtPanel p = new JtPanel(geo[i]);
						tp.addTab("level " + i, null, p, "");
						p.setMouseClickAction(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								super.mouseClicked(e);
							}
						});
						// p.repaint();
					}
				}

				mainFrame.add(tp, BorderLayout.CENTER);
				mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				mainFrame.setVisible(true);

			}
		});
	}

}
