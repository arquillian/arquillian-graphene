package org.jboss.arquillian.ajocado.framework;

import java.io.File;
import java.net.URL;

import org.jboss.arquillian.ajocado.browser.Browser;

public interface AjocadoConfiguration {

    /**
     * Returns context root, the root URL for server instance.
     * 
     * @return context root, the root URL for server instance.
     */
    URL getContextRoot();

    /**
     * Returns context path, the URL for application context incl. context root ({@link #getContextRoot()}.
     * 
     * @return context path, the URL for application context incl. context root ({@link #getContextRoot()}.
     */
    URL getContextPath();

    /**
     * Returns current browser implementation used in tests.
     * 
     * @return current browser implementation used in tests.
     */
    Browser getBrowser();

    /**
     * Returns current maven resources dir, such as images, XMLs, etc.
     * 
     * @return current maven resources dir, such as images, XMLs, etc.
     */
    File getMavenResourcesDir();

    /**
     * Returns current maven project build (target) directory.
     * 
     * @return current maven project build (target) directory.
     */
    File getMavenProjectBuildDirectory();

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
        
        public int getDefaultTimeout() {
            return defaultTimeout;
        }
    }
}