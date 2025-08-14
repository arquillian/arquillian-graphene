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
package org.jboss.arquillian.graphene.enricher;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.jboss.arquillian.core.spi.LoadableExtension.ExtensionBuilder;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.proxy.GrapheneProxy;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyHandler;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.test.spi.enricher.resource.ResourceProvider;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.interactions.Actions;

/**
 * Provides common Selenium objects as Arquillian resources
 *
 * @author Lukas Fryc
 */
public abstract class SeleniumResourceProvider implements ResourceProvider {

    /**
     * This is a resource provider for WebDriver interface.
     * It is used in an internal code.
     */
    public static class WebDriverProvider extends DirectProvider {
        @Override
        protected String getReturnType() {
            return WebDriver.class.getName();
        }
    }

    /**
     * This is a resource provider for JavascriptExecutor interface.
     * It is used in an internal code.
     */
    public static class JavascriptExecutorProvider extends DirectProvider {
        @Override
        protected String getReturnType() {
            return JavascriptExecutor.class.getName();
        }
    }

    public static class TakesScreenshotProvider extends DirectProvider {
        @Override
        protected String getReturnType() {
            return "org.openqa.selenium.TakesScreenshot";
        }
    }

    public static class RotatableProvider extends DirectProvider {
        @Override
        protected String getReturnType() {
            return "org.openqa.selenium.Rotatable";
        }
    }

    public static class CapabilitiesProvider extends IndirectProvider<HasCapabilities> {
        @Override
        public Object generateProxy(HasCapabilities base) {
            return base.getCapabilities();
        }

        @Override
        protected String getReturnType() {
            return "org.openqa.selenium.Capabilities";
        }
    }

    public static class ActionsProvider extends SeleniumResourceProvider {
        @Override
        public Object lookup(ArquillianResource resource, Annotation... qualifiers) {
            return new Actions(DirectProvider.lookupWebDriver(new Class<?>[0], qualifiers));
        }

        @Override
        protected String getReturnType() {
            return Actions.class.getName();
        }
    }

    protected abstract String getReturnType();

    @Override
    public boolean canProvide(Class<?> type) {
        return type.getName().equals(getReturnType());
    }

    protected final Class<?> getTypeArgument(int i) {
        ParameterizedType superType = (ParameterizedType) getClass().getGenericSuperclass();
        Type[] typeArguments = superType.getActualTypeArguments();
        return (Class<?>) typeArguments[i];
    }

    /**
     * Provides a given object type directly by casting WebDriver base instance
     */
    private abstract static class DirectProvider extends SeleniumResourceProvider {
        @Override
        public WebDriver lookup(ArquillianResource resource, Annotation... qualifiers) {
            try {
                return lookupWebDriver(new Class<?>[] { Class.forName(getReturnType()) }, qualifiers);
            } catch (ClassNotFoundException ex) {
                //the external users:
                //  - does not have any chance to build a test with classes which are not added on classpath
                //the intern usage of Providers:
                //  - the class path may contain a different version of Selenium
                //  - problem with internal use of Actions, Javascript, Mouse
                throw new IllegalStateException("The class of the provider is not on the class path.", ex);
            }
        }

        static WebDriver lookupWebDriver(Class<?>[] interfaces, Annotation... qualifiers) {
            GrapheneContext context = GrapheneContext.getContextFor(ReflectionHelper.getQualifier(qualifiers));
            return context.getWebDriver(interfaces);
        }
    }

    /**
     * This provides must provide a way how to obtain a given provider from provided WebDriver base object
     *
     * @param <T> type of the returned object
     * @param <M> type of the WebDriver base
     */
    private abstract static class IndirectProvider<M> extends SeleniumResourceProvider {

        protected Class<?> mediatorType;

        IndirectProvider() {
            this.mediatorType = getTypeArgument(0);
        }

        protected <BASE> BASE base(final Annotation[] annotations) {
            final GrapheneProxy.FutureTarget futureTarget = new GrapheneProxy.FutureTarget() {
                @Override
                public Object getTarget() {
                    GrapheneContext context = GrapheneContext.getContextFor(ReflectionHelper.getQualifier(annotations));
                    return context.getWebDriver(mediatorType);
                }
            };

            GrapheneProxyHandler mediatorHandler = new GrapheneProxyHandler(futureTarget) {
                @Override
                public Object invoke(Object proxy, final Method mediatorMethod, final Object[] mediatorArgs) throws Throwable {

                    GrapheneProxyHandler handler = new GrapheneProxyHandler(futureTarget) {

                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            Object mediatorObject = mediatorMethod.invoke(getTarget(), mediatorArgs);
                            return method.invoke(mediatorObject, args);
                        }
                    };

                    return GrapheneProxy.getProxyForHandler(handler, mediatorMethod.getReturnType());
                }
            };

            return (BASE) GrapheneProxy.getProxyForHandler(mediatorHandler, WebDriver.class, mediatorType);
        }

        @Override
        public Object lookup(ArquillianResource resource, Annotation... qualifiers) {
            final M base = base(qualifiers);
            return generateProxy(base);
        }

        public abstract Object generateProxy(M mediator);
    }

    /**
     * Register all providers specified in this class
     */
    @SuppressWarnings("unchecked")
    public static void registerAllProviders(ExtensionBuilder builder) {
        for (Class<?> clazz : SeleniumResourceProvider.class.getClasses()) {
            if (SeleniumResourceProvider.class.isAssignableFrom(clazz)) {
                builder.service(ResourceProvider.class, (Class<SeleniumResourceProvider>) clazz);
            }
        }
    }
}
