package guimodule;

import processing.core.PApplet;
import processing.core.PImage;

public class MyDisplay extends PApplet {
	private PImage img;
	
	@Override
	public void setup() {
		size(400, 400);
		//background(255);
		stroke(0);
		img = loadImage("http://cseweb.ucsd.edu/~minnes/palmTrees.jpg", "jpg");
		img.resize(0,  height);
		image(img, 0, 0);
	}

	@Override
	public void draw() {
		int[] colorsRGB = sunColorSec(second());
		fill(colorsRGB[0], colorsRGB[1], colorsRGB[2]);
		ellipse(width / 4, height / 5, width / 4, height / 5);
	}
	
	public int[] sunColorSec(float second) {
		int[] rgb = new int[3];
		float diffFrom30 = Math.abs(30 - second);
		float ratio = diffFrom30 / 30;
		rgb[0] = (int)(255 * ratio);
		rgb[1] = (int)(255 * ratio);
		rgb[2] = 0;
		return rgb;
	}
}
