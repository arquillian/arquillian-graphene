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