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
package org.jboss.arquillian.ajocado.drone.factory;

import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.jboss.arquillian.config.descriptor.api.ArquillianDescriptor;
import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.core.spi.ServiceLoader;
import org.jboss.arquillian.drone.impl.DroneConfigurator;
import org.jboss.arquillian.drone.impl.DroneCreator;
import org.jboss.arquillian.drone.impl.DroneDestructor;
import org.jboss.arquillian.drone.impl.DroneRegistrar;
import org.jboss.arquillian.drone.impl.DroneTestEnricher;
import org.jboss.arquillian.test.test.AbstractTestTestBase;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.junit.Before;
import org.mockito.Mockito;

/**
 * @author Lukas Fryc
 */
public class AbstractDroneTestCase extends AbstractTestTestBase {

    ServiceLoader serviceLoader;

    ArquillianDescriptor descriptor;

    @Override
    protected void addExtensions(List<Class<?>> extensions) {
        extensions.add(DroneRegistrar.class);
        extensions.add(DroneConfigurator.class);
        extensions.add(DroneCreator.class);
        extensions.add(DroneTestEnricher.class);
        extensions.add(DroneDestructor.class);
    }

    @Before
    public void initialize() {
        descriptor = Descriptors.create(ArquillianDescriptor.class);
        serviceLoader = Mockito.mock(ServiceLoader.class);

        getManager().bind(ApplicationScoped.class, ServiceLoader.class, serviceLoader);
        getManager().bind(ApplicationScoped.class, ArquillianDescriptor.class, descriptor);
    }

    public <T> void addAllServices(Class<T> clazz, T... arrayOfInstances) {
        when(serviceLoader.all(clazz)).thenReturn(Arrays.<T>asList(arrayOfInstances));
    }

    public <T> void addOnlyService(Class<T> clazz, T instance) {
        when(serviceLoader.onlyOne(clazz)).thenReturn(instance);
    }
}