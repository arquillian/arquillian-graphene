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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.elements.GrapheneSelect;
import org.jboss.arquillian.graphene.elements.GrapheneSelectImpl;
import org.jboss.arquillian.graphene.findby.FindByUtilities;
import org.jboss.arquillian.graphene.proxy.GrapheneContextualHandler;
import org.jboss.arquillian.graphene.proxy.GrapheneProxy;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.jboss.arquillian.graphene.spi.configuration.GrapheneConfiguration;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class GrapheneSelectEnricher extends AbstractSearchContextEnricher {

    @Inject
    private Instance<GrapheneConfiguration> configuration;

    @Override
    public void enrich(final SearchContext searchContext, Object target) {
            List<Field> fields = FindByUtilities.getListOfFieldsAnnotatedWithFindBys(target);
            for (Field field : fields) {
                GrapheneContext grapheneContext = searchContext == null ? null : ((GrapheneProxyInstance) searchContext).getGrapheneContext();
                final SearchContext localSearchContext;
                if (grapheneContext == null) {
                    grapheneContext = GrapheneContext.getContextFor(ReflectionHelper.getQualifier(field.getAnnotations()));
                    localSearchContext = grapheneContext.getWebDriver(SearchContext.class);
                } else {
                    localSearchContext = searchContext;
                }
                //by should never be null, by default it is ByIdOrName using field name
                By by = FindByUtilities.getCorrectBy(field, configuration.get().getDefaultElementLocatingStrategy());
                // GrapheneSelect
                if (field.getType().isAssignableFrom(GrapheneSelect.class)) {
                    WebElement element = WebElementUtils.findElementLazily(by, localSearchContext);
                    setValue(field, target, new GrapheneSelectImpl(element));
                    System.out.println(">>> HELLO");
                }
            }
    }

    @Override
    public Object[] resolve(SearchContext searchContext, Method method, Object[] resolvedParams) {
        return resolvedParams;
    }

    @Override
    public int getPrecedence() {
        return 1;
    }

    protected <T> T createWrapper(GrapheneContext grapheneContext, final Class<T> type, final WebElement element)
            throws Exception {
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
}
