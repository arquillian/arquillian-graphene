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
package org.jboss.test.selenium.browser;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

public enum BrowserType {
    FIREFOX,
    IEXPLORE,
    SAFARI,
    OPERA,
    GOOGLE_CHROME,
    KONQUEROR, MOCK;

    public static BrowserType parseType(String type) {
        type = type.toUpperCase();
        return BrowserType.valueOf(type);
    }

    public static EnumSet<BrowserType> parseTypes(String browserTypesEnumeration) {
        Set<BrowserType> types = new HashSet<BrowserType>();
        for (String type : StringUtils.split(browserTypesEnumeration, ", ")) {
            if ("*".equals(type)) {
                return EnumSet.allOf(BrowserType.class);
            }
            types.add(parseType(type));
        }
        return EnumSet.copyOf(types);
    }
}