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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.EnumSet;

import org.jboss.test.selenium.browser.Browser;
import org.jboss.test.selenium.browser.BrowserMode;
import org.jboss.test.selenium.browser.BrowserType;
import org.jboss.test.selenium.encapsulated.JavaScript;
import org.jboss.test.selenium.framework.AjaxSelenium;
import org.jboss.test.selenium.locator.type.LocationStrategy;
import org.jboss.test.selenium.pagespeed.EventRecorder;
import org.jboss.test.selenium.waiting.SeleniumWaiting;
import org.jboss.test.selenium.waiting.Wait;
import org.jboss.test.selenium.waiting.ajax.AjaxWaiting;
import org.jboss.test.selenium.waiting.conditions.ElementPresent;
import org.jboss.test.selenium.waiting.conditions.TextEquals;
import org.jboss.test.selenium.waiting.retrievers.AttributeRetriever;
import org.jboss.test.selenium.waiting.retrievers.TextRetriever;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import static org.jboss.test.selenium.utils.text.SimplifiedFormat.format;

/**
 * <p>
 * Abstract implementation of TestNG test using RichFaces Selenium
 * </p>
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public abstract class AbstractTestCase {

    private static final int WAIT_MODEL_INTERVAL = 500;
    private static final int WAIT_MODEL_TIMEOUT = 30000;
    private static final int WAIT_GUI_INTERVAL = 100;
    private static final int WAIT_GUI_TIMEOUT = 5000;

    protected AjaxSelenium selenium;

    protected SeleniumWaiting waitModel = Wait.interval(WAIT_MODEL_INTERVAL).timeout(WAIT_MODEL_TIMEOUT);
    protected AjaxWaiting waitGui = Wait.interval(WAIT_GUI_INTERVAL).timeout(WAIT_GUI_TIMEOUT);

    protected ElementPresent elementPresent = ElementPresent.getInstance();
    protected TextEquals textEquals = TextEquals.getInstance();

    protected TextRetriever retrieveText = TextRetriever.getInstance();
    protected AttributeRetriever retrieveAttribute = AttributeRetriever.getInstance();

    /**
     * the event recorder controller
     */
    protected EventRecorder eventRecorder;
    
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

    @BeforeClass
    @Parameters({"context.root", "context.path", "browser", "selenium.debug", "maven.resources.dir",
        "maven.project.build.directory"})
    public void initializeParameters(String contextRoot, String contextPath, String browser, String seleniumDebug,
        String mavenResourcesDir, String mavenProjectBuildDirectory) throws MalformedURLException {
        this.contextRoot = new URL(contextRoot);
        this.contextPath = new URL(this.contextRoot, contextPath);
        this.mavenResourcesDir = new File(mavenResourcesDir);
        this.mavenProjectBuildDirectory = new File(mavenProjectBuildDirectory);
        this.seleniumDebug = Boolean.valueOf(seleniumDebug);
        this.browser = new Browser(browser);
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
    @BeforeClass(dependsOnMethods = {"initializeParameters", "isTestBrowserEnabled" })
    @Parameters({"selenium.host", "selenium.port", "selenium.maximize" })
    public void initializeBrowser(String seleniumHost, String seleniumPort, String seleniumMaximize) {
        selenium = new AjaxSelenium(seleniumHost, Integer.valueOf(seleniumPort), browser, contextPath);
        selenium.start();
        loadCustomLocationStrategies();

        if (Boolean.valueOf(seleniumMaximize)) {
            // focus and maximaze tested window
            selenium.windowFocus();
            selenium.windowMaximize();
        }
    }

    /**
     * initializes event recorder controller
     */
    @BeforeClass(dependsOnMethods = {"initializeParameters", "isTestBrowserEnabled" })
    public void initializeEventRecorder() {
        eventRecorder = new EventRecorder(new File(mavenProjectBuildDirectory, "eventrecorder"));
    }
    
    /**
     * Uses selenium.addLocationStrategy to implement own strategies to locate items in the tested page
     */
    private void loadCustomLocationStrategies() {
        // jQuery location strategy
        JavaScript strategySource = JavaScript
            .fromResource("javascript/selenium-location-strategies/jquery-location-strategy.js");
        selenium.addLocationStrategy(LocationStrategy.JQUERY, strategySource);
    }

    /**
     * Finalize context after each class run.
     */
    @AfterClass
    public void finalizeBrowser() {
        // for browser session reuse needs to be not closed (it will be handled by selenium.stop() automatically)
        // selenium.close();
        selenium.stop();
        selenium = null;
    }

    @Parameters({"enabled-browsers", "disabled-browsers", "enabled-modes", "disabled-modes" })
    @BeforeClass(dependsOnMethods = "initializeParameters")
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
