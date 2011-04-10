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
package org.jboss.arquillian.ajocado.locator;

import org.jboss.arquillian.ajocado.format.SimplifiedFormat;
import org.jboss.arquillian.ajocado.locator.element.AbstractElementLocator;
import org.jboss.arquillian.ajocado.locator.element.ElementLocationStrategy;
import org.jboss.arquillian.ajocado.locator.element.ExtendedLocator;
import org.jboss.arquillian.ajocado.locator.iteration.ChildElementList;
import org.jboss.arquillian.ajocado.locator.iteration.ElementOcurrenceList;

/**
 * Locates the element using <a href="http://www.w3.org/TR/xpath/">XPath expression</a>.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class XPathLocator extends AbstractElementLocator<XPathLocator> implements ExtendedLocator<XPathLocator> {

    /**
     * Instantiates a new xpath locator.
     * 
     * @param xpath
     *            the xpath
     */
    public XPathLocator(String xpath) {
        super(xpath);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.ajocado.locator.Locator#getLocationStrategy()
     */
    public ElementLocationStrategy getLocationStrategy() {
        return ElementLocationStrategy.XPATH;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.ajocado.locator.IterableLocator#getNthChildElement(int)
     */
    public XPathLocator getNthChildElement(int index) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.ajocado.locator.IterableLocator#getNthOccurence(int)
     */
    public XPathLocator getNthOccurence(int index) {
        return new XPathLocator(SimplifiedFormat.format("getLocator[{0}]", index - 1));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.ajocado.locator.IterableLocator#getAllChildren()
     */
    public Iterable<XPathLocator> getAllChildren() {
        return new ChildElementList<XPathLocator>(this.getChild(LocatorFactory.xp("*")));
    }
    
    /*
     * (non-Javadoc)
     * @see org.jboss.test.selenium.locator.IterableLocator#getAllOccurrences()
     */
    public Iterable<XPathLocator> getAllOccurrences() {
        return new ElementOcurrenceList<XPathLocator>(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.ajocado.locator.IterableLocator#getChildren(org.jboss.test.selenium.locator.IterableLocator)
     */
    public Iterable<XPathLocator> getChildren(XPathLocator elementLocator) {
        return new ChildElementList<XPathLocator>(this.getChild(elementLocator));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.ajocado.locator.IterableLocator#getDescendants
     * (org.jboss.arquillian.ajocado.locator.IterableLocator)
     */
    public Iterable<XPathLocator> getDescendants(XPathLocator elementLocator) {
        return new ElementOcurrenceList<XPathLocator>(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.ajocado.locator.CompoundableLocator#getChild
     * (org.jboss.arquillian.ajocado.locator.CompoundableLocator)
     */
    public XPathLocator getChild(XPathLocator elementLocator) {
        return new XPathLocator(SimplifiedFormat.format("{0}/{1}", getRawLocator(), elementLocator.getRawLocator()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.ajocado.locator.CompoundableLocator#getDescendant
     * (org.jboss.arquillian.ajocado.locator.CompoundableLocator)
     */
    public XPathLocator getDescendant(XPathLocator elementLocator) {
        return new XPathLocator(SimplifiedFormat.format("{0}//{1}", getRawLocator(), elementLocator.getRawLocator()));
    }
    
    @Override
    public ExtendedLocator<XPathLocator> format(Object... args) {
        return (ExtendedLocator<XPathLocator>) super.format(args);
    }

}
