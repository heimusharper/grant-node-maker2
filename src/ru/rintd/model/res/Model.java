package ru.rintd.model.res;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vividsolutions.jts.geom.Polygon;

import ru.rintd.json2grid.Building;
import ru.rintd.json2grid.GridTransformation;
import ru.rintd.json2grid.Json2Grid;
import ru.rintd.json2grid.Node;

public class Model {

	private Building building = new Building();
	private ArrayList<ArrayList<Node>> nodes = new ArrayList<ArrayList<Node>>();
	// private ToDrawPolygons[] toDrawPolygons = null;
	// логгер
	private static final Logger log = LogManager.getLogger(Model.class
			.getName());

	public Model() {

	}

	/**
	 * Открывает файл JSON и преобразует его в Building
	 * 
	 * @param jsonFilePath
	 *            абсолютный или относительный путь
	 * @return
	 */
	public Building setJsonString(String jsonFilePath) {

		log.info("Get building from: " + jsonFilePath);
		try {
			log.info("Transform json to building");
			building = Json2Grid.getStructure(jsonFilePath);

			for (int i = 0; i < building.Level.length; i++) {
				nodes.add(new ArrayList<Node>());
				if (building.Level[i].nodes != null)
					for (int j = 0; j < building.Level[i].nodes.length; j++) {
						nodes.get(i).add(building.Level[i].nodes[j]);
					}
			}
		} catch (IOException e) {
			log.error("Fail get file " + jsonFilePath + " StackTrace: "
					+ e.getMessage());
			// TODO Реакция, когда файл Json невозможно открыть
			e.printStackTrace();
		}

		return building;
	}

	public Polygon[][] getToDrawPolygons() {

		return GridTransformation.getJts(building);
	}

	public Building getBuilding() {
		return building;
	}

	public ArrayList<Node> getNodesLevel(int level) {
		return nodes.get(level);
	}

	public void setNode(Node node, int level) {

		try {
			nodes.get(level).add(node);
		} catch (IndexOutOfBoundsException e) {
			nodes.set(level, new ArrayList<Node>());
			nodes.get(level).add(node);
		}
		log.info("Adding Node " + node + " into level " + level);
	}

	/*
	 * public void initNodes(int levels) { log.info("Nodes init..."); for (int i
	 * = 0; i < levels; i++) { nodes.add(new ArrayList<Node>()); } }
	 */
	public void removeNode(Node node, int level) {
		int i = 0;
		log.info("Node search...");
		boolean isBreaked = false;
		for (Node getNode : getNodesLevel(level)) {
			if (getNode.equals(node)) {
				isBreaked = true;
				break;
			}
			i++;
		}
		if (isBreaked) {
			getNodesLevel(level).remove(i);
			log.info("Node deleted! [ node " + node + " into level " + level
					+ "]");
			return;
		}
		log.info("Node not delete! Node not found! [ node " + node
				+ " into level " + level + "]");
	}

	public Building getBuildingToSave() {
		for (int i = 0; i < building.Level.length; i++) {
			building.Level[i].nodes = nodes.get(i).toArray(new Node[nodes.get(i).size()]);
		}
		return building;
	}

	public ArrayList<ArrayList<Node>> getNodes() {
		return nodes;
	}
	
	

}
