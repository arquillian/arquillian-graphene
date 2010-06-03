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
import org.jboss.test.selenium.framework.internal.Contextual;
import org.jboss.test.selenium.framework.internal.PageExtensions;
import org.jboss.test.selenium.framework.internal.SeleniumExtensions;
import org.jboss.test.selenium.guard.Guard;
import org.jboss.test.selenium.guard.Guarded;

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
public class AjaxSelenium extends ExtendedTypedSelenium implements Guarded {

    /** The reference. */
    private static final ThreadLocal<AjaxSelenium> REFERENCE = new ThreadLocal<AjaxSelenium>();

    /** The ajax aware command processor. */
    AjaxAwareCommandProcessor ajaxAwareCommandProcessor;

    /** The guarded command processor. */
    GuardedCommandProcessor guardedCommandProcessor;

    /** The JavaScript Extensions to tested page */
    PageExtensions pageExtensions;

    /** The JavaScript Extension to Selenium */
    SeleniumExtensions seleniumExtensions;

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
        ajaxAwareCommandProcessor = new AjaxAwareCommandProcessor(serverHost, serverPort, browser.getAsString(),
            contextPathURL.toString());
        guardedCommandProcessor = new GuardedCommandProcessor(ajaxAwareCommandProcessor);
        selenium = new ExtendedSelenium(guardedCommandProcessor);
        pageExtensions = new PageExtensions(this);
        seleniumExtensions = new SeleniumExtensions(this);
        setCurrentContext(this);
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
    public static void setCurrentContext(AjaxSelenium selenium) {
        REFERENCE.set(selenium);
    }

    /**
     * Gets the current context from Contextual objects.
     * 
     * @param inContext
     *            the in context
     * @return the current context
     */
    public static AjaxSelenium getCurrentContext(Contextual... contextual) {
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

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.test.selenium.guard.Guarded#registerGuard(org.jboss.test.selenium.guard.Guard)
     */
    public void registerGuard(Guard guard) {
        this.guardedCommandProcessor.registerGuard(guard);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.test.selenium.guard.Guarded#unregisterGuard(org.jboss.test.selenium.guard.Guard)
     */
    public void unregisterGuard(Guard guard) {
        this.guardedCommandProcessor.unregisterGuard(guard);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.test.selenium.guard.Guarded#unregisterGuards(java.lang.Class)
     */
    public void unregisterGuards(Class<? extends Guard> type) {
        this.guardedCommandProcessor.unregisterGuards(type);
    }

    /**
     * Immutable copy for copying this object.
     * 
     * @return the AjaxSelenium copy
     */
    public AjaxSelenium immutableCopy() {
        AjaxSelenium copy = new AjaxSelenium();

        copy.ajaxAwareCommandProcessor = this.ajaxAwareCommandProcessor;
        copy.guardedCommandProcessor = this.guardedCommandProcessor.immutableCopy();
        copy.selenium = new ExtendedSelenium(copy.guardedCommandProcessor);
        copy.pageExtensions = new PageExtensions(copy);
        copy.seleniumExtensions = new SeleniumExtensions(copy);

        return copy;
    }
}
