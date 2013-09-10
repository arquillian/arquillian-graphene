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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import org.jboss.arquillian.drone.api.annotation.Default;
import org.jboss.arquillian.graphene.enricher.SearchContextInterceptor;
import org.jboss.arquillian.graphene.enricher.StaleElementInterceptor;
import org.jboss.arquillian.graphene.page.extension.PageExtensionInstallatorProvider;
import org.jboss.arquillian.graphene.page.extension.PageExtensionRegistry;
import org.jboss.arquillian.graphene.page.extension.PageExtensionRegistryImpl;
import org.jboss.arquillian.graphene.page.extension.RemotePageExtensionInstallatorProvider;
import org.jboss.arquillian.graphene.proxy.GrapheneContextualHandler;
import org.jboss.arquillian.graphene.proxy.GrapheneProxy;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyHandler;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyUtil;
import org.jboss.arquillian.graphene.spi.configuration.GrapheneConfiguration;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class GrapheneContextImpl extends ExtendedGrapheneContext {

    private final GrapheneConfiguration configuration;
    private final WebDriver webDriver;
    private final Class<?> qualifier;
    /**
     * This field contains a reference to the context associated to the current
     * active browser actions. {@link LazyContext} instance is placed here.
     */
    private static final BrowserLocal<GrapheneContext> CURRENT_CONTEXT = new BrowserLocal<GrapheneContext>();
    /**
     * This field contains a map qualifier => real contexts. Real context is
     * an instance of {@link GrapheneContext} holding current {@link WebDriver},
     * qualifier and {@link GrapheneConfiguration}. Contexts from this field are
     * used by lazy contexts.
     */
    private static ThreadLocal<Map<Class<?>, GrapheneContext>> ALL_CONTEXTS = new ThreadLocal<Map<Class<?>, GrapheneContext>>() {
        @Override
        protected Map<Class<?>, GrapheneContext> initialValue() {
            return new HashMap<Class<?>, GrapheneContext>();
        }
    };
    /**
     * This field contains a map qualifier => lazy context. Lazy context uses
     * {@link #ALL_CONTEXTS} field for its method invocation and is able to create
     * a proxy for {@link WebDriver}.
     */
    private static ThreadLocal<Map<Class<?>, LazyContext>> LAZY_CONTEXTS = new ThreadLocal<Map<Class<?>, LazyContext>>() {
        @Override
        protected Map<Class<?>, LazyContext> initialValue() {
            return new HashMap<Class<?>, LazyContext>();
        }
    };

    private GrapheneContextImpl(GrapheneConfiguration configuration, WebDriver webDriver, Class<?> qualifier) {
        this.configuration = configuration;
        this.webDriver = webDriver;
        this.qualifier = qualifier;
    }

    public BrowserActions getBrowserActions() {
        return null;
    }

    public GrapheneConfiguration getConfiguration() {
        return configuration;
    }

    public PageExtensionInstallatorProvider getPageExtensionInstallatorProvider() {
        return null;
    }

    public PageExtensionRegistry getPageExtensionRegistry() {
        return null;
    }

    /**
     * If the {@link WebDriver} instance is not available yet, the returned
     * proxy just implements {@link WebDriver} interface. If the {@link WebDriver}
     * instance is available, its class is used to create a proxy, so the proxy extends it.
     *
     * @param interfaces interfaces which should be implemented by the returned {@link WebDriver}
     * @return proxy for the {@link WebDriver} held in the context
     */
    public WebDriver getWebDriver(Class<?>... interfaces) {
        return webDriver;
    }

    /**
     * @return qualifier identifying the context.
     */
    public Class<?> getQualifier() {
        return qualifier;
    }

    static class StaticInterfaceImplementation implements StaticInterface {

        // static methods
        @Override
        public GrapheneContext lastContext() {
            return CURRENT_CONTEXT.getLast();
        }

        /**
         * Get context associated to the given qualifier. If the {@link Default}
         * qualifier is given, the returned context tries to resolves the active context before
         * each method invocation. If the active context is available, the returned context
         * behaves like the active one.
         */
        @Override
        public GrapheneContext getContextFor(Class<?> qualifier) {
            if (qualifier == null) {
                throw new IllegalArgumentException("The parameter 'qualifer' is null.");
            }
            LazyContext context = (LazyContext) LAZY_CONTEXTS.get().get(qualifier);
            if (context == null) {
                try {
                    context = new LazyContext(qualifier, new BrowserActions(qualifier.getName()));
                    context.handler = GrapheneContextualHandler.forFuture(context, context.getFutureTarget());
                    GrapheneProxyInstance proxy = (GrapheneProxyInstance) context.getWebDriver();
                    proxy.registerInterceptor(new SearchContextInterceptor());
                    proxy.registerInterceptor(new StaleElementInterceptor());
                    context.installatorProvider = new RemotePageExtensionInstallatorProvider(context.registry, (JavascriptExecutor) context.getWebDriver(JavascriptExecutor.class));
                    final GrapheneContext finalContext = context;
                    context.getBrowserActions().performAction(new Callable<Void>() {
                        @Override
                        public Void call() throws Exception {
                            CURRENT_CONTEXT.set(finalContext);
                            return null;
                        }
                    });
                    LAZY_CONTEXTS.get().put(qualifier, context);
                } catch (Exception e) {
                    throw new IllegalStateException("Can't create a lazy context for " + qualifier.getName() + " qualifier.", e);
                }
            }
            return context;
        }

        /**
         * Creates a new context for the given webdriver, configuration and qualifier.
         * <strong>When you create the context, you are responsible to invoke {@link #removeContextFor(java.lang.Class) }
         * after the context is no longer valid.</strong>
         *
         * @return created context
         * @see #getContextFor(java.lang.Class)
         * @see #removeContextFor(java.lang.Class)
         */
        @Override
        public GrapheneContext setContextFor(GrapheneConfiguration configuration, WebDriver driver, Class<?> qualifier) {
            GrapheneContext grapheneContext = new GrapheneContextImpl(configuration, driver, qualifier);
            ALL_CONTEXTS.get().put(qualifier, grapheneContext);
            return getContextFor(qualifier);
        }

        /**
         * Removes the context associated to the given qualifier.
         * @param qualifier
         * @see #setContextFor(org.jboss.arquillian.graphene.spi.configuration.GrapheneConfiguration, org.openqa.selenium.WebDriver, java.lang.Class)
         */
        @Override
        public void removeContextFor(Class<?> qualifier) {
            final GrapheneContext context = LAZY_CONTEXTS.get().get(qualifier);
            if (context != null) {
                try {
                    ((LazyContext) context).getBrowserActions().performAction(new Callable<Void>() {
                            @Override
                            public Void call() throws Exception {
                                CURRENT_CONTEXT.remove();
                                return null;
                            }
                        });
                } catch (Exception ignored) {
                }
                LAZY_CONTEXTS.get().remove(qualifier);
            }
            ALL_CONTEXTS.get().remove(qualifier);
        }
    }

    private static class LazyContext extends GrapheneContextImpl {

        private final Class<?> qualifier;
        private final PageExtensionRegistry registry;
        private final BrowserActions browserActions;
        private PageExtensionInstallatorProvider installatorProvider;
        private GrapheneProxyHandler handler;

        public LazyContext(Class<?> qualifier, BrowserActions browserActions) {
            super(null, null, null);
            this.qualifier = qualifier;
            this.browserActions = browserActions;
            this.registry = new PageExtensionRegistryImpl();
        }

        @Override
        public BrowserActions getBrowserActions() {
            LazyContext context = LAZY_CONTEXTS.get().get(getQualifier());
            if (context == null) {
                return browserActions;
            } else {
                return context.browserActions;
            }
        }

        @Override
        public GrapheneConfiguration getConfiguration() {
            return getContext(true).getConfiguration();
        }

        @Override
        public PageExtensionInstallatorProvider getPageExtensionInstallatorProvider() {
            LazyContext context = LAZY_CONTEXTS.get().get(getQualifier());
            if (context == null) {
                return installatorProvider;
            } else {
                return context.installatorProvider;
            }
        }

        @Override
        public PageExtensionRegistry getPageExtensionRegistry() {
            LazyContext context = LAZY_CONTEXTS.get().get(getQualifier());
            if (context == null) {
                return registry;
            } else {
                return context.registry;
            }
        }

        @Override
        public Class<?> getQualifier() {
            if (Default.class.equals(qualifier)) {
                GrapheneContext context = getContext(false);
                if (context == null) {
                    return qualifier;
                } if (context instanceof LazyContext) {
                    return ((LazyContext) context).qualifier;
                } else {
                    return context.getQualifier();
                }
            } else {
                return qualifier;
            }
        }

        @Override
        public WebDriver getWebDriver(Class<?>... interfaces) {
            GrapheneContext context = getContext(false);
            if (context == null) {
                return GrapheneProxy.getProxyForHandler(handler, WebDriver.class, interfaces);
            } else if (GrapheneProxyUtil.isProxy(context.getWebDriver())) {
                Class<?> superclass = context.getWebDriver().getClass().getSuperclass();
                if (superclass != null && !GrapheneProxyUtil.isProxy(superclass) && WebDriver.class.isAssignableFrom(superclass)) {
                    return GrapheneProxy.getProxyForHandler(handler, context.getWebDriver().getClass().getSuperclass(), interfaces);
                } else {
                    return GrapheneProxy.getProxyForHandler(handler, WebDriver.class, interfaces);
                }
            } else {
                return GrapheneProxy.getProxyForHandler(handler, context.getWebDriver().getClass(), interfaces);
            }
        }

        protected GrapheneContext getContext(boolean exception) {
            GrapheneContext context = null;
            if (qualifier.equals(Default.class)) {
                try {
                    context = CURRENT_CONTEXT.get();
                } catch (Exception ignored) {
                }
            }
            if (context == null || context.equals(this)) {
                context = ALL_CONTEXTS.get().get(qualifier);
            }
            if (context == null && exception) {
                throw new IllegalStateException("There is no context available for qualifier " + qualifier.getName() + ". Available contexts are " + ALL_CONTEXTS.get().keySet() + ".");
            }
            return context;
        }

        protected GrapheneProxy.FutureTarget getFutureTarget() {
            return new GrapheneProxy.FutureTarget() {
                @Override
                public Object getTarget() {
                    return getContext(true).getWebDriver();
                }
            };
        }
    }
}
