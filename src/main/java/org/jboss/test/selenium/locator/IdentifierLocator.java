package org.jboss.test.selenium.locator;

import org.jboss.test.selenium.locator.type.LocationStrategy;

public class IdentifierLocator extends AbstractElementLocator {
	public IdentifierLocator(String cssSelector) {
		super(cssSelector);
	}

	public LocationStrategy getLocationStrategy() {
		return LocationStrategy.IDENTIFIER;
	}
}
