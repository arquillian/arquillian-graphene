package org.jboss.test.selenium.locator.iteration;

import org.jboss.test.selenium.framework.TypedSelenium;
import org.jboss.test.selenium.locator.IterableLocator;

public class ElementOcurrenceList<T extends IterableLocator> extends AbstractElementList<T> {

	public ElementOcurrenceList(TypedSelenium typedSelenium, T iterableLocator) {
		super(typedSelenium, iterableLocator);
	}

	@Override
	@SuppressWarnings("unchecked")
	T abstractNthElement(int index) {
		return (T) iterableLocator.getNthOccurence(index);
	}
}
