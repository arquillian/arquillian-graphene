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
package org.jboss.test.selenium.framework;

import org.jboss.test.selenium.geometry.Point;
import org.jboss.test.selenium.locator.AttributeLocator;
import org.jboss.test.selenium.locator.ElementLocator;
import org.jboss.test.selenium.locator.IterableLocator;
import org.jboss.test.selenium.locator.JQueryLocator;
import org.jboss.test.selenium.locator.LocatorUtils;

/**
 * Type-safe selenium wrapper for Selenium API with extension of some useful commands defined in
 * {@link ExtendedSelenium}
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class ExtendedTypedSeleniumImpl extends TypedSeleniumImpl implements ExtendedTypedSelenium {

    protected ExtendedSelenium getExtendedSelenium() {
        if (selenium instanceof ExtendedSelenium) {
            return (ExtendedSelenium) selenium;
        } else {
            throw new UnsupportedOperationException("Assigned Selenium isn't instance of ExtendedSelenium");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.jboss.test.selenium.framework.ExtendedTypedSelenium#getStyle(org.jboss.test.selenium.locator.ElementLocator,
     * java.lang.String)
     */
    public String getStyle(ElementLocator elementLocator, String property) {
        return getExtendedSelenium().getStyle(elementLocator.getAsString(), property);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ExtendedTypedSelenium#scrollIntoView(org.jboss.test.selenium.locator.ElementLocator , boolean)
     */
    public void scrollIntoView(ElementLocator elementLocator, boolean alignToTop) {
        getExtendedSelenium().scrollIntoView(elementLocator.getAsString(), String.valueOf(alignToTop));
    }

    /*
     * (non-Javadoc)
     * 
     * @see ExtendedTypedSelenium#mouseOverAt(org.jboss.test.selenium.locator.ElementLocator ,
     * org.jboss.test.selenium.geometry.Point)
     */
    public void mouseOverAt(ElementLocator elementLocator, Point point) {
        getExtendedSelenium().mouseOverAt(elementLocator.getAsString(), point.getCoords());
    }

    /*
     * (non-Javadoc)
     * 
     * @see ExtendedTypedSelenium#isDisplayed(org.jboss.test.selenium.locator.ElementLocator )
     */
    public boolean isDisplayed(ElementLocator elementLocator) {
        return getExtendedSelenium().isDisplayed(elementLocator.getAsString());
    }

    /*
     * (non-Javadoc)
     * 
     * @see ExtendedTypedSelenium#belongsClass(org.jboss.test.selenium.locator.ElementLocator , java.lang.String)
     */
    public boolean belongsClass(ElementLocator elementLocator, String className) {
        return getExtendedSelenium().belongsClass(elementLocator.getAsString(), className);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ExtendedTypedSelenium#isAttributePresent(org.jboss.test.selenium.locator. AttributeLocator)
     */
    public boolean isAttributePresent(AttributeLocator attributeLocator) {
        final String elementLocator = attributeLocator.getAssociatedElement().getAsString();
        final String attributeName = attributeLocator.getAttribute().getAttributeName();
        return getExtendedSelenium().isAttributePresent(elementLocator, attributeName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see DefaultTypedSelenium#getCount(org.jboss.test.selenium.locator.IterableLocator)
     */
    @Override
    public int getCount(IterableLocator<?> locator) {
        Object reference = (Object) locator;
        if (reference instanceof JQueryLocator) {
            return getExtendedSelenium().getJQueryCount(LocatorUtils.getRawLocator((JQueryLocator) reference))
                .intValue();
        }
        try {
            return super.getCount(locator);
        } catch (UnsupportedOperationException e) {
            throw new UnsupportedOperationException("Only JQuery and XPath locators are supported for counting");
        }
    }
}