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
package org.jboss.arquillian.ajocado.browser;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import org.jboss.arquillian.ajocado.utils.StringUtils;

/**
 * <p>Enumeration of supported browsers.</p>
 * 
 * <p>This enum has direct association to browser modes, see {@link BrowserMode}.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public enum BrowserType {

    /** The Mozilla Firefox browser */
    FIREFOX,

    /** The Internet Explorer browser */
    IEXPLORE,

    /** The Safari browser */
    SAFARI,

    /** The Opera browser */
    OPERA,

    /** The Google Chrome browser */
    GOOGLE_CHROME,

    /** The Konqueror browser */
    KONQUEROR,

    /** Mock browser */
    MOCK;

    /**
     * Parses the type in case-insensitive manner.
     * 
     * @param browserType
     *            the browser type
     * @return the browser type
     * @throws IllegalArgumentException
     *             if the given browserMode isn't supported
     */
    public static BrowserType parseType(String browserType) {
        String upperCased = browserType.toUpperCase();
        return BrowserType.valueOf(upperCased);
    }

    /**
     * <p>
     * Returns set of browser types derived from string enumeration of comma- and/or space-separated representation of
     * browser types ({@link BrowserType#parseType(String)}).
     * </p>
     * 
     * @param browserTypesEnumeration
     *            comma and/or spaces separated string enumeration of string representation of browser types
     * @return the set of browser modes
     * @throws IllegalArgumentException
     *             if one of the given browser modes isn't supported
     */
    public static EnumSet<BrowserType> parseTypes(String browserTypesEnumeration) {
        Set<BrowserType> types = new HashSet<BrowserType>();
        for (String type : StringUtils.split(browserTypesEnumeration, ", ")) {
            if ("*".equals(type)) {
                return EnumSet.allOf(BrowserType.class);
            }
            types.add(parseType(type));
        }
        if (types.isEmpty()) {
            return EnumSet.noneOf(BrowserType.class);
        }
        return EnumSet.copyOf(types);
    }
}