/**
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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
package org.jboss.arquillian.graphene.location;

import java.util.ArrayList;

import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.core.spi.ServiceLoader;
import org.jboss.arquillian.graphene.page.location.LocationDecider;
import org.jboss.arquillian.graphene.spi.configuration.GrapheneConfigured;
import org.jboss.arquillian.test.spi.annotation.ClassScoped;

/**
 * Grabs all {@link LocationDecider}s on class path and initializes {@link LocationDeciderRegistry}.
 *
 * Observes:<br>
 * <ul>
 * <li>{@link GrapheneConfigured}</li>
 * </ul>
 * Produces:<br>
 * <ul>
 * <li>{@link LocationDeciderRegistry}</li>
 * </ul>
 *
 * @author <a href="smikloso@redhat.com">Stefan Miklosovic</a>
 *
 */
public class LocationDeciderRegistryInitializer {

    @Inject
    @ClassScoped
    private InstanceProducer<LocationDeciderRegistry> registry;

    @Inject
    private Instance<ServiceLoader> serviceLoader;

    public void onGrapheneConfigured(@Observes GrapheneConfigured event) {

        LocationDeciderRegistry registryInstance = new LocationDeciderRegistry();

        registryInstance.set(new ArrayList<LocationDecider>(serviceLoader.get().all(LocationDecider.class)));

        registry.set(registryInstance);

    }

}
