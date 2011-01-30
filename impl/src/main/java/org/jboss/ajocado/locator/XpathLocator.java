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
package org.jboss.ajocado.locator;

import org.jboss.ajocado.locator.iteration.ChildElementList;
import org.jboss.ajocado.locator.iteration.ElementOcurrenceList;
import org.jboss.ajocado.utils.SimplifiedFormat;

/**
 * Locates the element using <a href="http://www.w3.org/TR/xpath/">XPath expression</a>.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class XpathLocator extends AbstractElementLocator<XpathLocator> implements ExtendedLocator<XpathLocator> {

    /**
     * Instantiates a new xpath locator.
     * 
     * @param xpath
     *            the xpath
     */
    public XpathLocator(String xpath) {
        super(xpath);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.ajocado.locator.Locator#getLocationStrategy()
     */
    public ElementLocationStrategy getLocationStrategy() {
        return ElementLocationStrategy.XPATH;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.ajocado.locator.IterableLocator#getNthChildElement(int)
     */
    public XpathLocator getNthChildElement(int index) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.ajocado.locator.IterableLocator#getNthOccurence(int)
     */
    public XpathLocator getNthOccurence(int index) {
        return new XpathLocator(SimplifiedFormat.format("getLocator[{0}]", index - 1));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.ajocado.locator.IterableLocator#getAllChildren()
     */
    public Iterable<XpathLocator> getAllChildren() {
        return new ChildElementList<XpathLocator>(this.getChild(LocatorFactory.xp("*")));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.ajocado.locator.IterableLocator#getChildren(org.jboss.test.selenium.locator.IterableLocator)
     */
    public Iterable<XpathLocator> getChildren(XpathLocator elementLocator) {
        return new ChildElementList<XpathLocator>(this.getChild(elementLocator));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.ajocado.locator.IterableLocator#getDescendants
     * (org.jboss.ajocado.locator.IterableLocator)
     */
    public Iterable<XpathLocator> getDescendants(XpathLocator elementLocator) {
        return new ElementOcurrenceList<XpathLocator>(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.ajocado.locator.CompoundableLocator#getChild
     * (org.jboss.ajocado.locator.CompoundableLocator)
     */
    public XpathLocator getChild(XpathLocator elementLocator) {
        return new XpathLocator(SimplifiedFormat.format("{0}/{1}", getRawLocator(), elementLocator.getRawLocator()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.ajocado.locator.CompoundableLocator#getDescendant
     * (org.jboss.ajocado.locator.CompoundableLocator)
     */
    public XpathLocator getDescendant(XpathLocator elementLocator) {
        return new XpathLocator(SimplifiedFormat.format("{0}//{1}", getRawLocator(), elementLocator.getRawLocator()));
    }

}
