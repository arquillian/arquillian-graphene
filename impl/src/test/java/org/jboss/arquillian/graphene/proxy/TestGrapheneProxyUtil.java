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
package org.jboss.arquillian.graphene.proxy;

import org.jboss.arquillian.graphene.proxy.GrapheneProxyUtil;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.jboss.arquillian.graphene.TestingDriver;
import org.jboss.arquillian.graphene.TestingDriverStub;
import org.jboss.arquillian.graphene.TestingDriverStubExtension;

import org.junit.Test;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;

/**
 * @author Lukas Fryc
 */
public class TestGrapheneProxyUtil {

    @Test
    public void test_getInterface_with_WebDriver() {
        Set<Class<?>> actual = actual(WebDriver.class);
        Set<Class<?>> expected = expected(WebDriver.class, SearchContext.class);
        assertEquals(expected, actual);
    }

    @Test
    public void test_getInterface_with_interface_which_extends_another_interfaces() {
        Set<Class<?>> actual = actual(TestingDriver.class);
        Set<Class<?>> expected = expected(TestingDriver.INTERFACES, TestingDriver.class);
        assertEquals(expected, actual);
    }

    @Test
    public void test_getInterfaces_with_class_which_implements_wanted_interface() {
        Set<Class<?>> actual = actual(TestingDriverStub.class);
        Set<Class<?>> expected = expected(TestingDriver.class);
        assertEquals(expected, actual);
    }

    @Test
    public void test_getInterfaces_with_class_extending_class_which_implements_wanted_interface() {
        Set<Class<?>> actual = actual(TestingDriverStubExtension.class);
        Set<Class<?>> expected = expected(TestingDriver.class);
        assertEquals(expected, actual);
    }

    private Set<Class<?>> actual(Class<?>... classes) {
        return new HashSet<Class<?>>(Arrays.asList(GrapheneProxyUtil.getInterfaces(classes)));
    }

    private Set<Class<?>> expected(Class<?>... classes) {
        return new HashSet<Class<?>>(Arrays.asList(classes));
    }

    private Set<Class<?>> expected(Class<?>[] classes1, Class<?>... classes2) {
        Set<Class<?>> set = new HashSet<Class<?>>(Arrays.asList(classes1));
        set.addAll(Arrays.asList(classes2));
        return set;
    }
}
