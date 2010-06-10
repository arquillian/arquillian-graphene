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

import static org.jboss.test.selenium.framework.AjaxSelenium.getCurrentSelenium;

import java.util.Vector;
import java.util.concurrent.atomic.AtomicReference;

import org.jboss.test.selenium.encapsulated.JavaScript;
import org.jboss.test.selenium.waiting.ajax.AjaxWaiting;
import org.jboss.test.selenium.waiting.ajax.JavaScriptCondition;
import org.jboss.test.selenium.waiting.ajax.JavaScriptRetriever;

import static org.jboss.test.selenium.utils.text.SimplifiedFormat.format;

/**
 * <p>
 * Implementation of waiting to satisfy condition.
 * </p>
 * 
 * <p>
 * This class constructs instances of Waiting by factory methods but provides its functionality (defaulted waiting) by
 * static method until.
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
 * Waiting waitResponse = Wait.interval(1000).timeout(30000);
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
     * Constructs default preset instance of waiting (@see Waiting).
     * 
     * @return default Waiting instance
     */
    public static Waiting getDefault() {
        return Waiting.getInstance();
    }

    /**
     * Constructs preset instance of waiting (@see Waiting) with given interval.
     * 
     * @param interval
     *            in miliseconds that will be preset to Waiting
     * @return Waiting instance initialized with given interval
     */
    public static Waiting interval(long interval) {
        return getDefault().interval(interval);
    }

    /**
     * Constructs preset instance of waiting (@see Waiting) with given timeout.
     * 
     * @param timeout
     *            in miliseconds that will be preset to Waiting
     * @return Waiting instance initialized with given timeout
     */
    public static Waiting timeout(long timeout) {
        return getDefault().timeout(timeout);
    }

    /**
     * Constructs preset instance of waiting (@see Waiting) with given subject of failure. It can be a Throwable (in
     * that case it will be used as cause) or it can be any other object (it will be converted to String and used as
     * exception message).
     * 
     * @param failureSubject
     *            Throwable (used in cause) or any other object (will be converted to expection message by its string
     *            value)
     * @return Waiting instance initialized with given failure subject
     */
    public static Waiting failWith(Object failureSubject) {
        return getDefault().failWith(failureSubject);
    }

    /**
     * Constructs preset instance of waiting (@see Waiting) with given failure message parametrized by given objects
     * 
     * If failure is set to null, timeout will not result to failure!
     * 
     * @param failureMessage
     *            character sequence that will be used as message of exception thrown in case of waiting timeout or null
     *            if waiting timeout shouldn't result to failure
     * @param args
     *            arguments to failureMessage which will be use to parametrization of failureMessage
     * @return Waiting instance initialized with given failureMessage and arguments
     */
    public static Waiting failWith(CharSequence failureMessage, Object... args) {
        return getDefault().failWith(failureMessage, args);
    }

    /**
     * Constructs preset instance of waiting (@see Waiting) with no failure.
     * 
     * Waiting timeout with this preset dont result to failure!
     * 
     * @return Waiting instance initialized with no failure
     */
    public static Waiting dontFail() {
        return getDefault().dontFail();
    }

    /**
     * Constructs preset instance of waiting (@see Waiting) with no delay by interval between start waiting and first
     * test for conditions.
     * 
     * @return Waiting instance initialized with no delay
     */
    public static Waiting noDelay() {
        return getDefault().withDelay(false);
    }

    /**
     * Constructs preset instance of waiting (@see Waiting) and set that testing condition should or shouldn't be
     * delayed of one interval after the start of waiting.
     * 
     * @param isDelayed
     *            true if condition testing should be delayed; false otherwise
     * @return Waiting instance initialized with delay if isDelayed is set to true; with no delay otherwise
     */
    public static Waiting withDelay(boolean isDelayed) {
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

    /**
     * Class intented to construct by factories in Wait class.
     * 
     * Implementation of waiting for satisfaction of condition.
     */
    public static final class Waiting implements SeleniumWaiting, AjaxWaiting {

        /**
         * Singleton
         */
        private static final AtomicReference<Waiting> SINGLETON = new AtomicReference<Waiting>(null);

        /**
         * Indicates when the first test for the condition should be delayed after waiting starts.
         */
        private boolean isDelayed = true;

        /**
         * Interval between tries to test condition for satisfaction
         */
        private long interval = DEFAULT_INTERVAL;
        /**
         * Timeout of whole waiting procedure
         */
        private long timeout = DEFAULT_TIMEOUT;

        /**
         * Failure indicates waiting timeout.
         * 
         * If is set to null, no failure will be thrown after timeout.
         */
        private Object failure = new AssertionError();

        /**
         * Arguments to format failure if it is string value and should be formatted
         */
        private Object[] failureArgs;

        /**
         * This class cannot be constructed directly
         */
        private Waiting() {
        }

        /**
         * Factory method
         * 
         * @return singleton
         */
        private static Waiting getInstance() {
            if (SINGLETON.get() == null) {
                SINGLETON.compareAndSet(null, new Waiting());
            }
            return SINGLETON.get();
        }

        /**
         * Sets condition testing interval to this instance and return it.
         * 
         * @param interval
         *            in miliseconds that will be preset to Waiting
         * @return Waiting instance initialized with given interval
         */
        public Waiting interval(long interval) {
            if (interval == this.interval) {
                return this;
            }
            Waiting copy = this.copy();
            copy.interval = interval;
            return copy;
        }

        /**
         * Sets waiting timeout to this instance and return it.
         * 
         * @param timeout
         *            in miliseconds that will be preset to Waiting
         * @return Waiting instance configured with given timeout
         */
        public Waiting timeout(long timeout) {
            if (timeout == this.timeout) {
                return this;
            }
            Waiting copy = this.copy();
            copy.timeout = timeout;
            return copy;
        }

        /**
         * Returns Waiting object initialized with given subject of failure. It can be a Throwable (in that case it will
         * be used as cause) or it can be any other object (it will be converted to String and used as exception
         * message).
         * 
         * @param failureSubject
         *            Throwable (used in cause) or any other object (will be converted to expection message by its
         *            string value)
         * @return Waiting instance initialized with given failure subject
         */
        public Waiting failWith(Object failureSubject) {
            if (failureSubject == null) {
                if (this.failure == null) {
                    return this;
                }
            }
            Waiting copy = this.copy();
            copy.failure = failureSubject;
            copy.failureArgs = null;
            return copy;
        }

        /**
         * Returns preset instance of waiting (@see Waiting) with given failure message parametrized by given objects
         * 
         * If failure is set to null, timeout will not result to failure!
         * 
         * @param failureMessage
         *            character sequence that will be used as message of exception thrown in case of waiting timeout or
         *            null if waiting timeout shouldn't result to failure
         * @param args
         *            arguments to failureMessage which will be use to parametrization of failureMessage
         * @return Waiting instance initialized with given failureMessage and arguments
         */
        public Waiting failWith(CharSequence failureMessage, Object... args) {
            Waiting copy = this.copy();
            copy.failure = failureMessage;
            copy.failureArgs = args;
            return copy;
        }

        /**
         * Prepares a exception for failing the waiting
         * 
         * @return runtime exception
         */
        protected RuntimeException processFailure() {
            if (failure instanceof RuntimeException) {
                return (RuntimeException) failure;
            }

            if (failure instanceof CharSequence) {
                return new WaitTimeoutException((CharSequence) failure, failureArgs);
            }

            return new WaitTimeoutException(failure);
        }

        /**
         * Indicates timeout of waiting.
         */
        protected static class WaitTimeoutException extends RuntimeException {
            private static final long serialVersionUID = 6056785264760499779L;

            // failure subject - cannot be null
            private Object failure = "Waiting timed out";

            public WaitTimeoutException(Object failure) {
                if (failure != null) {
                    this.failure = failure;
                }
            }

            public WaitTimeoutException(CharSequence message, Object... args) {
                this.failure = format(message.toString(), args);
            }

            @Override
            public Throwable getCause() {
                if (failure instanceof Throwable) {
                    return ((Throwable) failure);
                }

                return super.getCause();
            }

            @Override
            public String getMessage() {
                if (failure instanceof Throwable) {
                    return ((Throwable) failure).getMessage();
                }
                return failure.toString();
            }
        }

        /**
         * Sets no failure after waiting timeout.
         * 
         * Waiting timeout with this preset dont result to failure!
         * 
         * @return Waiting instance initialized with no failure
         */
        public Waiting dontFail() {
            return failWith(null);
        }

        /**
         * Sets no delay by interval between start waiting and first test for conditions.
         * 
         * @return Waiting instance initialized with no delay
         */
        public Waiting noDelay() {
            return withDelay(false);
        }

        /**
         * Set if testing condition should be delayed of one interval after the start of waiting.
         * 
         * @param isDelayed
         *            true if condition testing should be delayed; false otherwise
         * @return Waiting instance initialized with delay if isDelayed is set to true; with no delay otherwise
         */
        public Waiting withDelay(boolean isDelayed) {
            if (isDelayed == this.isDelayed) {
                return this;
            }
            Waiting copy = this.copy();
            copy.isDelayed = isDelayed;
            return copy;
        }

        /**
         * Stars loop waiting to satisfy condition.
         * 
         * @param condition
         *            what wait for to be satisfied
         */
        public void until(Condition condition) {
            long start = System.currentTimeMillis();
            long end = start + this.timeout;
            boolean delay = this.isDelayed;
            while (System.currentTimeMillis() < end) {
                if (!delay && condition.isTrue()) {
                    return;
                }
                delay = false;
                try {
                    Thread.sleep(this.interval);
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

        public void until(JavaScriptCondition condition) {
            getCurrentSelenium().waitForCondition(condition.getJavaScriptCondition(), timeout);
        }

        /**
         * Waits for predefined amount of time.
         */
        public void waitForTimeout() {
            try {
                Thread.sleep(timeout);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
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
        public <T> void waitForChange(T oldValue, Retriever<T> retrieve) {
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
        public <T> T waitForChangeAndReturn(final T oldValue, final Retriever<T> retrieve) {
            final Vector<T> vector = new Vector<T>(1);

            this.until(new Condition() {
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

        public <T> void waitForChange(T oldValue, JavaScriptRetriever<T> retrieve) {
            JavaScript waitCondition = new JavaScript(format("{0} != '{1}'", retrieve.getJavaScriptRetrieve()
                .getAsString(), oldValue));
            getCurrentSelenium().waitForCondition(waitCondition, timeout);
        }

        public <T> T waitForChangeAndReturn(T oldValue, JavaScriptRetriever<T> retrieve) {
            final String oldValueString = retrieve.getConvertor().forwardConversion(oldValue);
            JavaScript waitingRetriever = new JavaScript(format("selenium.waitForCondition({0} != '{1}'); {0}",
                retrieve.getJavaScriptRetrieve().getAsString(), oldValueString));
            String retrieved = getCurrentSelenium().getEval(waitingRetriever);
            return retrieve.getConvertor().backwardConversion(retrieved);
        }

        /**
         * Tries to fail by throwing 'failure' throwable.
         * 
         * If failure is instance of RuntimeException, will be directly thrown. Otherwise will be failure clothe to
         * RuntimeException.
         * 
         * If failure is null, method wont fail.
         */
        private void fail() {
            if (failure != null) {
                throw processFailure();
            }
        }

        /**
         * This methods helps to make copies of current instance.
         * 
         * @return copy of current instance
         */
        private Waiting copy() {
            Waiting copy = new Waiting();
            copy.interval = this.interval;
            copy.timeout = this.timeout;
            copy.failure = this.failure;
            copy.failureArgs = this.failureArgs;
            copy.isDelayed = this.isDelayed;
            return copy;
        }
    }
}
