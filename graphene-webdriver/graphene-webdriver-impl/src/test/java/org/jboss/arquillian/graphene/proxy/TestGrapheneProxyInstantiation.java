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

import org.jboss.arquillian.graphene.context.TestingDriverStub;
import org.jboss.arquillian.graphene.proxy.GrapheneProxy;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.jboss.arquillian.graphene.proxy.GrapheneProxy.FutureTarget;
import org.junit.Test;

public class TestGrapheneProxyInstantiation {

    @Test
    public void when_proxy_is_created_then_no_constructor_is_called() {
        // having
        FutureTarget target = new FutureTarget() {

            @Override
            public Object getTarget() {
                throw new ExpectedException();
            }
        };

        // when
        TestingDriver driver = GrapheneProxy.<TestingDriver>getProxyForFutureTarget(target, TestingDriver.class);
        try {
            driver.quit();
            fail("exception should be thrown because of FutureTarget definition");
        } catch (ExpectedException e) {
            // expected
        }

        // verify
        assertFalse(TestingDriver.contructorInvoked);
    }

    public static class TestingDriver extends TestingDriverStub {

        public static boolean contructorInvoked = false;

        public TestingDriver() {
            contructorInvoked = true;
        }
    }

    @SuppressWarnings("serial")
    public static class ExpectedException extends RuntimeException {

    }

}
