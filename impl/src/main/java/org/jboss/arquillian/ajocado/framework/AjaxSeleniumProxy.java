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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.jboss.arquillian.ajocado.framework.internal.UnsupportedTypedSelenium;

/**
 * <p>
 * Proxy for retrieving thread local context of AjaxSelenium.
 * </p>
 * 
 * <p>
 * All methods on returned proxy will be invoked on AjaxSelenium instance associated with current thread.
 * </p>
 * 
 * <p>
 * Proxy specifically handles the situations when no context is set - in this situation, runtime exception with
 * IllegalStateException cause is thrown.
 * </p>
 * 
 * <p>
 * Especially, the {@link AjaxSelenium#isStarted()} method is handled in that situation, it returns false instead of
 * throwing exception. Therefore it can be safely used for obtaining current status of Selenium initialization.
 * </p>
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public final class AjaxSeleniumProxy implements InvocationHandler {

    /**
     * The thread local context of AjaxSelenium
     */
    private static final ThreadLocal<AjaxSelenium> REFERENCE = new ThreadLocal<AjaxSelenium>();

    private AjaxSeleniumProxy() {
    }

    /**
     * Sets the AjaxSelenium context for current thread
     * 
     * @param selenium
     *            the AjaxSelenium instance
     */
    public static void setCurrentContext(AjaxSelenium selenium) {
        REFERENCE.set(selenium);
    }

    /**
     * Returns the context of AjaxSelenium for current thread
     * 
     * @return the context of AjaxSelenium for current thread
     */
    private static AjaxSelenium getCurrentContext() {
        return REFERENCE.get();
    }

    public static boolean isContextInitialized() {
        return getCurrentContext() != null;
    }

    /**
     * Returns the instance of proxy to thread local context of AjaxSelenium
     * 
     * @return the instance of proxy to thread local context of AjaxSelenium
     */
    public static AjaxSelenium getInstance() {
        return (AjaxSelenium) Proxy.newProxyInstance(AjaxSelenium.class.getClassLoader(), new Class[] {
            AjaxSelenium.class, UnsupportedTypedSelenium.class }, new AjaxSeleniumProxy());
    }

    /**
     * End point for handling invocations on proxy
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result;
        try {
            if (!isContextInitialized()) {
                if (method.getName().equals("isStarted")) {
                    return false;
                } else {
                    throw new IllegalStateException("Context is not initialized");
                }
            }
            result = method.invoke(getCurrentContext(), args);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        } catch (Exception e) {
            throw new RuntimeException("unexpected invocation exception: " + e.getMessage(), e);
        }
        return result;
    }
}
