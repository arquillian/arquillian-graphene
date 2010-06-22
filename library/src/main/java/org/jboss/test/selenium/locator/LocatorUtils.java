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

/**
 * Utility class for locators.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>, <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision$
 */
public final class LocatorUtils {

    /**
     * Instantiates a new locator factory.
     */
    private LocatorUtils() {
    }

    /**
     * Returns the raw form of a locator, i.e. without its type. For instance, it makes "div input" from
     * "jquery=div input".
     * 
     * @param locator
     *            locator of an element
     * @return raw form of a locator without locator type prefix
     */
    public static String getRawLocator(AbstractLocator locator) {
        return locator.getLocator();
    }
}
