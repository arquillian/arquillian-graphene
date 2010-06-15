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
package org.jboss.test.selenium.waiting;

/**
 * <p>
 * Implementation of waiting to satisfy condition.
 * </p>
 * 
 * <p>
 * This class constructs instances of DefaultWaiting by factory methods but provides its functionality (defaulted
 * waiting) by static method until.
 * </p>
 * 
 * <p>
 * Other factories (getDefault(), timeout(long), interval(long)) serves as simplified way to build new configured
 * instance, which can simple run waiting loop.
 * </p>
 * 
 * <p>
 * Class is intended to be used like these:
 * </p>
 * 
 * <ol>
 * <li>
 * <h3>direct waiting loop</h3>
 * 
 * <pre>
 * Wait.until(new Condition()) {
 *     public boolean isTrue() {
 *         return ...;
 * 	   }
 * }
 * </pre>
 * 
 * </li>
 * 
 * <li>
 * <h3>direct waiting loop with parameters</h3>
 * 
 * <pre>
 * Wait.interval(100).timeout(3000).until(new Not()) {
 *     public boolean not() {
 *         return ...;
 *     }
 * }
 * </pre>
 * 
 * </li>
 * 
 * <li>
 * <h3>save settings and run configured wait loop</h3>
 * 
 * <pre>
 * final String locator1 = &quot;...&quot;;
 * final String locator2 = &quot;...&quot;;
 * 
 * ...
 * 
 * Condition locatorsEqual = new Condition() {
 *     public boolean isTrue() {
 *         return selenium.equals(locator1, locator2);
 *     }
 * };
 * 
 * DefaultWaiting waitResponse = Wait.interval(1000).timeout(30000);
 * 			
 * ...
 * 
 * waitResponse.until(locatorsEqual); // some usage
 * 
 * ...
 * 
 * waitResponse.until(locatorsEqual); // other usage
 * </pre>
 * 
 * </li>
 * </ol>
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public final class Wait {

    /**
     * Default waiting interval
     */
    public static final long DEFAULT_INTERVAL = com.thoughtworks.selenium.Wait.DEFAULT_INTERVAL;
    /**
     * Default timeout of waiting
     */
    public static final long DEFAULT_TIMEOUT = com.thoughtworks.selenium.Wait.DEFAULT_TIMEOUT;

    /**
     * Class cannot be constructed in standard way, use factories method instead since it has static members only and no
     * other purposes.
     */
    private Wait() {
    }

    /**
     * Constructs default preset instance of waiting (@see DefaultWaiting).
     * 
     * @return default DefaultWaiting instance
     */
    public static DefaultWaiting getDefault() {
        return DefaultWaiting.getInstance();
    }

    /**
     * Constructs preset instance of waiting (@see DefaultWaiting) with given interval.
     * 
     * @param interval
     *            in miliseconds that will be preset to DefaultWaiting
     * @return DefaultWaiting instance initialized with given interval
     */
    public static DefaultWaiting interval(long interval) {
        return getDefault().interval(interval);
    }

    /**
     * Constructs preset instance of waiting (@see DefaultWaiting) with given timeout.
     * 
     * @param timeout
     *            in miliseconds that will be preset to DefaultWaiting
     * @return DefaultWaiting instance initialized with given timeout
     */
    public static DefaultWaiting timeout(long timeout) {
        return getDefault().timeout(timeout);
    }

    /**
     * Constructs preset instance of waiting (@see DefaultWaiting) with given subject of failure. It can be a Throwable
     * (in that case it will be used as cause) or it can be any other object (it will be converted to String and used as
     * exception message).
     * 
     * @param failureSubject
     *            Throwable (used in cause) or any other object (will be converted to expection message by its string
     *            value)
     * @return DefaultWaiting instance initialized with given failure subject
     */
    public static DefaultWaiting failWith(Object failureSubject) {
        return getDefault().failWith(failureSubject);
    }

    /**
     * Constructs preset instance of waiting (@see DefaultWaiting) with given failure message parametrized by given
     * objects
     * 
     * If failure is set to null, timeout will not result to failure!
     * 
     * @param failureMessage
     *            character sequence that will be used as message of exception thrown in case of waiting timeout or null
     *            if waiting timeout shouldn't result to failure
     * @param args
     *            arguments to failureMessage which will be use to parametrization of failureMessage
     * @return DefaultWaiting instance initialized with given failureMessage and arguments
     */
    public static DefaultWaiting failWith(CharSequence failureMessage, Object... args) {
        return getDefault().failWith(failureMessage, args);
    }

    /**
     * Constructs preset instance of waiting (@see DefaultWaiting) with no failure.
     * 
     * DefaultWaiting timeout with this preset dont result to failure!
     * 
     * @return DefaultWaiting instance initialized with no failure
     */
    public static DefaultWaiting dontFail() {
        return getDefault().dontFail();
    }

    /**
     * Constructs preset instance of waiting (@see DefaultWaiting) with no delay by interval between start waiting and
     * first test for conditions.
     * 
     * @return DefaultWaiting instance initialized with no delay
     */
    public static DefaultWaiting noDelay() {
        return getDefault().withDelay(false);
    }

    /**
     * Constructs preset instance of waiting (@see DefaultWaiting) and set that testing condition should or shouldn't be
     * delayed of one interval after the start of waiting.
     * 
     * @param isDelayed
     *            true if condition testing should be delayed; false otherwise
     * @return DefaultWaiting instance initialized with delay if isDelayed is set to true; with no delay otherwise
     */
    public static DefaultWaiting withDelay(boolean isDelayed) {
        return getDefault().withDelay(isDelayed);
    }

    /**
     * Will wait for default amount of time. Default timeout specified in Wait.DEFAULT_TIMEOUT
     */
    public static void waitForTimeout() {
        getDefault().waitForTimeout();
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
    public static <T> void waitForChange(T oldValue, Retriever<T> retrieve) {
        getDefault().waitForChangeAndReturn(oldValue, retrieve);
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
    public static <T> T waitForChangeAndReturn(T oldValue, Retriever<T> retrieve) {
        return getDefault().waitForChangeAndReturn(oldValue, retrieve);
    }

    /**
     * Will wait for specified amount of time.
     * 
     * Timeout is specified in miliseconds
     * 
     * @param timeoutInMilis
     *            time to wait in miliseconds
     */
    public static void waitFor(long timeoutInMilis) {
        getDefault().timeout(timeoutInMilis).waitForTimeout();
    }

    /**
     * Stars loop waiting to satisfy condition with default timeout and interval.
     * 
     * @param condition
     *            what wait for to be satisfied
     */
    public static void until(Condition condition) {
        getDefault().until(condition);
    }
}
