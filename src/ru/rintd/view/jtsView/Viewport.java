package ru.rintd.view.jtsView;
import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import com.vividsolutions.jts.awt.PointTransformation;
import com.vividsolutions.jts.geom.Coordinate;

public class Viewport implements PointTransformation {

	private AffineTransform modelToViewTransform;

	private Dimension viewSize;
	private double scale = 1;
	
	
	public Viewport(Dimension d, double scale){
		this.viewSize = d;
		this.scale = scale;
	}
	

	@Override
	public void transform(Coordinate src, Point2D dest) {
		dest.setLocation(src.x, src.y);
		getModelToViewTransform().transform(dest, dest);
	}

	public AffineTransform getModelToViewTransform() {
		if (modelToViewTransform == null) {
			updateModelToViewTransform();
		}
		return modelToViewTransform;
	}

	private void updateModelToViewTransform() {
		modelToViewTransform = new AffineTransform();
		//modelToViewTransform.translate(0, viewSize.height);
		//modelToViewTransform.scale(1, -1);
		modelToViewTransform.scale(scale, scale);
	}

	public Dimension getViewSize() {
		return viewSize;
	}

	public void setViewSize(Dimension viewSize) {
		this.viewSize = viewSize;
	}

	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	
	
}
