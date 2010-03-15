package org.jboss.test.selenium.locator;

import org.jboss.test.selenium.locator.type.LocationStrategy;
import static org.jboss.test.selenium.utils.text.LocatorFormat.format;

public class JQueryLocator extends AbstractElementLocator implements IterableLocator, CompoundableLocator<JQueryLocator> {
	public JQueryLocator(String cssSelector) {
		super(cssSelector);
	}

	public LocationStrategy getLocationStrategy() {
		return LocationStrategy.JQUERY;
	}

	public JQueryLocator getNthChildElement(int index) {
		return new JQueryLocator(format("{0}:nth-child({1})", getLocator(), index + 1));
	}

	public JQueryLocator getNthOccurence(int index) {
		return new JQueryLocator(format("{0}:eq({1})", getLocator(), index));
	}

	public JQueryLocator getChild(JQueryLocator elementLocator) {
		return new JQueryLocator(format("{0} > {1}", getLocator(), elementLocator.getLocator()));
	}

	public JQueryLocator getDescendant(JQueryLocator elementLocator) {
		return new JQueryLocator(format("{0} {1}", getLocator(), elementLocator.getLocator()));
	}
}
