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

import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.util.concurrent.CountDownLatch;

import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

/**
 * @author Lukas Fryc
 */
public class TestGrapheneContextTheadLocality {

    @Test
    public void context_holds_one_instance_per_thread() {
        final CountDownLatch secondInstanceSet = new CountDownLatch(1);
        final CountDownLatch firstInstanceVerified = new CountDownLatch(1);
        final WebDriver driver1 = mock(WebDriver.class);
        final WebDriver driver2 = mock(WebDriver.class);

        new Thread(new Runnable() {
            public void run() {
                GrapheneContext.set(driver1);
                await(secondInstanceSet);
                assertSame(driver1, GrapheneContext.get());
                firstInstanceVerified.countDown();
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                GrapheneContext.set(driver2);
                secondInstanceSet.countDown();
                assertSame(driver2, GrapheneContext.get());
            }
        }).start();
        await(firstInstanceVerified);
    }

    private void await(CountDownLatch condition) {
        try {
            condition.await();
        } catch (InterruptedException e) {
            fail("thread has been interrupted");
        }
    }
}
