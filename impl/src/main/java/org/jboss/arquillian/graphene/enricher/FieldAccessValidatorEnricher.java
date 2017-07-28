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
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.logging.Logger;

import org.jboss.arquillian.graphene.spi.enricher.SearchContextTestEnricher;
import org.openqa.selenium.SearchContext;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class FieldAccessValidatorEnricher implements SearchContextTestEnricher {

    private static final Logger LOGGER = Logger.getLogger(FieldAccessValidatorEnricher.class.getName());

    @Override
    public void enrich(SearchContext searchContext, Object target) {
        List<Field> fields = ReflectionHelper.getFields(target.getClass());
        for (Field field : fields) {
            checkFieldValidity(searchContext, target, field);
        }

    }

    protected void checkFieldValidity(final SearchContext searchContext, final Object target, final Field field) {
        if ((!Modifier.isStatic(field.getModifiers()) || !Modifier.isFinal(field.getModifiers()))
            && !field.getName().startsWith("CGLIB")) { // ARQGRA-510 cglib adds one variable that is public static but not final
            // public field
            if (searchContext != null && field.isAccessible() || Modifier.isPublic(field.getModifiers())) {
                final String message = "Public field '" + field.getName() + "' found in " + target.getClass().getName() + ". Direct access to fields outside of the declaring class is not allowed.";
                LOGGER.warning(message);
            }
            // package friendly field
            if (searchContext != null && !field.getName().startsWith("this$") && !Modifier.isPrivate(field.getModifiers()) && !Modifier.isProtected(field.getModifiers())) {
                final String message = "Package-friendly field '" + field.getName() + "' found in " + target.getClass().getName() + ". Direct access to fields outside of the declaring class is not allowed.";
                LOGGER.warning(message);
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
}
