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

import org.openqa.selenium.WebDriver;

import java.time.Duration;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class WebDriverWaitImpl<FLUENT> implements WebDriverWait<FLUENT> {

    private final org.openqa.selenium.support.ui.WebDriverWait wait;
    private final FLUENT fluent;

    protected WebDriverWaitImpl(org.openqa.selenium.support.ui.WebDriverWait wait, FLUENT fluent) {
        this.wait = wait;
        this.fluent = fluent;
    }

    public WebDriverWaitImpl(FLUENT fluent, WebDriver driver, long timeOutInSeconds) {
        this(new org.openqa.selenium.support.ui.WebDriverWait(driver, timeOutInSeconds), fluent);
    }

    @Override
    public FluentWait<WebDriver, FLUENT> withMessage(String message) {
        wait.withMessage(message);
        return this;
    }

    @Override
    public FluentWait<WebDriver, FLUENT> withTimeout(long duration, TimeUnit unit) {
        wait.withTimeout(Duration.ofMillis(unit.toMillis(duration)));
        return this;
    }

    @Override
    public FluentWait<WebDriver, FLUENT> withTimeout(Duration duration) {
        wait.withTimeout(duration);
        return this;
    }

    @Override
    public FluentWait<WebDriver, FLUENT> pollingEvery(long duration, TimeUnit unit) {
        wait.pollingEvery(Duration.ofMillis(unit.toMillis(duration)));
        return this;
    }

    @Override
    public FluentWait<WebDriver, FLUENT> pollingEvery(Duration duration) {
        wait.pollingEvery(duration);
        return this;
    }

    @Override
    public <K extends Throwable> FluentWait<WebDriver, FLUENT> ignoreAll(Collection<Class<? extends K>> types) {
        wait.ignoreAll(types);
        return this;
    }

    @Override
    public <K extends Throwable> FluentWait<WebDriver, FLUENT> ignoring(Class<? extends Throwable> exceptionType) {
        wait.ignoring(exceptionType);
        return this;
    }

    @Override
    public <K extends Throwable> FluentWait<WebDriver, FLUENT> ignoring(Class<? extends Throwable> firstType,
        Class<? extends Throwable> secondType) {
        wait.ignoring(firstType, secondType);
        return this;
    }

    @Override
    public FluentBuilder<FLUENT> until() {
        return new FluentBuilderImpl<FLUENT>((WebDriverWait<FLUENT>) this);
    }

    @Override
    public FluentBuilder<FLUENT> until(String failMessage) {
        return new FluentBuilderImpl<FLUENT>((WebDriverWait<FLUENT>) withMessage(failMessage));
    }

    @Override
    public <T> T until(Function<? super WebDriver, T> isTrue) {
        return wait.until(isTrue);
    }

    @Override
    public <ACTION> FLUENT commit(ACTION action) {
        if (action instanceof Function) {
            until((Function) action);
        } else {
            throw new UnsupportedOperationException("TODO");
        }
        return fluent;
    }

}
