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
package org.jboss.arquillian.graphene.ftest.wait;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.jboss.arquillian.graphene.Graphene;
import org.junit.Test;
import org.openqa.selenium.TimeoutException;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class WebDriverWaitTest extends AbstractWaitTest {

    @Test
    public void testMessageUntil() {
        String message = "blah blah blah blah blah";
        try {
            Graphene.waitGui()
                    .until(message)
                    .element(BY_HEADER)
                    .text()
                    .equalTo("sjkldhkdjfgjlkfg");
            Assert.fail(TimeoutException.class.getName() + " should be thrown.");
        } catch (TimeoutException e) {
            Assert.assertTrue("The exception message <" + e.getMessage() + "> should contain message <" + message + "> defined by 'withMessage()' method", e.getMessage().contains(message));
        }
    }

    @Test
    public void testWithMessage() {
        String message = "blah blah blah blah blah";
        try {
            Graphene.waitGui()
                    .withMessage(message)
                    .until()
                    .element(BY_HEADER)
                    .text()
                    .equalTo("sjkldhkdjfgjlkfg");
            Assert.fail(TimeoutException.class.getName() + " should be thrown.");
        } catch (TimeoutException e) {
            Assert.assertTrue("The exception message <" + e.getMessage() + "> should contain message <" + message + "> defined by 'withMessage()' method", e.getMessage().contains(message));
        }
    }

    @Test
    public void testWithMessageAndMessageUntil() {
        String message = "blah blah blah blah blah";
        try {
            Graphene.waitGui()
                    .withMessage("srhjkfsdbhfjkfbsdnbfm,sndbsdmnbf")
                    .until(message)
                    .element(BY_HEADER)
                    .text()
                    .equalTo("sjkldhkdjfgjlkfg");
            Assert.fail(TimeoutException.class.getName() + " should be thrown.");
        } catch (TimeoutException e) {
            Assert.assertTrue("The exception message <" + e.getMessage() + "> should contain message <" + message + "> defined by 'withMessage()' method", e.getMessage().contains(message));
        }
    }

    @Test
    public void testWithTimeout() {
        TimeUnit unit = TimeUnit.MILLISECONDS;
        long duration = 2000;
        long started = System.currentTimeMillis();
        try {
            Graphene.waitModel()
                    .withTimeout(duration, unit)
                    .until()
                    .element(BY_HEADER)
                    .text()
                    .equalTo("sjkldhkdjfgjlkfg");
            Assert.fail(TimeoutException.class.getName() + " should be thrown.");
        } catch(TimeoutException e) {
            long was = System.currentTimeMillis() - started;
            Assert.assertTrue("The waiting time shouldn't be much bigger than " + duration + " " + unit + ", but was " + was + " ms.",
                    was < unit.toMillis(duration + duration / 2));
            Assert.assertTrue("The waiting time shouldn't lower than " + duration + " " + unit + ", but was " + was + " ms.",
                    was >= unit.toMillis(duration));
        }
    }

    @Test
    public void testWithTimeoutDuration() {
        Duration duration = Duration.ofMillis(2000);
        long started = System.currentTimeMillis();
        try {
            Graphene.waitModel()
                    .withTimeout(duration)
                    .until()
                    .element(BY_HEADER)
                    .text()
                    .equalTo("sjkldhkdjfgjlkfg");
            Assert.fail(TimeoutException.class.getName() + " should be thrown.");
        } catch(TimeoutException e) {
            long was = System.currentTimeMillis() - started;
            Assert.assertTrue("The waiting time shouldn't be much bigger than " + duration.toMillis() + " ms, but was " + was + " ms.",
                              was < 3000);
            Assert.assertTrue("The waiting time shouldn't lower than " + duration.toMillis() + " ms, but was " + was + " ms.",
                              was >= 2000);
        }
    }

}
