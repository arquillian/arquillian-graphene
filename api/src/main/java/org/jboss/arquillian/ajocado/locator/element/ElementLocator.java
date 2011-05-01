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
package org.jboss.arquillian.ajocado.locator.element;

import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.ajocado.locator.Locator;
import org.jboss.arquillian.ajocado.locator.attribute.AttributeLocator;

/**
 * Locator for locating elements on page.
 * 
 * @param <T>
 *            the type of locator which can be derived from this locator
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public interface ElementLocator<T extends ElementLocator<T>> extends Locator<T> {
    /**
     * Returns the locator for attribute for given locator.
     * 
     * @param attribute
     *            type for obtaining from this locator
     * @return the attribute for this locator
     */
    AttributeLocator<T> getAttribute(Attribute attribute);

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.ajocado.locator.Locator#getLocationStrategy()
     */
    ElementLocationStrategy getLocationStrategy();

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.ajocado.locator.Locator#format(java.lang.Object[])
     */
    ElementLocator<T> format(Object... args);
}
