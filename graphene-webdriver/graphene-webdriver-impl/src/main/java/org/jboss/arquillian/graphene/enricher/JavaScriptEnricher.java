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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.javascript.JSInterfaceFactory;
import org.jboss.arquillian.graphene.javascript.JavaScript;
import org.jboss.arquillian.test.spi.TestEnricher;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class JavaScriptEnricher implements TestEnricher {

    @Override
    public void enrich(Object o) {
        Collection<Field> fields = ReflectionHelper.getFieldsWithAnnotation(o.getClass(), JavaScript.class);
        for (Field field: fields) {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            try {
                field.set(o, JSInterfaceFactory.create(
                        (WebDriver) GrapheneContext.getProxyForInterfaces(JavascriptExecutor.class),
                        field.getType()));
            } catch (Exception e) {
                throw new IllegalStateException("Can't inject value to the field '" + field.getName() + "' declared in class '" + field.getDeclaringClass().getName() +"'");
            }
        }
    }

    @Override
    public Object[] resolve(Method method) {
        return new Object[method.getParameterTypes().length];
    }

}
