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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.jboss.arquillian.drone.api.annotation.Default;
import org.jboss.arquillian.graphene.DefaultGrapheneRuntime;
import org.jboss.arquillian.graphene.GrapheneRuntime;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.enricher.SeleniumResourceProvider.ActionsProvider;
import org.jboss.arquillian.graphene.enricher.SeleniumResourceProvider.KeyboardProvider;
import org.jboss.arquillian.graphene.enricher.SeleniumResourceProvider.MouseProvider;
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
import org.openqa.selenium.interactions.Coordinates;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.interactions.Mouse;

/**
 * @author Lukas Fryc
 */
@RunWith(MockitoJUnitRunner.class)
public class TestSeleniumResourceProvider {

    @Mock(extraInterfaces = HasInputDevices.class)
    WebDriver driver;

    @Before
    public void setUp() {
        when(((HasInputDevices) driver).getKeyboard()).thenReturn(new Keyboard() {
            @Override
            public void sendKeys(CharSequence... keysToSend) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void pressKey(CharSequence keyToPress) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void releaseKey(CharSequence keyToRelease) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        when(((HasInputDevices) driver).getMouse()).thenReturn(new Mouse() {

            @Override
            public void click(Coordinates where) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void doubleClick(Coordinates where) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void mouseDown(Coordinates where) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void mouseUp(Coordinates where) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void mouseMove(Coordinates where) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void mouseMove(Coordinates where, long xOffset, long yOffset) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void contextClick(Coordinates where) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
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
        KeyboardProvider provider = new KeyboardProvider();
        // then
        assertTrue(provider.canProvide(Keyboard.class));
        assertFalse(provider.canProvide(Mouse.class));
    }

    @Test
    public void testIndirectProviderLookup() {
        // having
        KeyboardProvider provider = new KeyboardProvider();
        // when
        Object object = provider.lookup(null, null);
        // then
        assertNotNull(object);
        assertTrue(object instanceof Keyboard);
        assertTrue(object instanceof GrapheneProxyInstance);
    }

    @Test
    public void testMouseProviderLookup() {
        // having
        MouseProvider provider = new MouseProvider();
        // when
        Object object = provider.lookup(null, null);
        // then
        assertNotNull(object);
        assertTrue(object instanceof Mouse);
        assertTrue(object instanceof GrapheneProxyInstance);
    }

    @Test
    public void testActionsProviderLookup() {
        // having
        ActionsProvider provider = new ActionsProvider();
        Mouse mouse = mock(Mouse.class);
        Keyboard keyboard = mock(Keyboard.class);
        when(((HasInputDevices) driver).getMouse()).thenReturn(mouse);
        when(((HasInputDevices) driver).getKeyboard()).thenReturn(keyboard);
        // when
        Actions actions = (Actions) provider.lookup(null, null);
        actions.click().perform();
        // then
        verify(mouse).click(null);
        verifyNoMoreInteractions(mouse, keyboard);
    }
}