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
 * Interface for implementations of waiting for satisfaction of condition.
 * </p>
 * 
 * <p>
 * Implementations should keep contract of immutability.
 * </p>
 * 
 * @param <T>
 *            the end implementation of Waiting to return by methods on Waiting
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public interface Waiting<T extends Waiting<T>> {
    /**
     * Returns instance of waiting with same properties like this object and interval set to given interval.
     * 
     * @param interval
     *            in milliseconds that will be preset to returned instance of Waiting
     * @return Waiting instance configured with given interval
     */
    T interval(long interval);

    /**
     * Returns instance of waiting with same properties like this object and timeout set to given timeout.
     * 
     * @param timeout
     *            in milliseconds that will be preset to returned instance of Waiting
     * @return Waiting instance configured with given timeout
     */
    T timeout(long timeout);

    /**
     * <p>
     * Returns Waiting object initialized with given exception.
     * </p>
     * 
     * <p>
     * If the exception is instance of RuntimeException, it will be thrown in case of waiting timed out.
     * </p>
     * 
     * <p>
     * If the exception isn't instance of RuntimeException, the WaitingTimeoutException will be thrown with cause preset
     * to the given Throwable.
     * </p>
     * 
     * <p>
     * If failure is set to null, timeout will not result to failure!
     * </p>
     * 
     * @param exception
     *            the instance of RuntimeException to be thrown or any other Exception when the WaitTimeoutException
     *            should be thrown with this exception as cause
     * @return Waiting instance configured with given exception as cause of waiting timeout
     */
    T failWith(Exception exception);

    /**
     * <p>
     * Returns preset instance of waiting with given failure message parametrized by given objects.
     * </p>
     * 
     * <p>
     * To parametrize failure message, the
     * {@link org.jboss.test.selenium.utils.text.SimplifiedFormat#format(String, Object...)} will be used.
     * </p>
     * 
     * <p>
     * If failure is set to null, timeout will not result to failure!
     * </p>
     * 
     * @param failureMessage
     *            character sequence that will be used as message of exception thrown in case of waiting timeout or null
     *            if waiting timeout shouldn't result to failure
     * @param arguments
     *            arguments to failureMessage which will be use in parametrization of failureMessage
     * @return Waiting instance initialized with given failureMessage and arguments
     */
    T failWith(CharSequence failureMessage, Object... arguments);

    /**
     * Sets no failure after waiting timeout.
     * 
     * Waiting timeout with this preset don't result to failure!
     * 
     * @return Waiting instance initialized with no failure
     */
    T dontFail();

    /**
     * Sets no delay between start of waiting and first test for conditions.
     * 
     * @return Waiting instance initialized with no delay
     */
    T noDelay();

    /**
     * <p>
     * Set if testing condition should be delayed of one interval after the start of waiting.
     * </p>
     * 
     * <p>
     * The length of delay is one interval (see {@link #interval(long)}).
     * </p>
     * 
     * @param isDelayed
     *            true if start of condition testing should be delayed; false otherwise
     * @return Waiting instance initialized with the delay before start of testing conditions if isDelayed is set to
     *         true; with no delay otherwise
     */
    T withDelay(boolean isDelayed);

    /**
     * Waits for predefined amount of time (see {@link #timeout(long)}).
     */
    void waitForTimeout();
}
