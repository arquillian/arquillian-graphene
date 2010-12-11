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
package org.jboss.ajocado;

import static org.jboss.ajocado.utils.URLUtils.buildUrl;
import static java.lang.System.getProperty;
import static org.jboss.ajocado.utils.PrimitiveUtils.*;

import java.io.File;
import java.net.URL;

import org.apache.commons.lang.Validate;
import org.jboss.ajocado.browser.Browser;

/**
 * Exposing of test system properties.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public final class SystemProperties {

    private SystemProperties() {
    }

    /**
     * Returns context root, the root URL for server instance.
     * 
     * @return context root, the root URL for server instance.
     */
    public static URL getContextRoot() {
        String contextRoot = getProperty("context.root");
        Validate.notNull(contextRoot, "context.root system property should be set");
        return buildUrl(contextRoot);
    }

    /**
     * Returns context path, the URL for application context incl. context root ({@link #getContextRoot()}.
     * 
     * @return context path, the URL for application context incl. context root ({@link #getContextRoot()}.
     */
    public static URL getContextPath() {
        String contextPath = getProperty("context.path");
        Validate.notNull(contextPath, "context.path system property should be set");
        return buildUrl(getContextRoot(), contextPath);
    }

    /**
     * Returns current browser implementation used in tests.
     * 
     * @return current browser implementation used in tests.
     */
    public static Browser getBrowser() {
        String browser = getProperty("browser");
        Validate.notNull(browser, "browser system property should be set");
        return new Browser(browser);
    }

    /**
     * Returns current maven resources dir, such as images, XMLs, etc.
     * 
     * @return current maven resources dir, such as images, XMLs, etc.
     */
    public static File getMavenResourcesDir() {
        return new File(getProperty("maven.resources.dir", "./target/test-classes/"));
    }

    /**
     * Returns current maven project build (target) directory.
     * 
     * @return current maven project build (target) directory.
     */
    public static File getMavenProjectBuildDirectory() {
        return new File(getProperty("maven.project.build.directory", "./target/"));
    }

    /**
     * Returns the host of Selenium Server
     * 
     * @return the host of Selenium Server
     */
    public static String getSeleniumHost() {
        String seleniumHost = getProperty("selenium.host", "localhost");
        Validate.notNull(seleniumHost, "selenium.host system property should be set");
        return seleniumHost;
    }

    /**
     * Returns the port for Selenium Server
     * 
     * @return the port for Selenium Server
     */
    public static int getSeleniumPort() {
        String seleniumPort = getProperty("selenium.port");
        Validate.notNull(seleniumPort, "selenium.port system property should be set");
        return asInteger(seleniumPort);
    }

    /**
     * Returns whenever should browser window be maximized after start.
     * 
     * @return whenever should browser window be maximized after start
     */
    public static boolean isSeleniumMaximize() {
        return asBoolean(getProperty("selenium.maximize", "false"));
    }

    /**
     * Returns if Selenium test is in debug mode
     * 
     * @return if Selenium test is in debug mode
     */
    public static boolean isSeleniumDebug() {
        return asBoolean(getProperty("selenium.debug", "false"));
    }

    /**
     * Returns the speed of performing selenium commands
     * 
     * @return the speed of performing selenium commands
     */
    public static int getSeleniumSpeed() {
        return asInteger(getProperty("selenium.speed", "0"));
    }

    /**
     * Returns if the network traffic should be captured during the selenium session.
     * 
     * @return if the network traffic should be captured during the selenium session.
     */
    public static boolean isSeleniumNetworkTrafficEnabled() {
        return asBoolean(getProperty("selenium.network.traffic", "false"));
    }

    /**
     * Returns the predefined timeout for given type.
     * 
     * @param type
     *            the type of timeout ({@link SeleniumTimeoutType})
     * @return the predefined timeout for given type.
     */
    public static long getSeleniumTimeout(SeleniumTimeoutType type) {
        Validate.notNull(type);

        String seleniumTimeout = getProperty("selenium.timeout." + type.toString().toLowerCase());

        if (seleniumTimeout == null) {
            return type.defaultTimeout;
        }

        return asLong(seleniumTimeout);
    }

    /**
     * Type of selenium timeout
     */
    public static enum SeleniumTimeoutType {
        /**
         * Default waiting set in Selenium API
         */
        DEFAULT(30000),

        /**
         * Waiting for GUI operations, such as rendering
         */
        GUI(5000),

        /**
         * Waiting for AJAX operations (computational intensive server operations)
         */
        AJAX(15000),

        /**
         * Waiting for Model change operations (computational intensive server operations)
         */
        MODEL(30000);

        int defaultTimeout;

        private SeleniumTimeoutType(int defaultTimeout) {
            this.defaultTimeout = defaultTimeout;
        }
    }
}
