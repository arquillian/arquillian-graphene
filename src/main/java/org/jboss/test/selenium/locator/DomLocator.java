package org.jboss.test.selenium.locator;

import org.jboss.test.selenium.locator.type.LocationStrategy;

public class DomLocator extends AbstractElementLocator {
	public DomLocator(String cssSelector) {
		super(cssSelector);
	}

	public LocationStrategy getLocationStrategy() {
		return LocationStrategy.DOM;
	}
}
