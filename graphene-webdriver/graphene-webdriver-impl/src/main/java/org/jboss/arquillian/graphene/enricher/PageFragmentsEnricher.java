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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.jboss.arquillian.test.spi.TestEnricher;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

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

            initFieldsAnnotatedByFindBy(testCase);
        }
        // initialize Page objects if there are any
        if (ReflectionHelper.isClassPresent(PAGE_ANNOTATION)) {

            List<Field> fields = ReflectionHelper.getFieldsWithAnnotation(testCase.getClass(), Page.class);

            initializePageObjectFields(testCase, fields);
        }
    }

    private void initFieldsAnnotatedByFindBy(Object object) {

        // gets all fields with findBy annotations and then removes these
        // which are not Page Fragments
        List<Field> fields = ReflectionHelper.getFieldsWithAnnotation(object.getClass(), FindBy.class);

        List<Field> copy = new ArrayList<Field>();
        copy.addAll(fields);

        fields = removePlainFindBy(fields);
        initPageFragmentsFields(fields, object);

        // initialize other non Page Fragment fields annotated with FindBy
        copy.removeAll(fields);
        Factory.initNotPageFragmentsFields(copy, object);
    }

    private void initializePageObjectFields(Object testCase, List<Field> fields) {

        for (Field i : fields) {

            try {
                Class declaredClass = Class.forName(i.getGenericType().toString().split(" ")[1]);
                Object page = declaredClass.newInstance();

                enrich(page);

                boolean accessible = i.isAccessible();
                if (!accessible) {
                    i.setAccessible(true);
                }
                i.set(testCase, page);
                if (!accessible) {
                    i.setAccessible(false);
                }
            } catch (Exception ex) {
                throw new RuntimeException("Can not initialise Page Object!");
            }
        }
    }

    private void initPageFragmentsFields(List<Field> fields, Object object) {
        for (Field pageFragmentField : fields) {

            // sets the root of the Page Fragment, retrieved from annotation
            FindBy findBy = pageFragmentField.getAnnotation(FindBy.class);
            final By by = Factory.getReferencedBy(findBy);

            WebElement rootElement = Factory.setUpTheProxyForWebElement(by);

            // initialise Page Fragment
            Class<?> implementationClass = pageFragmentField.getType();
            Object pageFragment = Factory.initializePageFragment(implementationClass, rootElement);

            Factory.setObjectToField(pageFragmentField, object, pageFragment);
        }
    }

    /**
     * It removes all fields with type <code>WebElement</code> from the given list of fields.
     * 
     * @param findByFields
     * @return
     */
    private List<Field> removePlainFindBy(List<Field> findByFields) {

        for (Iterator<Field> i = findByFields.iterator(); i.hasNext();) {

            Field field = i.next();

            Class<?> fieldType = field.getType();

            if (fieldType.equals(WebElement.class)) {
                i.remove();
            } else if (fieldType.equals(List.class)) {
                i.remove();
            }
        }

        return findByFields;
    }

    @Override
    public Object[] resolve(Method method) {
        return new Object[method.getParameterTypes().length];
    }

}
