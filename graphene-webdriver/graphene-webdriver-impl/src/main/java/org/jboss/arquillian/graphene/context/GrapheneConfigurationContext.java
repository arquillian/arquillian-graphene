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

import org.jboss.arquillian.graphene.proxy.GrapheneProxy;
import org.jboss.arquillian.graphene.configuration.GrapheneConfiguration;
import org.jboss.arquillian.graphene.proxy.GrapheneProxy.FutureTarget;

/**
 * <p>
 * Class for keeping thread local context of {@link GrapheneConfiguration}.
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
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class GrapheneConfigurationContext {

    private static final ThreadLocal<GrapheneConfiguration> REFERENCE = new ThreadLocal<GrapheneConfiguration>();

    /**
     * Returns the context of configuration for current thread
     *
     * @return the context of configuration for current thread
     * @throws NullPointerException when context is null
     */
    static GrapheneConfiguration get() {
        GrapheneConfiguration configuration = REFERENCE.get();
        if (configuration == null) {
            throw new NullPointerException("configuration is null - it needs to be setup before starting to use it");
        }
        return configuration;
    }

    /**
     * Returns the instance of proxy to thread local context of configuration
     *
     * @return the instance of proxy to thread local context of configuration
     */
    public static GrapheneConfiguration getProxy() {
        return GrapheneProxy.getProxyForFutureTarget(TARGET, GrapheneConfiguration.class);
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
     * Resets the WebDriver context for current thread
     */
    public static void reset() {
        REFERENCE.set(null);
    }

    /**
     * Sets the configuration context for current thread
     *
     * @param configuration the configuration instance
     * @throws IllegalArgumentException when provided configuration instance is null
     */
    public static void set(GrapheneConfiguration configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException("configuration instance can't be null");
        }
        if (GrapheneProxy.isProxyInstance(configuration)) {
            throw new IllegalArgumentException("instance of the proxy can't be set to the configuration");
        }
        REFERENCE.set(configuration);
    }

    private static FutureTarget TARGET = new FutureTarget() {
        @Override
        public Object getTarget() {
            return get();
        }
    };

}
