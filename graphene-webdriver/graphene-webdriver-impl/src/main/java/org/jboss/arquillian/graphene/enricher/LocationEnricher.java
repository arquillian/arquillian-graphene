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
import org.jboss.arquillian.graphene.GrapheneContext;
import org.jboss.arquillian.graphene.container.ServletURLLookupService;
import org.jboss.arquillian.graphene.enricher.exception.GrapheneTestEnricherException;
import org.jboss.arquillian.graphene.spi.annotations.InitialPage;
import org.jboss.arquillian.graphene.spi.annotations.Location;
import org.jboss.arquillian.test.spi.TestEnricher;
import org.openqa.selenium.WebDriver;

public class LocationEnricher implements TestEnricher {

    private static ThreadLocal<URL> contextRootStore = new ThreadLocal<URL>();

    @Inject
    private Instance<ServiceLoader> serviceLoader;

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

        URL contextRoot = getContextRoot(method);
        contextRootStore.set(contextRoot);

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

    private URL getURLFromLocation(Location location) throws MalformedURLException {
        final URL contextRoot = contextRootStore.get();

        URI uri;

        try {
            uri = new URI(location.value());
        } catch (URISyntaxException e) {
            if (contextRoot != null) {
                return new URL(contextRoot, location.value());
            } else {
                throw new IllegalStateException(String.format(
                    "The location %s is not valid URI and no contextRoot was discovered to treat it as relative URL", location));
            }
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

    private URL getContextRoot(Method method) {
        ServletURLLookupService service = serviceLoader.get().onlyOne(ServletURLLookupService.class);
        return service.getContextRoot(method);
    }
}
