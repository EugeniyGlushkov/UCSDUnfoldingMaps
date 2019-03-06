package citymaps.earthquake;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.utils.MapUtils;

public class EarthquakeCityMap extends PApplet {
	private UnfoldingMap map;
	
	@Override
	public void setup() {
		size(950, 600, OPENGL);
		map = new UnfoldingMap(this, 200, 50, 700, 500, new Google.GoogleMapProvider());
		map.zoomToLevel(2);
		MapUtils.createDefaultEventDispatcher(this, map);
	}

	@Override
	public void draw() {
		background(220);
		map.draw();
		//addKey();
	}
}
