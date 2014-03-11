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
package org.jboss.arquillian.graphene.ftest.intercept;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.jboss.arquillian.graphene.proxy.Interceptor;
import org.jboss.arquillian.graphene.proxy.InvocationContext;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(Arquillian.class)
public class TestInterceptorSetupIsolation {
    @Drone
    private WebDriver browser;

    private boolean invoked = false;

    @Before
    public void resetFlags() {
        invoked = false;
    }

    @Test
    @InSequence(1)
    public void interceptor_can_be_registered_in_test_itself() {
        ((GrapheneProxyInstance) browser).registerInterceptor(new Interceptor() {

            @Override
            public Object intercept(InvocationContext context) throws Throwable {
                invoked = true;
                return context.invoke();
            }
            @Override
            public int getPrecedence() {
                return 1;
            }
        });

        assertFalse(invoked);
        browser.get("about:blank");
        assertTrue(invoked);
    }

    /**
     * Tests that interceptor from previous test isn't propagated to this test
     */
    @Test
    @InSequence(2)
    public void interceptors_arent_shared_across_tests() {
        assertFalse(invoked);
        browser.get("about:blank");
        assertFalse(invoked);
    }
}
