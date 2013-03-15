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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.arquillian.graphene.intercept.InterceptorBuilder;
import org.jboss.arquillian.graphene.proxy.GrapheneProxy;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.support.ByIdOrName;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public final class WebElementUtils {

    private final static Logger LOGGER = Logger.getLogger(WebElementUtils.class.getName());
    private final static String EMPTY_FIND_BY_WARNING = " Be aware of the fact that fields anotated with empty "
            + "@FindBy were located by default strategy, which is ByIdOrName with field name as locator! ";

    private WebElementUtils() {
    }

    public static WebElement findElement(final By by, final GrapheneProxy.FutureTarget searchContextFuture) {
        // Here the web element has to be found to ensure that SearchContext throws
        // NoSuchElementException if there is no element with the given By locator.
        dropProxyAndFindElement(by, (SearchContext) searchContextFuture.getTarget());
        return findElement(new GrapheneProxy.FutureTarget() {
            @Override
            public Object getTarget() {
                return dropProxyAndFindElement(by, (SearchContext) searchContextFuture.getTarget());
            }
        });
    }

    public static List<WebElement> findElementsLazily(final By by, final GrapheneProxy.FutureTarget searchContextFuture) {
        return GrapheneProxy.getProxyForFutureTarget(new GrapheneProxy.FutureTarget() {
            @Override
            public Object getTarget() {
                List<WebElement> result = new ArrayList<WebElement>();
                List<WebElement> elements = dropProxyAndFindElements(by, (SearchContext) searchContextFuture.getTarget());
                if ((by instanceof ByIdOrName) && (elements.isEmpty())) {
                    LOGGER.log(Level.WARNING, EMPTY_FIND_BY_WARNING);
                }
                for (int i = 0; i < elements.size(); i++) {
                    result.add(findElementLazily(by, searchContextFuture, i));
                }
                return result;
            }
        }, List.class);
    }

    public static WebElement findElementLazily(final By by, final GrapheneProxy.FutureTarget searchContextFuture, final int indexInList) {
        return findElement(new GrapheneProxy.FutureTarget() {
            @Override
            public Object getTarget() {
                return dropProxyAndFindElements(by, (SearchContext) searchContextFuture.getTarget()).get(indexInList);
            }
        });
    }

    public static WebElement findElementLazily(final By by, final SearchContext searchContext, final int indexInList) {
        return findElement(new GrapheneProxy.FutureTarget() {
            @Override
            public Object getTarget() {
                return dropProxyAndFindElements(by, searchContext).get(indexInList);
            }
        });
    }

    public static WebElement findElementLazily(final By by, final SearchContext searchContext) {
        return findElement(new GrapheneProxy.FutureTarget() {
            @Override
            public Object getTarget() {
                try {
                    return dropProxyAndFindElement(by, searchContext);
                } catch (NoSuchElementException ex) {
                    throw new NoSuchElementException((by instanceof ByIdOrName ? EMPTY_FIND_BY_WARNING : "") + ex.getMessage(),
                            ex);
                }
            }
        });
    }

    public static List<WebElement> findElementsLazily(final By by, final SearchContext searchContext) {
        return findElementsLazily(by, new GrapheneProxy.FutureTarget() {
            @Override
            public Object getTarget() {
                return searchContext;
            }
        });
    }

    protected static WebElement findElement(final GrapheneProxy.FutureTarget target) {
        final WebElement element = GrapheneProxy.getProxyForFutureTarget(target, WebElement.class, Locatable.class,
                WrapsElement.class);
        final GrapheneProxyInstance elementProxy = (GrapheneProxyInstance) element;

        InterceptorBuilder b = new InterceptorBuilder();
        b.interceptInvocation(WrapsElement.class, new WrapsElementInterceptor(elementProxy)).getWrappedElement();

        elementProxy.registerInterceptor(b.build());
        elementProxy.registerInterceptor(new StaleElementInterceptor());
        elementProxy.registerInterceptor(new SearchContextInterceptor());
        return element;
    }

    protected static WebElement dropProxyAndFindElement(By by, SearchContext searchContext) {
        if (searchContext instanceof GrapheneProxyInstance) {
            return ((SearchContext) ((GrapheneProxyInstance) searchContext).unwrap()).findElement(by);
        } else {
            return searchContext.findElement(by);
        }
    }

    protected static List<WebElement> dropProxyAndFindElements(By by, SearchContext searchContext) {
        if (searchContext instanceof GrapheneProxyInstance) {
            return ((SearchContext) ((GrapheneProxyInstance) searchContext).unwrap()).findElements(by);
        } else {
            return searchContext.findElements(by);
        }
    }

}
