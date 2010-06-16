package org.jboss.test.selenium.waiting.selenium;

import java.util.Vector;

import org.jboss.test.selenium.waiting.DefaultWaiting;

/**
 * Implementation of waiting for satisfaction of conditions on page using polling the Selenium API with given question.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class SeleniumWaiting extends DefaultWaiting<SeleniumWaiting> {

    /**
     * Stars loop waiting to satisfy condition.
     * 
     * @param condition
     *            what wait for to be satisfied
     */
    public void until(SeleniumCondition condition) {
        long start = System.currentTimeMillis();
        long end = start + this.getTimeout();
        boolean delay = this.isDelayed();
        while (System.currentTimeMillis() < end) {
            if (!delay && condition.isTrue()) {
                return;
            }
            delay = false;
            try {
                Thread.sleep(this.getInterval());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (System.currentTimeMillis() >= end) {
                if (condition.isTrue()) {
                    return;
                }
            }
        }
        fail();
    }

    /**
     * Waits until Retrieve's implementation doesn't retrieve value other than oldValue.
     * 
     * @param <T>
     *            type of value what we are waiting for change
     * @param oldValue
     *            value that we are waiting for change
     * @param retrieve
     *            implementation of retrieving actual value
     */
    public <T> void waitForChange(T oldValue, SeleniumRetriever<T> retrieve) {
        waitForChangeAndReturn(oldValue, retrieve);
    }

    /**
     * Waits until Retrieve's implementation doesn't retrieve value other than oldValue and this new value returns.
     * 
     * @param <T>
     *            type of value what we are waiting for change
     * @param oldValue
     *            value that we are waiting for change
     * @param retrieve
     *            implementation of retrieving actual value
     * @return new retrieved value
     */
    public <T> T waitForChangeAndReturn(final T oldValue, final SeleniumRetriever<T> retrieve) {
        final Vector<T> vector = new Vector<T>(1);

        this.until(new SeleniumCondition() {
            public boolean isTrue() {
                vector.add(0, retrieve.retrieve());
                if (oldValue == null) {
                    return vector.get(0) != null;
                }
                return !oldValue.equals(vector.get(0));
            }
        });

        return vector.get(0);
    }
}