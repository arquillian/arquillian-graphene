/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.arquillian.graphene.drone.factory;

import org.jboss.arquillian.drone.spi.Configurator;
import org.jboss.arquillian.drone.spi.Destructor;
import org.jboss.arquillian.drone.spi.Instantiator;
import org.jboss.arquillian.drone.webdriver.configuration.ChromeDriverConfiguration;
import org.jboss.arquillian.drone.webdriver.configuration.TypedWebDriverConfiguration;
import org.jboss.arquillian.drone.webdriver.factory.ChromeDriverFactory;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Extends the {@link ChromeDriverFactory} and provides the created instance to the {@link GrapheneContext}.
 * 
 * @author Lukas Fryc
 * 
 */
public class GrapheneChromeDriverFactory extends ChromeDriverFactory implements
        Configurator<ChromeDriver, TypedWebDriverConfiguration<ChromeDriverConfiguration>>,
        Instantiator<ChromeDriver, TypedWebDriverConfiguration<ChromeDriverConfiguration>>, Destructor<ChromeDriver> {

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
    public void destroyInstance(ChromeDriver instance) {
        try {
            super.destroyInstance(instance);
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
    public ChromeDriver createInstance(TypedWebDriverConfiguration<ChromeDriverConfiguration> configuration) {
        ChromeDriver driver = super.createInstance(configuration);
        ChromeDriver proxy = GrapheneContext.getProxyForDriver(ChromeDriver.class);
        GrapheneContext.set(driver);
        return proxy;
    }
}