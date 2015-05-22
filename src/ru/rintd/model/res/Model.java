package ru.rintd.model.res;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vividsolutions.jts.geom.Polygon;

import ru.rintd.json2grid.BuildElement;
import ru.rintd.json2grid.Building;
import ru.rintd.json2grid.Json2Grid;
import ru.rintd.json2grid.Node;
import ru.rintd.json2grid.Building.InternLevel;
/**
 * модель данных
 * @author sheihar
 *
 */
public class Model {

	/**
	 * план здания
	 */
	private Building building = new Building();
	/**
	 * набор узлов
	 * первый ArrayList - этаж
	 * второй - набор злов этажа
	 */
	private ArrayList<ArrayList<Node>> nodes = new ArrayList<ArrayList<Node>>();
	/**
	 * карта элементов для ускорения и простоты
	 */
	private HashMap<String, BuildElement> elementsMap = new HashMap<String, BuildElement>();
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

		return Json2Grid.getJts(building);
	}

	public Building getBuilding() {
		return building;
	}

	public ArrayList<Node> getNodesLevel(int level) {
		return nodes.get(level);
	}

	/**
	 * добавить узел в модель
	 * @param node узел
	 * @param level этаж здания
	 */
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
	/**
	 * удалить узел из модели
	 * @param node узел
	 * @param level этаж
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

	/**
	 * отдает план здания с узлами, готовый для сохранения
	 * @return план здания для json
	 */
	public Building getBuildingToSave() {
		for (int i = 0; i < building.Level.length; i++) {
			building.Level[i].nodes = nodes.get(i).toArray(new Node[nodes.get(i).size()]);
		}
		return building;
	}

	public ArrayList<ArrayList<Node>> getNodes() {
		return nodes;
	}
	
	
	/**
	 * получить элемент по его id
	 * @param id id элемента
	 * @return
	 */
	public BuildElement getElementFromId(String id){
		if (elementsMap.size() == 0){
			if (building.Level != null){
				for (InternLevel level : building.Level) {
					for (BuildElement el : level.BuildElement){
						elementsMap.put(el.Id, el);
					}
				}
			}
		}
		if (elementsMap.size() != 0){
			return elementsMap.get(id);
		}
		return null;
	}
	

}
