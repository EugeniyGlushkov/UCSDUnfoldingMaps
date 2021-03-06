package module4;

import java.util.ArrayList;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractShapeMarker;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MultiMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import processing.core.PApplet;

import static module4.EarthquakeMarker.*;

/**
 * EarthquakeCityMap An application with an interactive map displaying
 * earthquake data. Author: UC San Diego Intermediate Software Development MOOC
 * team
 * 
 * @author Evgen Glushkov Date: July 17, 2015
 */
public class EarthquakeCityMap extends PApplet {

	// We will use member variables, instead of local variables, to store the data
	// that the setUp and draw methods will need to access (as well as other
	// methods)
	// You will use many of these variables, but the only one you should need to add
	// code to modify is countryQuakes, where you will store the number of
	// earthquakes
	// per country.

	// You can ignore this. It's to get rid of eclipse warnings
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFILINE, change the value of this variable to true
	private static final boolean offline = false;

	/**
	 * This is where to find the local tiles, for working without an Internet
	 * connection
	 */
	public static String mbTilesString = "blankLight-1-3.mbtiles";

	// feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";

	// The files containing city names and info and country names and info
	private String cityFile = "city-data.json";
	private String countryFile = "countries.geo.json";

	// The map
	private UnfoldingMap map;

	// Markers for each city
	private List<Marker> cityMarkers;
	// Markers for each earthquake
	private List<Marker> quakeMarkers;

	// A List of country markers
	private List<Marker> countryMarkers;

