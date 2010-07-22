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
 * Defines element locators which can be used to derive child and generally descendant elements by given locator by
 * composition by composition.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 * 
 * @param <T>
 *            type what will be used as result of composition
 */
public interface CompoundableLocator<T extends CompoundableLocator<T>> extends ElementLocator<T> {

    /**
     * Gets a child of this locator by composing this locator with given elementLocator.
     * 
     * @param elementLocator
     *            are added to this locator to compose new compount locator
     * @return the composed locator for this locator with added elementLocator
     */
    T getChild(T elementLocator);

    /**
     * Gets a descendant of this locator by composing this locator with given elementLocator.
     * 
     * @param elementLocator
     *            are added to this locator to compose new compound locator
     * @return the composed locator for this locator with added elementLocator
     */
    T getDescendant(T elementLocator);
}
