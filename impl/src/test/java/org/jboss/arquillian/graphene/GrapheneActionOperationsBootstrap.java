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
package org.jboss.arquillian.graphene;

import org.jboss.arquillian.drone.api.annotation.Default;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.spi.configuration.GrapheneConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.htmlunit.HtmlUnitWebElement;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.interactions.Mouse;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public abstract class GrapheneActionOperationsBootstrap {

    @Mock(extraInterfaces = HasInputDevices.class)
    HtmlUnitDriver driver;

    @Mock
    Mouse mouse;

    @Mock
    Keyboard keyboard;

    @Mock
    HtmlUnitWebElement webElement;

    @Before
    public final void setUp() {
        GrapheneContext.setContextFor(new GrapheneConfiguration(), driver, Default.class);
        GrapheneRuntime.pushInstance(new DefaultGrapheneRuntime());

        when(((HasInputDevices) driver).getMouse()).thenReturn(mouse);
        when(((HasInputDevices) driver).getKeyboard()).thenReturn(keyboard);
    }

    @After
    public void tearDown() {
        GrapheneRuntime.popInstance();
        GrapheneContext.removeContextFor(Default.class);
    }
}
