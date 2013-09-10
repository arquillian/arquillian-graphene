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
package org.jboss.arquillian.graphene.javascript;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.jboss.arquillian.drone.api.annotation.Default;
import org.jboss.arquillian.graphene.TestingDriverStub;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.spi.configuration.GrapheneConfiguration;
import org.junit.Test;

public class TestExecution extends AbstractJavaScriptTest {


    @JavaScript
    public interface TestingInterface {
        void method();

        @MethodName("anotherMethodName")
        void namedMethod();
    }

    @Test
    public void test_execution() {

        // given
        TestingDriverStub executor = spy(new TestingDriverStub());
        when(executor.executeScript("return true;")).thenReturn(true);

        // when
        TestingInterface instance = JSInterfaceFactory.create(GrapheneContext.setContextFor(new GrapheneConfiguration(), executor, Default.class), TestingInterface.class);
        instance.method();

        // then
        verify(executor, times(1)).executeScript(invocation("TestingInterface", "method"));
    }

    @Test
    public void test_execution_with_named_method() {

        // given
        TestingDriverStub executor = spy(new TestingDriverStub());
        when(executor.executeScript("return true;")).thenReturn(true);

        // when
        TestingInterface instance = JSInterfaceFactory.create(GrapheneContext.setContextFor(new GrapheneConfiguration(), executor, Default.class), TestingInterface.class);
        instance.namedMethod();

        // then
        verify(executor, times(1)).executeScript(invocation("TestingInterface", "anotherMethodName"));
    }

    @JavaScript("base")
    public interface TestingInterfaceWithBase {
        void method();
    }

    @Test
    public void test_execution_with_base() {

        // given
        TestingDriverStub executor = spy(new TestingDriverStub());
        when(executor.executeScript("return true;")).thenReturn(true);

        // when
        TestingInterfaceWithBase instance = JSInterfaceFactory.create(GrapheneContext.setContextFor(new GrapheneConfiguration(), executor, Default.class), TestingInterfaceWithBase.class);
        instance.method();

        // then
        verify(executor, times(1)).executeScript(invocation("base", "method"));
    }
}
