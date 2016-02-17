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
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.jboss.arquillian.graphene.enricher.exception.GrapheneTestEnricherException;
import org.jboss.arquillian.graphene.page.InFrame;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.openqa.selenium.SearchContext;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class InFrameEnricher extends AbstractSearchContextEnricher {

    @Override
    public void enrich(SearchContext searchContext, Object objectToEnrich) {
        Collection<Field> inFrameFields = ReflectionHelper.getFieldsWithAnnotation(objectToEnrich.getClass(), InFrame.class);
        for (Field field : inFrameFields) {
            boolean isAccessible = field.isAccessible();
            if (!isAccessible) {
                field.setAccessible(true);
            }
            InFrame inFrame = field.getAnnotation(InFrame.class);
            int index = inFrame.index();
            String nameOrId = inFrame.nameOrId();
            checkInFrameParameters(field, index, nameOrId);

            try {
                registerInFrameInterceptor(objectToEnrich, field, index, nameOrId);
            } catch (IllegalArgumentException e) {
                throw new GrapheneTestEnricherException(
                    "Only org.openqa.selenium.WebElement, Page fragments fields and Page Object fields can be annotated with @InFrame. Check the field: "
                        + field + " declared in the class: " + objectToEnrich.getClass(), e);
            } catch (Exception e) {
                throw new GrapheneTestEnricherException(e);
            }
            if (!isAccessible) {
                field.setAccessible(false);
            }
        }
    }

    @Override
    public Object[] resolve(SearchContext searchContext, Method method, Object[] resolvedParams) {
        StringBuffer errorMsgBegin = new StringBuffer("");
        List<Object[]> paramCouple = new LinkedList<Object[]>();
        paramCouple.addAll(ReflectionHelper.getParametersWithAnnotation(method, InFrame.class));

        for (int i = 0; i < resolvedParams.length; i++) {
            if (paramCouple.get(i) != null && resolvedParams[i] != null) {
                Class<?> param = (Class<?>) paramCouple.get(i)[0];
                Annotation[] parameterAnnotations = (Annotation[]) paramCouple.get(i)[1];
                InFrame inFrame = ReflectionHelper.findAnnotation(parameterAnnotations, InFrame.class);
                int index = inFrame.index();
                String nameOrId = inFrame.nameOrId();
                checkInFrameParameters(method, param, index, nameOrId);

                try {
                    registerInFrameInterceptor((GrapheneProxyInstance) resolvedParams[i], index, nameOrId);
                } catch (IllegalArgumentException e) {
                    throw new GrapheneTestEnricherException(
                        "Only org.openqa.selenium.WebElement, Page fragments fields and Page Object fields can be annotated with @InFrame. Check parameter "
                            + param + " of the method: " + method.getName() + " declared in: " + method
                            .getDeclaringClass(), e);
                } catch (Exception e) {
                    throw new GrapheneTestEnricherException(e);
                }
            }
        }
        return resolvedParams;
    }

    private void registerInFrameInterceptor(Object objectToEnrich, Field field, int index, String nameOrId)
        throws IllegalAccessException, ClassNotFoundException {
        GrapheneProxyInstance proxy = (GrapheneProxyInstance) field.get(objectToEnrich);

        registerInFrameInterceptor(proxy, index, nameOrId);
    }

    private void registerInFrameInterceptor(GrapheneProxyInstance proxy, int index, String nameOrId) {
        if (index != -1) {
            proxy.registerInterceptor(new InFrameInterceptor(index));
        } else {
            proxy.registerInterceptor(new InFrameInterceptor(nameOrId));
        }
    }

    private void checkInFrameParameters(Field field, int index, String nameOrId) {
        if (checkInFrameParameters(index, nameOrId)) {
            throw new GrapheneTestEnricherException(
                "You have to provide either non empty nameOrId or non negative index value of the frame/iframe in the @InFrame. Check field "
                    + field + " declared in: " + field.getDeclaringClass());
        }
    }

    private void checkInFrameParameters(Method method, Class<?> param, int index, String nameOrId) {
        if (checkInFrameParameters(index, nameOrId)) {
            throw new GrapheneTestEnricherException(
                "You have to provide either non empty nameOrId or non negative index value of the frame/iframe in the @InFrame. Check parameter "
                    + param + " of the method: " + method.getName() + " declared in: " + method.getDeclaringClass());
        }
    }

    private boolean checkInFrameParameters(int index, String nameOrId) {
        return nameOrId.trim().equals("") && index < 0;
    }

    @Override
    public int getPrecedence() {
        return 0;
    }
}
