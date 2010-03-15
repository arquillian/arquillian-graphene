package org.jboss.test.selenium.locator;

import org.apache.commons.lang.Validate;
import org.jboss.test.selenium.locator.type.LocationStrategy;
import static org.jboss.test.selenium.utils.text.LocatorFormat.format;

public class DefaultAttributeLocator extends AbstractLocator implements AttributeLocator {
	
	Attribute attribute;
	LocationStrategy locationStrategy;
	
	public DefaultAttributeLocator(AbstractElementLocator elementLocator, Attribute attribute) {
		super(elementLocator.getLocator());
		
		Validate.notNull(attribute);
		locationStrategy = elementLocator.getLocationStrategy();
	}
	
	@Override
	public String getAsString() {
		return format("{0}@{1}", getAsString(), attribute.getAttributeName());
	}

	/* (non-Javadoc)
	 * @see org.jboss.test.selenium.locator.AttributeLocator#getLocationStrategy()
	 */
	public LocationStrategy getLocationStrategy() {
		return locationStrategy;
	}
}
