package module3;

//Java utilities libraries
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;

//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;
//Parsing library
import parsing.ParseFeed;
//Processing library
import processing.core.PApplet;

/**
 * EarthquakeCityMap An application with an interactive map displaying
 * earthquake data. Author: UC San Diego Intermediate Software Development MOOC
 * team
 * 
 * @author Hector Sanchez -- Rootdevelopper Date: July 17, 2015
 */
public class EarthquakeCityMap extends PApplet {

	// You can ignore this. It's to keep eclipse from generating a warning.
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFLINE, change the value of this variable to true
	private static final boolean offline = false;

	// Less than this threshold is a light earthquake
	public static final float THRESHOLD_MODERATE = 5;
	// Less than this threshold is a minor earthquake
	public static final float THRESHOLD_LIGHT = 4;

	/**
	 * This is where to find the local tiles, for working without an Internet
	 * connection
	 */
	public static String mbTilesString = "blankLight-1-3.mbtiles";

	// The map
	private UnfoldingMap map;

	// feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";

	public void setup() {
		size(950, 600, OPENGL);

		if (offline) {
			map = new UnfoldingMap(this, 200, 50, 700, 500, new MBTilesMapProvider(mbTilesString));
			earthquakesURL = "2.5_week.atom"; // Same feed, saved Aug 7, 2015,
												// for working offline
		} else {
			map = new UnfoldingMap(this, 200, 50, 700, 500, new Google.GoogleMapProvider()); //
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
			// earthquakesURL = "2.5_week.atom";
		}

		map.zoomToLevel(2);
		MapUtils.createDefaultEventDispatcher(this, map);

		// The List you will populate with new SimplePointMarkers
		List<Marker> markers = new ArrayList<Marker>();

		// Use provided parser to collect properties for each earthquake
		// PointFeatures have a getLocation method
		List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);

		// have magnitude as a global so we can calculate the color and size for
		// the markers

		// These print statements show you (1) all of the relevant properties
		// in the features, and (2) how to get one property and use it
		if (earthquakes.size() > 0) {

			PointFeature f = earthquakes.get(0);
			System.out.println(f.getProperties());
			Object magObj = f.getProperty("magnitude");
			float mag = Float.parseFloat(magObj.toString());
			// PointFeatures also have a getLocation method
		}

		// Here is an example of how to use Processing's color method to
		// generate
		// an int that represents the color yellow.
		int yellow = color(255, 255, 0);

		// create a simple marker to pull toguether the pointfeature and
		// location and
		// iterate through the size of the earthquake set
		// TODO: Add code here as appropriate

		// for each
		for (PointFeature eartquake : earthquakes) {

			SimplePointMarker eartquakeMarker = createMarker(eartquake);
			markers.add(eartquakeMarker);
		}

		map.addMarkers(markers);
	}

	// A suggested helper method that takes in an earthquake feature and
	// returns a SimplePointMarker for that earthquake
	// TODO: Implement this method and call it from setUp, if it helps
	private SimplePointMarker createMarker(PointFeature feature) {
		// finish implementing and use this method, if it helps.

		// create colors

		int red = color(255, 0, 0);
		int green = color(0, 255, 0);
		int blue = color(0, 0, 255);
		// get the magnitude and assign a radius size
		// name of column in csv file
		Object magnitude = feature.getProperty("magnitude");
		float mag = Float.parseFloat(magnitude.toString());

		// set radius based on magnitude
		float radiousMagnitude = mag * 2;

		// set color based on magnitude

		int colorMagnitude;

		if (mag <= 3) {
			colorMagnitude = green;
		} else if (mag >= 5) {
			colorMagnitude = red;
		} else {
			colorMagnitude = blue;
		}

		Location earthquakeLocation = feature.getLocation();
		SimplePointMarker earthQuakeMarker = new SimplePointMarker(earthquakeLocation);
		earthQuakeMarker.setRadius(radiousMagnitude);
		earthQuakeMarker.setColor(colorMagnitude);

		return earthQuakeMarker;

		// return new SimplePointMarker(feature.getLocation());
	}

	public void draw() {
		background(10);
		map.draw();
		addKey();

	}

	// helper method to draw key in GUI
	// TODO: Implement this method to draw the key
	private void addKey() {
		// Remember you can use Processing's graphics methods here

		// First create a box, then add all the elements inside

		// box
		fill(255, 250, 240);
		rect(25, 50, 150, 250);
		fill(0);

		// keys
		// map the key according to the color of each magnitude

		text("Earthquake Key", 50, 80);
		text("Magnitude 5.0+ ", 50, 130);
		fill(255, 0, 0);
		ellipse(40, 130, 16, 16);
		fill(0);// fill 0 is necessary to avoid the color being used by the next
				// text color
		text("Magnitude 4.0+", 50, 180);
		fill(0, 0, 255);
		ellipse(40, 180, 12, 12);
		fill(0);
		text("Magnitude under 4.0", 50, 230);
		fill(0, 255, 0);
		ellipse(40, 230, 8, 8);

	}
}
