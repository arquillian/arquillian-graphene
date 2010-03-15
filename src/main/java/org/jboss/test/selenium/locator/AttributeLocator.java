package org.jboss.test.selenium.locator;

import org.jboss.test.selenium.locator.type.LocationStrategy;

public interface AttributeLocator extends Locator {

	public abstract LocationStrategy getLocationStrategy();

}