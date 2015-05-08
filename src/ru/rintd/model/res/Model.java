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
    private static final Logger log = LogManager.getLogger(Model.class.getName());

    public Model() {

    }

    /**
     * Открывает файл JSON и преобразует его в Building
     * 
     * @param jsonFilePath абсолютный или относительный путь
     * @return
     */
    public Building setJsonString(String jsonFilePath) {

        log.info("Get building from: " + jsonFilePath);
        try {
            log.info("Transform json to building");
            building = Json2Grid.getStructure(jsonFilePath);
        } catch (IOException e) {
            log.error("Fail get file " + jsonFilePath + " StackTrace: " + e.getMessage());
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
    
    public ArrayList<Node> getNodesLevel(int level){
    	return nodes.get(level);
    }
    
    public void setNode(Node node, int level){
    	nodes.get(level).add(node);
    }
    
    public void removeNode(Node node, int level){
    	int i = 0;
		boolean isBreaked = false;
		for (Node getNode : getNodesLevel(level)) {
			if (getNode.equals(node)){
				isBreaked = true;
				break;
			}
			i++;
		}
		if (isBreaked)
			getNodesLevel(level).remove(i);
		
		
    }
	
}
