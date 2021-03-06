package ru.rintd.view;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.vividsolutions.jts.geom.Coordinate;

import ru.rintd.controller.network.NodeInfo;
import ru.rintd.json2grid.BuildElement;
import ru.rintd.json2grid.Building;
import ru.rintd.json2grid.Node;

/**
 * информационная панель
 * 
 * @author sheihar
 *
 */
public class MultiPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4978069637198717431L;
	/**
	 * текст
	 */
	private JTextArea area;
	/**
	 * координаты
	 */
	private JTextArea coords;
	private ElementsList elementsList;

	public MultiPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		area = new JTextArea();
		area.setEditable(false);
		// area.setMaximumSize(new Dimension(10, 100));
		add(area);
		coords = new JTextArea("Click");
		coords.setEditable(false);
		add(coords);

	}

	public void setNode(Node node, NodeInfo info) {
		String buildText = "";
		buildText = buildText + "UUID: " + node.uid + "\n";
		buildText = buildText + "H: " + node.h + "\n";
		buildText = buildText + "Type: " + getNodeTypeString(node.type) + "\n";
		buildText = buildText + "buildId: " + node.buildElID + "\n";
		buildText = buildText + "XY: [" + node.xy[0] + ";" + node.xy[1] + "]\n";
		if (info != null) {
			buildText = buildText + "Node status\n";
			buildText = buildText + "Is fire?: " + info.isFire + "\n";
			buildText = buildText + "Density: " + (info.density * 100) + "% \n";
			buildText = buildText + "Temperature: " + info.Temp + " C\n";
			buildText = buildText + "Status: "
					+ getNodeStatusString(info.status) + "\n";
		}
		area.setText(buildText);
	}

	private String getNodeStatusString(int status) {
		switch (status) {
		case 0:
			return "NAN";
		case 1:
			return "Work";
		case 2:
			return "Sensor not work";
		case 3:
			return "Not signal";

		default:
			return "N/A";
		}
	}

	private String getNodeTypeString(int type) {
		switch (type) {
		case 0:
			return "Sensor";
		case 1:
			return "Light";
		case 2:
			return "Pointer";
		case 3:
			return "Server";

		default:
			return "N/A";
		}
	}

	public void setBuildElement(BuildElement buildElement) {
		String buildText = "";
		buildText = "id: " + buildElement.Id + "\n";
		buildText = buildText + "Name: " + buildElement.Name + "\n";
		buildText = buildText + "Note: " + buildElement.Note + "\n";
		buildText = buildText + "Numpeople: " + buildElement.NumPeople + "\n";
		buildText = buildText + "Sign: " + buildElement.Sign + "\n";
		buildText = buildText + "Type: " + buildElement.Type + "\n";
		buildText = buildText + "Width: " + buildElement.Width + "\n";

		area.setText(buildText);

	}

	public void setBufferElement(BuildElement buildElement) {
		String buildText = "";
		buildText = "id: " + buildElement.Id + "\n";
		buildText = buildText + "Name: " + buildElement.Name + "\n";
		buildText = buildText + "Note: " + buildElement.Note + "\n";
		buildText = buildText + "Numpeople: " + buildElement.NumPeople + "\n";
		buildText = buildText + "Sign: " + buildElement.Sign + "\n";
		buildText = buildText + "Type: " + buildElement.Type + "\n";
		buildText = buildText + "Width: " + buildElement.Width + "\n";

		area.setText(buildText);

	}

	public void setBuilding(Building building) {
		String buildText = "";
		buildText = "Name: " + building.NameBuilding + "\n";
		buildText = buildText + "AddInfo: " + building.Address.AddInfo + "\n";
		buildText = buildText + "City: " + building.Address.City + "\n";
		buildText = buildText + "Street: " + building.Address.StreetAddress
				+ "\n";
		buildText = buildText + "Levels: " + building.Level.length + "\n";

		area.setText(buildText);
	}

	public static String textSeparator(String text, int chars) {
		if (text != null) {
			char[] c = text.toCharArray();
			String newText = "";
			for (int i = 0; i < c.length; i++) {
				newText += c[i];
				if (i % chars == 0)
					newText += "\n";
			}
			return newText;
		}
		return null;
	}

	public void setClick(Coordinate coords) {
		String text = "XY: [" + String.valueOf(coords.x).substring(0, 10)
				+ "; " + String.valueOf(coords.y).substring(0, 10) + "]";
		this.coords.setText(text);
	}

	public void setClick(Coordinate coords, Point2D point) {
		String text = "XY: [" + coords.x + "; " + coords.y + "] \n old ["
				+ point.getX() + "; " + point.getY() + "]";
		this.coords.setText(text);
	}

	public void setNodeTree(ArrayList<ArrayList<Node>> nodes) {
		if (elementsList != null)
			elementsList.setNodes(nodes);
		else {
			elementsList = new ElementsList(nodes);
			add(elementsList);
		}
	}
}
