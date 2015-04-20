package ru.rintd.model.res;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.vividsolutions.jts.geom.Polygon;
import ru.rintd.json2grid.Building;
import ru.rintd.json2grid.GridTransformation;
import ru.rintd.json2grid.Json2Grid;

public class Model {

    private Building building = new Building();
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
    
    
}
