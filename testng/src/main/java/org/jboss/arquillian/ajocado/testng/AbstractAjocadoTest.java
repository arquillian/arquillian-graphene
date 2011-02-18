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
package org.jboss.arquillian.ajocado.testng;

import static org.jboss.arquillian.ajocado.encapsulated.JavaScript.fromResource;
import static org.jboss.arquillian.ajocado.utils.SimplifiedFormat.format;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.EnumSet;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.jboss.arquillian.ajocado.browser.Browser;
import org.jboss.arquillian.ajocado.browser.BrowserMode;
import org.jboss.arquillian.ajocado.browser.BrowserType;
import org.jboss.arquillian.ajocado.encapsulated.JavaScript;
import org.jboss.arquillian.ajocado.framework.AjaxSelenium;
import org.jboss.arquillian.ajocado.framework.AjaxSeleniumImpl;
import org.jboss.arquillian.ajocado.framework.AjaxSeleniumContext;
import org.jboss.arquillian.ajocado.framework.AjocadoConfiguration;
import org.jboss.arquillian.ajocado.framework.SystemPropertiesConfiguration;
import org.jboss.arquillian.ajocado.framework.AjocadoConfiguration.TimeoutType;
import org.jboss.arquillian.ajocado.framework.AjocadoConfigurationContext;
import org.jboss.arquillian.ajocado.locator.ElementLocationStrategy;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