	public void setup() {
		// (1) Initializing canvas and map tiles
		size(900, 700, OPENGL);
		if (offline) {
			map = new UnfoldingMap(this, 200, 50, 650, 600, new MBTilesMapProvider(mbTilesString));
			earthquakesURL = "2.5_week.atom"; // The same feed, but saved August 7, 2015
		} else {
			//map = new UnfoldingMap(this, 200, 50, 650, 600, new Google.GoogleMapProvider());
			map = new UnfoldingMap(this, 200, 50, 650, 600, new Microsoft.AerialProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
			// earthquakesURL = "2.5_week.atom";
			earthquakesURL = "quiz1.atom";
		}
		MapUtils.createDefaultEventDispatcher(this, map);

		// FOR TESTING: Set earthquakesURL to be one of the testing files by
		// uncommenting
		// one of the lines below. This will work whether you are online or offline
		// earthquakesURL = "test1.atom";
		// earthquakesURL = "test2.atom";

		// WHEN TAKING THIS QUIZ: Uncomment the next line
		// earthquakesURL = "quiz1.atom";

		// (2) Reading in earthquake data and geometric properties
		// STEP 1: load country features and markers
		List<Feature> countries = GeoJSONReader.loadData(this, countryFile);
		countryMarkers = MapUtils.createSimpleMarkers(countries);

		// STEP 2: read in city data
		List<Feature> cities = GeoJSONReader.loadData(this, cityFile);
		cityMarkers = new ArrayList<Marker>();
		for (Feature city : cities) {
			cityMarkers.add(new CityMarker(city));
		}

		// STEP 3: read in earthquake RSS feed
		List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
		quakeMarkers = new ArrayList<Marker>();

		for (PointFeature feature : earthquakes) {
			// System.out.println(isLand(feature));
			// check if LandQuake
			if (isLand(feature)) {
				quakeMarkers.add(new LandQuakeMarker(feature));
			}
			// OceanQuakes
			else {
				quakeMarkers.add(new OceanQuakeMarker(feature));
			}
		}

		// could be used for debugging
		printQuakes();

		// (3) Add markers to map
		// NOTE: Country markers are not added to the map. They are used
		// for their geometric properties
		map.addMarkers(quakeMarkers);
		map.addMarkers(cityMarkers);
		
		background(0);
	} // End setup

	public void draw() {
		map.draw();
		addKey();
		drawButtons();

	}
	
	@Override
	public void mouseReleased() {
		if (mouseX > 60 && mouseX < 85 && mouseY > 270 && mouseY < 295) {
			background(255);
		} else if (mouseX > 110 && mouseX < 135 && mouseY > 270 && mouseY < 295) {
			background(100);
		};
	}
	
	@Override
	public void keyPressed() {
		if (key == 'w') {
			background(255);
		}
	}

	// helper method to draw key in GUI
	// TODO: Update this method as appropriate
	private void addKey() {
		// Remember you can use Processing's graphics methods here
		fill(255, 250, 240);
		rect(25, 50, 150, 250);

		fill(0);
		textSize(15);
		text("Earthquake Key", 45, 80);
		text("City Marker", 70, 105);
		text("Land Quake", 70, 125);
		text("Ocean Quake", 70, 145);
		text("Size ~ Magnitude", 40, 170);
		text("Shallow", 70, 195);
		text("Intermediate", 70, 215);
		text("Deep", 70, 235);
		text("Past Day", 70, 255);
		drawTriangle(50, 101, 8);
		fill(255);
		ellipse(50, 120, 15, 15);
		fill(255);
		rect(43, 132, 15, 15);
		fill(YELLOW);
		ellipse(50, 190, 13, 13);
		fill(BLUE);
		ellipse(50, 210, 13, 13);
		fill(RED);
		ellipse(50, 230, 13, 13);
		fill(255);
		ellipse(50, 250, 13, 13);
		EarthquakeMarker.drawX(this.g, 50, 250, 20);	
		
	}
	
	private void drawButtons( ) {
		fill(255);
		rect(60, 270, 25, 25);
		
		fill(100);
		rect(110, 270, 25, 25);
	}
	
	private void drawTriangle(float x, float y, float size ) {
		fill(200, 0, 0);
		float trX1 = x;
		float trY1 = y - size;
		float dX = size * (float)Math.cos(Math.PI / 6);
		float dY = size * (float)Math.sin(Math.PI / 6);
		float trX2 = x + dX;
		float trY2 = y + dY;
		float trX3 = x - dX;
		float trY3 = y + dY;
		triangle(trX1, trY1, trX2, trY2, trX3, trY3);
	}

	// Checks whether this quake occurred on land. If it did, it sets the
	// "country" property of its PointFeature to the country where it occurred
	// and returns true. Notice that the helper method isInCountry will
	// set this "country" property already. Otherwise it returns false.
	private boolean isLand(PointFeature earthquake) {

		// Loop over all the country markers.
		// For each, check if the earthquake PointFeature is in the
		// country in m. Notice that isInCountry takes a PointFeature
		// and a Marker as input.
		// If isInCountry ever returns true, isLand should return true.
		for (Marker m : countryMarkers) {
			// TODO: Finish this method using the helper method isInCountry

			if (isInCountry(earthquake, m)) {
				return true;
			}
		}

		// not inside any country
		return false;
	}

	/*
	 * prints countries with number of earthquakes as Country1: numQuakes1 Country2:
	 * numQuakes2 ... OCEAN QUAKES: numOceanQuakes
	 */
	private void printQuakes() {
		// TODO: Implement this method
		// One (inefficient but correct) approach is to:
		// Loop over all of the countries, e.g. using
		// for (Marker cm : countryMarkers) { ... }
		//
		// Inside the loop, first initialize a quake counter.
		// Then loop through all of the earthquake
		// markers and check to see whether (1) that marker is on land
		// and (2) if it is on land, that its country property matches
		// the name property of the country marker. If so, increment
		// the country's counter.

		// Here is some code you will find useful:
		//
		// * To get the name of a country from a country marker in variable cm, use:
		// String name = (String)cm.getProperty("name");
		// * If you have a reference to a Marker m, but you know the underlying object
		// is an EarthquakeMarker, you can cast it:
		// EarthquakeMarker em = (EarthquakeMarker)m;
		// Then em can access the methods of the EarthquakeMarker class
		// (e.g. isOnLand)
		// * If you know your Marker, m, is a LandQuakeMarker, then it has a "country"
		// property set. You can get the country with:
		// String country = (String)m.getProperty("country");
		List<PointFeature> quakes = ParseFeed.parseEarthquake(this, earthquakesURL);
		StringBuilder contrEarthQuaks = new StringBuilder("+-------------------------+----------+\n");
		contrEarthQuaks.append(String.format("|%-25s|%-10s|\n", "Country's name", "quakes"))
				.append("+-------------------------+----------+\n");
		int quakCount = 0;
		int quakInSea = quakes.size();

		for (Marker cm : countryMarkers) {
			for (Marker eQuake : quakeMarkers) {
				if (!((EarthquakeMarker) eQuake).isOnLand) {
					continue;
				}

				String countryName = (String) (cm.getProperty("name"));
				String eqCountryName = (String) (eQuake.getProperty("country"));

				if (countryName.equals(eqCountryName)) {
					quakCount++;
					quakInSea--;
				}
			}

			if (quakCount > 0) {
				contrEarthQuaks.append(String.format("|%-25s|%-10d|\n", (String) (cm.getProperty("name")), quakCount));
				quakCount = 0;
			}
		}

		contrEarthQuaks.append("+-------------------------+----------+\n");
		contrEarthQuaks.append(String.format("|%-25s|%-10d|\n", "in the sea", quakInSea));
		contrEarthQuaks.append("+-------------------------+----------+\n");
		System.out.println(contrEarthQuaks);
	}

	// helper method to test whether a given earthquake is in a given country
	// This will also add the country property to the properties of the earthquake
	// feature if it's in one of the countries.
	// You should not have to modify this code
	private boolean isInCountry(PointFeature earthquake, Marker country) {
		// getting location of feature
		Location checkLoc = earthquake.getLocation();

		// some countries represented it as MultiMarker
		// looping over SimplePolygonMarkers which make them up to use isInsideByLoc
		if (country.getClass() == MultiMarker.class) {

			// looping over markers making up MultiMarker
			for (Marker marker : ((MultiMarker) country).getMarkers()) {

				// checking if inside
				if (((AbstractShapeMarker) marker).isInsideByLocation(checkLoc)) {
					earthquake.addProperty("country", country.getProperty("name"));

					// return if is inside one
					return true;
				}
			}
		}

		// check if inside country represented by SimplePolygonMarker
		else if (((AbstractShapeMarker) country).isInsideByLocation(checkLoc)) {
			earthquake.addProperty("country", country.getProperty("name"));

			return true;
		}
		return false;
	}

}
