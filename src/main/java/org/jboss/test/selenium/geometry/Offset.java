package org.jboss.test.selenium.geometry;

public class Offset {
	int x, y;

	public Offset(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	public String getMovement() {
		return x + "," + y;
	}
}
