/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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
package org.jboss.arquillian.graphene.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.arquillian.config.descriptor.api.ArquillianDescriptor;
import org.jboss.arquillian.config.descriptor.api.ExtensionDef;
import org.jboss.arquillian.graphene.spi.configuration.GrapheneConfigured;
import org.jboss.arquillian.graphene.spi.configuration.GrapheneUnconfigured;
import org.jboss.arquillian.test.spi.annotation.SuiteScoped;
import org.jboss.arquillian.test.spi.event.suite.AfterClass;
import org.jboss.arquillian.test.spi.event.suite.BeforeClass;
import org.jboss.arquillian.test.test.AbstractTestTestBase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class ConfigurationContextTestCase extends AbstractTestTestBase {

    @Mock
    private ArquillianDescriptor descriptor;

    @Mock
    private ExtensionDef extensionDefinition;

    @Before
    public void prepareDescriptor() {
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("waitGuiInterval", "5");
        properties.put("waitAjaxInterval", "25");
        properties.put("waitModelInterval", "125");
        when(extensionDefinition.getExtensionName()).thenReturn("graphene");
        when(extensionDefinition.getExtensionProperties()).thenReturn(properties);
        when(descriptor.getExtensions()).thenReturn(Arrays.asList(new ExtensionDef[] { extensionDefinition }));
    }

    @Test
    public void testConfigurationViaDescriptor() {
        getManager().bind(SuiteScoped.class, ArquillianDescriptor.class, descriptor);
        fire(new BeforeClass(Object.class));
        assertEventFired(GrapheneConfigured.class);
        GrapheneConfiguration configuration = getManager().resolve(GrapheneConfiguration.class);
        assertNotNull("Configuration instance has to be available.", configuration);
        configuration.validate();
        assertEquals("'waitGuiInterval' should be 5", 5, configuration.getWaitGuiInterval());
        assertEquals("'waitAjaxInterval' should be 25", 25, configuration.getWaitAjaxInterval());
        assertEquals("'waitModelInterval' should be 125", 125, configuration.getWaitModelInterval());
        fire(new AfterClass(Object.class));
        assertEventFired(GrapheneUnconfigured.class);
    }

    @Override
    protected void addExtensions(List<Class<?>> extensions) {
        extensions.add(GrapheneConfigurator.class);
    }

}
