package ru.rintd.model.res;

import java.awt.Dimension;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.rintd.json2grid.Building;
import ru.rintd.json2grid.Json2Grid;
import ru.rintd.view.ToDrawPolygons;

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

    public ToDrawPolygons[] getToDrawPolygons(Dimension dimension) {
        ToDrawPolygons[] toDrawPolygons = new ToDrawPolygons[building.Level.length];
        for (int i = 0; i < toDrawPolygons.length; i++) {
            toDrawPolygons[i] = new ToDrawPolygons();
            toDrawPolygons[i].setBuilding(building, i);
            toDrawPolygons[i].setWindowDim(dimension);
        }
        return toDrawPolygons;
    }

}
