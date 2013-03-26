package org.jboss.arquillian.graphene.integration;

import java.lang.annotation.Annotation;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.drone.spi.Enhancer;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyUtil;
import org.openqa.selenium.WebDriver;

/**
 * Wraps the {@link WebDriver} instance provided by {@link Drone} in a proxy and sets the driver into context.
 *
 * @author Lukas Fryc
 */
public class DroneEnhancer implements Enhancer<WebDriver> {

    @Override
    public int getPrecedence() {
        return 0;
    }

    @Override
    public boolean canEnhance(Class<?> type, Class<? extends Annotation> qualifier) {
        return type.isAssignableFrom(WebDriver.class);
    }

    /**
     * Wraps the {@link WebDriver} instance provided by {@link Drone} in a proxy and sets the driver into context
     */
    @Override
    public WebDriver enhance(WebDriver driver, Class<? extends Annotation> qualifier) {
        GrapheneContext.set(driver);

        Class<?>[] interfaces = GrapheneProxyUtil.getInterfaces(driver.getClass());
        WebDriver proxy = GrapheneContext.getProxyForDriver(driver.getClass(), interfaces);

        return proxy;
    }

    /**
     * Unwraps the proxy
     */
    @Override
    public WebDriver deenhance(WebDriver enhancedDriver, Class<? extends Annotation> qualifier) {
        if (enhancedDriver instanceof GrapheneProxyInstance) {
            return ((GrapheneProxyInstance) enhancedDriver).unwrap();
        }

        GrapheneContext.reset();

        return enhancedDriver;
    }

}
