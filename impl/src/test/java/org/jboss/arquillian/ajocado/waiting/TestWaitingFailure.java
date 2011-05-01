/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.jboss.arquillian.ajocado.waiting;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import org.jboss.arquillian.ajocado.waiting.selenium.SeleniumCondition;
import org.jboss.arquillian.ajocado.waiting.selenium.SeleniumWaiting;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class TestWaitingFailure {
    SeleniumWaiting seleniumWaiting;

    private final SeleniumCondition failureCondition = new SeleniumCondition() {

        public boolean isTrue() {
            return false;
        }
    };

    @BeforeClass
    public void setupSeleniumWaiting() {
        seleniumWaiting = new SeleniumWaiting().timeout(1);
    }

    @Test
    public void testDefault() {

        try {
            seleniumWaiting.until(failureCondition);
            fail();
        } catch (WaitTimeoutException e) {
            // should succeed
        }
    }

    @Test
    public void testCustomRuntimeException() {
        RuntimeException exception = new RuntimeException();

        try {
            seleniumWaiting.failWith(exception).until(failureCondition);
            fail();
        } catch (Exception e) {
            assertSame(e, exception);
        }
    }

    @Test
    public void testCustomException() {
        Exception exception = new Exception();

        try {
            seleniumWaiting.failWith(exception).until(failureCondition);
            fail();
        } catch (Exception e) {
            assertSame(e.getCause(), exception);
        }
    }

    @Test
    public void testMessage() {
        String message = "some message";

        try {
            seleniumWaiting.failWith(message).until(failureCondition);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof WaitTimeoutException);
            assertEquals(e.getMessage(), message);
        }
    }

    @Test
    public void testDontFail() {
        seleniumWaiting.dontFail().until(failureCondition);
    }
}
