/**
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
package org.jboss.arquillian.graphene.drone.factory;

import org.jboss.arquillian.drone.spi.Configurator;
import org.jboss.arquillian.drone.spi.Destructor;
import org.jboss.arquillian.drone.spi.Instantiator;
import org.jboss.arquillian.drone.webdriver.configuration.TypedWebDriverConfiguration;
import org.jboss.arquillian.drone.webdriver.configuration.WebDriverConfiguration;
import org.jboss.arquillian.drone.webdriver.factory.WebDriverFactory;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.proxy.GrapheneProxy;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.openqa.selenium.WebDriver;

/**
 * Extends the {@link WebDriverFactory} and provides the created instance to the {@link GrapheneContext}.
 *
 * @author Lukas Fryc
 *
 */
public class GrapheneWebDriverFactory extends WebDriverFactory implements
        Configurator<WebDriver, TypedWebDriverConfiguration<WebDriverConfiguration>>,
        Instantiator<WebDriver, TypedWebDriverConfiguration<WebDriverConfiguration>>, Destructor<WebDriver> {

    /*
     * (non-Javadoc)
     *
     * @see org.jboss.arquillian.drone.spi.Sortable#getPrecedence()
     */
    @Override
    public int getPrecedence() {
        return 20;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jboss.arquillian.drone.spi.Destructor#destroyInstance(java.lang.Object)
     */
    @Override
    public void destroyInstance(WebDriver instance) {
        try {
            if (GrapheneProxy.isProxyInstance(instance)) {
                super.destroyInstance(((GrapheneProxyInstance) instance).<WebDriver>unwrap());
            } else {
                super.destroyInstance(instance);
            }
        } finally {
            GrapheneContext.reset();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jboss.arquillian.drone.spi.Instantiator#createInstance(org.jboss.arquillian.drone.spi.DroneConfiguration)
     */
    @Override
    public WebDriver createInstance(TypedWebDriverConfiguration<WebDriverConfiguration> configuration) {
        WebDriver driver = super.createInstance(configuration);

        if (GrapheneProxy.isProxyInstance(driver)) {
            return driver;
        }

        WebDriver proxy = GrapheneContext.getProxyForDriver(WebDriver.class);
        GrapheneContext.set(driver);
        return proxy;
    }

}
