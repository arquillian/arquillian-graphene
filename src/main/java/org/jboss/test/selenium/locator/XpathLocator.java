package org.jboss.test.selenium.locator;

import org.jboss.test.selenium.locator.type.LocationStrategy;
import static org.jboss.test.selenium.utils.text.LocatorFormat.format;

public class XpathLocator extends AbstractElementLocator implements IterableLocator, CompoundableLocator<XpathLocator> {
	public XpathLocator(String xpathLocator) {
		super(xpathLocator);
	}

	public LocationStrategy getLocationStrategy() {
		return LocationStrategy.XPATH;
	}

	public XpathLocator getNthChildElement(int index) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public XpathLocator getNthOccurence(int index) {
		return new XpathLocator(format("getLocator[{0}]", index));
	}

	public XpathLocator getChild(XpathLocator elementLocator) {
		return new XpathLocator(format("{0}/{1}", getLocator(), elementLocator.getLocator()));
	}

	public XpathLocator getDescendant(XpathLocator elementLocator) {
		return new XpathLocator(format("{0}//{1}", getLocator(), elementLocator.getLocator()));
	}
}
