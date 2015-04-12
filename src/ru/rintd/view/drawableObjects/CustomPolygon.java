package ru.rintd.view.drawableObjects;

import java.awt.Polygon;
import java.util.Arrays;

import ru.rintd.view.ToDrawPolygons;

public class CustomPolygon extends Polygon {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7915756454031564730L;
	/**
     * id объекта
     */
    private String buildingId;
    /**
     * тип объекта
     */
    private String buildingType;

    /**
     * слой
     */
    private int layer = 0;
    
    /**
     * этаж
     */
	private int level = 0;
    
    private String output[];

    /**
     * координаты в системе JSON'а и их преобразование
     */
    public double xpointsf[];
    public double ypointsf[];
    public int npointsf;

    public CustomPolygon(String buildingId, String buildingType, int layer, String[] ouput) {
        super();
        // минимум у Plygon 4, пусть и тут будет так
        xpointsf = new double[4];
        ypointsf = new double[4];

        this.buildingId = buildingId;
        this.buildingType = buildingType;
        this.layer = layer;
        this.output = ouput;
    }

    /**
     * добавление точки в системе координат JSON
     * 
     * @param x
     * @param y
     */
    public void addPoint(double x, double y) {
        if (npointsf >= xpointsf.length || npointsf >= ypointsf.length) {
            int newLength = npointsf * 2;
            // Make sure that newLength will be greater than MIN_LENGTH and
            // aligned to the power of 2
            if (newLength < 4) {
                newLength = 4;
            } else if ((newLength & (newLength - 1)) != 0) {
                newLength = Integer.highestOneBit(newLength);
            }

            xpointsf = Arrays.copyOf(xpointsf, newLength);
            ypointsf = Arrays.copyOf(ypointsf, newLength);
        }
        xpointsf[npointsf] = x;
        ypointsf[npointsf] = y;
        npointsf++;
    }

    /**
     * преобразование координат из json в кординаты приложения
     * 
     * @param point неообходимая точка
     * @param min минимум в json
     * @param max максимум в json
     * @param windMax максимум эрана
     * @return координата в приложении
     */
    public static int double2int(double point, double min, double max, int windMax) {
        // System.out.print(windMax+" * "+(point-min)/(max-min)+"  ");
        return (int) (windMax * (point - min) / (max - min));
    }

    public static int double2int(double point, double min, double mult) {
        return (int) ((point - min) * mult);
    }

    /**
     * преобразование координат из кординаты приложения в json
     * 
     * @param point неообходимая точка
     * @param max максимум экрана
     * @param jsonMin минимум в json
     * @param jsonMax максимум в json
     * @return координата в json
     */
    public static double int2double(int point, double max, double jsonMin, double jsonMax) {
        // return (((jsonMax-jsonMin)*point)/(max));
        return (((jsonMax - jsonMin) * point) / (max)) + jsonMin;
    }

    /**
     * переворачивает по оси X
     * 
     * @param y значение Y
     * @param max max Y
     * @return
     */
    public static int translateY(int y, int max) {
        int a = (int) (max / 2);
        if (y < a) return a + (a - y);
        if (y >= a) return a - (y - a);
        return y;
    }

    /**
     * переводит все точки в int координаты приложения
     * 
     * @param zoom масштаб
     */
    public void double2int(double minXjson, double maxXjson, double minYjson, double maxYjson, int windWidth,
            int windHeight, int adjustementTo, double zoom) {
    	
    	double maxXjson2 = maxXjson - minXjson;
		double maxYjson2 = maxYjson - minYjson;
		double minXjson2 = minXjson - minXjson;
		double minYjson2 = minYjson - minYjson;
    	
        xpoints = new int[4];
        ypoints = new int[4];

        double multi = 0;
        if (adjustementTo == ToDrawPolygons.ADJUST_TO_WIDTH){
        	multi = windWidth / (maxXjson2 - minXjson2);
        } else {
        	if (adjustementTo == ToDrawPolygons.ADJUST_TO_HEIGHT){
            	multi = windHeight / (maxYjson2 - minYjson2);
            }
        }
        
        // TODO: неправильно выводится X, изображение выходит за края экрана!
        for (int i = 0; i < npointsf; i++) {
            // TODO: Надо учесть размеры вкладки а не приложения
            // int x = double2int(xpointsf[i], Json2Polygon.minXjson, Json2Polygon.maxXjson,
            // CustomProperties.getWindowWidth())*zoom;
            // int y = double2int(ypointsf[i], Json2Polygon.minYjson, Json2Polygon.maxYjson,
            // CustomProperties.getWindowHeight())*zoom;
            int x = (int) (double2int(xpointsf[i]-minXjson, minXjson2, multi) * zoom);
            int y = (int) (double2int(ypointsf[i]-minYjson, minYjson2, multi));
            y = translateY(y, double2int(maxYjson2, minYjson2, multi));

            this.addPoint(x, (int) (y * zoom));
        }
    }

    public boolean inOutput(String s){
    	for (String out : output) {
			if (s.equals(out))
				return true;
		}
    	return false;
    }
    
	public String[] getOutput() {
		return output;
	}

	public void setOutput(String[] output) {
		this.output = output;
	}

	public String getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}

	public String getBuildingType() {
		return buildingType;
	}

	public void setBuildingType(String buildingType) {
		this.buildingType = buildingType;
	}

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public double[] getXpointsf() {
		return xpointsf;
	}

	public void setXpointsf(double[] xpointsf) {
		this.xpointsf = xpointsf;
	}

	public double[] getYpointsf() {
		return ypointsf;
	}

	public void setYpointsf(double[] ypointsf) {
		this.ypointsf = ypointsf;
	}

	public int getNpointsf() {
		return npointsf;
	}

	public void setNpointsf(int npointsf) {
		this.npointsf = npointsf;
	}
    

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}