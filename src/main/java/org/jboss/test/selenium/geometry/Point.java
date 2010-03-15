package org.jboss.test.selenium.geometry;

public class Point {
	int x, y;

	public Point(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public String getCoords() {
		return x + "," + y;
	}

}
