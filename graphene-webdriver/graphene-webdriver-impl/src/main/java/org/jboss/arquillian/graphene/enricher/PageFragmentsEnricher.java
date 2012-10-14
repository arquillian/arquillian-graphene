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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;

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
    public void enrich(Object testCase) {

        // at first initialize findBy annotations
        if (ReflectionHelper.isClassPresent(FIND_BY_ANNOTATION)) {

            Factory.initFieldsAnnotatedByFindBy(testCase, null);
        }
        // initialize Page objects if there are any
        if (ReflectionHelper.isClassPresent(PAGE_ANNOTATION)) {

            List<Field> fields = ReflectionHelper.getFieldsWithAnnotation(testCase.getClass(), Page.class);

            initializePageObjectFields(testCase, fields);
        }
    }

    private void initializePageObjectFields(Object testCase, List<Field> fields) {

        for (Field i : fields) {

            try {
                Type type = i.getGenericType();
                Object page = null;

                Class<?> declaredClass = null;

                // check whether it is type variable e.g. T
                if (type instanceof TypeVariable) {
                    declaredClass = getActualType(i, testCase);

                } else {
                    // no it is normal type, e.g. TestPage
                    declaredClass = i.getType();
                }

                Class<?> outerClass = declaredClass.getDeclaringClass();

                // check whether declared page object is not nested class
                if (outerClass != null) {
                    Constructor<?> construtor = declaredClass.getDeclaredConstructor(new Class[] { outerClass });
                    page = construtor.newInstance(new Object[] { outerClass.newInstance() });
                } else {
                    page = declaredClass.newInstance();
                }

                enrich(page);

                Factory.setObjectToField(i, testCase, page);

            } catch (Exception ex) {
                throw new RuntimeException("Can not initialise Page Object!");
            }
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
