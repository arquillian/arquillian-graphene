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
package org.jboss.arquillian.graphene.context;

import org.jboss.arquillian.graphene.enricher.SearchContextInterceptor;
import org.jboss.arquillian.graphene.enricher.StaleElementInterceptor;
import org.jboss.arquillian.graphene.proxy.GrapheneProxy;
import org.jboss.arquillian.graphene.proxy.GrapheneProxy.FutureTarget;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyHandler;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyUtil;
import org.openqa.selenium.HasInputDevices;
import org.openqa.selenium.JavascriptExecutor;
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
 * NullPointerException cause is thrown.
 * </p>
 *
 * @author Lukas Fryc
 */
public final class GrapheneContext {

    /**
     * The thread local context of WebDriver
     */
    private static final ThreadLocal<WebDriver> REFERENCE = new ThreadLocal<WebDriver>();

    /**
     * The thread local context of {@link GrapheneProxyHandler}
     */
    private static final ThreadLocal<GrapheneProxyHandler> HANDLER = new ThreadLocal<GrapheneProxyHandler>() {
        protected GrapheneProxyHandler initialValue() {
            return GrapheneProxyHandler.forFuture(TARGET);
        };
    };

    private GrapheneContext() {
    }

    private static FutureTarget TARGET = new FutureTarget() {
        public Object getTarget() {
            return get();
        }
    };

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
        if (GrapheneProxy.isProxyInstance(driver)) {
            throw new IllegalArgumentException("instance of the proxy can't be set to the context");
        }

        REFERENCE.set(driver);
    }

    /**
     * Resets the WebDriver context for current thread
     */
    public static void reset() {
        REFERENCE.set(null);
        HANDLER.get().resetInterceptors();
    }

    /**
     * Returns the context of WebDriver for current thread
     *
     * @return the context of WebDriver for current thread
     * @throws NullPointerException when context is null
     */
    static WebDriver get() {
        WebDriver driver = REFERENCE.get();
        if (driver == null) {
            throw new NullPointerException("context is null - it needs to be setup before starting to use it");
        }
        return driver;
    }

    /**
     * Returns true if the context is initialized
     *
     * @return true if the context is initialized
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
        WebDriver webdriver = GrapheneProxy.getProxyForHandler(HANDLER.get(), null, WebDriver.class, JavascriptExecutor.class, HasInputDevices.class);
        GrapheneProxyInstance proxy = (GrapheneProxyInstance) webdriver;
        proxy.registerInterceptor(new SearchContextInterceptor());
        proxy.registerInterceptor(new StaleElementInterceptor());
        return webdriver;
    }

    /**
     * Returns the proxy for current thread local context of WebDriver which implements all interfaces as the current instance
     * in the context.
     *
     * @return the instance of proxy to thread local context of WebDriver
     */
    public static WebDriver getAugmentedProxy() {
        WebDriver currentDriver = REFERENCE.get();
        Class<?>[] interfaces = GrapheneProxyUtil.getInterfaces(currentDriver.getClass());
        WebDriver augmentedProxy = GrapheneContext.getProxyForInterfaces(interfaces);
        return augmentedProxy;
    }

    /**
     * Returns the instance of proxy to thread local context of WebDriver, the proxy handles the same interfaces which
     * implements provided class.
     *
     * @return the instance of proxy to thread local context of WebDriver
     */
    public static <T extends WebDriver> T getProxyForDriver(Class<T> webDriverImplClass, Class<?>... additionalInterfaces) {
        T webdriver = GrapheneProxy.<T>getProxyForHandler(HANDLER.get(), webDriverImplClass, additionalInterfaces);
        GrapheneProxyInstance proxy = (GrapheneProxyInstance) webdriver;
        proxy.registerInterceptor(new SearchContextInterceptor());
        proxy.registerInterceptor(new StaleElementInterceptor());
        return webdriver;
    }

    /**
     * Returns the instance of proxy to thread local context of WebDriver, the proxy handles all the interfaces provided as
     * parameter.
     *
     * @return the instance of proxy to thread local context of WebDriver
     */

    public static <T> T getProxyForInterfaces(Class<?>... interfaces) {
        Class<?>[] interfacesIncludingWebdriver = GrapheneProxyUtil.concatClasses(interfaces, WebDriver.class);
        T webdriver = GrapheneProxy.<T>getProxyForHandler(HANDLER.get(), null, interfacesIncludingWebdriver);
        GrapheneProxyInstance proxy = (GrapheneProxyInstance) webdriver;
        proxy.registerInterceptor(new SearchContextInterceptor());
        proxy.registerInterceptor(new StaleElementInterceptor());
        return webdriver;
    }

    /**
     * Returns true when the current context is the instance of provided class.
     *
     * @param clazz the class used to check current context
     * @return true when the current context is the instance of provided class; false otherwise.
     */
    public static boolean holdsInstanceOf(Class<?> clazz) {
        return clazz.isAssignableFrom(get().getClass());
    }
}
