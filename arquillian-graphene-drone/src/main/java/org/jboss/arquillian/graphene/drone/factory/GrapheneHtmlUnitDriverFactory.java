/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
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
import org.jboss.arquillian.drone.webdriver.configuration.HtmlUnitDriverConfiguration;
import org.jboss.arquillian.drone.webdriver.configuration.TypedWebDriverConfiguration;
import org.jboss.arquillian.drone.webdriver.factory.HtmlUnitDriverFactory;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * Extends the {@link HtmlUnitDriverFactory} and provides the created instance to the {@link GrapheneContext}.
 * 
 * @author Lukas Fryc
 * 
 */
public class GrapheneHtmlUnitDriverFactory extends org.jboss.arquillian.drone.webdriver.factory.HtmlUnitDriverFactory implements
        Configurator<HtmlUnitDriver, TypedWebDriverConfiguration<HtmlUnitDriverConfiguration>>,
        Instantiator<HtmlUnitDriver, TypedWebDriverConfiguration<HtmlUnitDriverConfiguration>>, Destructor<HtmlUnitDriver> {

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
    public void destroyInstance(HtmlUnitDriver instance) {
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
    public HtmlUnitDriver createInstance(TypedWebDriverConfiguration<HtmlUnitDriverConfiguration> configuration) {
        HtmlUnitDriver driver = super.createInstance(configuration);
        HtmlUnitDriver proxy = GrapheneContext.getProxyForDriver(HtmlUnitDriver.class);
        GrapheneContext.set(driver);
        return (HtmlUnitDriver) proxy;
    }
}
