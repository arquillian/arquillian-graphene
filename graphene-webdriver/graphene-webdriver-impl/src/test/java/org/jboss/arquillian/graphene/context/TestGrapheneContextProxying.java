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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Navigation;
import org.openqa.selenium.WebElement;

/**
 * @author Lukas Fryc
 */
@RunWith(MockitoJUnitRunner.class)
public class TestGrapheneContextProxying {

    private static final String SAMPLE_STRING = "sample";

    @Mock
    WebDriver driver;

    @Test
    public void context_provides_proxy_which_delegates_to_current_context() {
        // having
        WebDriver driver = new DriverReturningSampleString();

        // when
        GrapheneContext.set(driver);

        // then
        WebDriver proxy = GrapheneContext.getProxy();
        assertEquals(SAMPLE_STRING, proxy.toString());
    }

    @Test
    public void when_proxy_returns_webdriver_api_then_another_proxy_is_returned_wrapping_the_result_of_invocation() {
        // having
        Navigation navigation = mock(Navigation.class);

        // when
        GrapheneContext.set(driver);
        when(driver.navigate()).thenReturn(navigation);

        // then
        WebDriver driverProxy = GrapheneContext.getProxy();
        Navigation navigationProxy = driverProxy.navigate();
        assertTrue(navigationProxy instanceof GrapheneProxyInstance);

        // verify
        verify(driver, only()).navigate();
    }

    @Test
    public void when_proxy_returns_result_of_invocation_with_arguments_then_returned_object_is_proxied() {
        // having
        WebElement webElement = mock(WebElement.class);
        By byId = By.id("id");

        // when
        GrapheneContext.set(driver);
        when(driver.findElement(byId)).thenReturn(webElement);

        // then
        WebDriver driverProxy = GrapheneContext.getProxy();
        WebElement webElementProxy = driverProxy.findElement(byId);
        assertTrue(webElementProxy instanceof GrapheneProxyInstance);

        // verify
        verify(driver, only()).findElement(byId);
    }

    @Test
    public void when_proxy_returns_result_of_invocation_with_arguments_then_returned_object_can_be_invoked() {
        // having
        WebElement webElement = mock(WebElement.class);
        By byId = By.id("id");

        // when
        GrapheneContext.set(driver);
        when(driver.findElement(byId)).thenReturn(webElement);

        // then
        WebDriver driverProxy = GrapheneContext.getProxy();
        WebElement webElementProxy = driverProxy.findElement(byId);
        webElementProxy.clear();

        // verify
        verify(webElement, only()).clear();
    }

    @Test
    public void test_that_context_can_be_unwrapped() {
        // having
        GrapheneContext.set(driver);
        WebDriver driverProxy = GrapheneContext.getProxy();

        // when
        WebDriver unwrapped = ((GrapheneProxyInstance) driverProxy).unwrap();

        // then
        assertSame(driver, unwrapped);
    }

    private static class DriverReturningSampleString extends TestingDriverStub {
        @Override
        public String toString() {
            return SAMPLE_STRING;
        }
    }
}
