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
package org.jboss.arquillian.graphene.context;

import org.jboss.arquillian.drone.api.annotation.Default;
import org.jboss.arquillian.graphene.configuration.GrapheneConfiguration;
import org.openqa.selenium.WebDriver;

public abstract class GrapheneContext {

    private static final String GRAPHENE_CONTEXT_STATIC_INTERFACE_IMPL = "org.jboss.arquillian.graphene.context.GrapheneContextImpl$StaticInterfaceImplementation";

    private static final StaticInterface INSTANCE = instantiate();

    /**
     * @return qualifier identifying the context.
     */
    public abstract Class<?> getQualifier();

    /**
     * If the {@link WebDriver} instance is not available yet, the returned proxy just implements {@link WebDriver} interface.
     * If the {@link WebDriver} instance is available, its class is used to create a proxy, so the proxy extends it.
     *
     * @param interfaces interfaces which should be implemented by the returned {@link WebDriver}
     * @return proxy for the {@link WebDriver} held in the context
     */
    public abstract WebDriver getWebDriver(Class<?>... interfaces);

    public abstract GrapheneConfiguration getConfiguration();

    public static GrapheneContext lastContext() {
        return INSTANCE.lastContext();
    }

    /**
     * Get context associated to the given qualifier. If the {@link Default} qualifier is given, the returned context tries to
     * resolves the active context before each method invocation. If the active context is available, the returned context
     * behaves like the active one.
     */
    public static GrapheneContext getContextFor(Class<?> qualifier) {
        return INSTANCE.getContextFor(qualifier);
    }

    /**
     * Creates a new context for the given webdriver, configuration and qualifier. <strong>When you create the context, you are
     * responsible to invoke {@link #removeContextFor(java.lang.Class) } after the context is no longer valid.</strong>
     *
     * @return created context
     * @see #getContextFor(java.lang.Class)
     * @see #removeContextFor(java.lang.Class)
     */
    public static GrapheneContext setContextFor(GrapheneConfiguration configuration, WebDriver driver, Class<?> qualifier) {
        return INSTANCE.setContextFor(configuration, driver, qualifier);
    }

    /**
     * Removes the context associated to the given qualifier.
     *
     * @param qualifier
     * @see #setContextFor(org.jboss.arquillian.graphene.configuration.GrapheneConfiguration, org.openqa.selenium.WebDriver,
     *      java.lang.Class)
     */
    public static void removeContextFor(Class<?> qualifier) {
        INSTANCE.removeContextFor(qualifier);
    }

    private static StaticInterface instantiate() {
        try {
            @SuppressWarnings("unchecked")
            Class<? extends StaticInterface> clazz = (Class<? extends StaticInterface>) Class.forName(GRAPHENE_CONTEXT_STATIC_INTERFACE_IMPL);

            return clazz.newInstance();

        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Cannot find class " + GRAPHENE_CONTEXT_STATIC_INTERFACE_IMPL
                    + ", make sure you have arquillian-graphene-impl.jar included on the classpath.", e);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    interface StaticInterface {
        GrapheneContext lastContext();

        GrapheneContext getContextFor(Class<?> qualifier);

        GrapheneContext setContextFor(GrapheneConfiguration configuration, WebDriver driver, Class<?> qualifier);

        void removeContextFor(Class<?> qualifier);
    }
}
