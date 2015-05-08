package ru.rintd.view;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import ru.rintd.controller.ExtendBuildingElement;
import ru.rintd.json2grid.BuildElement;
import ru.rintd.json2grid.Building;

public class MultiPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4978069637198717431L;
	private JTextArea area;
	
	public MultiPanel() {
		area = new JTextArea();
		area.setEditable(false);
		area.setMaximumSize(new Dimension(10, 100));
		add(area);
		
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
	
	public void setBufferElement(ExtendBuildingElement buildElement){
		String buildText = "";
		buildText = "id: "+buildElement.Id+"\n";
		buildText = buildText + "Name: "+buildElement.Name+"\n";
		buildText = buildText + "Note: "+buildElement.Note+"\n";
		buildText = buildText + "Numpeople: "+buildElement.NumPeople+"\n";
		buildText = buildText + "Sign: "+buildElement.Sign+"\n";
		buildText = buildText + "Type: "+buildElement.Type+"\n";
		buildText = buildText + "Width: "+buildElement.Width+"\n";
		buildText = buildText + "To room id"+buildElement.Output[buildElement.selected];
		
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
}
