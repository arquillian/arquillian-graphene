package org.jboss.test.selenium.locator;

import org.jboss.test.selenium.locator.type.LocationStrategy;

public interface Locator {
	public LocationStrategy getLocationStrategy();
	public String getAsString();
}
