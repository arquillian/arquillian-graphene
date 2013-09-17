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

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.jboss.arquillian.core.api.Injector;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.spi.ServiceLoader;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.enricher.exception.GrapheneTestEnricherException;
import org.jboss.arquillian.graphene.page.InitialPage;
import org.jboss.arquillian.graphene.page.Location;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.test.spi.TestEnricher;
import org.jboss.arquillian.test.spi.enricher.resource.ResourceProvider;
import org.openqa.selenium.WebDriver;

public class LocationEnricher implements TestEnricher {

//    private static ThreadLocal<URL> contextRootStore = new ThreadLocal<URL>();

    @Inject
    private static Instance<ServiceLoader> serviceLoader;

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
            throw new IllegalArgumentException(
                String
                    .format(
                        "The page object '%s' that you are navigating to using either Graphene.goTo(<page_object>) or @InitialPage isn't annotated with @Location",
                        pageObjectClass.getSimpleName()));
        }

        try {
            URL url = getURLFromLocation(location);
            browser.get(url.toExternalForm());
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(String.format("Location '%s' specified on %s is not valid URL",
                location.value(), pageObjectClass.getSimpleName()));
        }
    }

    private URL getURLFromLocationWithRoot(Location location) throws MalformedURLException {
        URL contextRoot = getContextRoot();
        if (contextRoot != null) {
            return new URL(contextRoot, location.value());
        } else {
            throw new IllegalStateException(String.format(
                "The location %s is not valid URI and no contextRoot was discovered to treat it as relative URL", location));
        }
    }

    private URL getURLFromLocation(Location location) throws MalformedURLException {
        URI uri;

        try {
            uri = new URI(location.value());
            if (!uri.isAbsolute()) {
                return getURLFromLocationWithRoot(location);
            }
        } catch (URISyntaxException e) {
            return getURLFromLocationWithRoot(location);
        }

        if ("resource".equals(uri.getScheme())) {
            String resourceName = uri.getSchemeSpecificPart();
            if (resourceName.startsWith("//")) {
                resourceName = resourceName.substring(2);
            }
            URL url = LocationEnricher.class.getClassLoader().getResource(resourceName);
            if (url == null) {
                throw new IllegalArgumentException(String.format("Resource '%s' specified by %s was not found", resourceName,
                    location));
            }
            return url;
        }

        if ("file".equals(uri.getScheme())) {
            File file = new File(uri);
            if (file.exists()) {
                throw new IllegalArgumentException(String.format("File specified by %s was not found", location));
            }
            return file.getAbsoluteFile().toURI().toURL();
        }

        return uri.toURL();
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

    private URL getContextRoot() {
        URL result = null;
        for (ResourceProvider provider : serviceLoader.get().all(ResourceProvider.class)) {
            if (provider.canProvide(URL.class)) {
                result = (URL) provider.lookup(new ArquillianResourceAnnotation());
            }
        }
        return result;
    }

    @SuppressWarnings("all")
    private class ArquillianResourceAnnotation implements ArquillianResource, Annotation {

        @Override
        public Class<? extends Annotation> annotationType() {
            return ArquillianResource.class;
        }

        @Override
        public Class<?> value() {
            return ArquillianResource.class;
        }
    }
}
