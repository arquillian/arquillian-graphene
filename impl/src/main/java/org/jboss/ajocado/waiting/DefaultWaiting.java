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
package org.jboss.ajocado.waiting;

/**
 * Abstract implementation of immutable class with purpose of waiting with customizable timeout, interval, and failure
 * behaviour and delay on start of waiting.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 * @param <T>
 *            the end implementation of DefaultWaiting as the return type for setter methods
 */
@SuppressWarnings("unchecked")
public abstract class DefaultWaiting<T extends DefaultWaiting<T>> implements Waiting<T>, Cloneable {

    /**
     * Indicates when the first test for the condition should be delayed after waiting starts.
     */
    private boolean isDelayed = true;

    /**
     * Interval between tries to test condition for satisfaction
     */
    private long interval = Wait.DEFAULT_INTERVAL;

    /**
     * Timeout of whole waiting procedure
     */
    private long timeout = Wait.DEFAULT_TIMEOUT;

    /**
     * Failure indicates that waiting timeouted.
     * 
     * If is set to null, no failure will be thrown after timeout.
     */
    private Object failure = new WaitTimeoutException("Waiting timed out");

    /**
     * Arguments to format failure message if it is string value and should be formatted
     */
    private Object[] failureArgs;

    /**
     * Returns the interval set for this object.
     * 
     * @return the set interval
     */
    protected final long getInterval() {
        return interval;
    }

    /**
     * Returns the timeout set for this object.
     * 
     * @return the timeout set for this object
     */
    protected final long getTimeout() {
        return timeout;
    }

    /**
     * Returns if this waiting's start is delayed.
     * 
     * @return if this waiting's start is delayed
     */
    protected final boolean isDelayed() {
        return isDelayed;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.ajocado.waiting.Waiting#interval(long)
     */
    public final T interval(long interval) {
        if (interval == this.interval) {
            return (T) this;
        }
        T copy = this.copy();
        copy.interval = interval;
        return copy;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.ajocado.waiting.Waiting#timeout(long)
     */
    public final T timeout(long timeout) {
        if (timeout == this.timeout) {
            return (T) this;
        }
        T copy = this.copy();
        copy.timeout = timeout;
        return copy;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.ajocado.waiting.Waiting#failWith(java.lang.Exception)
     */
    public final T failWith(Exception exception) {
        if (exception == null && this.failure == null) {
            return (T) this;
        }
        T copy = this.copy();
        copy.failure = exception;
        copy.failureArgs = null;
        return copy;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.ajocado.waiting.Waiting#failWith(java.lang.CharSequence, java.lang.Object[])
     */
    public final T failWith(CharSequence failureMessage, Object... arguments) {
        T copy = this.copy();
        copy.failure = failureMessage;
        copy.failureArgs = arguments;
        return copy;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.ajocado.waiting.Waiting#dontFail()
     */
    public final T dontFail() {
        return failWith(null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.ajocado.waiting.Waiting#noDelay()
     */
    public final T noDelay() {
        return withDelay(false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.ajocado.waiting.Waiting#withDelay(boolean)
     */
    public final T withDelay(boolean isDelayed) {
        if (isDelayed == this.isDelayed) {
            return (T) this;
        }
        T copy = this.copy();
        copy.isDelayed = isDelayed;
        return copy;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.ajocado.waiting.Waiting#waitForTimeout()
     */
    public final void waitForTimeout() {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Tries to fail by throwing 'failure' throwable.
     * 
     * If failure is instance of RuntimeException, will be directly thrown. Otherwise will be failure clothe to
     * RuntimeException.
     * 
     * If failure is null, method wont fail.
     */
    protected final void fail() {
        if (failure != null) {
            throw prepareFailure();
        }
    }

    /**
     * Prepares a exception for failing the waiting
     * 
     * @return runtime exception
     */
    private RuntimeException prepareFailure() {
        if (failure instanceof RuntimeException) {
            return (RuntimeException) failure;
        }

        if (failure instanceof CharSequence) {
            return new WaitTimeoutException((CharSequence) failure, failureArgs);
        }

        return new WaitTimeoutException((Exception) failure);
    }

    /**
     * This methods helps to make copies of current instance.
     * 
     * @return copy of current instance
     */
    private T copy() {
        try {
            return (T) this.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException(e);
        }
    }
}