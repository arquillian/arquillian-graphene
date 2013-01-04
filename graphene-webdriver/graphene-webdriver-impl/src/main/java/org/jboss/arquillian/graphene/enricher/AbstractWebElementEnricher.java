/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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

import java.util.ArrayList;
import java.util.List;

import org.jboss.arquillian.graphene.intercept.InterceptorBuilder;
import org.jboss.arquillian.graphene.proxy.GrapheneProxy;
import org.jboss.arquillian.graphene.proxy.GrapheneProxy.FutureTarget;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.jboss.arquillian.graphene.proxy.Interceptor;
import org.jboss.arquillian.graphene.proxy.InvocationContext;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.internal.WrapsElement;

/**
 * This class should help you to implement {@link org.jboss.arquillian.graphene.spi.enricher.SearchContextTestEnricher}
 * working with {@link WebElement} instances.
 *
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public abstract class AbstractWebElementEnricher extends AbstractSearchContextEnricher {

    protected final WebElement createWebElement(final By by, final SearchContext searchContext) {
        return createWebElement(new FutureTarget() {
            @Override
            public Object getTarget() {
                return searchContext.findElement(by);
            }
        });
    }

    protected final WebElement createWebElement(final By by, final SearchContext searchContext, final int indexInList) {
        return createWebElement(new FutureTarget() {
            @Override
            public Object getTarget() {
                return searchContext.findElements(by).get(indexInList);
            }
        });
    }

    protected final WebElement createWebElement(final FutureTarget target) {
        WebElement result = GrapheneProxy.getProxyForFutureTarget(target, WebElement.class, Locatable.class, WrapsElement.class);

        final GrapheneProxyInstance proxy = (GrapheneProxyInstance) result;

        Interceptor wrapsElementInterceptor = new Interceptor() {
            public Object intercept(InvocationContext context) throws Throwable {
                return proxy.unwrap();
            }
        };

        InterceptorBuilder interceptorBuilder = new InterceptorBuilder();
        interceptorBuilder.interceptInvocation(WrapsElement.class, wrapsElementInterceptor).getWrappedElement();

        proxy.registerInterceptor(interceptorBuilder.build());

        return result;
    }

    public List<WebElement> createWebElements(final By by, final SearchContext searchContext) {
        return GrapheneProxy.getProxyForFutureTarget(new FutureTarget() {
            @Override
            public Object getTarget() {
                List<WebElement> result = new ArrayList<WebElement>();
                List<WebElement> elements = searchContext.findElements(by);
                for (int i = 0; i < elements.size(); i++) {
                    result.add(createWebElement(by, searchContext, i));
                }
                return result;
            }
        }, List.class);
    }

}
