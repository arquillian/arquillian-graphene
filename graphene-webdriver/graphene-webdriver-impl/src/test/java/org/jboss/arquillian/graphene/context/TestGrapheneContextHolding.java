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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * @author Lukas Fryc
 */
public class TestGrapheneContextHolding {

    @Mock
    WebDriver driver;

    @Mock(extraInterfaces = GrapheneProxyInstance.class)
    WebDriver driverProxy;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void when_instance_is_provided_to_context_then_context_holds_it() {
        // having
        // when
        GrapheneContext.set(driver);

        // then
        assertSame(driver, GrapheneContext.get());
    }

    @Test
    public void when_instance_is_provided_to_context_then_context_is_initialized() {
        // having
        // when
        GrapheneContext.set(driver);

        // then
        assertTrue(GrapheneContext.isInitialized());
    }

    @Test
    public void when_context_is_reset_then_context_is_not_initialized() {
        // having
        GrapheneContext.set(driver);

        // when
        GrapheneContext.reset();

        // then
        assertFalse(GrapheneContext.isInitialized());
    }

    @Test(expected = NullPointerException.class)
    public void when_calling_get_on_not_initialized_context_then_context_throws_exception() {
        GrapheneContext.reset();
        GrapheneContext.get();
    }

    @Test(expected = IllegalArgumentException.class)
    public void when_set_null_instance_to_context_then_context_throws_exception() {
        GrapheneContext.set(null);
    }

    @Test
    public void context_should_determine_that_holds_instance_of_some_interface() {
        GrapheneContext.set(new TestingDriverStub());
        assertTrue(GrapheneContext.holdsInstanceOf(TakesScreenshot.class));
    }

    @Test
    public void context_should_fail_when_checked_for_being_instance_of_non_implemented_class() {
        GrapheneContext.set(new TestingDriverStub());
        assertFalse(GrapheneContext.holdsInstanceOf(Collection.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void when_set_proxy_instance_to_context_then_context_throws_exception() {
        GrapheneContext.set(driverProxy);
    }
}
