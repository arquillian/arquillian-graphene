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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.GrapheneElementImpl;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.graphene.intercept.InterceptorBuilder;
import org.jboss.arquillian.graphene.proxy.GrapheneContextualHandler;
import org.jboss.arquillian.graphene.proxy.GrapheneProxy;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsElement;
import org.openqa.selenium.interactions.Locatable;
import org.openqa.selenium.support.ByIdOrName;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public final class WebElementUtils {

    private static final Logger LOGGER = Logger.getLogger(WebElementUtils.class.getName());
    private static final String EMPTY_FIND_BY_WARNING = " Be aware of the fact that fields anotated with empty "
        + "@FindBy were located by default strategy, which is ByIdOrName with field name as locator! ";

    private static final Class<?>[] INTERFACES_PROXY_SHOULD_IMPLEMENT = {Locatable.class,
        WrapsElement.class, GrapheneElement.class};

    private WebElementUtils() {
    }

    public static WebElement findElement(GrapheneContext context, final By by, final GrapheneProxy.FutureTarget searchContextFuture) {
        // Here the web element has to be found to ensure that SearchContext throws
        // NoSuchElementException if there is no element with the given By locator.
        dropProxyAndFindElement(by, (SearchContext) searchContextFuture.getTarget());
        return findElement(context, new GrapheneProxy.FutureTarget() {
            @Override
            public Object getTarget() {
                return dropProxyAndFindElement(by, (SearchContext) searchContextFuture.getTarget());
            }
        });
    }

    public static List<WebElement> findElementsLazily(final GrapheneContext context, final By by, final GrapheneProxy.FutureTarget searchContextFuture) {
        List<WebElement> elements = GrapheneProxy.getProxyForHandler(GrapheneContextualHandler.forFuture(context, new GrapheneProxy.FutureTarget() {
            @Override
            public Object getTarget() {
                List<WebElement> result = new ArrayList<WebElement>();
                List<WebElement> elements = dropProxyAndFindElements(by, (SearchContext) searchContextFuture.getTarget());
                if ((by instanceof ByIdOrName) && (elements.isEmpty())) {
                    LOGGER.log(Level.WARNING, EMPTY_FIND_BY_WARNING);
                }
                for (int i = 0; i < elements.size(); i++) {
                    WebElement foundElement = findElementLazily(context, by, searchContextFuture, i);
                    if (foundElement != null) {
                        result.add(foundElement);
                    }
                }
                return result;
            }
        }), List.class);
        GrapheneProxyInstance proxy = (GrapheneProxyInstance) elements;
        proxy.registerInterceptor(new StaleElementInterceptor());
        return elements;
    }

    public static WebElement findElementLazily(GrapheneContext context, final By by, final GrapheneProxy.FutureTarget searchContextFuture, final int indexInList) {
        return findElement(context, new GrapheneProxy.FutureTarget() {
            @Override
            public Object getTarget() {
                List<WebElement> elements = dropProxyAndFindElements(by, (SearchContext) searchContextFuture.getTarget());
                if (elements.size() > indexInList) {
                    return elements.get(indexInList);
                }
                return null;
            }
        });
    }

    public static WebElement findElementLazily(final By by, final SearchContext searchContext, final int indexInList) {
        return findElement(getContext(searchContext), new GrapheneProxy.FutureTarget() {
            @Override
            public Object getTarget() {
                List<WebElement> elements = dropProxyAndFindElements(by, searchContext);
                if (elements.size() > indexInList) {
                    return elements.get(indexInList);
                }
                return null;
            }
        });
    }

    public static WebElement findElementLazily(final By by, final SearchContext searchContext) {
        return findElement(getContext(searchContext), new GrapheneProxy.FutureTarget() {
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
        return findElementsLazily(getContext(searchContext), by, new GrapheneProxy.FutureTarget() {
            @Override
            public Object getTarget() {
                return searchContext;
            }
        });
    }

    protected static WebElement findElement(GrapheneContext context, final GrapheneProxy.FutureTarget target) {
        final WebElement element = GrapheneProxy.getProxyForFutureTarget(context, target, WebElement.class, INTERFACES_PROXY_SHOULD_IMPLEMENT);
        final GrapheneProxyInstance elementProxy = (GrapheneProxyInstance) element;

        InterceptorBuilder b = new InterceptorBuilder();
        b.interceptInvocation(WrapsElement.class, new WrapsElementInterceptor(elementProxy)).getWrappedElement();

        elementProxy.registerInterceptor(b.build());
        elementProxy.registerInterceptor(new StaleElementInterceptor());
        elementProxy.registerInterceptor(new SearchContextInterceptor());
        return element;
    }

    protected static WebElement dropProxyAndFindElement(By by, SearchContext searchContext) {
        WebElement result;
        if (searchContext instanceof GrapheneProxyInstance) {
            SearchContext unwrapped = ((GrapheneProxyInstance) searchContext).unwrap();
            result = unwrapped.findElement(by);
        } else {
            result = searchContext.findElement(by);
        }
        return new GrapheneElementImpl(result);
    }

    protected static List<WebElement> dropProxyAndFindElements(By by, SearchContext searchContext) {
        List<WebElement> webElements;
        if (searchContext instanceof GrapheneProxyInstance) {
            if (by instanceof ByJQuery) {
                webElements = by.findElements(searchContext);
            } else {
                webElements = ((SearchContext) ((GrapheneProxyInstance) searchContext).unwrap()).findElements(by);
            }
        } else {
            webElements = searchContext.findElements(by);
        }
        List<WebElement> result = new ArrayList<WebElement>(webElements.size());
        for (WebElement webElement : webElements) {
            result.add(new GrapheneElementImpl(webElement));
        }
        return result;
    }

    protected static GrapheneContext getContext(Object object) {
        if (!GrapheneProxy.isProxyInstance(object)) {
            throw new IllegalArgumentException("The parameter [object] has to be instance of " + GrapheneProxyInstance.class.getName() + ", but it is not. The given object is " + object + ".");
        }
        return ((GrapheneProxyInstance) object).getGrapheneContext();
    }

}
