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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.LinkedList;
import java.util.List;

import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.enricher.exception.PageObjectInitializationException;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.graphene.proxy.GrapheneContextualHandler;
import org.jboss.arquillian.graphene.proxy.GrapheneProxy;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.openqa.selenium.SearchContext;

/**
 * Enricher injecting page objects to the fields of the given object.
 *
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class PageObjectEnricher extends AbstractSearchContextEnricher {

    @Override
    public void enrich(final SearchContext searchContext, Object target) {

        List<Field> fields = new LinkedList<Field>();
        fields.addAll(ReflectionHelper.getFieldsWithAnnotation(target.getClass(), Page.class));
        for (Field field : fields) {
            Object page = createPage(searchContext, field.getAnnotations(), target, field, null, null);
            setValue(field, target, page);
        }
    }

    @Override
    public Object[] resolve(SearchContext searchContext, Method method, Object[] resolvedParams) {
        StringBuffer errorMsgBegin = new StringBuffer("");
        List<Object[]> paramCouple = new LinkedList<Object[]>();
        paramCouple.addAll(ReflectionHelper.getParametersWithAnnotation(method, Page.class));

        for (int i = 0; i < resolvedParams.length; i++) {
            if (paramCouple.get(i) != null) {
                Class<?> param = (Class<?>) paramCouple.get(i)[0];
                Annotation[] parameterAnnotations = (Annotation[]) paramCouple.get(i)[1];
                Object page = createPage(searchContext, parameterAnnotations, null, null, method, param);
                resolvedParams[i] = page;
            }
        }
        return resolvedParams;
    }

    private Object createPage(SearchContext searchContext, Annotation[] annotations, Object target, Field field,
        Method method, Class<?> param) {
        StringBuffer errorMsgBegin = new StringBuffer("");

        GrapheneContext grapheneContext =
            searchContext == null ? null : ((GrapheneProxyInstance) searchContext).getGrapheneContext();
        final SearchContext localSearchContext;
        if (grapheneContext == null) {
            grapheneContext =
                GrapheneContext.getContextFor(ReflectionHelper.getQualifier(annotations));
            localSearchContext = grapheneContext.getWebDriver(SearchContext.class);
        } else {
            localSearchContext = searchContext;
        }
        try {
            Class<?> declaredClass;
            if (target != null) {
                declaredClass = getDeclaredClass(target, field);
            } else {
                declaredClass = param;
            }

            appendErrorMsgBegin(errorMsgBegin, declaredClass, target, method);

            return setupPage(grapheneContext, localSearchContext, declaredClass);

        } catch (NoSuchMethodException ex) {
            errorMsgBegin.append(" Check whether declared Page Object has no argument constructor!");
            throw new PageObjectInitializationException(errorMsgBegin.toString(), ex);

        } catch (IllegalAccessException ex) {
            throw new PageObjectInitializationException(
                " Check whether declared Page Object has public no argument constructor!", ex);

        } catch (InstantiationException ex) {
            errorMsgBegin.append(" Check whether you did not declare Page Object with abstract type!");
            throw new PageObjectInitializationException(errorMsgBegin.toString(), ex);

        } catch (Exception ex) {
            throw new PageObjectInitializationException(errorMsgBegin.toString(), ex);
        }
    }

    private Class<?> getDeclaredClass(Object target, Field field) {
        Type type = field.getGenericType();
        // check whether it is type variable e.g. T
        if (type instanceof TypeVariable) {
            return getActualType(field, target);

        } else {
            // no it is normal type, e.g. TestPage
            return field.getType();
        }
    }

    private void appendErrorMsgBegin(StringBuffer errorMsgBegin, Class<?> declaredClass, Object target, Method method) {
        errorMsgBegin.append("Can not instantiate Page Object ").append(NEW_LINE);
        errorMsgBegin.append(declaredClass).append(NEW_LINE);
        errorMsgBegin.append(" declared in: ").append(NEW_LINE);
        if (target != null) {
            errorMsgBegin.append(target.getClass().getName());
        } else {
            errorMsgBegin.append(method.getDeclaringClass().getClass().getName());
            errorMsgBegin.append("#").append(method.getName());
        }
        errorMsgBegin.append(NEW_LINE);
    }

    public static <P> P setupPage(GrapheneContext context, SearchContext searchContext, Class<?> pageClass) throws Exception{
        P page = (P) instantiate(pageClass);
        P proxy = GrapheneProxy.getProxyForHandler(GrapheneContextualHandler.forTarget(context, page), pageClass);
        enrichRecursively(searchContext, page);
        enrichRecursively(searchContext, proxy); // because of possibility of direct access to attributes from test class
        return proxy;
    }

    @Override
    public int getPrecedence() {
        return 1;
    }
}
