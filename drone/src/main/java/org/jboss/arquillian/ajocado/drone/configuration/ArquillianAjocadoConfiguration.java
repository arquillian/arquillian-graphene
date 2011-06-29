/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.arquillian.ajocado.drone.configuration;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.jboss.arquillian.ajocado.browser.Browser;
import org.jboss.arquillian.ajocado.framework.AjocadoConfiguration;
import org.jboss.arquillian.config.descriptor.api.ArquillianDescriptor;
import org.jboss.arquillian.drone.configuration.ConfigurationMapper;
import org.jboss.arquillian.drone.spi.DroneConfiguration;

/**
 * Configuration for Arquillian Ajocado. This configuration can be fetched from Arquillian Descriptor and overridden by
 * System properties.
 * 
 * @author <a href="kpiwko@redhat.com>Karel Piwko</a>
 * @see ArquillianDescriptor
 * @see ConfigurationMapper
 * 
 */
public class ArquillianAjocadoConfiguration implements AjocadoConfiguration,
    DroneConfiguration<ArquillianAjocadoConfiguration> {
    // serialVersionUID
    private static final long serialVersionUID = 5560505506114056625L;

    /**
     * A name used to determine configuration from ArquillianDescriptor
     */
    public static final String CONFIGURATION_NAME = "ajocado";

    private URL contextRoot;

    private String contextPath = "";

    private String browser = "*firefox";

    private File resourcesDirectory = new File("target/test-classes");

    private File buildDirectory = new File("target/");

    private String seleniumHost = "localhost";

    private int seleniumPort = 14444;

    private boolean seleniumMaximize = false;

    private boolean seleniumDebug = false;

    private boolean seleniumNetworkTrafficEnabled = false;

    private int seleniumSpeed = 0;

    private long seleniumTimeoutDefault = 30000;

    private long seleniumTimeoutGui = 5000;

    private long seleniumTimeoutAjax = 15000;

    private long seleniumTimeoutModel = 30000;

    /**
     * Creates default Arquillian Ajocado Configuration
     */
    public ArquillianAjocadoConfiguration() {
        initContextRoot();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.selenium.spi.WebTestConfiguration#configure(org.jboss
     * .arquillian.impl.configuration.api.ArquillianDescriptor, java.lang.Class)
     */
    @Override
    public ArquillianAjocadoConfiguration configure(ArquillianDescriptor descriptor,
        Class<? extends Annotation> qualifier) {
        ConfigurationMapper.fromArquillianDescriptor(descriptor, this, qualifier);
        return ConfigurationMapper.fromSystemConfiguration(this, qualifier);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.selenium.spi.WebTestConfiguration#getConfigurationName ()
     */
    @Override
    public String getConfigurationName() {
        return CONFIGURATION_NAME;
    }

    /**
     * @return the contextRoot
     */
    public URL getContextRoot() {
        try {
            if (contextRoot != null && !contextRoot.toString().endsWith("/")) {

                contextRoot = new URL(contextRoot.toString() + "/");
            }
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Unable to convert contextRoot from configuration to URL", e);
        }

        return contextRoot;
    }

    /**
     * @param contextRoot
     *            the contextRoot to set
     */
    public void setContextRoot(URL contextRoot) {
        this.contextRoot = contextRoot;
    }

    /**
     * @return the contextPath
     */
    public URL getContextPath() {
        if (contextPath.startsWith("/")) {
            contextPath = contextPath.substring(1);
        }
        if (!contextPath.endsWith("/")) {
            contextPath = new StringBuilder(contextPath).append("/").toString();
        }

        try {
            return new URL(getContextRoot(), contextPath);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Unable to convert context path from configuration to URL", e);
        }
    }

    /**
     * @param contextPath
     *            the contextPath to set
     */
    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    /**
     * @return the browser
     */
    @Override
    public Browser getBrowser() {
        return new Browser(browser);
    }

    /**
     * @param browser
     *            the browser to set
     */
    public void setBrowser(String browser) {
        this.browser = browser;
    }

    /**
     * @return the resourcesDirectory
     */
    public File getResourcesDirectory() {
        return resourcesDirectory;
    }

    /**
     * @param resourcesDirectory
     *            the resourcesDirectory to set
     */
    public void setResourcesDirectory(File resourcesDirectory) {
        this.resourcesDirectory = resourcesDirectory;
    }

    /**
     * @return the buildDirectory
     */
    public File getBuildDirectory() {
        return buildDirectory;
    }

    /**
     * @param buildDirectory
     *            the buildDirectory to set
     */
    public void setBuildDirectory(File buildDirectory) {
        this.buildDirectory = buildDirectory;
    }

    /**
     * @return the seleniumHost
     */
    @Override
    public String getSeleniumHost() {
        return seleniumHost;
    }

    /**
     * @param seleniumHost
     *            the seleniumHost to set
     */
    public void setSeleniumHost(String seleniumHost) {
        this.seleniumHost = seleniumHost;
    }

    /**
     * @return the seleniumPort
     */
    @Override
    public int getSeleniumPort() {
        return seleniumPort;
    }

    /**
     * @param seleniumPort
     *            the seleniumPort to set
     */
    public void setSeleniumPort(int seleniumPort) {
        this.seleniumPort = seleniumPort;
    }

    /**
     * @return the seleniumMaximize
     */
    @Override
    public boolean isSeleniumMaximize() {
        return seleniumMaximize;
    }

    /**
     * @param seleniumMaximize
     *            the seleniumMaximize to set
     */
    public void setSeleniumMaximize(boolean seleniumMaximize) {
        this.seleniumMaximize = seleniumMaximize;
    }

    /**
     * @return the seleniumSpeed
     */
    @Override
    public int getSeleniumSpeed() {
        return seleniumSpeed;
    }

    /**
     * @param seleniumSpeed
     *            the seleniumSpeed to set
     */
    public void setSeleniumSpeed(int seleniumSpeed) {
        this.seleniumSpeed = seleniumSpeed;
    }

    /**
     * @return the seleniumNetworkTrafficEnabled
     */
    @Override
    public boolean isSeleniumNetworkTrafficEnabled() {
        return seleniumNetworkTrafficEnabled;
    }

    /**
     * @param seleniumNetworkTrafficEnabled
     *            the seleniumNetworkTrafficEnabled to set
     */
    public void setSeleniumNetworkTrafficEnabled(boolean seleniumNetworkTrafficEnabled) {
        this.seleniumNetworkTrafficEnabled = seleniumNetworkTrafficEnabled;
    }

    /**
     * @return the seleniumTimeoutDefault
     */
    public long getSeleniumTimeoutDefault() {
        return seleniumTimeoutDefault;
    }

    /**
     * @param seleniumTimeoutDefault
     *            the seleniumTimeoutDefault to set
     */
    public void setSeleniumTimeoutDefault(long seleniumTimeoutDefault) {
        this.seleniumTimeoutDefault = seleniumTimeoutDefault;
    }

    /**
     * @return the seleniumTimeoutGui
     */
    public long getSeleniumTimeoutGui() {
        return seleniumTimeoutGui;
    }

    /**
     * @param seleniumTimeoutGui
     *            the seleniumTimeoutGui to set
     */
    public void setSeleniumTimeoutGui(long seleniumTimeoutGui) {
        this.seleniumTimeoutGui = seleniumTimeoutGui;
    }

    /**
     * @return the seleniumTimeoutAjax
     */
    public long getSeleniumTimeoutAjax() {
        return seleniumTimeoutAjax;
    }

    /**
     * @param seleniumTimeoutAjax
     *            the seleniumTimeoutAjax to set
     */
    public void setSeleniumTimeoutAjax(long seleniumTimeoutAjax) {
        this.seleniumTimeoutAjax = seleniumTimeoutAjax;
    }

    /**
     * @return the seleniumTimeoutModel
     */
    public long getSeleniumTimeoutModel() {
        return seleniumTimeoutModel;
    }

    /**
     * @param seleniumTimeoutModel
     *            the seleniumTimeoutModel to set
     */
    public void setSeleniumTimeoutModel(long seleniumTimeoutModel) {
        this.seleniumTimeoutModel = seleniumTimeoutModel;
    }

    /**
     * @param seleniumDebug
     *            the seleniumDebug to set
     */
    public void setSeleniumDebug(boolean seleniumDebug) {
        this.seleniumDebug = seleniumDebug;
    }

    /**
     * @return the seleniumDebug
     */
    @Override
    public boolean isSeleniumDebug() {
        return seleniumDebug;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.ajocado.framework.AjocadoConfiguration#getTimeout
     * (org.jboss.arquillian.ajocado.framework.AjocadoConfiguration.TimeoutType)
     */
    @Override
    public long getTimeout(TimeoutType type) {
        switch (type) {
            case DEFAULT:
                return seleniumTimeoutDefault;
            case GUI:
                return seleniumTimeoutGui;
            case AJAX:
                return seleniumTimeoutAjax;
            case MODEL:
                return seleniumTimeoutModel;
        }

        throw new UnsupportedOperationException("Unable to determite wait time for given timout type: " + type);

    }

    private void initContextRoot() {
        try {
            this.contextRoot = new URI("http://localhost:8080").toURL();
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Unable to set default value for contextRoot", e);
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Unable to set default value for contextRoot", e);
        }

    }

}