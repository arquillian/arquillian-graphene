package org.jboss.test.selenium.locator;

public interface Attribute {
	public String getAttributeName();

	public Attribute STYLE = new Attribute() {
		public String getAttributeName() {
			return "style";
		}
	};
}
