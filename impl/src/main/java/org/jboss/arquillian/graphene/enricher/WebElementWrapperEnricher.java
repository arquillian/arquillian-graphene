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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.arquillian.graphene.enricher;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.elements.GrapheneSelect;
import org.jboss.arquillian.graphene.enricher.exception.GrapheneTestEnricherException;
import org.jboss.arquillian.graphene.findby.FindByUtilities;
import org.jboss.arquillian.graphene.proxy.GrapheneContextualHandler;
import org.jboss.arquillian.graphene.proxy.GrapheneProxy;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.jboss.arquillian.graphene.spi.configuration.GrapheneConfiguration;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class WebElementWrapperEnricher extends AbstractSearchContextEnricher {

    @Inject
    private Instance<GrapheneConfiguration> configuration;

    public WebElementWrapperEnricher() {
    }

    @Override
    public void enrich(final SearchContext searchContext, Object target) {
        List<Field> fields = FindByUtilities.getListOfFieldsAnnotatedWithFindBys(target);
        for (Field field : fields) {
            try {
                if (isValidClass(field.getType())) {
                    final SearchContext localSearchContext;
                    GrapheneContext grapheneContext = searchContext == null ? null : ((GrapheneProxyInstance) searchContext)
                        .getGrapheneContext();
                    if (grapheneContext == null) {
                        grapheneContext = GrapheneContext.getContextFor(ReflectionHelper.getQualifier(field.getAnnotations()));
                        localSearchContext = grapheneContext.getWebDriver(SearchContext.class);
                    } else {
                        localSearchContext = searchContext;
                    }
                    final By rootBy = FindByUtilities.getCorrectBy(field, configuration.get()
                        .getDefaultElementLocatingStrategy());
                    Object wrapper;
                    try {
                        Class<?> type = field.getType();
                        wrapper = createWrapper(grapheneContext, type,
                            WebElementUtils.findElementLazily(rootBy, localSearchContext));
                    } catch (Exception e) {
                        throw new GrapheneTestEnricherException("Can't instantiate element wrapper " + target.getClass() + "."
                            + field.getName() + " of type " + field.getType(), e);
                    }
                    try {
                        setValue(field, target, wrapper);
                    } catch (Exception e) {
                        throw new GrapheneTestEnricherException("Can't set a value to the " + target.getClass() + "."
                            + field.getName() + ".", e);
                    }
                } else if (field.getType().isAssignableFrom(List.class) && isValidClass(getListType(field))) {
                    final SearchContext localSearchContext;
                    GrapheneContext grapheneContext = searchContext == null ? null : ((GrapheneProxyInstance) searchContext)
                        .getGrapheneContext();
                    if (grapheneContext == null) {
                        grapheneContext = GrapheneContext.getContextFor(ReflectionHelper.getQualifier(field.getAnnotations()));
                        localSearchContext = grapheneContext.getWebDriver(SearchContext.class);
                    } else {
                        localSearchContext = searchContext;
                    }
                    final By rootBy = FindByUtilities.getCorrectBy(field, configuration.get()
                        .getDefaultElementLocatingStrategy());
                    try {
                        Class<?> type = getListType(field);
                        Object wrappers = createWrappers(grapheneContext, type,
                            WebElementUtils.findElementsLazily(rootBy, localSearchContext));
                        setValue(field, target, wrappers);
                    } catch (Exception e) {
                        throw new GrapheneTestEnricherException("Can't set a value to the " + target.getClass() + "."
                            + field.getName() + ".", e);
                    }
                }
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
    }

    @Override
    public Object[] resolve(SearchContext searchContext, Method method, Object[] resolvedParams) {
        return resolvedParams;
    }

    protected <T> T createWrapper(GrapheneContext grapheneContext, final Class<T> type, final WebElement element) {
        T wrapper = GrapheneProxy.getProxyForHandler(
                GrapheneContextualHandler.forFuture(grapheneContext, () -> {
                    try {
                        return instantiate(type, element);
                    } catch (Exception e) {
                        throw new IllegalStateException("Can't instantiate the " + type, e);
                    }
                }), type);
        return wrapper;
    }

    @SuppressWarnings("unchecked")
    protected <T> List<T> createWrappers(GrapheneContext grapheneContext, final Class<T> type, final List<WebElement> elements) {
        List<T> wrapper = GrapheneProxy.getProxyForHandler(
                GrapheneContextualHandler.forFuture(grapheneContext, () -> {
                    try {
                        List<T> target = new ArrayList<T>();
                        for (WebElement element : elements) {
                            target.add((T) instantiate(type, element));
                        }
                        return target;
                    } catch (Exception e) {
                        throw new IllegalStateException("Can't instantiate the " + type, e);
                    }
                }), List.class);
        return wrapper;
    }

    protected final boolean isValidClass(Class<?> clazz) {
        Class<?> outerClass = clazz.getDeclaringClass();
        if (outerClass == null || Modifier.isStatic(clazz.getModifiers())) {
            if (clazz.equals(GrapheneElement.class) || clazz.equals(GrapheneSelect.class)) {
                return true;
            } else {
                return ReflectionHelper.hasConstructor(clazz, WebElement.class);
            }
        } else {
            return ReflectionHelper.hasConstructor(clazz, outerClass, WebElement.class);
        }
    }

    @Override
    public int getPrecedence() {
        return 1;
    }
}
