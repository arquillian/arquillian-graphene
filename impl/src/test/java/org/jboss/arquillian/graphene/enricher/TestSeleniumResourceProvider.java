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
package org.jboss.arquillian.graphene.enricher;

import org.jboss.arquillian.drone.api.annotation.Default;
import org.jboss.arquillian.graphene.DefaultGrapheneRuntime;
import org.jboss.arquillian.graphene.GrapheneRuntime;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.enricher.SeleniumResourceProvider.ActionsProvider;
import org.jboss.arquillian.graphene.enricher.SeleniumResourceProvider.WebDriverProvider;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.jboss.arquillian.graphene.spi.configuration.GrapheneConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Lukas Fryc
 */
public class TestSeleniumResourceProvider extends AbstractGrapheneEnricherTest{

    WebDriver driver;

    @Before
    public void setUp() {
        GrapheneContext.setContextFor(new GrapheneConfiguration(), driver, Default.class);
        GrapheneRuntime.pushInstance(new DefaultGrapheneRuntime());
    }

    @After
    public void tearDown() {
        GrapheneRuntime.popInstance();
        GrapheneContext.removeContextFor(Default.class);
    }

    @Test
    public void testDirectProviderCanProvideMethod() {
        // having
        WebDriverProvider provider = new WebDriverProvider();
        // then
        assertTrue(provider.canProvide(WebDriver.class));
        assertFalse(provider.canProvide(JavascriptExecutor.class));
    }

    @Test
    public void testDirectProviderLookup() {
        // having
        WebDriverProvider provider = new WebDriverProvider();
        // when
        Object object = provider.lookup(null, null);
        // then
        assertTrue(object instanceof WebDriver);
    }

    @Test
    public void testIndirectProviderCanProvideMethod() {
        // having
        ActionsProvider provider = new ActionsProvider();
        // then
        assertTrue(provider.canProvide(Actions.class));
    }

    @Test
    public void testIndirectProviderLookup() {
        // having
        ActionsProvider provider = new ActionsProvider();
        // when
        Object object = provider.lookup(null, null);
        // then
        assertNotNull(object);
        assertTrue(object instanceof Actions);
        assertTrue(object instanceof GrapheneProxyInstance);
    }
}