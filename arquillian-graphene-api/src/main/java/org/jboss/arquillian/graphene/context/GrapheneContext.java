/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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
package org.jboss.arquillian.graphene.context;

import java.lang.reflect.Proxy;

import org.openqa.selenium.WebDriver;

/**
 * <p>
 * Class for keeping thread local context of {@link WebDriver}.
 * </p>
 * 
 * <p>
 * Provides {@link #getProxy()} method for accessing that context in model of your tests.
 * </p>
 * 
 * <p>
 * Proxy specifically handles the situations when no context is set - in this situation, runtime exception with
 * IllegalStateException cause is thrown.
 * </p>
 * 
 * @author Lukas Fryc
 */
public final class GrapheneContext {

    /**
     * The thread local context of AjaxSelenium
     */
    private static final ThreadLocal<WebDriver> REFERENCE = new ThreadLocal<WebDriver>();

    private GrapheneContext() {
    }

    /**
     * Sets the WebDriver context for current thread
     * 
     * @param driver the WebDriver instance
     * @throws IllegalArgumentException when provided WebDriver instance is null
     */
    public static void set(WebDriver driver) {
        if (driver == null) {
            throw new IllegalArgumentException("context instance can't be null");
        }
        REFERENCE.set(driver);
    }

    /**
     * Resets the WebDriver context for current thread
     * 
     * @param driver the WebDriver instance
     */
    public static void reset() {
        REFERENCE.set(null);
    }

    /**
     * Returns the context of WebDriver for current thread
     * 
     * @return the context of WebDriver for current thread
     * @throws NullPointerException when context is null
     */
    protected static WebDriver get() {
        WebDriver driver = REFERENCE.get();
        if (driver == null) {
            throw new NullPointerException("context is null");
        }
        return driver;
    }

    /**
     * Returns true of the context is initialized
     * 
     * @return true of the context is initialized
     */
    public static boolean isInitialized() {
        return REFERENCE.get() != null;
    }

    /**
     * Returns the instance of proxy to thread local context of WebDriver
     * 
     * @return the instance of proxy to thread local context of WebDriver
     */
    public static WebDriver getProxy() {
        return (WebDriver) Proxy.newProxyInstance(WebDriver.class.getClassLoader(), new Class[] { WebDriver.class },
                new GrapheneProxyHandler());
    }

    

}