package ru.rintd.view;

import java.awt.Dimension;
import java.awt.geom.Point2D;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.vividsolutions.jts.geom.Coordinate;

import ru.rintd.json2grid.BuildElement;
import ru.rintd.json2grid.Building;

public class MultiPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4978069637198717431L;
	private JTextArea area;
	private JTextArea coords;
	
	public MultiPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		area = new JTextArea();
		area.setEditable(false);
		//area.setMaximumSize(new Dimension(10, 100));
		add(area);
		coords = new JTextArea("Click");
		coords.setEditable(false);
		add(coords);
		
	}
	
	public void setBuildElement(BuildElement buildElement){
		String buildText = "";
		buildText = "id: "+buildElement.Id+"\n";
		buildText = buildText + "Name: "+buildElement.Name+"\n";
		buildText = buildText + "Note: "+buildElement.Note+"\n";
		buildText = buildText + "Numpeople: "+buildElement.NumPeople+"\n";
		buildText = buildText + "Sign: "+buildElement.Sign+"\n";
		buildText = buildText + "Type: "+buildElement.Type+"\n";
		buildText = buildText + "Width: "+buildElement.Width+"\n";
		
		area.setText(buildText);
		
	}
	
	public void setBufferElement(BuildElement buildElement){
		String buildText = "";
		buildText = "id: "+buildElement.Id+"\n";
		buildText = buildText + "Name: "+buildElement.Name+"\n";
		buildText = buildText + "Note: "+buildElement.Note+"\n";
		buildText = buildText + "Numpeople: "+buildElement.NumPeople+"\n";
		buildText = buildText + "Sign: "+buildElement.Sign+"\n";
		buildText = buildText + "Type: "+buildElement.Type+"\n";
		buildText = buildText + "Width: "+buildElement.Width+"\n";
		
		area.setText(buildText);
		
	}

	public void setBuilding(Building building){
		String buildText = "";
		buildText = "Name: "+building.NameBuilding+"\n";
		buildText = buildText + "AddInfo: "+building.Address.AddInfo+"\n";
		buildText = buildText + "City: "+building.Address.City+"\n";
		buildText = buildText + "Street: "+building.Address.StreetAddress+"\n";
		buildText = buildText + "Levels: "+building.Level.length+"\n";
		
		area.setText(buildText);
	}
	
	public static String textSeparator(String text, int chars){
		if (text != null){char[] c = text.toCharArray();
		String newText = "";
		for (int i = 0; i < c.length; i++) {
			newText += c[i];
			if (i%chars == 0)
				newText += "\n";
		}
		return newText;}
		return null;
	}
	
	public void setClick(Coordinate coords){
		String text = "XY: ["+String.valueOf(coords.x).substring(0, 10)+"; "+String.valueOf(coords.y).substring(0, 10)+"]";
		this.coords.setText(text);
	}
	public void setClick(Coordinate coords, Point2D point){
		String text = "XY: ["+coords.x+"; "+coords.y+"] \n old ["+point.getX()+"; "+point.getY()+"]";
		this.coords.setText(text);
	}
}
