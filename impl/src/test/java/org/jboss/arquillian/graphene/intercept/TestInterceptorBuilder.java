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
package org.jboss.arquillian.graphene.intercept;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.jboss.arquillian.drone.api.annotation.Default;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.proxy.GrapheneProxy;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.jboss.arquillian.graphene.proxy.Interceptor;
import org.jboss.arquillian.graphene.proxy.InvocationContext;
import org.jboss.arquillian.graphene.spi.configuration.GrapheneConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * @author Lukas Fryc
 */
@RunWith(MockitoJUnitRunner.class)
public class TestInterceptorBuilder {

    @Mock
    WebDriver driver;

    @Mock
    Interceptor interceptor1;
    @Mock
    Interceptor interceptor2;

    @Mock
    By by;

    GrapheneContext context;

    @Before
    public void before() throws Throwable {
        Answer invoke = new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return ((InvocationContext) invocation.getArguments()[0]).invoke();
            }
        };

        when(interceptor1.intercept(Mockito.any(InvocationContext.class))).thenAnswer(invoke);
        when(interceptor2.intercept(Mockito.any(InvocationContext.class))).thenAnswer(invoke);
        context = GrapheneContext.setContextFor(new GrapheneConfiguration(), driver, Default.class);
    }

    @Test
    public void test() throws Throwable {
        InterceptorBuilder builder = new InterceptorBuilder();
        builder.interceptInvocation(WebDriver.class, interceptor1).findElement(Interceptors.any(By.class));
        builder.interceptInvocation(WebDriver.class, interceptor2).findElement(Interceptors.any(By.class));
        Interceptor builtInterceptor = builder.build();

        WebDriver driverProxy = GrapheneProxy.getProxyForTargetWithInterfaces(context, driver, WebDriver.class);
        GrapheneProxyInstance proxy = (GrapheneProxyInstance) driverProxy;

        proxy.registerInterceptor(builtInterceptor);
        driverProxy.findElement(by);

        Mockito.inOrder(interceptor1, interceptor2);
        verify(interceptor1).intercept(Mockito.any(InvocationContext.class));
        verify(interceptor2).intercept(Mockito.any(InvocationContext.class));
    }
}
