/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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
package org.jboss.arquillian.graphene.integration;

import java.lang.annotation.Annotation;

import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.drone.spi.Enhancer;
import org.jboss.arquillian.graphene.configuration.GrapheneConfiguration;
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

    @Inject
    private Instance<GrapheneConfiguration> configuration;

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
    public WebDriver enhance(WebDriver driver, Class<? extends Annotation> qualifier) {
        GrapheneContext grapheneContext = GrapheneContext.setContextFor(configuration.get(), driver, qualifier);
        Class<?>[] interfaces = GrapheneProxyUtil.getInterfaces(driver.getClass());
        return grapheneContext.getWebDriver(interfaces);
    }

    /**
     * Unwraps the proxy
     */
    @Override
    public WebDriver deenhance(WebDriver enhancedDriver, Class<? extends Annotation> qualifier) {
        if (enhancedDriver instanceof GrapheneProxyInstance) {
            WebDriver driver = ((GrapheneProxyInstance) enhancedDriver).unwrap();
            GrapheneContext.removeContextFor(qualifier);
            return driver;
        }
        return enhancedDriver;
    }

}
