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
package org.jboss.arquillian.ajocado.framework;

import static org.jboss.arquillian.ajocado.javascript.JavaScript.fromResource;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.jboss.arquillian.ajocado.browser.Browser;
import org.jboss.arquillian.ajocado.command.CommandInterceptorProxy;
import org.jboss.arquillian.ajocado.command.CommandInterceptorProxyImpl;
import org.jboss.arquillian.ajocado.framework.AjocadoConfiguration.TimeoutType;
import org.jboss.arquillian.ajocado.framework.internal.AjocadoInitializator;
import org.jboss.arquillian.ajocado.framework.internal.PageExtensionsImpl;
import org.jboss.arquillian.ajocado.framework.internal.SeleniumExtensionsImpl;
import org.jboss.arquillian.ajocado.guard.RequestGuard;
import org.jboss.arquillian.ajocado.guard.RequestGuardImpl;
import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.locator.element.ElementLocationStrategy;

import com.thoughtworks.selenium.CommandProcessor;
import com.thoughtworks.selenium.HttpCommandProcessor;

/**
 * <p>
 * Implementation of {@link TypedSelenium} extended by methods in {@link ExtendedTypedSeleniumImpl}.
 * </p>
 * 
 * <p>
 * Internally using {@link org.jboss.arquillian.ajocado.ajaxaware.AjaxAwareInterceptor} and
 * {@link CommandInterceptorProxyImpl}.
 * </p>
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class AjaxSeleniumImpl extends ExtendedTypedSeleniumImpl implements AjaxSelenium, AjocadoInitializator {

    /** The JavaScript Extensions to tested page */
    PageExtensions pageExtensions;

    /** The JavaScript Extension to Selenium */
    SeleniumExtensions seleniumExtensions;

    /** The RequestInterceptor */
    RequestGuard requestInterceptor;

    /**
     * The command interception proxy
     */
    CommandInterceptorProxyImpl interceptionProxy;

    /**
     * Instantiates a new ajax selenium.
     */
    private AjaxSeleniumImpl() {
    }

    /**
     * Instantiates a new ajax selenium.
     * 
     * @param serverHost
     *            the server host
     * @param serverPort
     *            the server port
     * @param browser
     *            the browser
     * @param contextPathURL
     *            the context path url
     */
    public AjaxSeleniumImpl(String serverHost, int serverPort, Browser browser, URL contextPathURL) {
        CommandProcessor commandProcessor = new HttpCommandProcessor(serverHost, serverPort,
            browser.inSeleniumRepresentation(), contextPathURL.toString());
        interceptionProxy = new CommandInterceptorProxyImpl(commandProcessor);
        selenium = new ExtendedSelenium(interceptionProxy.getCommandProcessorProxy());
        pageExtensions = new PageExtensionsImpl();
        seleniumExtensions = new SeleniumExtensionsImpl();
        requestInterceptor = new RequestGuardImpl();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.ajocado.framework.AjaxSelenium#getPageExtensions()
     */
    @Override
    public PageExtensions getPageExtensions() {
        return pageExtensions;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.ajocado.framework.AjaxSelenium#getSeleniumExtensions()
     */
    @Override
    public SeleniumExtensions getSeleniumExtensions() {
        return seleniumExtensions;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.ajocado.framework.AjaxSelenium#getRequestInterceptor()
     */
    @Override
    public RequestGuard getRequestGuard() {
        return requestInterceptor;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.ajocado.framework.AjaxSelenium#getInterceptionProxy()
     */
    @Override
    public CommandInterceptorProxy getCommandInterceptionProxy() {
        return interceptionProxy;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    public AjaxSelenium clone() {
        AjaxSeleniumImpl copy = new AjaxSeleniumImpl();
        copy.pageExtensions = new PageExtensionsImpl();
        copy.seleniumExtensions = new SeleniumExtensionsImpl();
        copy.interceptionProxy = this.interceptionProxy.immutableCopy();
        copy.selenium = new ExtendedSelenium(copy.interceptionProxy.getCommandProcessorProxy());
        return copy;
    }

    @Override
    public void initializeBrowser() {
        this.enableNetworkTrafficCapturing(configuration.isSeleniumNetworkTrafficEnabled());
        this.start();
    }

    @Override
    public void configureBrowser() {
        if (configuration.isSeleniumMaximize()) {
            this.windowFocus();
            this.windowMaximize();
        }

        loadCustomLocationStrategies();

        this.setTimeout(configuration.getTimeout(TimeoutType.DEFAULT));

        // set speed after all configurations done
        this.setSpeed(configuration.getSeleniumSpeed());
    }

    @Override
    public void initializeSeleniumExtensions() {
        List<String> seleniumExtensions = getExtensionsListFromResource("javascript/selenium-extensions-order.txt");
        // loads the extensions to the selenium
        getSeleniumExtensions().requireResources(seleniumExtensions);
        // register the handlers for newly loaded extensions
        getSeleniumExtensions().registerCustomHandlers();
    }

    @Override
    public void initializePageExtensions() {
        List<String> pageExtensions = getExtensionsListFromResource("javascript/page-extensions-order.txt");
        // prepares the resources to load into page
        getPageExtensions().loadFromResources(pageExtensions);
    }

    @Override
    public void finalizeBrowser() {
        this.setSpeed(0);

        if (this.isStarted()) {
            this.deleteAllVisibleCookies();
            this.stop();
        }
    }

    @Override
    public void restartBrowser() {
        this.finalizeBrowser();
        this.initializeBrowser();
        this.initializeSeleniumExtensions();
        this.initializePageExtensions();
        this.configureBrowser();
    }

    protected void loadCustomLocationStrategies() {
        // jQuery location strategy
        JavaScript strategySource = fromResource("javascript/selenium-location-strategies/jquery-location-strategy.js");
        this.addLocationStrategy(ElementLocationStrategy.JQUERY, strategySource);
    }

    /**
     * Loads the list of resource names from the given resource.
     * 
     * @param resourceName
     *            the path to resource on classpath
     * @return the list of resource names from the given resource.
     */
    @SuppressWarnings("unchecked")
    protected static List<String> getExtensionsListFromResource(String resourceName) {
        try {
            return IOUtils.readLines(ClassLoader.getSystemResourceAsStream(resourceName));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
