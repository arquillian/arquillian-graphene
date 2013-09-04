/**
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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
package org.jboss.arquillian.graphene.wait;

import com.google.common.base.Predicate;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import org.jboss.arquillian.graphene.fluent.FluentBase;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.Wait;

/**
 * This interface is a replication of
 * {@link org.openqa.selenium.support.ui.FluentWait}. It's here because of a
 * fluent API in Graphene.
 *
 * @param <ARG> The input type for each condition used with this instance.
 * @param <FLUENT> The type of value returned in the end of fluent chain.
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public interface FluentWait<ARG, FLUENT> extends Wait<ARG>, FluentBase<FLUENT> {

    /**
     * Sets the message to be displayed when time expires.
     *
     * @param message to be appended to default.
     * @return A self reference.
     */
    FluentWait<ARG, FLUENT> withMessage(String message);

    /**
     * Sets how long to wait for the evaluated condition to be true. The default
     * timeout is {@link #FIVE_HUNDRED_MILLIS}.
     *
     * @param duration The timeout duration.
     * @param unit The unit of time.
     * @return A self reference.
     */
    FluentWait<ARG, FLUENT> withTimeout(long duration, TimeUnit unit);

    /**
     * Sets how often the condition should be evaluated.
     *
     * <p> In reality, the interval may be greater as the cost of actually
     * evaluating a condition function is not factored in. The default polling
     * interval is {@link #FIVE_HUNDRED_MILLIS}.
     *
     * @param duration The timeout duration.
     * @param unit The unit of time.
     * @return A self reference.
     */
    FluentWait<ARG, FLUENT> pollingEvery(long duration, TimeUnit unit);

    /**
     * Configures this instance to ignore specific types of exceptions while
     * waiting for a condition. Any exceptions not whitelisted will be allowed
     * to propagate, terminating the wait.
     *
     * @param types The types of exceptions to ignore.
     * @return A self reference.
     */
    <K extends Throwable> FluentWait<ARG, FLUENT> ignoreAll(Collection<Class<? extends K>> types);

    /**
     * @see #ignoreAll(Collection)
     */
    <K extends Throwable> FluentWait<ARG, FLUENT> ignoring(Class<? extends Throwable> exceptionType);

    /**
     * @see #ignoreAll(Collection)
     */
    <K extends Throwable> FluentWait<ARG, FLUENT> ignoring(Class<? extends Throwable> firstType, Class<? extends Throwable> secondType);

    /**
     * Repeatedly applies this instance's input value to the given predicate until the timeout expires
     * or the predicate evaluates to true.
     *
     * @param isTrue The predicate to wait on.
     * @throws TimeoutException If the timeout expires.
     */
    void until(final Predicate<ARG> isTrue);

    /**
     * Returns the fluent condition builder. The builder automatically calls {@link #commit(java.lang.Object) }
     * which calls {@link #until(com.google.common.base.Predicate) }.
     */
    FluentBuilder<FLUENT> until();

    /**
     * Returns the fluent condition builder. The builder automatically calls {@link #commit(java.lang.Object)}
     * which calls {@link #until(com.google.common.base.Predicate) }.
     *
     * @param failMessage message used when the waiting fails
     */
    FluentBuilder<FLUENT> until(String failMessage);

}
