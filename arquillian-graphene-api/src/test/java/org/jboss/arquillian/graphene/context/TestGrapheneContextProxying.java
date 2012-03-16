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
package org.jboss.arquillian.graphene.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Proxy;

import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Navigation;

/**
 * @author Lukas Fryc
 */
public class TestGrapheneContextProxying {

    private static final String SAMPLE_STRING = "sample";

    @Test
    public void context_provides_proxy_which_delegates_to_current_context() {
        // having
        WebDriver driver = mock(WebDriver.class);

        // when
        GrapheneContext.set(driver);
        when(driver.toString()).thenReturn(SAMPLE_STRING);

        // then
        WebDriver proxy = GrapheneContext.getProxy();
        assertEquals(SAMPLE_STRING, proxy.toString());
    }

    @Test
    public void when_proxy_returns_webdriver_api_then_another_proxy_is_returned_wrapping_the_result_of_invocation() {
        // having
        WebDriver driver = mock(WebDriver.class);
        Navigation navigation = mock(Navigation.class);

        // when
        GrapheneContext.set(driver);
        when(driver.navigate()).thenReturn(navigation);

        // then
        WebDriver driverProxy = GrapheneContext.getProxy();
        Navigation navigationProxy = driverProxy.navigate();
        assertTrue(Proxy.isProxyClass(navigationProxy.getClass()));

        // verify
        verify(driver, only()).navigate();
    }
}
