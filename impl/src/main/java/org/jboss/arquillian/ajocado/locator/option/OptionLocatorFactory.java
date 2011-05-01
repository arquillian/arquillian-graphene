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
package org.jboss.arquillian.ajocado.locator.option;

/**
 * Utility class simplifying creation of option locators of various types providing it's factories.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public final class OptionLocatorFactory {

    /**
     * Instantiates a new option locator factory.
     */
    private OptionLocatorFactory() {
    }

    /**
     * Locates the select option with specified id.
     * 
     * @param id
     *            the id of select option
     * @return the locator for the select option with given id
     */
    public static OptionIdLocator optionId(String id) {
        return new OptionIdLocator(id);
    }

    /**
     * Locates the select option based on its index (offset from zero).
     * 
     * @param index
     *            the index of select option (offset from zero)
     * @return the locator for the select option with given id
     */
    public static OptionIndexLocator optionIndex(int index) {
        return new OptionIndexLocator(index);
    }

    /**
     * <p>
     * Locates select options based on their labels, i.e. the visible text.
     * </p>
     * 
     * <p>
     * Can be regular expression: "regexp:^[Oo]ther"
     * </p>
     * 
     * @param label
     *            the label for the select options (visible text).
     * @return the locator for the select options matching given label
     */
    public static OptionLabelLocator optionLabel(String label) {
        return new OptionLabelLocator(label);
    }

    /**
     * Locates select options based on their values.
     * 
     * @param value
     *            the value matching select options
     * @return the locator for the select options matching given value
     */
    public static OptionValueLocator optionValue(String value) {
        return new OptionValueLocator(value);
    }
}
