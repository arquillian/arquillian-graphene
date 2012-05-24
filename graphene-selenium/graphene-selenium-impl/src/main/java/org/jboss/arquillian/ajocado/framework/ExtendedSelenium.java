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

import org.apache.commons.lang.Validate;
import org.jboss.arquillian.ajocado.format.SimplifiedFormat;

import com.thoughtworks.selenium.CommandProcessor;
import com.thoughtworks.selenium.DefaultSelenium;

/**
 * Selenium API extended by useful commands.
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>, <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision$
 */
public class ExtendedSelenium extends DefaultSelenium {

    public ExtendedSelenium(CommandProcessor commandProcessor) {
        super(commandProcessor);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.thoughtworks.selenium.DefaultSelenium#open(java.lang.String)
     *
     * Workaround for http://code.google.com/p/selenium/issues/detail?id=408
     */
    @Override
    public void open(String url) {
        commandProcessor.doCommand("open", new String[] { url, "true" });
    }

    /**
     * Get current style value of element given by locator.
     *
     * Use CSS style notation instead of JavaScript's camel notation!
     *
     * This methods of getting current style value haven't absolute browser compatibility.
     *
     * E.g.: use property "background-color" instead of "backgroundColor"
     *
     * @param locator of element from what we want to get current style value
     * @param property CSS style property what we can to recognize
     * @return current value of property if its element exists and has this property value set; null value otherwise
     * @throws IllegalStateException if is caught unrecognized throwable
     */
    public String getStyle(String locator, String property) {
        return commandProcessor.getString("getStyle", new String[] { locator, property });
    }

    /**
     * Aligns screen to top (resp. bottom) of element given by locator.
     *
     * @param locator of element which should be screen aligned to
     * @param alignToTop should be top border of screen aligned to top border of element
     */
    public void scrollIntoView(String locator, String alignToTop) {
        commandProcessor.doCommand("scrollIntoView", new String[] { locator, alignToTop });
    }

    /**
     * Simulates a user hovering a mouse over the specified element at specific coordinates relative to element.
     *
     * @param locator element's locator
     * @param coordString specifies the x,y position (i.e. - 10,20) of the mouse event relative to the element returned by the
     *        locator.
     */
    public void mouseOverAt(String locator, String coordString) {
        commandProcessor.doCommand("mouseOverAt", new String[] { locator, coordString });
    }

    /**
     * Simulates a user hovering a mouse out of the specified element at specific coordinates relative to element.
     *
     * @param locator element's locator
     * @param coordString specifies the x,y position (i.e. - 10,20) of the mouse event relative to the element returned by the
     *        locator.
     */
    public void mouseOutAt(String locator, String coordString) {
        commandProcessor.doCommand("mouseOutAt", new String[] { locator, coordString });
    }

    /**
     * Checks if element given by locator is member of CSS class given by className.
     *
     * @param className name of CSS class
     * @param locator element's locator
     * @return true if element given by locator is member of CSS class given by className
     */
    public boolean belongsClass(String locator, String className) {
        Validate.notNull(className);
        Validate.notNull(locator);

        String classLocator = SimplifiedFormat.format("{0}@class", locator);
        String classNames = getAttribute(classLocator);

        String regex = SimplifiedFormat.format("(?:^|.*\\s){0}(?:$|\\s.*)", className);
        return classNames.matches(regex);
    }

    /**
     * Verifies that the specified attribute is defined for the element.
     *
     * @param elementLocator an element locator
     * @param attributeName a name of an attribute
     * @return true if the element's attribute is present, false otherwise
     */
    public boolean isAttributePresent(String elementLocator, String attributeName) {
        return commandProcessor.getBoolean("isAttributePresent", new String[] { elementLocator, attributeName });
    }

    /**
     * Returns the number of elements with given jQuery locator.
     *
     * @param jqueryLocator jQuery locator of an element
     * @return number of found elements
     */
    public Number getJQueryCount(String jqueryLocator) {
        return commandProcessor.getNumber("getJQueryCount", new String[] { jqueryLocator });
    }

    public void doCommand(String command, String param1, String param2) {
        String[] array = new String[] { param1, param2 };
        if (param2 == null) {
            array = new String[] { param1 };
        }
        commandProcessor.doCommand(command, array);
    }
}
