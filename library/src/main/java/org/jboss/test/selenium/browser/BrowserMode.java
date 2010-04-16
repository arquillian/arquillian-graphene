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

import static org.jboss.test.selenium.utils.text.LocatorFormat.format;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

public enum BrowserMode {
    FIREFOX_PROXY(BrowserType.FIREFOX, "firefoxproxy"),
    FIREFOX(BrowserType.FIREFOX, "firefox"),
    CHROME(BrowserType.FIREFOX, "chrome"),
    FIREFOX_CHROME(BrowserType.FIREFOX, "firefoxchrome"),
    FIREFOX2(BrowserType.FIREFOX, "firefox2"),
    FIREFOX3(BrowserType.FIREFOX, "firefox3"),
    IEXPLORE_PROXY(BrowserType.IEXPLORE, "iexploreproxy"),
    SAFARI(BrowserType.SAFARI, "safari"),
    SAFARI_PROXY(BrowserType.SAFARI, "safariproxy"),
    IEXPLORE_HTA(BrowserType.IEXPLORE, "iehta"),
    IEXPLORE(BrowserType.IEXPLORE, "iexplore"),
    OPERA(BrowserType.OPERA, "opera"),
    IEXPLORE_PROXY_INJECTION(BrowserType.IEXPLORE, "piiexplore"),
    FIREFOX_PROXY_INJECTION(BrowserType.FIREFOX, "pifirefox"),
    SAFARI_PROXY_INJECTION(BrowserType.SAFARI, "pisafari"),
    KONQUEROR(BrowserType.KONQUEROR, "konqueror"),
    MOCK(BrowserType.MOCK, "mock"),
    GOOGLE_CHROME(BrowserType.GOOGLE_CHROME, "googlechrome");

    private String mode;
    private BrowserType browserType;

    private BrowserMode(BrowserType browserType, String mode) {
        this.mode = mode;
        this.browserType = browserType;
    }

    public static BrowserMode parseMode(String browserMode) {
        for (BrowserMode value : values()) {
            if (value.mode.equals(browserMode)) {
                return value;
            }
        }
        throw new IllegalArgumentException(format("The browser defined by mode '{0}' wasn't found", browserMode));
    }

    public static EnumSet<BrowserMode> parseModes(String browserModesEnumeration) {
        Set<BrowserMode> modes = new HashSet<BrowserMode>();
        for (String mode : StringUtils.split(browserModesEnumeration, ", ")) {
            if ("*".equals(mode)) {
                return EnumSet.allOf(BrowserMode.class);
            }
            modes.add(parseMode(mode));
        }
        if (modes.isEmpty()) {
            return EnumSet.noneOf(BrowserMode.class);
        }
        return EnumSet.copyOf(modes);
    }
    
    public static EnumSet<BrowserMode> getModesFromTypes(EnumSet<BrowserType> types) {
        Set<BrowserMode> list = new HashSet<BrowserMode>();
        for (BrowserMode mode : values()) {
            if (types.contains(mode.getBrowserType())) {
                list.add(mode);
            }
        }
        return EnumSet.copyOf(list);
    }

    public String getMode() {
        return mode;
    }

    public BrowserType getBrowserType() {
        return browserType;
    }
    
    @Override
    public String toString() {
        return "*" + mode;
    }
}
