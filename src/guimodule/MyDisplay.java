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
	}
}
