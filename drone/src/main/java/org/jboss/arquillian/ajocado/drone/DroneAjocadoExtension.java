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
package org.jboss.arquillian.ajocado.drone;

import org.jboss.arquillian.ajocado.drone.factory.AjocadoFactory;
import org.jboss.arquillian.core.spi.LoadableExtension;
import org.jboss.arquillian.drone.spi.Configurator;
import org.jboss.arquillian.drone.spi.Destructor;
import org.jboss.arquillian.drone.spi.Instantiator;

/**
 * @author <a href="mailto:kpiwko@redhat.com">Karel Piwko</a>
 */
public class DroneAjocadoExtension implements LoadableExtension {
    @Override
    public void register(ExtensionBuilder builder) {
        builder.service(Configurator.class, AjocadoFactory.class);
        builder.service(Instantiator.class, AjocadoFactory.class);
        builder.service(Destructor.class, AjocadoFactory.class);
    }
}
