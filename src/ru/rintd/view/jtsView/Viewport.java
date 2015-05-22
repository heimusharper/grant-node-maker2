package ru.rintd.view.jtsView;

import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
//import java.text.NumberFormat;

import com.vividsolutions.jts.awt.PointTransformation;
import com.vividsolutions.jts.geom.Coordinate;
//import com.vividsolutions.jts.geom.Envelope;
//import com.vividsolutions.jts.geom.PrecisionModel;
/**
 * афинные преобразования
 * @author sheihar
 *
 */
public class Viewport implements PointTransformation {

	private AffineTransform modelToViewTransform;

	private Dimension viewSize;
	private double scale = 1;

	private static int INITIAL_ORIGIN_X = -10;
	private static int INITIAL_ORIGIN_Y = -10;

	// private Envelope viewEnvInModel;
	private java.awt.geom.Point2D.Double srcPt = new java.awt.geom.Point2D.Double(
			0, 0);
	private java.awt.geom.Point2D.Double destPt = new java.awt.geom.Point2D.Double(
			0, 0);
	/**
	 * Origin of view in model space
	 */
	private Point2D originInModel = new Point2D.Double(INITIAL_ORIGIN_X,
			INITIAL_ORIGIN_Y);

	public Viewport(Dimension d, double scale) {
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
		modelToViewTransform.translate(0, viewSize.height);
		modelToViewTransform.scale(1, -1);
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

	public Point2D toModel(Point2D viewPt) {
		srcPt.x = viewPt.getX();
		srcPt.y = viewPt.getY();
		try {
			getModelToViewTransform().inverseTransform(srcPt, destPt);
		} catch (NoninvertibleTransformException ex) {
			return new Point2D.Double(0, 0);
			// Assert.shouldNeverReachHere();
		}

		// snap to scale grid
		/*
		 * double x = scalePM.makePrecise(destPt.x); double y =
		 * scalePM.makePrecise(destPt.y);
		 */
		double x = destPt.x;
		double y = destPt.y;

		return new Point2D.Double(x, y);
	}

	public Coordinate toModelCoordinate(Point2D viewPt) {
		Point2D p = toModel(viewPt);
		return new Coordinate(p.getX(), p.getY());
	}

	/**
	 * Zoom to a point, ensuring that the zoom point remains in the same screen
	 * location.
	 * 
	 * @param zoomPt
	 * @param zoomFactor
	 */
	public void zoom(Point2D zoomPt, double zoomScale) {
		double originOffsetX = zoomPt.getX() - originInModel.getX();
		double originOffsetY = zoomPt.getY() - originInModel.getY();

		// set scale first, because it may be snapped
		double scalePrev = getScale();
		setScale(zoomScale);

		double actualZoomFactor = getScale() / scalePrev;
		double zoomOriginX = zoomPt.getX() - originOffsetX / actualZoomFactor;
		double zoomOriginY = zoomPt.getY() - originOffsetY / actualZoomFactor;
		setOrigin(zoomOriginX, zoomOriginY);
	}

	private void setOrigin(double viewOriginX, double viewOriginY) {
		this.originInModel = new Point2D.Double(viewOriginX, viewOriginY);
		update();
	}

	private void update() {
		updateModelToViewTransform();
		// viewEnvInModel = computeEnvelopeInModel();
	}

	/*
	 * private Envelope computeEnvelopeInModel() { return new
	 * Envelope(originInModel.getX(), originInModel.getX() + getWidthInModel(),
	 * originInModel.getY(), originInModel.getY() + getHeightInModel()); }
	 * 
	 * private double getWidthInModel() { return toModel(viewSize.width); }
	 * 
	 * private double getHeightInModel() { return toModel(viewSize.height); }
	 */
	/**
	 * Converts a distance in the view to a distance in the model.
	 * 
	 * @param viewDist
	 * @return the model distance
	 */
	public double toModel(double viewDist) {
		return viewDist / scale;
	}

}
