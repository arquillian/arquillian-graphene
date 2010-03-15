package org.jboss.test.selenium.locator;

import org.jboss.test.selenium.locator.type.LocationStrategy;

public class LinkLocator extends AbstractElementLocator {
	public LinkLocator(String cssSelector) {
		super(cssSelector);
	}

	public LocationStrategy getLocationStrategy() {
		return LocationStrategy.LINK;
	}
}
