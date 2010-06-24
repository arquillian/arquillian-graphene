package org.jboss.test.selenium;

import static org.jboss.test.selenium.utils.URLUtils.buildUrl;
import static java.lang.System.getProperty;
import static org.jboss.test.selenium.utils.PrimitiveUtils.*;

import java.io.File;
import java.net.URL;

import org.jboss.test.selenium.browser.Browser;

public final class SystemProperties {

    private SystemProperties() {
    }

    public static URL getContextRoot() {
        return buildUrl(getProperty("context.root"));
    }

    public static URL getContextPath() {
        return buildUrl(getContextRoot(), getProperty("context.path"));
    }

    public static Browser getBrowser() {
        return new Browser(getProperty("browser"));
    }

    public static File getMavenResourcesDir() {
        return new File(getProperty("maven.resources.dir"));
    }

    public static File getMavenProjectBuildDirectory() {
        return new File(getProperty("maven.project.build.directory"));
    }

    public static String getSeleniumHost() {
        return getProperty("selenium.host");
    }

    public static int getSeleniumPort() {
        return asInt(getProperty("selenium.port"));
    }

    public static boolean isSeleniumMaximize() {
        return asBoolean(getProperty("selenium.maximize"));
    }

    public static boolean isSeleniumDebug() {
        return asBoolean(getProperty("selenium.debug"));
    }

    public static long getSeleniumTimeout(SeleniumTimeoutType type) {
        return asLong(getProperty("selenium.timeout." + type.toString().toLowerCase()));
    }

    public static enum SeleniumTimeoutType {
        DEFAULT, GUI, AJAX, MODEL;
    }
}
