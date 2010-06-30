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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.EnumSet;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.jboss.test.selenium.browser.Browser;
import org.jboss.test.selenium.browser.BrowserMode;
import org.jboss.test.selenium.browser.BrowserType;
import org.jboss.test.selenium.encapsulated.JavaScript;
import org.jboss.test.selenium.framework.AjaxSelenium;
import org.jboss.test.selenium.framework.AjaxSeleniumImpl;
import org.jboss.test.selenium.framework.AjaxSeleniumProxy;
import org.jboss.test.selenium.locator.type.LocationStrategy;
import org.jboss.test.selenium.waiting.ajax.AjaxWaiting;
import org.jboss.test.selenium.waiting.conditions.AttributeEquals;
import org.jboss.test.selenium.waiting.conditions.AttributePresent;
import org.jboss.test.selenium.waiting.conditions.ElementPresent;
import org.jboss.test.selenium.waiting.conditions.TextEquals;
import org.jboss.test.selenium.waiting.retrievers.AttributeRetriever;
import org.jboss.test.selenium.waiting.retrievers.TextRetriever;
import org.jboss.test.selenium.waiting.selenium.SeleniumWaiting;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import static org.jboss.test.selenium.utils.text.SimplifiedFormat.format;
import static org.jboss.test.selenium.waiting.Wait.*;
import static org.jboss.test.selenium.encapsulated.JavaScript.fromResource;
import static org.jboss.test.selenium.SystemProperties.*;

/**
 * <p>
 * Abstract implementation of TestNG test using RichFaces Selenium
 * </p>
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public abstract class AbstractTestCase {

    private static final int WAIT_GUI_INTERVAL = 100;
    private static final int WAIT_AJAX_INTERVAL = 500;
    private static final int WAIT_MODEL_INTERVAL = 1500;

    protected AjaxSelenium selenium;

    /**
     * Waits for GUI interaction, such as rendering
     */
    protected AjaxWaiting waitGui;
    /**
     * Waits for AJAX interaction with server - not computationally difficult
     */
    protected AjaxWaiting waitAjax;
    /**
     * Waits for computationally difficult requests
     */
    protected SeleniumWaiting waitModel;

    protected ElementPresent elementPresent = ElementPresent.getInstance();
    protected TextEquals textEquals = TextEquals.getInstance();
    protected AttributePresent attributePresent = AttributePresent.getInstance();
    protected AttributeEquals attributeEquals = AttributeEquals.getInstance();

    protected TextRetriever retrieveText = TextRetriever.getInstance();
    protected AttributeRetriever retrieveAttribute = AttributeRetriever.getInstance();

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
     * Introduce some maven build properties
     */
    protected File mavenProjectBuildDirectory; // usually ${project}/target
    protected File mavenResourcesDir; // usually ${project}/target/test-classes
    protected boolean seleniumDebug; // if used specified debug mode of selenium testing
    protected Browser browser;

    @BeforeClass(alwaysRun = true)
    public void initializeParameters() throws MalformedURLException {
        this.seleniumDebug = isSeleniumDebug();
        this.contextRoot = getContextRoot();
        this.contextPath = getContextPath();
        this.mavenResourcesDir = getMavenResourcesDir();
        this.mavenProjectBuildDirectory = getMavenProjectBuildDirectory();
        this.browser = getBrowser();
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
    @BeforeClass(dependsOnMethods = {"initializeParameters", "isTestBrowserEnabled"}, alwaysRun = true)
    public void initializeBrowser() {
        selenium = new AjaxSeleniumImpl(getSeleniumHost(), getSeleniumPort(), browser, contextPath);
        AjaxSeleniumProxy.setCurrentContext(selenium);
        selenium.start();
        loadCustomLocationStrategies();

        if (isSeleniumMaximize()) {
            // focus and maximaze tested window
            selenium.windowFocus();
            selenium.windowMaximize();
        }
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
        selenium.setTimeout(getSeleniumTimeout(SeleniumTimeoutType.DEFAULT));
        waitGui = waitAjax().interval(WAIT_GUI_INTERVAL).timeout(getSeleniumTimeout(SeleniumTimeoutType.GUI));
        waitAjax = waitAjax().interval(WAIT_AJAX_INTERVAL).timeout(getSeleniumTimeout(SeleniumTimeoutType.AJAX));
        waitModel = waitSelenium().interval(WAIT_MODEL_INTERVAL).timeout(getSeleniumTimeout(SeleniumTimeoutType.MODEL));
    }

    /**
     * Initializes page and Selenium's extensions to correctly install before test run.
     */
    @SuppressWarnings("unchecked")
    @BeforeClass(dependsOnMethods = {"initializeBrowser"}, alwaysRun = true)
    public void initializeExtensions() throws IOException {

        List<String> seleniumExtensions =
            IOUtils.readLines(ClassLoader.getSystemResourceAsStream("javascript/selenium-extensions-order.txt"));
        List<String> pageExtensions =
            IOUtils.readLines(ClassLoader.getSystemResourceAsStream("javascript/page-extensions-order.txt"));

        // loads the extensions to the selenium
        selenium.getSeleniumExtensions().requireResources(seleniumExtensions);
        // register the handlers for newly loaded extensions
        selenium.getSeleniumExtensions().registerCustomHandlers();
        // prepares the resources to load into page
        selenium.getPageExtensions().loadFromResources(pageExtensions);
    }

    /**
     * Uses selenium.addLocationStrategy to implement own strategies to locate items in the tested page
     */
    private void loadCustomLocationStrategies() {
        // jQuery location strategy
        JavaScript strategySource = fromResource("javascript/selenium-location-strategies/jquery-location-strategy.js");
        selenium.addLocationStrategy(LocationStrategy.JQUERY, strategySource);
    }

    /**
     * Finalize context after each class run.
     */
    @AfterClass(alwaysRun = true)
    public void finalizeBrowser() {
        if (selenium != null) {
            AjaxSeleniumProxy.setCurrentContext(null);
            selenium.stop();
            selenium = null;
        }
    }

    /**
     * Check whenever the current test is enabled for selected browser (evaluated from testng.xml).
     * 
     * If it is not enabled, skip the particular test.
     */
    @Parameters({"enabled-browsers", "disabled-browsers", "enabled-modes", "disabled-modes"})
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
