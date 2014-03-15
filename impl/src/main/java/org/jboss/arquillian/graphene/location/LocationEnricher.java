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
package org.jboss.arquillian.graphene.location;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Collection;

import org.jboss.arquillian.core.api.Injector;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.spi.ServiceLoader;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.enricher.PageObjectEnricher;
import org.jboss.arquillian.graphene.enricher.ReflectionHelper;
import org.jboss.arquillian.graphene.enricher.exception.GrapheneTestEnricherException;
import org.jboss.arquillian.graphene.page.InitialPage;
import org.jboss.arquillian.graphene.page.Location;
import org.jboss.arquillian.graphene.spi.location.LocationDecider;
import org.jboss.arquillian.test.spi.TestEnricher;
import org.openqa.selenium.WebDriver;

public class LocationEnricher implements TestEnricher {

    @Inject
    private Instance<ServiceLoader> serviceLoader;

    @Inject
    private Instance<ContextRootStore> locationStore;

    @Inject
    private Instance<Injector> injector;

    @Override
    public void enrich(Object testCase) {
    }

    @Override
    public Object[] resolve(Method method) {
        int indexOfInitialPage = getIndexOfParameterWithAnnotation(InitialPage.class, method);
        if (indexOfInitialPage == -1) {
            return new Object[method.getParameterTypes().length];
        }
        Class<?> qualifier = ReflectionHelper.getQualifier(method.getParameterAnnotations()[indexOfInitialPage]);

        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] result = new Object[method.getParameterTypes().length];
        result[indexOfInitialPage] = goTo(parameterTypes[indexOfInitialPage], qualifier);

        return result;
    }

    public <T> T goTo(Class<T> pageObject, Class<?> browserQualifier) {
        T result = null;
        GrapheneContext grapheneContext = GrapheneContext.getContextFor(browserQualifier);
        WebDriver browser = grapheneContext.getWebDriver();
        try {
            result = PageObjectEnricher.setupPage(grapheneContext, browser, pageObject);
        } catch (Exception e) {
            throw new GrapheneTestEnricherException("Error while initializing: " + pageObject, e);
        }
        handleLocationOf(pageObject, browser);
        return result;
    }

    private void handleLocationOf(Class<?> pageObjectClass, WebDriver browser) {
        Location location = pageObjectClass.getAnnotation(Location.class);
        if (location == null) {
            throw new IllegalArgumentException(String.format("The page object '%s' that you are navigating to "
                + "using either Graphene.goTo(<page_object>) or @InitialPage isn't annotated with @Location",
                pageObjectClass.getSimpleName()));
        }

        injector.get().inject(locationStore.get());

        LocationDecider decider = resolveDecider(serviceLoader.get().all(LocationDecider.class), location.scheme());

        if (decider == null) {
            throw new UnsupportedOperationException(String.format("There is not any registered location decider "
                + "which can decide '%s' scheme.", location.scheme().toString()));
        }

        URL url = decider.decide(location.value());

        browser.get(url.toExternalForm());
    }

    /**
     * Returns the index of the first parameter which contains the <code>annotation</code>
     *
     * @param annotation
     * @param method
     * @return
     */
    private int getIndexOfParameterWithAnnotation(Class<? extends Annotation> annotation, Method method) {
        Annotation[][] annotationsOfAllParameters = method.getParameterAnnotations();

        int result = 0;
        boolean founded = false;
        for (; result < annotationsOfAllParameters.length; result++) {
            for (int j = 0; j < annotationsOfAllParameters[result].length; j++) {
                if (annotationsOfAllParameters[result][j].annotationType().equals(annotation)) {
                    founded = true;
                    break;
                }
            }
            if (founded) {
                break;
            }
        }
        if (!founded) {
            // the method has no parameters with Location annotation
            return -1;
        }

        return result;
    }

    private LocationDecider resolveDecider(Collection<LocationDecider> deciders, Class<?> scheme) {
        for (LocationDecider decider : deciders) {
            if (decider.canDecide().getClass().equals(scheme)) {
                return decider;
            }
        }
        return null;
    }
}
