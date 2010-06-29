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
package org.jboss.test.selenium;

import static org.jboss.test.selenium.utils.URLUtils.buildUrl;
import static java.lang.System.getProperty;
import static org.jboss.test.selenium.utils.PrimitiveUtils.*;

import java.io.File;
import java.net.URL;

import org.jboss.test.selenium.browser.Browser;

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
     * @return context root, the root URL for server instance.
     */
    public static URL getContextRoot() {
        return buildUrl(getProperty("context.root"));
    }

    /**
     * Returns context path, the URL for application context incl. context root ({@link #getContextRoot()}.
     * @return context path, the URL for application context incl. context root ({@link #getContextRoot()}.
     */
    public static URL getContextPath() {
        return buildUrl(getContextRoot(), getProperty("context.path"));
    }

    /**
     * Returns current browser implementation used in tests.
     * @return current browser implementation used in tests.
     */
    public static Browser getBrowser() {
        return new Browser(getProperty("browser"));
    }

    /**
     * Returns current maven resources dir, such as images, XMLs, etc.
     * @return current maven resources dir, such as images, XMLs, etc.
     */
    public static File getMavenResourcesDir() {
        return new File(getProperty("maven.resources.dir"));
    }

    /**
     * Returns current maven project build (target) directory.
     * @return current maven project build (target) directory.
     */
    public static File getMavenProjectBuildDirectory() {
        return new File(getProperty("maven.project.build.directory"));
    }

    /**
     * Returns the host of Selenium Server
     * @return the host of Selenium Server
     */
    public static String getSeleniumHost() {
        return getProperty("selenium.host");
    }

    /**
     * Returns the port for Selenium Server
     * @return the port for Selenium Server
     */
    public static int getSeleniumPort() {
        return asInt(getProperty("selenium.port"));
    }

    /**
     * Returns whenever should browser window be maximized after start.
     * @return whenever should browser window be maximized after start
     */
    public static boolean isSeleniumMaximize() {
        return asBoolean(getProperty("selenium.maximize"));
    }

    /**
     * Returns if Selenium test is in debug mode
     * @return if Selenium test is in debug mode
     */
    public static boolean isSeleniumDebug() {
        return asBoolean(getProperty("selenium.debug"));
    }

    /**
     * Returns the predefined timeout for given type.
     * @param type the type of timeout ({@link SeleniumTimeoutType})
     * @return the predefined timeout for given type.
     */
    public static long getSeleniumTimeout(SeleniumTimeoutType type) {
        return asLong(getProperty("selenium.timeout." + type.toString().toLowerCase()));
    }

    /**
     * Type of selenium timeout
     */
    public static enum SeleniumTimeoutType {
        /**
         * Default waiting set in Selenium API
         */
        DEFAULT,
        
        /**
         * Waiting for GUI operations, such as rendering
         */
        GUI,
        
        /**
         * Waiting for AJAX operations (computational intensive server operations)
         */
        AJAX,
        
        /**
         * Waiting for Model change operations (computational intensive server operations)
         */
        MODEL;
    }
}
