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

import java.time.Duration;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.openqa.selenium.WebDriver;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public interface WebDriverWait<FLUENT> extends FluentWait<WebDriver, FLUENT> {

    FluentWait<WebDriver, FLUENT> withMessage(String message);

    FluentWait<WebDriver, FLUENT> withTimeout(long duration, TimeUnit unit);

    FluentWait<WebDriver, FLUENT> withTimeout(Duration duration);

    FluentWait<WebDriver, FLUENT> pollingEvery(long duration, TimeUnit unit);

    FluentWait<WebDriver, FLUENT> pollingEvery(Duration duration);

    <K extends Throwable> FluentWait<WebDriver, FLUENT> ignoreAll(Collection<Class<? extends K>> types);

    <K extends Throwable> FluentWait<WebDriver, FLUENT> ignoring(Class<? extends Throwable> exceptionType);

    <K extends Throwable> FluentWait<WebDriver, FLUENT> ignoring(Class<? extends Throwable> firstType, Class<? extends Throwable> secondType);

    FluentBuilder<FLUENT> until();

    FluentBuilder<FLUENT> until(String failMessage);

    <T> T until(Function<? super WebDriver, T> isTrue);

    <ACTION> FLUENT commit(ACTION action);
}
