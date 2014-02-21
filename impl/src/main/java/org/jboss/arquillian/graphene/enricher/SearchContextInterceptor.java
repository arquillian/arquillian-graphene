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

import java.lang.reflect.Method;

import org.jboss.arquillian.graphene.proxy.GrapheneProxy;
import org.jboss.arquillian.graphene.proxy.Interceptor;
import org.jboss.arquillian.graphene.proxy.InvocationContext;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class SearchContextInterceptor implements Interceptor {

    @Override
    public Object intercept(final InvocationContext context) throws Throwable {
        GrapheneProxy.FutureTarget future = new GrapheneProxy.FutureTarget() {
            @Override
            public Object getTarget() {
                return context.getProxy();
            }
        };
        if (methodsEqual(context.getMethod(), SearchContext.class.getDeclaredMethod("findElement", By.class))) {
            return WebElementUtils.findElement(context.getGrapheneContext(), (By) context.getArguments()[0], future);
        } else if (methodsEqual(context.getMethod(), SearchContext.class.getDeclaredMethod("findElements", By.class))) {
            return WebElementUtils.findElementsLazily(context.getGrapheneContext(), (By) context.getArguments()[0], future);
        } else {
            return context.invoke();
        }
    }

    protected static boolean methodsEqual(Method first, Method second) {
        if (first == second) {
            return true;
        }
        if (first == null || second == null) {
            return false;
        }
        if (!first.getName().equals(second.getName())) {
            return false;
        }
        if (first.getParameterTypes().length != second.getParameterTypes().length) {
            return false;
        }
        for (int i=0; i < first.getParameterTypes().length; i++) {
            if (!first.getParameterTypes()[i].equals(second.getParameterTypes()[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int getPrecedence() {
        return 1;
    }
}
