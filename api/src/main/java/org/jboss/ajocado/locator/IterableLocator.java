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
 * Locator which can iterate over it's descendant.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 * @param <T>
 *            type what we want to iterate over - this type will be returned by method provided by this interface
 */
public interface IterableLocator<T extends IterableLocator<T>> extends ElementLocator<T> {

    /**
     * Gets the <i>N</i>-th child element of this locator.
     * 
     * @param index
     *            the index of the child subsequent to this locator
     * @return the <i>N</i>-th child element of this locator
     */
    T getNthChildElement(int index);

    /**
     * Gets the <i>N</i>-th occurence of descendant for this locator's element
     * 
     * @param index
     *            the index of the descendant of this locator
     * @return the <i>N</i>-th occurence of descendant
     */
    T getNthOccurence(int index);

    /**
     * Gets the all children of element given by this locator
     * 
     * @return the all children of element given by this locator
     */
    Iterable<T> getAllChildren();

    /**
     * Gets the children given by locator composed from this locator and given elementLocator.
     * 
     * @param elementLocator
     *            the locator for element for composition with this locator
     * @return the children given by locator composed from this locator and elementLocator
     */
    Iterable<T> getChildren(T elementLocator);

    /**
     * Gets all the descendants for this locator specified by composed locator from this locator and given
     * elementLocator.
     * 
     * @param elementLocator
     *            the element locator, which should be added to this locator to specify this element's descendants
     * @return the descendants of element given by composition of this locator and elementLocator
     */
    Iterable<T> getDescendants(T elementLocator);
}
