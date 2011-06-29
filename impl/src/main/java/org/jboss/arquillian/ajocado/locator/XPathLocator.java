/**
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
import org.jboss.arquillian.ajocado.locator.element.AbstractIterableLocator;
import org.jboss.arquillian.ajocado.locator.element.ElementLocationStrategy;
import org.jboss.arquillian.ajocado.locator.element.ExtendedLocator;

/**
 * Locates the element using <a href="http://www.w3.org/TR/xpath/">XPath expression</a>.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class XPathLocator extends AbstractIterableLocator<XPathLocator> implements ExtendedLocator<XPathLocator> {

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
    @Override
    public ElementLocationStrategy getLocationStrategy() {
        return ElementLocationStrategy.XPATH;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.ajocado.locator.IterableLocator#getNthOccurence(int)
     */
    @Override
    public XPathLocator get(int index) {
        return new XPathLocator(SimplifiedFormat.format("getLocator[{0}]", index - 1));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.ajocado.locator.CompoundableLocator#getChild
     * (org.jboss.arquillian.ajocado.locator.CompoundableLocator)
     */
    @Override
    public XPathLocator getChild(XPathLocator elementLocator) {
        return new XPathLocator(SimplifiedFormat.format("{0}/{1}", getRawLocator(), elementLocator.getRawLocator()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.ajocado.locator.CompoundableLocator#getDescendant
     * (org.jboss.arquillian.ajocado.locator.CompoundableLocator)
     */
    @Override
    public XPathLocator getDescendant(XPathLocator elementLocator) {
        return new XPathLocator(SimplifiedFormat.format("{0}//{1}", getRawLocator(), elementLocator.getRawLocator()));
    }

    @Override
    public ExtendedLocator<XPathLocator> format(Object... args) {
        return (ExtendedLocator<XPathLocator>) super.format(args);
    }

}
