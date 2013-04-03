package org.jboss.arquillian.graphene.integration;

import java.lang.annotation.Annotation;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.drone.spi.Enhancer;
import org.jboss.arquillian.drone.webdriver.spi.DroneAugmented;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyUtil;
import org.openqa.selenium.WebDriver;

/**
 * Wraps the {@link WebDriver} instance provided by {@link Drone} in a proxy and sets the driver into context.
 *
 * @author Lukas Fryc
 */
public class GrapheneEnhancer implements Enhancer<WebDriver> {

    @Override
    public int getPrecedence() {
        return -100;
    }

    @Override
    public boolean canEnhance(Class<?> type, Class<? extends Annotation> qualifier) {
        return WebDriver.class.isAssignableFrom(type);
    }

    /**
     * Wraps the {@link WebDriver} instance provided by {@link Drone} in a proxy and sets the driver into context
     */
    @Override
    @SuppressWarnings("unchecked")
    public WebDriver enhance(WebDriver driver, Class<? extends Annotation> qualifier) {
        GrapheneContext.set(driver);

        Class<? extends WebDriver> baseDriverClass = driver.getClass();
        if (driver instanceof DroneAugmented) {
            baseDriverClass = (Class<? extends WebDriver>)((DroneAugmented) driver).getWrapped().getClass();
        }

        Class<?>[] interfaces = GrapheneProxyUtil.getInterfaces(driver.getClass());

        WebDriver proxy = GrapheneContext.getProxyForDriver(baseDriverClass, interfaces);

        return proxy;
    }

    /**
     * Unwraps the proxy
     */
    @Override
    public WebDriver deenhance(WebDriver instance, Class<? extends Annotation> qualifier) {
        if (instance instanceof GrapheneProxyInstance) {
            instance = ((GrapheneProxyInstance) instance).unwrap();
        }

        GrapheneContext.reset();

        return instance;
    }

}
