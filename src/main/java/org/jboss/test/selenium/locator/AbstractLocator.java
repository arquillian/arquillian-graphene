package org.jboss.test.selenium.locator;

import org.apache.commons.lang.Validate;
import org.jboss.test.selenium.locator.type.LocationStrategy;
import static org.jboss.test.selenium.utils.text.LocatorFormat.format;

public abstract class AbstractLocator implements Locator {
	
	private String locator;
	
	public AbstractLocator(String locator) {
		Validate.notNull(locator);
		this.locator = locator;
	}
	
	public String getAsString() {
		final LocationStrategy locationStrategy = getLocationStrategy();
		
		return format("{0}={1}", locationStrategy.getStrategyName(), locator);
	}
	
	String getLocator() {
		return locator;
	}
	
	@Override
	public String toString() {
		return getAsString();
	}
}