/**
 * <p>
 * Abstract implementation of TestNG test using RichFaces Selenium
 * </p>
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public abstract class AbstractAjocadoTest {

    public static final int WAIT_GUI_INTERVAL = 100;
    public static final int WAIT_AJAX_INTERVAL = 500;
    public static final int WAIT_MODEL_INTERVAL = 1500;

    protected AjaxSelenium selenium;
    protected AjocadoConfiguration configuration = new SystemPropertiesConfiguration();

    /**
     * context root can be used to obtaining full URL paths, is set to actual tested application's context root
     */
    protected URL contextRoot;

    /**
     * ContextPath will be used to retrieve pages from right URL. Don't hesitate to use it in cases of building absolute
     * URLs.
     */
    protected URL contextPath;

    /**
     * Introduce some build properties
     */
    protected File buildDirectory;
    protected File resourcesDir;
    protected boolean seleniumDebug; // if used specified debug mode of selenium testing
    protected Browser browser;

    @BeforeClass(alwaysRun = true)
    public void initializeParameters() throws MalformedURLException {
        AjocadoConfigurationContext.set(configuration);
        this.seleniumDebug = configuration.isSeleniumDebug();
        this.contextRoot = configuration.getContextRoot();
        this.contextPath = configuration.getContextPath();
        this.resourcesDir = configuration.getResourcesDirectory();
        this.buildDirectory = configuration.getBuildDirectory();
        this.browser = configuration.getBrowser();
    }

    /**
     * Initializes context before each class run.
     * 
     * Parameters will be obtained from TestNG.
     * 
     * @param contextRoot
     *            server's context root, e.g. http://localhost:8080/
     * @param contextPath
     *            context path to application in context of server's root (e.g. /myapp)
     * @param browser
     *            used browser (e.g. "*firefox", see selenium reference API)
     * @param seleniumPort
     *            specifies on which port should selenium server run
     */
    @BeforeClass(dependsOnMethods = { "initializeParameters", "isTestBrowserEnabled" }, alwaysRun = true)
    public void initializeBrowser() {
        selenium = new AjaxSeleniumImpl(configuration.getSeleniumHost(), configuration.getSeleniumPort(), browser,
            contextPath);
        AjaxSeleniumContext.set(selenium);

        selenium.enableNetworkTrafficCapturing(configuration.isSeleniumNetworkTrafficEnabled());
        selenium.start();

        loadCustomLocationStrategies();

        selenium.setSpeed(configuration.getSeleniumSpeed());

        if (configuration.isSeleniumMaximize()) {
            // focus and maximaze tested window
            selenium.windowFocus();
            selenium.windowMaximize();
        }
    }

    /**
     * Restarts the browser by finalizing current session and initializing new one.
     */
    public void restartBrowser() {
        finalizeBrowser();
        initializeBrowser();
        initializeExtensions();
    }

    /**
     * Initializes the timeouts for waiting on interaction
     * 
     * @param seleniumTimeoutDefault
     *            the timeout set in Selenium API
     * @param seleniumTimeoutGui
     *            initial timeout set for waiting GUI interaction
     * @param seleniumTimeoutAjax
     *            initial timeout set for waiting server AJAX interaction
     * @param seleniumTimeoutModel
     *            initial timeout set for waiting server computationally difficult interaction
     */
    @BeforeClass(alwaysRun = true, dependsOnMethods = "initializeBrowser")
    public void initializeWaitTimeouts() {
        selenium.setTimeout(configuration.getTimeout(TimeoutType.DEFAULT));
    }

    /**
     * Initializes page and Selenium's extensions to correctly install before test run.
     */
    @BeforeClass(dependsOnMethods = { "initializeBrowser" }, alwaysRun = true)
    public void initializeExtensions() {

        List<String> seleniumExtensions = getExtensionsListFromResource("javascript/selenium-extensions-order.txt");
        List<String> pageExtensions = getExtensionsListFromResource("javascript/page-extensions-order.txt");

        // loads the extensions to the selenium
        selenium.getSeleniumExtensions().requireResources(seleniumExtensions);
        // register the handlers for newly loaded extensions
        selenium.getSeleniumExtensions().registerCustomHandlers();
        // prepares the resources to load into page
        selenium.getPageExtensions().loadFromResources(pageExtensions);
    }

    /**
     * Loads the list of resource names from the given resource.
     * 
     * @param resourceName
     *            the path to resource on classpath
     * @return the list of resource names from the given resource.
     */
    @SuppressWarnings("unchecked")
    private List<String> getExtensionsListFromResource(String resourceName) {
        try {
            return IOUtils.readLines(ClassLoader.getSystemResourceAsStream(resourceName));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Uses selenium.addLocationStrategy to implement own strategies to locate items in the tested page
     */
    private void loadCustomLocationStrategies() {
        // jQuery location strategy
        JavaScript strategySource = fromResource("javascript/selenium-location-strategies/jquery-location-strategy.js");
        selenium.addLocationStrategy(ElementLocationStrategy.JQUERY, strategySource);
    }

    /**
     * Finalize context after each class run.
     */
    @AfterClass(alwaysRun = true)
    public void finalizeBrowser() {
        if (selenium != null && selenium.isStarted()) {
            selenium.deleteAllVisibleCookies();
            AjaxSeleniumContext.set(null);
            selenium.stop();
            selenium = null;
        }
    }

    /**
     * Check whenever the current test is enabled for selected browser (evaluated from testng.xml).
     * 
     * If it is not enabled, skip the particular test.
     */
    @Parameters({ "enabled-browsers", "disabled-browsers", "enabled-modes", "disabled-modes" })
    @BeforeClass(dependsOnMethods = "initializeParameters", alwaysRun = true)
    public void isTestBrowserEnabled(@Optional("*") String enabledBrowsersParam,
        @Optional("") String disabledBrowsersParam, @Optional("*") String enabledModesParam,
        @Optional("") String disabledModesParam) {

        EnumSet<BrowserType> enabledBrowserTypes = BrowserType.parseTypes(enabledBrowsersParam);
        EnumSet<BrowserType> disabledBrowserTypes = BrowserType.parseTypes(disabledBrowsersParam);
        EnumSet<BrowserMode> enabledBrowserModes = BrowserMode.parseModes(enabledModesParam);
        EnumSet<BrowserMode> disabledBrowserModes = BrowserMode.parseModes(disabledModesParam);

        enabledBrowserTypes.removeAll(disabledBrowserTypes);
        enabledBrowserModes.addAll(BrowserMode.getModesFromTypes(enabledBrowserTypes));
        enabledBrowserModes.removeAll(disabledBrowserModes);

        if (!enabledBrowserModes.contains(browser.getMode())) {
            throw new SkipException(format("This test isn't supported in {0}", browser));
        }
    }
}
