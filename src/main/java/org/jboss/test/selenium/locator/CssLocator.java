package org.jboss.test.selenium.locator;

import org.jboss.test.selenium.locator.type.LocationStrategy;

public class CssLocator extends AbstractElementLocator implements CompoundableLocator<CssLocator> {
	public CssLocator(String cssSelector) {
		super(cssSelector);
	}

	public LocationStrategy getLocationStrategy() {
		return LocationStrategy.CSS;
	}

	public CssLocator getChild(CssLocator elementLocator) {
		throw new UnsupportedOperationException("not implemented yet");
	} 

	public CssLocator getDescendant(CssLocator elementLocator) {
		throw new UnsupportedOperationException("not implemented yet");
	}
}
