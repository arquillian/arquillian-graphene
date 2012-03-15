package org.jboss.arquillian.graphene.context;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.util.concurrent.CountDownLatch;

import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

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
