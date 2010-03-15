package org.jboss.test.selenium.locator;



public abstract class AbstractElementLocator extends AbstractLocator implements ElementLocator {
	public AbstractElementLocator(String locator) {
		super(locator);
	}
	
	public AttributeLocator getAttribute(Attribute attribute) {
		return new DefaultAttributeLocator(this, attribute);
	}
}
