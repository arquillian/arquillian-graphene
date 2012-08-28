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
import org.jboss.arquillian.graphene.proxy.GrapheneProxy.FutureTarget;
import org.jboss.arquillian.graphene.page.extension.PageExtensionInstallatorProvider;
import org.jboss.arquillian.graphene.page.extension.PageExtensionRegistry;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class GraphenePageExtensionsContext {

    private static final ThreadLocal<PageExtensionRegistry> REGISTRY_REFERENCE = new ThreadLocal<PageExtensionRegistry>();
    private static final ThreadLocal<PageExtensionInstallatorProvider> INSTALLATOR_PROVIDER_REFERENCE = new ThreadLocal<PageExtensionInstallatorProvider>();

    public static PageExtensionRegistry getRegistryProxy() {
        return GrapheneProxy.getProxyForFutureTarget(REGISTRY_TARGET, PageExtensionRegistry.class);
    }

    public static PageExtensionInstallatorProvider getInstallatorProviderProxy() {
        return GrapheneProxy.getProxyForFutureTarget(INSTALLATOR_PROVIDER_TARGET, PageExtensionInstallatorProvider.class);
    }

    /**
     * Returns true if the context is initialized
     *
     * @return true if the context is initialized
     */
    public static boolean isInitialized() {
        return REGISTRY_REFERENCE.get() != null && INSTALLATOR_PROVIDER_REFERENCE != null;
    }

    /**
     * Resets the WebDriver context for current thread
     */
    public static void reset() {
        REGISTRY_REFERENCE.set(null);
        INSTALLATOR_PROVIDER_REFERENCE.set(null);
    }


    public static void setInstallatorProvider(PageExtensionInstallatorProvider installatorProvider) {
        if (installatorProvider == null) {
            throw new IllegalArgumentException("context instance can't be null");
        }
        if (GrapheneProxy.isProxyInstance(installatorProvider)) {
            throw new IllegalArgumentException("instance of the proxy can't be set to the context");
        }
        INSTALLATOR_PROVIDER_REFERENCE.set(installatorProvider);
    }

    public static void setRegistry(PageExtensionRegistry registry) {
        if (registry == null) {
            throw new IllegalArgumentException("context instance can't be null");
        }
        if (GrapheneProxy.isProxyInstance(registry)) {
            throw new IllegalArgumentException("instance of the proxy can't be set to the context");
        }
        REGISTRY_REFERENCE.set(registry);
    }

    static PageExtensionInstallatorProvider getInstallatorProvider() {
        PageExtensionInstallatorProvider installatorProvider = INSTALLATOR_PROVIDER_REFERENCE.get();
        if (installatorProvider == null) {
            throw new NullPointerException("context is null - it needs to be setup before starting to use it");
        }
        return installatorProvider;
    }

    static PageExtensionRegistry getRegistry() {
        PageExtensionRegistry registry = REGISTRY_REFERENCE.get();
        if (registry == null) {
            throw new NullPointerException("context is null - it needs to be setup before starting to use it");
        }
        return registry;
    }

    private static FutureTarget INSTALLATOR_PROVIDER_TARGET = new FutureTarget() {
        @Override
        public Object getTarget() {
            return getInstallatorProvider();
        }
    };

    private static FutureTarget REGISTRY_TARGET = new FutureTarget() {
        @Override
        public Object getTarget() {
            return getRegistry();
        }
    };

}
