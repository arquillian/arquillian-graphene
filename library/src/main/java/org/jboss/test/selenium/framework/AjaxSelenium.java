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
package org.jboss.test.selenium.framework;

import java.net.URL;

import org.jboss.test.selenium.browser.Browser;
import org.jboss.test.selenium.framework.internal.PageExtensions;
import org.jboss.test.selenium.framework.internal.SeleniumExtensions;
import org.jboss.test.selenium.interception.InterceptedCommandProcessor;

import com.thoughtworks.selenium.CommandProcessor;

/**
 * <p>
 * Implementation of {@link TypedSelenium} extended by methods in {@link ExtendedTypedSelenium}.
 * </p>
 * 
 * <p>
 * Internally using {@link AjaxAwareCommandProcessor} and {@link GuardedCommandProcessor}.
 * </p>
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class AjaxSelenium extends ExtendedTypedSelenium {

    /** The reference. */
    private static final ThreadLocal<AjaxSelenium> REFERENCE = new ThreadLocal<AjaxSelenium>();

    /** The JavaScript Extensions to tested page */
    PageExtensions pageExtensions;

    /** The JavaScript Extension to Selenium */
    SeleniumExtensions seleniumExtensions;

    /**
     * The intercepted command processor
     */
    InterceptedCommandProcessor interceptedCommandProcessor;

    /**
     * Instantiates a new ajax selenium.
     */
    private AjaxSelenium() {
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
    public AjaxSelenium(String serverHost, int serverPort, Browser browser, URL contextPathURL) {
        CommandProcessor commandProcessor =
            new AjaxAwareCommandProcessor(serverHost, serverPort, browser.getAsString(), contextPathURL.toString());
        interceptedCommandProcessor = new InterceptedCommandProcessor(commandProcessor);
        selenium = new ExtendedSelenium(interceptedCommandProcessor);
        pageExtensions = new PageExtensions(this);
        seleniumExtensions = new SeleniumExtensions(this);
        setCurrentSelenium(this);
    }

    /**
     * <p>
     * Sets the current context.
     * </p>
     * 
     * <p>
     * <b>FIXME</b> not safe for multi-instance environment
     * </p>
     * 
     * @param selenium
     *            the new current context
     */
    public static void setCurrentSelenium(AjaxSelenium selenium) {
        REFERENCE.set(selenium);
    }

    /**
     * Gets the current context from Contextual objects.
     * 
     * @param inContext
     *            the in context
     * @return the current context
     */
    public static AjaxSelenium getCurrentSelenium() {
        return REFERENCE.get();
    }

    /**
     * <p>
     * Gets a PageExtensions associated with this AjaxSelenium object.
     * </p>
     * 
     * <p>
     * PageExtensions represents the JavaScript extensions on the testes page.
     * </p>
     * 
     * @return the PageExtensions associated with this AjaxSelenium object.
     */
    public PageExtensions getPageExtensions() {
        return pageExtensions;
    }

    /**
     * <p>
     * Gets a SeleniumExtensions associated with this AjaxSelenium object.
     * </p>
     * 
     * <p>
     * Selenium extensions can be used in Selenium Test Runner to extend functionality.
     * </p>
     * 
     * @return the SeleniumExtensions associated with this AjaxSelenium object.
     */
    public SeleniumExtensions getSeleniumExtensions() {
        return seleniumExtensions;
    }

    /**
     * Returns associated intercepted command processor
     * @return associated intercepted command processor
     */
    public InterceptedCommandProcessor getInterceptedCommandProcessor() {
        return interceptedCommandProcessor;
    }

    /**
     * Immutable copy for copying this object.
     * 
     * @return the AjaxSelenium copy
     */
    public AjaxSelenium immutableCopy() {
        AjaxSelenium copy = new AjaxSelenium();
        copy.pageExtensions = new PageExtensions(copy);
        copy.seleniumExtensions = new SeleniumExtensions(copy);
        copy.interceptedCommandProcessor = this.interceptedCommandProcessor.immutableCopy();
        copy.selenium = new ExtendedSelenium(copy.interceptedCommandProcessor);

        return copy;
    }
}
