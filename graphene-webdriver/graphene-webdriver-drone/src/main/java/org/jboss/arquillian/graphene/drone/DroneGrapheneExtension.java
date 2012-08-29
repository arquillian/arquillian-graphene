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
import org.jboss.arquillian.graphene.drone.factory.GrapheneOperaDriverFactory;
import org.jboss.arquillian.graphene.drone.factory.GrapheneRemoteWebDriverFactory;
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

        builder.service(Configurator.class, GrapheneOperaDriverFactory.class);
        builder.service(Instantiator.class, GrapheneOperaDriverFactory.class);
        builder.service(Destructor.class, GrapheneOperaDriverFactory.class);

        builder.service(Configurator.class, GrapheneRemoteWebDriverFactory.class);
        builder.service(Instantiator.class, GrapheneRemoteWebDriverFactory.class);
        builder.service(Destructor.class, GrapheneRemoteWebDriverFactory.class);
    }
}
