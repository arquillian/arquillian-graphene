/**
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
/*
 *
 */
package org.jboss.arquillian.ajocado.waiting.selenium;

import java.util.Vector;

import org.jboss.arquillian.ajocado.waiting.DefaultWaiting;

/**
 * Implementation of waiting for satisfaction of conditions on page using polling the Selenium API with given question.
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class DefaultSeleniumWaiting extends DefaultWaiting<SeleniumWaiting> implements SeleniumWaiting {

    /**
     * Stars loop waiting to satisfy condition.
     *
     * @param condition what wait for to be satisfied
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
     * @param <T> type of value what we are waiting for change
     * @param oldValue value that we are waiting for change
     * @param retriever implementation of retrieving actual value
     */
    public <T> void waitForChange(T oldValue, SeleniumRetriever<T> retriever) {
        waitForChangeAndReturn(oldValue, retriever);
    }

    /**
     * <p>
     * Waits until Retrieve's implementation doesn't retrieve value other than value stored by initialization in retriever.
     * </p>
     *
     * <p>
     * After retrieving, new value will be associated with given Retriever.
     * </p>
     *
     * <p>
     * Note that Retriever needs to be initialized first by one of methods
     * {@link org.jboss.arquillian.ajocado.waiting.retrievers.Retriever#initializeValue()} or
     * {@link org.jboss.arquillian.ajocado.waiting.retrievers.Retriever#setValue(Object)}.
     * </p>
     *
     * @param <T> type of value what we are waiting for change
     * @param retriever implementation of retrieving actual value
     */
    public <T> void waitForChange(SeleniumRetriever<T> retriever) {
        T newValue = waitForChangeAndReturn(retriever.getValue(), retriever);
        retriever.setValue(newValue);
    }

    /**
     * Waits until Retrieve's implementation doesn't retrieve value other than oldValue and this new value returns.
     *
     * @param <T> type of value what we are waiting for change
     * @param oldValue value that we are waiting for change
     * @param retriever implementation of retrieving actual value
     * @return new retrieved value
     */
    public <T> T waitForChangeAndReturn(final T oldValue, final SeleniumRetriever<T> retriever) {
        final Vector<T> vector = new Vector<T>(1);

        this.until(new SeleniumCondition() {
            @Override
            public boolean isTrue() {
                vector.add(0, retriever.retrieve());
                if (oldValue == null) {
                    return vector.get(0) != null;
                }
                return !oldValue.equals(vector.get(0));
            }
        });

        return vector.get(0);
    }

    /**
     * <p>
     * Waits until Retrieve's implementation doesn't retrieve value other than value stored by initialization in retriever.
     * </p>
     *
     * <p>
     * After retrieving, new value will be associated with given Retriever.
     * </p>
     *
     * <p>
     * Note that Retriever needs to be initialized first by one of methods
     * {@link org.jboss.arquillian.ajocado.waiting.retrievers.Retriever#initializeValue()} or
     * {@link org.jboss.arquillian.ajocado.waiting.retrievers.Retriever#setValue(Object)}.
     * </p>
     *
     * @param <T> type of value what we are waiting for change
     * @param retriever implementation of retrieving actual value
     * @return new retrieved value
     */
    public <T> T waitForChangeAndReturn(final SeleniumRetriever<T> retriever) {
        T newValue = waitForChangeAndReturn(retriever.getValue(), retriever);
        retriever.setValue(newValue);
        return newValue;
    }
}