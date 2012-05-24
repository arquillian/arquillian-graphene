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
package org.jboss.arquillian.graphene.drone;

import org.jboss.arquillian.core.spi.LoadableExtension;
import org.jboss.arquillian.drone.spi.Configurator;
import org.jboss.arquillian.drone.spi.Destructor;
import org.jboss.arquillian.drone.spi.Instantiator;
import org.jboss.arquillian.graphene.drone.factory.GrapheneAndroidDriverFactory;
import org.jboss.arquillian.graphene.drone.factory.GrapheneChromeDriverFactory;
import org.jboss.arquillian.graphene.drone.factory.GrapheneFirefoxDriverFactory;
import org.jboss.arquillian.graphene.drone.factory.GrapheneHtmlUnitDriverFactory;
import org.jboss.arquillian.graphene.drone.factory.GrapheneIPhoneDriverFactory;
import org.jboss.arquillian.graphene.drone.factory.GrapheneInternetExplorerDriverFactory;
import org.jboss.arquillian.graphene.drone.factory.GrapheneWebDriverFactory;

/**
 * Arquillian Drone support Graphene and WebDriver and its implementations
 * 
 * Lukas Fryc
 */
public class DroneGrapheneExtension implements LoadableExtension {

    public void register(ExtensionBuilder builder) {

        builder.service(Configurator.class, GrapheneAndroidDriverFactory.class);
        builder.service(Instantiator.class, GrapheneAndroidDriverFactory.class);
        builder.service(Destructor.class, GrapheneAndroidDriverFactory.class);

        builder.service(Configurator.class, GrapheneChromeDriverFactory.class);
        builder.service(Instantiator.class, GrapheneChromeDriverFactory.class);
        builder.service(Destructor.class, GrapheneChromeDriverFactory.class);

        builder.service(Configurator.class, GrapheneFirefoxDriverFactory.class);
        builder.service(Instantiator.class, GrapheneFirefoxDriverFactory.class);
        builder.service(Destructor.class, GrapheneFirefoxDriverFactory.class);

        builder.service(Configurator.class, GrapheneHtmlUnitDriverFactory.class);
        builder.service(Instantiator.class, GrapheneHtmlUnitDriverFactory.class);
        builder.service(Destructor.class, GrapheneHtmlUnitDriverFactory.class);

        builder.service(Configurator.class, GrapheneInternetExplorerDriverFactory.class);
        builder.service(Instantiator.class, GrapheneInternetExplorerDriverFactory.class);
        builder.service(Destructor.class, GrapheneInternetExplorerDriverFactory.class);

        builder.service(Configurator.class, GrapheneIPhoneDriverFactory.class);
        builder.service(Instantiator.class, GrapheneIPhoneDriverFactory.class);
        builder.service(Destructor.class, GrapheneIPhoneDriverFactory.class);

        builder.service(Configurator.class, GrapheneWebDriverFactory.class);
        builder.service(Instantiator.class, GrapheneWebDriverFactory.class);
        builder.service(Destructor.class, GrapheneWebDriverFactory.class);
    }
}
