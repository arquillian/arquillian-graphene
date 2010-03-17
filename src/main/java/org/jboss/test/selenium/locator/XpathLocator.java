/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.test.selenium.locator;

import org.jboss.test.selenium.locator.iteration.ChildElementList;
import org.jboss.test.selenium.locator.iteration.ElementOcurrenceList;
import org.jboss.test.selenium.locator.type.LocationStrategy;
import static org.jboss.test.selenium.utils.text.LocatorFormat.format;

public class XpathLocator extends AbstractElementLocator implements IterableLocator, CompoundableLocator<XpathLocator> {
	public XpathLocator(String xpath) {
		super(xpath);
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
	
	public Iterable<XpathLocator> iterateChilds() {
		return new ChildElementList<XpathLocator>(this);
	}

	public Iterable<XpathLocator> iterateOccurences() {
		return new ElementOcurrenceList<XpathLocator>(this);
	}
	
	public XpathLocator getChild(XpathLocator elementLocator) {
		return new XpathLocator(format("{0}/{1}", getLocator(), elementLocator.getLocator()));
	}

	public XpathLocator getDescendant(XpathLocator elementLocator) {
		return new XpathLocator(format("{0}//{1}", getLocator(), elementLocator.getLocator()));
	}
}
