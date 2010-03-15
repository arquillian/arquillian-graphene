package org.jboss.test.selenium.locator;

public interface IterableLocator extends Locator {
	public IterableLocator getNthChildElement(int index);
	public IterableLocator getNthOccurence(int index);
}
