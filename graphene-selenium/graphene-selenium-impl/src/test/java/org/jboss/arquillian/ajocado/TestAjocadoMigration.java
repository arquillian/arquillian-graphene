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
package org.jboss.arquillian.ajocado;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.arquillian.ajocado.framework.AjaxSeleniumContext;
import org.jboss.arquillian.ajocado.framework.AjocadoConfigurationContext;
import org.jboss.arquillian.ajocado.framework.GrapheneSeleniumImpl;
import org.jboss.arquillian.ajocado.framework.SystemPropertiesConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Tests for migration path from Ajocado specific objects to Graphene specific objects.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class TestAjocadoMigration {

    @Mock
    GrapheneSeleniumImpl selenium;

    @Mock
    SystemPropertiesConfiguration configuration;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void impl_can_be_set_to_context() {
        AjaxSeleniumContext.set(selenium);
    }

    @Test
    public void impl_can_be_proxied() {
        AjaxSeleniumContext.set(selenium);
        AjaxSeleniumContext.getProxy();
    }

    @Test
    public void impl_is_not_initialized() {
        AjaxSeleniumContext.set(null);
        assertFalse(AjaxSeleniumContext.isInitialized());
    }

    @Test
    public void impl_is_initialized() {
        AjaxSeleniumContext.set(selenium);
        assertTrue(AjaxSeleniumContext.isInitialized());
    }

    @Test
    public void configuration_can_be_set_to_context() {
        AjocadoConfigurationContext.set(configuration);
    }

    @Test
    public void configuration_can_be_proxied() {
        AjocadoConfigurationContext.set(configuration);
        AjocadoConfigurationContext.getProxy();
    }

    @Test
    public void configuration_is_not_initialized() {
        AjocadoConfigurationContext.set(null);
        assertFalse(AjocadoConfigurationContext.isInitialized());
    }

    @Test
    public void configuration_is_initialized() {
        AjocadoConfigurationContext.set(configuration);
        assertTrue(AjocadoConfigurationContext.isInitialized());
    }
}