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

import org.jboss.test.selenium.locator.type.LocationStrategy;

/**
 * <p>
 * A element locator using CSS selectors.
 * </p>
 * 
 * <p>
 * Can be used to compose new locators.
 * </p>
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class CssLocator extends AbstractElementLocator implements CompoundableLocator<CssLocator> {
    /**
     * Initiates element locator by using CSS selectors.
     * 
     * @param cssSelector
     *            <a href="http://www.w3.org/TR/css3-selectors/">CSS selector</a>
     */
    public CssLocator(String cssSelector) {
        super(cssSelector);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.test.selenium.locator.Locator#getLocationStrategy()
     */
    public LocationStrategy getLocationStrategy() {
        return LocationStrategy.CSS;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.jboss.test.selenium.locator.CompoundableLocator#getChild(org.jboss.test.selenium.locator.CompoundableLocator)
     */
    public CssLocator getChild(CssLocator elementLocator) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.test.selenium.locator.CompoundableLocator#getDescendant
     * (org.jboss.test.selenium.locator.CompoundableLocator)
     */
    public CssLocator getDescendant(CssLocator elementLocator) {
        throw new UnsupportedOperationException("not implemented yet");
    }
}
