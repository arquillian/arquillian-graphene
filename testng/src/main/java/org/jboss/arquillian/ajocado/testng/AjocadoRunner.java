package org.jboss.arquillian.ajocado.testng;

import static org.jboss.arquillian.ajocado.encapsulated.JavaScript.fromResource;
import static org.jboss.arquillian.ajocado.utils.SimplifiedFormat.format;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.jboss.arquillian.ajocado.browser.BrowserMode;
import org.jboss.arquillian.ajocado.browser.BrowserType;
import org.jboss.arquillian.ajocado.encapsulated.JavaScript;
import org.jboss.arquillian.ajocado.framework.AjaxSelenium;
import org.jboss.arquillian.ajocado.framework.AjaxSeleniumContext;
import org.jboss.arquillian.ajocado.framework.AjaxSeleniumImpl;
import org.jboss.arquillian.ajocado.framework.AjocadoConfiguration;
import org.jboss.arquillian.ajocado.framework.AjocadoConfiguration.TimeoutType;
import org.jboss.arquillian.ajocado.framework.AjocadoConfigurationContext;
import org.jboss.arquillian.ajocado.framework.SystemPropertiesConfiguration;
import org.jboss.arquillian.ajocado.locator.ElementLocationStrategy;
import org.jboss.arquillian.ajocado.testng.listener.AbstractConfigurationListener;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

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

        if (!enabledBrowserModes.contains(configuration.getBrowser().getMode())) {
            throw new SkipException(format("This test isn't supported in {0}", configuration.getBrowser()));
        }
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
