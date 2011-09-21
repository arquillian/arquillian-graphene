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
package org.jboss.arquillian.ajocado.framework;

import org.jboss.arquillian.ajocado.css.CssResolver;
import org.jboss.arquillian.ajocado.geometry.Point;
import org.jboss.arquillian.ajocado.locator.attribute.AttributeLocator;
import org.jboss.arquillian.ajocado.locator.element.ElementLocator;
import org.jboss.arquillian.ajocado.locator.element.IterableLocator;

/**
 * <p>
 * Extends the common Selenium API by other useful functions.
 * </p>
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public interface ExtendedTypedSelenium extends TypedSelenium {

    /**
     * Indicates if selenium session was already successfully started.
     *
     * @return true if selenium session was already successfully started; false otherwise
     */
    boolean isStarted();

    /**
     * Get current style value of element given by locator.
     *
     * @param <R>
     *            the returning type (which was resolved by given resolver)
     *
     * @param <T>
     *            the type of resolver {@link CssResolver}
     *
     * @param elementLocator
     *            of element from what we want to get current style value
     * @param cssResolver
     *            CSS style property resolver, see {@link CssResolver}
     * @return current value of property if its element exists and has this property value set; null value otherwise
     * @throws IllegalStateException
     *             if is caught unrecognized throwable
     */
    <R, T extends CssResolver<R>> R getStyle(ElementLocator<?> elementLocator, T cssResolver);

    /**
     * Aligns screen to top (resp. bottom) of element given by locator.
     *
     * @param elementLocator
     *            of element which should be screen aligned to
     * @param alignToTop
     *            should be top border of screen aligned to top border of element
     */
    void scrollIntoView(ElementLocator<?> elementLocator, boolean alignToTop);

    /**
     * Simulates a user hovering a mouse over the specified element at specific coordinates relative to element.
     *
     * @param elementLocator
     *            element's locator
     * @param point
     *            specifies the x,y position (i.e. - 10,20) of the mouse event relative to the element returned by the
     *            locator.
     */
    void mouseOverAt(ElementLocator<?> elementLocator, Point point);

    /**
     * Simulates a user hovering a mouse out of the specified element at specific coordinates relative to element.
     *
     * @param elementLocator
     *            element's locator
     * @param point
     *            specifies the x,y position (i.e. - 10,20) of the mouse event relative to the element returned by the
     *            locator.
     */
    void mouseOutAt(ElementLocator<?> elementLocator, Point point);

    /**
     * Checks if element given by locator is member of CSS class given by className.
     *
     * @param className
     *            name of CSS class
     * @param elementLocator
     *            element's locator
     * @return true if element given by locator is member of CSS class given by className
     */
    boolean belongsClass(ElementLocator<?> elementLocator, String className);

    /**
     * Verifies that the specified attribute is defined for the element.
     *
     * @param attributeLocator
     *            an attribute locator
     * @return true if the element's attribute is present, false otherwise
     */
    boolean isAttributePresent(AttributeLocator<?> attributeLocator);

    /*
     * (non-Javadoc)
     *
     * @see org.jboss.arquillian.ajocado.framework.DefaultTypedSelenium
     * #getCount(org.jboss.test.selenium.locator.IterableLocator )
     */
    @Override
    int getCount(IterableLocator<?> locator);

    /**
     * <p>
     * Check a toggle-button (checkbox/radio) on or off.
     * </p>
     *
     * @param locator
     *            an element locator
     * @param checked
     *            if true, toggle-button will be checked on, otherwise checked off
     */
    void check(ElementLocator<?> locator, boolean checked);

    /**
     * Invokes selenium command.
     *
     * @param command
     *            the command name from Selenium's JavaScript API
     * @param param1
     *            first parameter
     * @param param2
     *            second parameter
     */
    void doCommand(String command, String param1, String param2);
}