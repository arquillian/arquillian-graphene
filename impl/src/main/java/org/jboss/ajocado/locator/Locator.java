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


/**
 * Locates the object by given strategy and parameters.
 * 
 * @param <T>
 *            the type of locator which can be derived from this locator
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public interface Locator<T extends Locator<T>> {
    /**
     * Returns the location strategy for this element
     * 
     * @return the location strategy for this element
     */
    LocationStrategy getLocationStrategy();

    /**
     * Returns the locator represented as string used in Selenium to locate elements.
     * 
     * @return the locator represented as string used in Selenium to locate elements.
     */
    String getAsString();

    /**
     * Returns the raw locator (without the prefix defining location strategy) representation.
     * 
     * @return the raw locator (without the prefix defining location strategy) representation.
     */
    String getRawLocator();

    T format(Object... args);
}
