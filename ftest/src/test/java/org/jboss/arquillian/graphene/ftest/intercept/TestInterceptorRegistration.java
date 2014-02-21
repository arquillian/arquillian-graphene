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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(Arquillian.class)
public class TestInterceptorRegistration {
    @Drone
    private WebDriver browser;

    private boolean interceptor_registered_before_test_invoked = false;

    @Before
    public void resetFlags() {
        interceptor_registered_before_test_invoked = false;
    }

    @Before
    public void register_interceptor_before_test() {
        ((GrapheneProxyInstance) browser).registerInterceptor(new Interceptor() {
            public Object intercept(InvocationContext context) throws Throwable {
                interceptor_registered_before_test_invoked = true;
                return context.invoke();
            }
            public int getPrecedence() {
                return 1;
            }
        });
    }

    @Test
    public void interceptor_can_be_registered_before_test() {
        assertFalse(interceptor_registered_before_test_invoked);
        browser.get("about:blank");
        assertTrue(interceptor_registered_before_test_invoked);
    }

    @Test
    public void interceptor_can_be_registered_by_extension_before_test() {
        assertFalse(InterceptorRegistrationExtension.invoked);
        browser.get("about:blank");
        assertTrue(InterceptorRegistrationExtension.invoked);
    }

}
