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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

import org.junit.Test;
import org.mockito.Mockito;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * @author Lukas Fryc
 */
public class TestGrapheneProxy {

    @Test
    public void test_createProxy_with_implementationClass() throws Exception {
        // given
        ProxyFactory factory = mock(ProxyFactory.class);
        MethodHandler handler = mock(MethodHandler.class);
        Class<ProxyObjectDriver> clazz = ProxyObjectDriver.class;

        // when
        when(factory.createClass()).thenReturn(clazz);
        GrapheneProxy.createProxy(factory, handler, TestingDriverStub.class);

        // then
        verify(factory).setSuperclass(TestingDriverStub.class);
        verify(factory).setInterfaces(new Class[] { GrapheneProxyInstance.class });
    }

    @Test
    public void test_createProxy_without_implementationClass() throws Exception {
        // given
        ProxyFactory factory = mock(ProxyFactory.class);
        MethodHandler handler = mock(MethodHandler.class);
        Class<ProxyObjectDriver> clazz = ProxyObjectDriver.class;

        // when
        when(factory.createClass()).thenReturn(clazz);
        GrapheneProxy.createProxy(factory, handler, null);

        // then
        verify(factory, never()).setSuperclass(Mockito.any(Class.class));
        verify(factory).setInterfaces(new Class[] { GrapheneProxyInstance.class });
    }

    @Test
    public void test_createProxy_with_interfaces() throws Exception {
        // given
        ProxyFactory factory = mock(ProxyFactory.class);
        MethodHandler handler = mock(MethodHandler.class);
        Class<ProxyObjectDriver> clazz = ProxyObjectDriver.class;

        // when
        when(factory.createClass()).thenReturn(clazz);
        GrapheneProxy.createProxy(factory, handler, null, WebDriver.class, TakesScreenshot.class);

        // then
        verify(factory).setInterfaces(new Class[] { WebDriver.class, TakesScreenshot.class, GrapheneProxyInstance.class });
    }

    static class ProxyObjectDriver extends TestingDriverStub implements ProxyObject {

        public void setHandler(MethodHandler mi) {
        }

        public MethodHandler getHandler() {
            return null;
        }
    }
}
