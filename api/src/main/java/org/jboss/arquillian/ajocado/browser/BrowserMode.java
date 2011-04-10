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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.arquillian.ajocado.selenium.SeleniumRepresentable;

/**
 * <p>
 * Encapsulates execution mode of browser ran by Selenium.
 * </p>
 * 
 * <p>
 * Enumerates all the browsers supported by Selenium.
 * </p>
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public enum BrowserMode implements SeleniumRepresentable {

    /** FirefoxCustomProfileLauncher */
    FIREFOX_PROXY(BrowserType.FIREFOX, "firefoxproxy"),

    /** FirefoxLauncher */
    FIREFOX(BrowserType.FIREFOX, "firefox"),

    /** FirefoxChromeLauncher */
    CHROME(BrowserType.FIREFOX, "chrome"),

    /** FirefoxChromeLauncher */
    FIREFOX_CHROME(BrowserType.FIREFOX, "firefoxchrome"),

    /** Firefox2Launcher */
    FIREFOX2(BrowserType.FIREFOX, "firefox2"),

    /** Firefox3Launcher */
    FIREFOX3(BrowserType.FIREFOX, "firefox3"),

    /** InternetExplorerCustomProxyLauncher */
    IEXPLORE_PROXY(BrowserType.IEXPLORE, "iexploreproxy"),

    /** SafariLauncher */
    SAFARI(BrowserType.SAFARI, "safari"),

    /** SafariCustomProfileLauncher */
    SAFARI_PROXY(BrowserType.SAFARI, "safariproxy"),

    /** HTABrowserLauncher */
    IEXPLORE_HTA(BrowserType.IEXPLORE, "iehta"),

    /** InternetExplorerLauncher */
    IEXPLORE(BrowserType.IEXPLORE, "iexplore"),

    /** OperaCustomProfileLauncher */
    OPERA(BrowserType.OPERA, "opera"),

    /** ProxyInjectionInternetExplorerCustomProxyLauncher */
    IEXPLORE_PROXY_INJECTION(BrowserType.IEXPLORE, "piiexplore"),

    /** ProxyInjectionFirefoxCustomProfileLauncher */
    FIREFOX_PROXY_INJECTION(BrowserType.FIREFOX, "pifirefox"),

    /** KonquerorLauncher */
    KONQUEROR(BrowserType.KONQUEROR, "konqueror"),

    /** MockBrowserLauncher */
    MOCK(BrowserType.MOCK, "mock"),

    /** GoogleChromeLauncher */
    GOOGLE_CHROME(BrowserType.GOOGLE_CHROME, "googlechrome"),

    /**
     * <p>
     * ProxyInjectionSafariCustomProfileLauncher
     * </p>
     * <p>
     * <b>Deprecated</b> - isn't working yet
     * </p>
     */
    @Deprecated
    SAFARI_PROXY_INJECTION(BrowserType.SAFARI, "pisafari");

    private static final Pattern PATTERN = Pattern.compile("([^, ]+)(?:[, ]+|$)");

    /** The mode. */
    private String mode;

    /** The browser type. */
    private BrowserType type;

    /**
     * Instantiates a new browser mode.
     * 
     * @param browserType
     *            the browser type
     * @param mode
     *            the mode
     */
    private BrowserMode(BrowserType browserType, String mode) {
        this.mode = mode;
        this.type = browserType;
    }

    /**
     * <p>
     * Parses the mode from given string representation.
     * </p>
     * 
     * <p>
     * String representation is derived from string representing mode in Selenium.
     * </p>
     * 
     * <p>
     * E.g.: for "*pifirefox" string you get FIREFOX_PROXY_INJECTION mode
     * </p>
     * 
     * @param browserMode
     *            the browser mode string representation
     * @return the browser mode
     * @throws IllegalArgumentException
     *             if the given browserMode isn't supported
     */
    public static BrowserMode parseMode(String browserMode) {
        for (BrowserMode value : values()) {
            if (value.mode.equals(browserMode)) {
                return value;
            }
        }
        throw new IllegalArgumentException("The browser defined by mode '" + browserMode + "' isn't supported");
    }

    /**
     * <p>
     * Returns set of browser modes derived from string enumeration of comma- and/or space-separated representation of
     * browser modes ({@link BrowserMode#parseMode(String)}).
     * </p>
     * 
     * @param browserModesEnumeration
     *            comma and/or spaces separated string enumeration of string representation of browser modes
     * @return the set of browser modes
     * @throws IllegalArgumentException
     *             if one of the given browser modes isn't supported
     */
    public static EnumSet<BrowserMode> parseModes(String browserModesEnumeration) {
        Set<BrowserMode> modes = new HashSet<BrowserMode>();
        Matcher matcher = PATTERN.matcher(browserModesEnumeration);
        while (matcher.find()) {
            final String mode = matcher.group(1);
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

    /**
     * Gets the set of browser modes, which are associated with browsers given by types (see {@link BrowserType}).
     * 
     * @param types
     *            the set of browser types
     * @return the browsers associated with browser by given set of types
     */
    public static EnumSet<BrowserMode> getModesFromTypes(EnumSet<BrowserType> types) {
        Set<BrowserMode> list = new HashSet<BrowserMode>();
        for (BrowserMode mode : values()) {
            if (types.contains(mode.getType())) {
                list.add(mode);
            }
        }
        return EnumSet.copyOf(list);
    }

    /**
     * Gets string representations of mode.
     * 
     * @return the mode
     */
    public String inSeleniumRepresentation() {
        return "*" + mode;
    }

    /**
     * Gets the browser type.
     * 
     * @return the browser type
     */
    public BrowserType getType() {
        return type;
    }
}
