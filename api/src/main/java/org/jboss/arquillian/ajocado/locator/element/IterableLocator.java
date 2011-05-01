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

import java.util.Iterator;

/**
 * Locator which can iterate over it's descendant.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 * @param <T>
 *            type what we want to iterate over - this type will be returned by method provided by this interface
 */
public interface IterableLocator<T extends IterableLocator<T>> extends ElementLocator<T>, Iterable<T> {

    /**
     * Gets the <i>index</i>-th occurrence of this locator
     * 
     * @param index
     *            the index of occurrence of this locator
     * @return the <i>index</i>-th occurrence of this locator
     */
    T get(int index);

    /**
     * Returns the iterator through the occurrences of this locator
     * 
     * @return the iterator through the occurrences of this locator
     */
    Iterator<T> iterator();

    /**
     * Returns the count of occurrences of this locator
     * 
     * @return the count of occurrences of this locator
     */
    int size();
}
