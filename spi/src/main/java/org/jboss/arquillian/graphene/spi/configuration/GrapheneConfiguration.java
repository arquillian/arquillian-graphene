/**
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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
package org.jboss.arquillian.graphene.spi.configuration;

import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URL;

import org.jboss.arquillian.config.descriptor.api.ArquillianDescriptor;
import org.jboss.arquillian.drone.configuration.ConfigurationMapper;
import org.jboss.arquillian.drone.spi.DroneConfiguration;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

/**
 * <p>
 * Graphene configuration can be defined in arquillian.xml or system properties.
 * </p>
 *
 * <h4>System Properties</h4>
 *
 * <pre>
 * -Darq.extension.webdriver.waitAjaxInterval=3
 * </pre>
 *
 * <h4>arquillian.xml configuration</h4>
 *
 * <pre>
 * &lt;arquillian&gt;
 *   &lt;extension qualifier="graphene"&gt;
 *     &lt;property name="waitAjaxInterval"&gt;3&lt;/property&gt;
 *   &lt;/extension&gt;
 * &lt;/arquillian&gt;
 * </pre>
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class GrapheneConfiguration implements DroneConfiguration<GrapheneConfiguration> {

    private long waitAjaxInterval = 2;

    private long waitGuiInterval = 1;

    private long waitModelInterval = 5;

    private long waitGuardInterval = waitAjaxInterval;

    private long javascriptInstallationLimit = 5;

    private String defaultElementLocatingStrategy = How.ID_OR_NAME.toString().toLowerCase();

    private String scheme = null;

    private String url = null;

    /**
     * Specifies default location strategy when no parameter is given to {@link FindBy} annotated injection point.
     */
    public How getDefaultElementLocatingStrategy() {
        return How.valueOf(defaultElementLocatingStrategy.toUpperCase());
    }

    /**
     * Get a default internal for waiting for AJAX operations.
     */
    public long getWaitAjaxInterval() {
        return waitAjaxInterval;
    }

    /**
     * Get a default internal for waiting for Request Guards
     */
    public long getWaitGuardInterval() {
        return waitGuardInterval;
    }

    /**
     * Get a default internal for waiting for GUI operations.
     */
    public long getWaitGuiInterval() {
        return waitGuiInterval;
    }

    /**
     * Get a default internal for waiting for time-consuming, typically server-side operations.
     */
    public long getWaitModelInterval() {
        return waitModelInterval;
    }

    /**
     * How long should Graphene wait before it fails to install a JavaScript extension into a page.
     */
    public long getJavascriptInstallationLimit() {
        return javascriptInstallationLimit;
    }

    /**
     * Default scheme to use for every Location annotation. Use FQCN of scheme to use.
     */
    public String getScheme() {
        return scheme;
    }

    /**
     * default url when container would inject null value to ArquillianResource otherwise
     */
    public String getUrl() {
        return url;
    }

    /**
     * Validates that configuration is correct
     */
    public void validate() {
        if (waitAjaxInterval <= 0) {
            throw new IllegalArgumentException("The waitAjaxInterval property has to be a positive number.");
        }
        if (waitGuiInterval <= 0) {
            throw new IllegalArgumentException("The waitGuiInterval property has to be a positive number.");
        }
        if (waitModelInterval <= 0) {
            throw new IllegalArgumentException("The waitModelInterval property has to be a positive number.");
        }
        if (waitGuardInterval <= 0) {
            throw new IllegalArgumentException("The waitGuardInterval property has to be a positive number.");
        }
        if (javascriptInstallationLimit <= 0) {
            throw new IllegalArgumentException("The javascriptInstallationLimut property has to a positive number.");
        }
        if (scheme != null && scheme.isEmpty()) {
            throw new IllegalArgumentException("The scheme property has to be a non-empty string.");
        }
        if (url != null && !canConstructURL(url)) {
            throw new IllegalArgumentException("The custom url you provided is not valid url address: " + url);
        }
        try {
            How.valueOf(defaultElementLocatingStrategy.toUpperCase());
        } catch (IllegalArgumentException ex) {
            String values = "";
            for (How value : How.values()) {
                values += value.toString().toLowerCase() + ", ";
            }
            throw new IllegalArgumentException("The defaultElementLocatingStrategy property has to be one of the: " + values
                + " and was: "
                + ((defaultElementLocatingStrategy.length() != 0) ? defaultElementLocatingStrategy : "empty"), ex);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jboss.arquillian.drone.spi.DroneConfiguration#getConfigurationName()
     */
    @Override
    public String getConfigurationName() {
        return "graphene";
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.jboss.arquillian.drone.spi.DroneConfiguration#configure(org.jboss.arquillian.config.descriptor.api.ArquillianDescriptor
     * , java.lang.Class)
     */
    @Override
    public GrapheneConfiguration configure(ArquillianDescriptor descriptor, Class<? extends Annotation> qualifier) {
        return ConfigurationMapper.fromArquillianDescriptor(descriptor, this, qualifier);
    }

    /**
     * Checks if {@code url} is a valid address.
     *
     * @param url
     * @return true if {@code url} can be used as URL, false otherwise
     */
    private boolean canConstructURL(String url) {

        if (url == null || url.isEmpty()) {
            return false;
        }

        boolean canConstruct;

        try {
            new URL(url);
            canConstruct = true;
        } catch (MalformedURLException ex) {
            canConstruct = false;
        }

        return canConstruct;
    }

}
