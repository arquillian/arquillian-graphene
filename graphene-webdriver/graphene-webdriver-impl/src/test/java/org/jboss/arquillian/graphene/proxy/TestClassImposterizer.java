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
package org.jboss.arquillian.graphene.proxy;

import static org.junit.Assert.assertTrue;
import net.sf.cglib.proxy.MethodInterceptor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Lukas Fryc
 */
@RunWith(MockitoJUnitRunner.class)
public class TestClassImposterizer {

    @Mock
    MethodInterceptor interceptor;

    @Test
    public void testClass() {
        Object object = ClassImposterizer.INSTANCE.imposterise(interceptor, TestingClass.class);
        assertTrue(object instanceof TestingClass);
    }

    @Test
    public void testInterface() {
        Object object = ClassImposterizer.INSTANCE.imposterise(interceptor, TestingInterface.class);
        assertTrue(object instanceof TestingInterface);
    }

    @Test
    public void testAbstractClass() {
        Object object = ClassImposterizer.INSTANCE.imposterise(interceptor, TestingAbstractClass.class);
        assertTrue(object instanceof TestingAbstractClass);
    }

    @Test
    public void testClassAndInterface() {
        Object object = ClassImposterizer.INSTANCE.imposterise(interceptor, TestingClass.class, TestingInterface.class);
        assertTrue(object instanceof TestingClass);
        assertTrue(object instanceof TestingInterface);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testPrivateInterface() {
        Object object = ClassImposterizer.INSTANCE.imposterise(interceptor, TestingPrivateInterface.class);
        assertTrue(object instanceof TestingPrivateInterface);
    }

    public static class TestingClass {
    }

    public static class TestingAbstractClass {
    }

    public static interface TestingInterface {
    }
    
    private static interface TestingPrivateInterface {
    }
}
