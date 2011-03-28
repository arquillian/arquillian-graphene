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
package org.jboss.arquillian.ajocado.framework;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static java.lang.System.getProperty;
import static org.jboss.arquillian.ajocado.utils.PrimitiveUtils.*;

import java.io.File;
import java.net.URL;

import org.apache.commons.lang.Validate;
import org.jboss.arquillian.ajocado.browser.Browser;

/**
 * Exposing of test system properties.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class SystemPropertiesConfiguration implements AjocadoConfiguration {
    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.ajocado.AjocadoConfiguration#getContextRoot()
     */
    @Override
    public URL getContextRoot() {
        String contextRoot = getProperty("context.root");
        Validate.notNull(contextRoot, "context.root system property should be set");
        return buildUrl(contextRoot);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.ajocado.AjocadoConfiguration#getContextPath()
     */
    @Override
    public URL getContextPath() {
        String contextPath = getProperty("context.path");
        Validate.notNull(contextPath, "context.path system property should be set");
        return buildUrl(getContextRoot(), contextPath);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.ajocado.AjocadoConfiguration#getBrowser()
     */
    @Override
    public Browser getBrowser() {
        String browser = getProperty("browser");
        Validate.notNull(browser, "browser system property should be set");
        return new Browser(browser);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.ajocado.AjocadoConfiguration#getMavenResourcesDir()
     */
    @Override
    public File getResourcesDirectory() {
        return new File(getProperty("maven.resources.dir", "./target/test-classes/"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.ajocado.AjocadoConfiguration#getMavenProjectBuildDirectory()
     */
    @Override
    public File getBuildDirectory() {
        return new File(getProperty("maven.project.build.directory", "./target/"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.ajocado.AjocadoConfiguration#getSeleniumHost()
     */
    @Override
    public String getSeleniumHost() {
        String seleniumHost = getProperty("selenium.host", "localhost");
        Validate.notNull(seleniumHost, "selenium.host system property should be set");
        return seleniumHost;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.ajocado.AjocadoConfiguration#getSeleniumPort()
     */
    @Override
    public int getSeleniumPort() {
        String seleniumPort = getProperty("selenium.port");
        Validate.notNull(seleniumPort, "selenium.port system property should be set");
        return asInteger(seleniumPort);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.ajocado.AjocadoConfiguration#isSeleniumMaximize()
     */
    @Override
    public boolean isSeleniumMaximize() {
        return asBoolean(getProperty("selenium.maximize", "false"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.ajocado.AjocadoConfiguration#isSeleniumDebug()
     */
    @Override
    public boolean isSeleniumDebug() {
        return asBoolean(getProperty("selenium.debug", "false"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.ajocado.AjocadoConfiguration#getSeleniumSpeed()
     */
    @Override
    public int getSeleniumSpeed() {
        return asInteger(getProperty("selenium.speed", "0"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.ajocado.AjocadoConfiguration#isSeleniumNetworkTrafficEnabled()
     */
    @Override
    public boolean isSeleniumNetworkTrafficEnabled() {
        return asBoolean(getProperty("selenium.network.traffic", "false"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.ajocado.AjocadoConfiguration#getSeleniumTimeout(org.jboss.arquillian.ajocado.
     * AjocadoConfigurationImpl.SeleniumTimeoutType)
     */
    @Override
    public long getTimeout(TimeoutType type) {
        Validate.notNull(type);

        String seleniumTimeout = getProperty("selenium.timeout." + type.toString().toLowerCase());

        if (seleniumTimeout == null) {
            return type.getDefaultTimeout();
        }

        return asLong(seleniumTimeout);
    }
}
