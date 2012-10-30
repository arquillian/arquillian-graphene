/**
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;

import org.jboss.arquillian.graphene.enricher.exception.PageObjectInitializationException;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.jboss.arquillian.test.spi.TestEnricher;

/**
 * Enricher is a class for injecting into fields initialised <code>WebElement</code> and Page Fragments instances.
 * 
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * 
 */
public class PageFragmentsEnricher implements TestEnricher {

    private static final String FIND_BY_ANNOTATION = "org.openqa.selenium.support.FindBy";
    private static final String PAGE_ANNOTATION = "org.jboss.arquillian.graphene.spi.annotations.Page";

    @Override
    public void enrich(Object objectToEnrich) {

        // at first initialize findBy annotations
        if (ReflectionHelper.isClassPresent(FIND_BY_ANNOTATION)) {

            Factory.initFieldsAnnotatedByFindBy(objectToEnrich, null);
        }
        // initialize Page objects if there are any
        if (ReflectionHelper.isClassPresent(PAGE_ANNOTATION)) {

            List<Field> fields = ReflectionHelper.getFieldsWithAnnotation(objectToEnrich.getClass(), Page.class);

            initializePageObjectFields(objectToEnrich, fields);
        }
    }

    private void initializePageObjectFields(Object objectWherePageObjectsAreDeclared, List<Field> pageObjectsFields) {
        Class<?> declaredClass = null;
        String errorMsgBegin = null;

        for (Field pageObjectField : pageObjectsFields) {

            Object page = null;
            try {
                Type type = pageObjectField.getGenericType();

                // check whether it is type variable e.g. T
                if (type instanceof TypeVariable) {
                    declaredClass = getActualType(pageObjectField, objectWherePageObjectsAreDeclared);

                } else {
                    // no it is normal type, e.g. TestPage
                    declaredClass = pageObjectField.getType();
                }
                errorMsgBegin = "Can not instantiate Page Object " + Factory.NEW_LINE + declaredClass + Factory.NEW_LINE
                    + " declared in: " + Factory.NEW_LINE + objectWherePageObjectsAreDeclared + Factory.NEW_LINE;

                page = Factory.instantiateClass(declaredClass);

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

            enrich(page);

            Factory.setObjectToField(pageObjectField, objectWherePageObjectsAreDeclared, page);
        }
    }

    private Class<?> getActualType(Field i, Object testCase) {

        // e.g. TestPage, HomePage
        Type[] superClassActualTypeArguments = getSuperClassActualTypeArguments(testCase);
        // e.g. T, E
        TypeVariable<?>[] superClassTypeParameters = getSuperClassTypeParameters(testCase);

        // the type parameter has the same index as the actual type
        String fieldParameterTypeName = i.getGenericType().toString();

        int index = Arrays.asList(superClassTypeParameters).indexOf(fieldParameterTypeName);
        for (index = 0; index < superClassTypeParameters.length; index++) {
            String superClassTypeParameterName = superClassTypeParameters[index].getName();
            if (fieldParameterTypeName.equals(superClassTypeParameterName)) {
                break;
            }
        }

        return (Class<?>) superClassActualTypeArguments[index];
    }

    private Type[] getSuperClassActualTypeArguments(Object testCase) {
        Type[] actualTypeArguemnts = ((ParameterizedType) testCase.getClass().getGenericSuperclass()).getActualTypeArguments();

        return actualTypeArguemnts;
    }

    private TypeVariable<?>[] getSuperClassTypeParameters(Object testCase) {
        TypeVariable<?>[] typeParameters = testCase.getClass().getSuperclass().getTypeParameters();

        return typeParameters;
    }

    @Override
    public Object[] resolve(Method method) {
        return new Object[method.getParameterTypes().length];
    }

}
