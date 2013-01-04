package org.jboss.arquillian.graphene.enricher;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.jboss.arquillian.core.spi.LoadableExtension.ExtensionBuilder;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.proxy.GrapheneProxy;
import org.jboss.arquillian.graphene.proxy.GrapheneProxy.FutureTarget;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.jboss.arquillian.graphene.proxy.Interceptor;
import org.jboss.arquillian.graphene.proxy.InvocationContext;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.test.spi.enricher.resource.ResourceProvider;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.HasInputDevices;
import org.openqa.selenium.HasTouchScreen;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keyboard;
import org.openqa.selenium.Mouse;
import org.openqa.selenium.Rotatable;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TouchScreen;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.html5.ApplicationCache;
import org.openqa.selenium.html5.BrowserConnection;
import org.openqa.selenium.html5.DatabaseStorage;
import org.openqa.selenium.html5.LocationContext;
import org.openqa.selenium.html5.WebStorage;
import org.openqa.selenium.interactions.Actions;

/**
 * Provides common Selenium objects as Arquillian resources
 *
 * @author Lukas Fryc
 */
public abstract class SeleniumResourceProvider<T> implements ResourceProvider {

    public static class WebDriverProvider extends DirectProvider<WebDriver> {
    }

    public static class JavascriptExecutorProvider extends DirectProvider<JavascriptExecutor> {
    }

    public static class TakesScreenshotProvider extends DirectProvider<TakesScreenshot> {
    }

    public static class RotatableProvider extends DirectProvider<Rotatable> {
    }

    public static class LocationContextProvider extends DirectProvider<LocationContext> {
    }

    public static class ApplicationCacheProvider extends DirectProvider<ApplicationCache> {
    }

    public static class BrowserConnectionProvider extends DirectProvider<BrowserConnection> {
    }

    public static class WebStorageProvider extends DirectProvider<WebStorage> {
    }

    @SuppressWarnings("deprecation")
    public static class DatabaseStorageProvider extends DirectProvider<DatabaseStorage> {
    }

    public static class KeyboardProvider extends IndirectProvider<Keyboard, HasInputDevices> {
        @Override
        public Keyboard invoke(HasInputDevices base) {
            return base.getKeyboard();
        }
    }

    public static class MouseProvider extends IndirectProvider<Mouse, HasInputDevices> {
        @Override
        public Mouse invoke(HasInputDevices base) {
            return base.getMouse();
        }
    }

    public static class CapabilitiesProvider extends IndirectProvider<Capabilities, HasCapabilities> {
        @Override
        public Capabilities invoke(HasCapabilities base) {
            return base.getCapabilities();
        }
    }

    public static class TouchScreenProvider extends IndirectProvider<TouchScreen, HasTouchScreen> {
        @Override
        public TouchScreen invoke(HasTouchScreen base) {
            return base.getTouch();
        }
    }

    public static class ActionsProvider extends IndirectProvider<Actions, HasInputDevices> {
        @Override
        public Actions invoke(HasInputDevices base) {
            Keyboard keyboard = base.getKeyboard();
            Mouse mouse = base.getMouse();
            return new Actions(keyboard, mouse);
        }
    }

    protected Class<?> mediatorType;
    protected Class<?> returnType;

    public SeleniumResourceProvider() {
        this.returnType = getTypeArgument(0);
        this.mediatorType = returnType;
    }

    @Override
    public boolean canProvide(Class<?> type) {
        return type == this.returnType;
    }

    protected <BASE> BASE base() {
        return GrapheneContext.getProxyForInterfaces(mediatorType);
    }

    protected Class<?> getTypeArgument(int i) {
        ParameterizedType superType = (ParameterizedType) getClass().getGenericSuperclass();
        Type[] typeArguments = superType.getActualTypeArguments();
        return (Class<?>) typeArguments[i];
    }

    /**
     * Provides a given object type directly by casting WebDriver base instance
     *
     * @param <T> type of the returned object
     */
    private static class DirectProvider<T> extends SeleniumResourceProvider<T> {

        @Override
        public T lookup(ArquillianResource resource, Annotation... qualifiers) {
            return base();
        }
    }

    /**
     * This provides must provide a way how to obtain a given provider from provided WebDriver base object
     *
     * @param <T> type of the returned object
     * @param <M> type of the WebDriver base
     */
    private abstract static class IndirectProvider<T, M> extends SeleniumResourceProvider<T> {

        public IndirectProvider() {
            this.mediatorType = getTypeArgument(1);
        }

        @Override
        public Object lookup(ArquillianResource resource, Annotation... qualifiers) {
            final M base = base();

            ((GrapheneProxyInstance) base).registerInterceptor(new Interceptor() {

                @Override
                public Object intercept(final InvocationContext context) throws Throwable {
                    final Method method = context.getMethod();
                    if (method.getDeclaringClass() == mediatorType) {
                        return GrapheneProxy.getProxyForFutureTarget(new FutureTarget() {
                            public Object getTarget() {
                                try {
                                    M unwrappedBase = ((GrapheneProxyInstance) base).unwrap();
                                    return method.invoke(unwrappedBase, context.getArguments());
                                } catch (Exception e) {
                                    throw new IllegalStateException(e);
                                }
                            }
                        }, method.getReturnType());
                    } else {
                        throw new IllegalStateException("You cannot invoke method " + method + " on the " + mediatorType);
                    }
                }
            });

            return invoke(base);
        }

        public abstract T invoke(M mediator);
    }

    /**
     * Register all providers specified in this class
     */
    @SuppressWarnings("unchecked")
    public static void registerAllProviders(ExtensionBuilder builder) {
        for (Class<?> clazz : SeleniumResourceProvider.class.getClasses()) {
            if (SeleniumResourceProvider.class.isAssignableFrom(clazz)) {
                builder.service(ResourceProvider.class, (Class<SeleniumResourceProvider<?>>) clazz);
            }
        }
    }
}
