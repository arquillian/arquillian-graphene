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

import com.opera.core.systems.OperaDriver;
import org.jboss.arquillian.drone.spi.Configurator;
import org.jboss.arquillian.drone.spi.Destructor;
import org.jboss.arquillian.drone.spi.Instantiator;
import org.jboss.arquillian.drone.webdriver.configuration.RemoteReusableWebDriverConfiguration;
import org.jboss.arquillian.drone.webdriver.configuration.TypedWebDriverConfiguration;
import org.jboss.arquillian.drone.webdriver.factory.RemoteWebDriverFactory;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Extends the {@link RemoteWebDriverFactory} and provides the created instance to the {@link GrapheneContext}.
 *
 * @author <a href="jpapouse@redhat.com">Jan Papousek</a>
 *
 */
public class GrapheneRemoteWebDriverFactory extends RemoteWebDriverFactory implements
        Configurator<RemoteWebDriver, TypedWebDriverConfiguration<RemoteReusableWebDriverConfiguration>>,
        Instantiator<RemoteWebDriver, TypedWebDriverConfiguration<RemoteReusableWebDriverConfiguration>>, Destructor<RemoteWebDriver> {

    @Override
    public int getPrecedence() {
        return 20;
    }

    @Override
    public void destroyInstance(RemoteWebDriver instance) {
        try {
            super.destroyInstance(instance);
        } finally {
            GrapheneContext.reset();
        }
    }

    @Override
    public RemoteWebDriver createInstance(TypedWebDriverConfiguration<RemoteReusableWebDriverConfiguration> configuration) {
        RemoteWebDriver driver = super.createInstance(configuration);
        RemoteWebDriver proxy = GrapheneContext.getProxyForDriver(OperaDriver.class);
        GrapheneContext.set(driver);
        return proxy;
    }

}
