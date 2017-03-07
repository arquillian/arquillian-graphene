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
package org.jboss.arquillian.graphene.enricher;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import org.jboss.arquillian.graphene.proxy.Interceptor;
import org.jboss.arquillian.graphene.proxy.InvocationContext;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

import static org.jboss.arquillian.graphene.Graphene.waitGui;

public class StaleElementInterceptor implements Interceptor {

    @Override
    public Object intercept(final InvocationContext context) throws Throwable {
        final AtomicReference<Object> result = new AtomicReference<Object>();
        final AtomicReference<Throwable> failure = new AtomicReference<Throwable>();
        final AtomicReference<Throwable> staleness = new AtomicReference<Throwable>();
        try {
            waitGui(context.getGrapheneContext().getWebDriver()).until(new Function<WebDriver, Boolean>() {
                @Override
                public Boolean apply(WebDriver driver) {
                    try {
                        result.set(context.invoke());
                        return true;
                    } catch (StaleElementReferenceException e) {
                        staleness.set(e);
                        return false;
                    } catch (Throwable e) {
                        failure.set(e);
                        return true;
                    }
                }
            });
        } catch (TimeoutException e) {
            if (staleness.get() != null) {
                throw staleness.get();
            } else {
                throw e;
            }
        }

        if (failure.get() != null) {
            throw failure.get();
        }

        return result.get();
    }

    @Override
    public int getPrecedence() {
        return 0;
    }
}
