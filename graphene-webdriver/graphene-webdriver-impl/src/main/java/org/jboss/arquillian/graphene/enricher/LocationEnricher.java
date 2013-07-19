package org.jboss.arquillian.graphene.enricher;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.spi.ServiceLoader;
import org.jboss.arquillian.graphene.GrapheneContext;
import org.jboss.arquillian.graphene.enricher.exception.GrapheneTestEnricherException;
import org.jboss.arquillian.graphene.spi.annotations.InitialPage;
import org.jboss.arquillian.graphene.spi.annotations.Location;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.test.impl.enricher.resource.ArquillianResourceTestEnricher;
import org.jboss.arquillian.test.spi.TestEnricher;
import org.openqa.selenium.WebDriver;

public class LocationEnricher implements TestEnricher {

    @Inject
    private static Instance<ServiceLoader> serviceLoader;

    @ArquillianResource
    private URL contextRoot;

    @Override
    public void enrich(Object testCase) {
    }

    @Override
    public Object[] resolve(Method method) {
        int indexOfInitialPage = getIndexOfParameterWithAnnotation(InitialPage.class, method);
        if(indexOfInitialPage == -1) {
            return new Object[method.getParameterTypes().length];
        }
        enrichArquillianResourceOfThis();
        Class<?> qualifier = ReflectionHelper.getQualifier(method.getParameterAnnotations()[indexOfInitialPage]);
        WebDriver browser = GrapheneContext.getContextFor(qualifier).getWebDriver();

        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] result = new Object[method.getParameterTypes().length];
        result[indexOfInitialPage] = goTo(parameterTypes[indexOfInitialPage], browser);

        return result;
    }

    private void enrichArquillianResourceOfThis() {
        if (contextRoot != null) {
            return;
        }
        for (TestEnricher enricher : serviceLoader.get().all(TestEnricher.class)) {
            if (enricher instanceof ArquillianResourceTestEnricher) {
                enricher.enrich(this);
            }
        }
    }

    public <T> T goTo(Class<T> pageObject, WebDriver browser) {
        enrichArquillianResourceOfThis();
        T result = null;
        try {
            T initializedPage = (T) pageObject.newInstance();
            AbstractSearchContextEnricher.enrichRecursively(browser, initializedPage);
            result = initializedPage;
        } catch (Exception e) {
            throw new GrapheneTestEnricherException("Error while initializing: " + pageObject, e);
        }
        handleLocationOf(pageObject, browser);
        return result;
    }

    public <T> T goTo(Class<T> pageObject, Class<?> browserQualifier) {
        WebDriver browser = GrapheneContext.getContextFor(browserQualifier).getWebDriver();
        return goTo(pageObject, browser);
    }

    private void handleLocationOf(Class<?> pageObjectClass, WebDriver browser) {
        String locationValue = pageObjectClass.getAnnotation(Location.class).value();
        if (locationValue.trim().length() == 0) {
            throw new IllegalArgumentException("The @Location value over: " + pageObjectClass
                + " should be a URL of the page which the page object represents. Can not be empty!");
        }
        if (contextRoot == null) {
            // there is no deployment
            URL url = LocationEnricher.class.getClassLoader().getResource(locationValue);
            if (url == null) {
                throw new IllegalArgumentException("Graphene is trying to open: " + locationValue
                    + " as a local resource as it seems that you are not deploying on any container and "
                    + "the resource does not exist. Check out the @Location value over: " + pageObjectClass);
            }
            browser.get(url.toExternalForm());
        } else {
            try {
                browser.get(new URL(contextRoot, locationValue).toExternalForm());
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException("Graphene is trying to open: " + contextRoot.toExternalForm()
                    + locationValue + ". However there was an error! Check out @Location value over: " + pageObjectClass, e);
            }
        }
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
}
