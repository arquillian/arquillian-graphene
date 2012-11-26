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
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.spi.ServiceLoader;
import org.jboss.arquillian.graphene.enricher.exception.PageObjectInitializationException;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.openqa.selenium.SearchContext;

/**
 * Enricher injecting page objects to the fields of the given object.
 *
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class PageObjectEnricher extends AbstractSearchContextEnricher {

    @SuppressWarnings("unused")
    @Inject
    private Instance<ServiceLoader> serviceLoader;

    @Override
    public void enrich(SearchContext searchContext, Object target) {
        String errorMsgBegin = "";
        List<Field> fields = ReflectionHelper.getFieldsWithAnnotation(target.getClass(), Page.class);
        for (Field field : fields) {
            try {
                Type type = field.getGenericType();
                Class<?> declaredClass;
                // check whether it is type variable e.g. T
                if (type instanceof TypeVariable) {
                    declaredClass = getActualType(field, target);

                } else {
                    // no it is normal type, e.g. TestPage
                    declaredClass = field.getType();
                }

                errorMsgBegin = "Can not instantiate Page Object " + NEW_LINE + declaredClass + NEW_LINE
                    + " declared in: " + NEW_LINE + target.getClass().getName() + NEW_LINE;

                Object page = instantiate(declaredClass);

                enrichRecursively(searchContext, page);

                setValue(field, target, page);
            } catch (NoSuchMethodException ex) {
                throw new PageObjectInitializationException(errorMsgBegin
                        + " Check whether declared Page Object has no argument constructor!", ex);
            } catch (IllegalAccessException ex) {
                throw new PageObjectInitializationException(
                        " Check whether declared Page Object has public no argument constructor!", ex);
            } catch (InstantiationException ex) {
                throw new PageObjectInitializationException(errorMsgBegin
                        + " Check whether you did not declare Page Object with abstract type!", ex);
            } catch (Exception ex) {
                throw new PageObjectInitializationException(errorMsgBegin, ex);
            }

        }
    }
}
