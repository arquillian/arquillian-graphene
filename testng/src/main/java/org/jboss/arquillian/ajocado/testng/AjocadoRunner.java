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
package org.jboss.arquillian.ajocado.testng;

import static org.jboss.arquillian.ajocado.javascript.JavaScript.fromResource;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.jboss.arquillian.ajocado.framework.AjaxSelenium;
import org.jboss.arquillian.ajocado.framework.AjaxSeleniumContext;
import org.jboss.arquillian.ajocado.framework.AjaxSeleniumImpl;
import org.jboss.arquillian.ajocado.framework.AjocadoConfiguration;
import org.jboss.arquillian.ajocado.framework.AjocadoConfiguration.TimeoutType;
import org.jboss.arquillian.ajocado.framework.AjocadoConfigurationContext;
import org.jboss.arquillian.ajocado.framework.SystemPropertiesConfiguration;
import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.locator.element.ElementLocationStrategy;
import org.jboss.arquillian.ajocado.testng.listener.AbstractConfigurationListener;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class AjocadoRunner extends AbstractConfigurationListener {

    private static AjaxSelenium selenium = AjaxSeleniumContext.getProxy();
    private static ThreadLocal<Boolean> seleniumInitializedByMe = new BooleanThreadLocal();
    private static AjocadoConfiguration configuration = AjocadoConfigurationContext.getProxy();
    private static ThreadLocal<Boolean> configurationInitializedByMe = new BooleanThreadLocal();

    @BeforeClass(alwaysRun = true)
    public void initializeConfiguration() throws MalformedURLException {
        if (!AjocadoConfigurationContext.isInitialized()) {
            AjocadoConfiguration newConfiguration = new SystemPropertiesConfiguration();
            AjocadoConfigurationContext.set(newConfiguration);
            configurationInitializedByMe.set(true);
        }
    }

    @AfterClass(alwaysRun = true)
    public void finalizeConfiguration() {
        if (configurationInitializedByMe.get()) {
            AjocadoConfigurationContext.set(null);
            configurationInitializedByMe.set(false);
        }
    }

    @BeforeClass(dependsOnMethods = "initializeConfiguration", alwaysRun = true)
    public void initializeSelenium() {
        if (!AjaxSeleniumContext.isInitialized()) {
            AjaxSelenium newSelenium = new AjaxSeleniumImpl(configuration.getSeleniumHost(),
                configuration.getSeleniumPort(), configuration.getBrowser(), configuration.getContextPath());
            AjaxSeleniumContext.set(newSelenium);
            seleniumInitializedByMe.set(true);
        }
    }

    @AfterClass(alwaysRun = true)
    public void finalizeSelenium() {
        if (seleniumInitializedByMe.get()) {
            AjaxSeleniumContext.set(null);
            seleniumInitializedByMe.set(false);
        }
    }

    @BeforeClass(alwaysRun = true, dependsOnMethods = "initializeSelenium")
    public void injectContext(ITestContext context) {
        for (ITestNGMethod testNGMethod : context.getAllTestMethods()) {
            Object[] testInstances = testNGMethod.getInstances();

            for (Object testInstance : testInstances) {
                for (Field field : getAllSuperDeclaredFields(testInstance)) {
                    tryInjectValue(testInstance, field, AjaxSelenium.class, selenium);
                    tryInjectValue(testInstance, field, AjocadoConfiguration.class, configuration);
                }
            }
        }
    }

    /**
     * Initializes context before each class run.
     * 
     * Parameters will be obtained from TestNG.
     */
    @BeforeClass(alwaysRun = true, dependsOnMethods = "initializeSelenium")
    public void initializeBrowser() {
        selenium.enableNetworkTrafficCapturing(configuration.isSeleniumNetworkTrafficEnabled());
        selenium.start();

        selenium.setSpeed(configuration.getSeleniumSpeed());

        if (configuration.isSeleniumMaximize()) {
            selenium.windowFocus();
            selenium.windowMaximize();
        }
    }

    public static void restartBrowser() {
        AjocadoRunner runner = new AjocadoRunner();
        runner.finalizeBrowser();
        runner.initializeBrowser();
        runner.initializeExtensions();
    }

    /**
     * Finalize context after each class run.
     */
    @AfterClass(alwaysRun = true)
    public void finalizeBrowser() {
        if (selenium.isStarted()) {
            selenium.deleteAllVisibleCookies();
            selenium.stop();
        }
    }

    /**
     * Uses selenium.addLocationStrategy to implement own strategies to locate items in the tested page
     */
    @BeforeClass(dependsOnMethods = "initializeBrowser", alwaysRun = true)
    public void loadCustomLocationStrategies() {
        // jQuery location strategy
        JavaScript strategySource = fromResource("javascript/selenium-location-strategies/jquery-location-strategy.js");
        selenium.addLocationStrategy(ElementLocationStrategy.JQUERY, strategySource);
    }

    /**
     * Initializes the timeouts for waiting on interaction
     */
    @BeforeClass(dependsOnMethods = "initializeBrowser", alwaysRun = true)
    public void initializeWaitTimeouts() {
        selenium.setTimeout(configuration.getTimeout(TimeoutType.DEFAULT));
    }

    /**
     * Initializes page and Selenium's extensions to correctly install before test run.
     */
    @BeforeClass(dependsOnMethods = "initializeBrowser", alwaysRun = true)
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
    private static List<String> getExtensionsListFromResource(String resourceName) {
        try {
            return IOUtils.readLines(ClassLoader.getSystemResourceAsStream(resourceName));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static class BooleanThreadLocal extends ThreadLocal<Boolean> {
        protected Boolean initialValue() {
            return false;
        }
    }

    private static <T> void tryInjectValue(Object testInstance, Field injectionField, Class<T> injectionType,
        T injectedValue) {
        try {
            if (injectionField.getType().isAssignableFrom(injectionType)) {
                final boolean accessible = injectionField.isAccessible();
                if (!accessible) {
                    injectionField.setAccessible(true);
                }
                Object currentValue = injectionField.get(testInstance);
                if (currentValue == null) {
                    injectionField.set(testInstance, injectedValue);
                }
                if (!accessible) {
                    injectionField.setAccessible(false);
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException("Can't proceed injection of " + injectionType + " to " + injectionField, e);
        }
    }

    private static List<Field> getAllSuperDeclaredFields(Object object) {
        List<Field> fields = new LinkedList<Field>();
        Class<?> classT = object.getClass();
        while (classT != null) {
            fields.addAll(Arrays.asList(classT.getDeclaredFields()));
            classT = classT.getSuperclass();
        }
        return fields;
    }
}
