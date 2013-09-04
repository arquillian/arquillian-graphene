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
import java.util.Collection;

import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.javascript.JSInterfaceFactory;
import org.jboss.arquillian.graphene.javascript.JavaScript;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.openqa.selenium.SearchContext;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class JavaScriptEnricher extends AbstractSearchContextEnricher {

    @Override
    public void enrich(final SearchContext searchContext, Object target) {
        Collection<Field> fields = ReflectionHelper.getFieldsWithAnnotation(target.getClass(), JavaScript.class);
        for (Field field : fields) {
            GrapheneContext grapheneContext = searchContext == null ? null : ((GrapheneProxyInstance) searchContext).getContext();
            if (grapheneContext == null) {
                grapheneContext = GrapheneContext.getContextFor(ReflectionHelper.getQualifier(field.getAnnotations()));
            }
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            try {
                field.set(target, JSInterfaceFactory.create(grapheneContext, field.getType()));
            } catch (Exception e) {
                throw new IllegalStateException("Can't inject value to the field '" + field.getName() + "' declared in class '" + field.getDeclaringClass().getName() + "'", e);
            }
        }
    }

    @Override
    public int getPrecedence() {
        return 1;
    }
}
