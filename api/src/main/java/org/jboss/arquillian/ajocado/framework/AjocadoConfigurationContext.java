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

/**
 * <p>
 * Context for keeping thread local context of {@link AjocadoConfiguration}.
 * </p>
 * 
 * <p>
 * Provides {@link #getProxy()} method for accessing that context over model of your tests.
 * </p>
 * 
 * <p>
 * All methods on returned proxy will be invoked on AjocadoConfiguration instance associated with current thread.
 * </p>
 * 
 * <p>
 * Proxy specifically handles the situations when no context is set - in this situation, IllegalStateException is
 * thrown.
 * </p>
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public final class AjocadoConfigurationContext implements InvocationHandler {

    /**
     * The thread local context of AjocadoConfiguration
     */
    private static final ThreadLocal<AjocadoConfiguration> REFERENCE = new ThreadLocal<AjocadoConfiguration>();

    private AjocadoConfigurationContext() {
    }

    /**
     * Sets the AjocadoConfiguration context for current thread
     * 
     * @param configuration
     *            the AjocadoConfiguration instance
     */
    public static void set(AjocadoConfiguration configuration) {
        REFERENCE.set(configuration);
    }

    /**
     * Returns the context of AjocadoConfiguration for current thread
     * 
     * @return the context of AjocadoConfiguration for current thread
     */
    private static AjocadoConfiguration get() {
        return REFERENCE.get();
    }

    /**
     * Returns true if configuration context is associated with current thread.
     * 
     * @return true if configuration context is associated with current thread, false otherwise.
     */
    public static boolean isInitialized() {
        return get() != null;
    }

    /**
     * Returns the instance of proxy to thread local context of AjocadoConfiguration
     * 
     * @return the instance of proxy to thread local context of AjocadoConfiguration
     */
    public static AjocadoConfiguration getProxy() {
        return (AjocadoConfiguration) Proxy.newProxyInstance(AjocadoConfigurationContext.class.getClassLoader(),
            new Class[] { AjocadoConfiguration.class }, new AjocadoConfigurationContext());
    }

    /**
     * End point for handling invocations on proxy
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result;
        if (!isInitialized()) {
            throw new IllegalStateException("AjocadoConfigurationContext is not initialized");
        }
        try {
            result = method.invoke(get(), args);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        } catch (Exception e) {
            throw new RuntimeException("unexpected invocation exception: " + e.getMessage(), e);
        }
        return result;
    }
}
