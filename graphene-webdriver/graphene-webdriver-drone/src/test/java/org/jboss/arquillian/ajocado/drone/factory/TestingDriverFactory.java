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
package org.jboss.arquillian.ajocado.drone.factory;

import java.lang.annotation.Annotation;

import org.jboss.arquillian.config.descriptor.api.ArquillianDescriptor;
import org.jboss.arquillian.drone.spi.Configurator;
import org.jboss.arquillian.drone.spi.Destructor;
import org.jboss.arquillian.drone.spi.Instantiator;
import org.jboss.arquillian.drone.webdriver.configuration.TypedWebDriverConfiguration;
import org.jboss.arquillian.graphene.context.TestingDriver;
import org.jboss.arquillian.graphene.context.TestingDriverStub;

/**
 * @author Lukas Fryc
 */
public class TestingDriverFactory implements
        Configurator<TestingDriver, TypedWebDriverConfiguration<TestingDriverConfiguration>>,
        Instantiator<TestingDriver, TypedWebDriverConfiguration<TestingDriverConfiguration>>, Destructor<TestingDriver> {

    @Override
    public int getPrecedence() {
        return 0;
    }

    @Override
    public void destroyInstance(TestingDriver instance) {
        instance.quit();
    }

    @Override
    public TestingDriver createInstance(TypedWebDriverConfiguration<TestingDriverConfiguration> configuration) {
        return new TestingDriverStub();
    }

    @Override
    public TypedWebDriverConfiguration<TestingDriverConfiguration> createConfiguration(ArquillianDescriptor descriptor,
            Class<? extends Annotation> qualifier) {
        return new TypedWebDriverConfiguration<TestingDriverConfiguration>(TestingDriverConfiguration.class,
                "org.jboss.arquillian.graphene.context.TestingDriverStub").configure(descriptor, qualifier);
    }

}