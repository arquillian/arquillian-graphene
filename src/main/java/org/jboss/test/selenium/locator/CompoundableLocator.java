package org.jboss.test.selenium.locator;

public interface CompoundableLocator<T extends CompoundableLocator<T>> extends Locator {
	public T getChild(T elementLocator);
	public T getDescendant(T elementLocator);
}
