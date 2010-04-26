package org.jboss.test.selenium.waiting;

/**
 * Interface for waiting for satisfaction of conditions on page
 * using Selenium objects.
 *  
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public interface SeleniumWaiting {

    /**
     * Stars loop waiting to satisfy condition.
     * 
     * @param condition
     *            what wait for to be satisfied
     */
    void until(Condition condition);

    /**
     * Waits until Retrieve's implementation doesn't retrieve value other
     * than oldValue.
     * 
     * @param <T>
     *            type of value what we are waiting for change
     * @param oldValue
     *            value that we are waiting for change
     * @param retrieve
     *            implementation of retrieving actual value
     */
    <T> void waitForChange(T oldValue, Retriever<T> retrieve);

    /**
     * Waits until Retrieve's implementation doesn't retrieve value other
     * than oldValue and this new value returns.
     * 
     * @param <T>
     *            type of value what we are waiting for change
     * @param oldValue
     *            value that we are waiting for change
     * @param retrieve
     *            implementation of retrieving actual value
     * @return new retrieved value
     */
    <T> T waitForChangeAndReturn(final T oldValue, final Retriever<T> retrieve);
}