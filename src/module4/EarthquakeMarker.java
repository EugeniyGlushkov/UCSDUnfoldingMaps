package module4;

import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PGraphics;

/**
 * Implements a visual marker for earthquakes on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 *
 */
public abstract class EarthquakeMarker extends SimplePointMarker {

	// Did the earthquake occur on land? This will be set by the subclasses.
	protected boolean isOnLand;

	// SimplePointMarker has a field "radius" which is inherited
	// by Earthquake marker:
	// protected float radius;
	//
	// You will want to set this in the constructor, either
	// using the thresholds below, or a continuous function
	// based on magnitude.

	/** Greater than or equal to this threshold is a moderate earthquake */
	public static final float THRESHOLD_MODERATE = 5;
	/** Greater than or equal to this threshold is a light earthquake */
	public static final float THRESHOLD_LIGHT = 4;

	/** Greater than or equal to this threshold is an intermediate depth */
	public static final float THRESHOLD_INTERMEDIATE = 70;
	/** Greater than or equal to this threshold is a deep depth */
	public static final float THRESHOLD_DEEP = 300;

	// ADD constants for colors
	public static final int RED = -65536;
	public static final int GREEN = -16711936;
	public static final int BLUE = -16776961;
	public static final int YELLOW = -256;

	// abstract method implemented in derived classes
	public abstract void drawEarthquake(PGraphics pg, float x, float y);

	// constructor
	public EarthquakeMarker(PointFeature feature) {
		super(feature.getLocation());
		// Add a radius property and then set the properties
		java.util.HashMap<String, Object> properties = feature.getProperties();
		float magnitude = Float.parseFloat(properties.get("magnitude").toString());
		properties.put("radius", 2 * magnitude);
		setProperties(properties);
		this.radius = 1.75f * getMagnitude();
	}

	// calls abstract method drawEarthquake and then checks age and draws X if
	// needed
	public void draw(PGraphics pg, float x, float y) {
		// save previous styling
		pg.pushStyle();

		// determine color of marker from depth
		colorDetermine(pg);

		// call abstract method implemented in child class to draw marker shape
		drawEarthquake(pg, x, y);
		
		String age = (String)getProperty("age");
		
		if (age.equals("Past Hour") || age.equals("Past Day")) {
			drawX(pg, x, y, 20);
		}

		// OPTIONAL TODO: draw X over marker if within past day

		// reset to previous styling
		pg.popStyle();

	}
	
	public static void drawX(PGraphics pg, float x, float y, int size) {
		float x1 = x - size / 2;
		float y1 = y - size / 2;
		float x2 = x + size / 2;
		float y2 = y + size / 2;
		float x3 = x - size / 2;
		float y3 = y + size / 2;
		float x4 = x + size / 2;
		float y4 = y - size / 2;
		pg.line(x1, y1, x2, y2);
		pg.line(x3, y3, x4, y4);
	}

	// determine color of marker from depth, and set pg's fill color
	// using the pg.fill method.
	// We suggest: Deep = red, intermediate = blue, shallow = yellow
	// But this is up to you, of course.
	// You might find the getters below helpful.
	private void colorDetermine(PGraphics pg) {
		//TODO: Implement this method
		if(getDepth() < THRESHOLD_INTERMEDIATE) {
			pg.fill(YELLOW);
		} else if (getDepth() < THRESHOLD_DEEP) {
			pg.fill(BLUE);
		} else {
			pg.fill(RED);
		}
	}

	/*
	 * getters for earthquake properties
	 */

	public float getMagnitude() {
		return Float.parseFloat(getProperty("magnitude").toString());
	}

	public float getDepth() {
		return Float.parseFloat(getProperty("depth").toString());
	}

	public String getTitle() {
		return (String) getProperty("title");

	}

	public float getRadius() {
		return Float.parseFloat(getProperty("radius").toString());
	}

	public boolean isOnLand() {
		return isOnLand;
	}

}
