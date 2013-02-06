package org.jboss.arquillian.graphene.ftest.wait;

import java.util.concurrent.TimeUnit;
import junit.framework.Assert;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.TimeoutException;

@RunWith(Arquillian.class)
public class WebDriverWaitTest extends AbstractWaitTest {

    @Test
    public void testMessageUntil() {
        loadPage();
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
        loadPage();
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
        loadPage();
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
        loadPage();
        TimeUnit unit = TimeUnit.MILLISECONDS;
        long duration = 500;
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

}
