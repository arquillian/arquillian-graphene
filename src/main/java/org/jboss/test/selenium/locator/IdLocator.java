package org.jboss.test.selenium.locator;

import org.jboss.test.selenium.locator.type.LocationStrategy;

public class IdLocator extends AbstractElementLocator {
	public IdLocator(String cssSelector) {
		super(cssSelector);
	}

	public LocationStrategy getLocationStrategy() {
		return LocationStrategy.CSS;
		
	}
}
