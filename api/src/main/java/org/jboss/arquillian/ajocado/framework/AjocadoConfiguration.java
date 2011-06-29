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

import java.io.Serializable;

import org.jboss.arquillian.ajocado.browser.Browser;

/**
 * The runtime configuration of Ajocado test.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public interface AjocadoConfiguration extends Cloneable, Serializable {

    /**
     * Returns current browser implementation used in tests.
     * 
     * @return current browser implementation used in tests.
     */
    Browser getBrowser();

    /**
     * Returns the host of Selenium Server
     * 
     * @return the host of Selenium Server
     */
    String getSeleniumHost();

    /**
     * Returns the port for Selenium Server
     * 
     * @return the port for Selenium Server
     */
    int getSeleniumPort();

    /**
     * Returns whenever should browser window be maximized after start.
     * 
     * @return whenever should browser window be maximized after start
     */
    boolean isSeleniumMaximize();

    /**
     * Returns if Selenium test is in debug mode
     * 
     * @return if Selenium test is in debug mode
     */
    boolean isSeleniumDebug();

    /**
     * Returns the speed of performing selenium commands
     * 
     * @return the speed of performing selenium commands
     */
    int getSeleniumSpeed();

    /**
     * Returns if the network traffic should be captured during the selenium session.
     * 
     * @return if the network traffic should be captured during the selenium session.
     */
    boolean isSeleniumNetworkTrafficEnabled();

    /**
     * Returns the predefined timeout for given type.
     * 
     * @param type
     *            the type of timeout ({@link TimeoutType})
     * @return the predefined timeout for given type.
     */
    long getTimeout(TimeoutType type);

    /**
     * Type of selenium timeout
     */
    public static enum TimeoutType {
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

        private int defaultTimeout;

        private TimeoutType(int defaultTimeout) {
            this.defaultTimeout = defaultTimeout;
        }

        /**
         * Returns the value of default timeout for given type
         * 
         * @return the value of default timeout for given type
         */
        public int getDefaultTimeout() {
            return defaultTimeout;
        }
    }
}