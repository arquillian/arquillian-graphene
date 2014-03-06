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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.spi.ServiceLoader;
import org.jboss.arquillian.graphene.enricher.exception.GrapheneTestEnricherException;
import org.jboss.arquillian.graphene.spi.TypeResolver;
import org.jboss.arquillian.graphene.spi.enricher.SearchContextTestEnricher;
import org.jboss.arquillian.test.spi.TestEnricher;
import org.openqa.selenium.SearchContext;

/**
 * This class should help you to implement {@link SearchContextTestEnricher}.
 *
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public abstract class AbstractSearchContextEnricher implements SearchContextTestEnricher {

    /**
     * Constant containing new line separator dependent on the environment.
     */
    protected static final String NEW_LINE = System.getProperty("line.separator");
    @Inject
    private static Instance<ServiceLoader> serviceLoader;

    /**
     * Performs further enrichment on the given instance with the given search context. That means all instances
     * {@link TestEnricher} and {@link SearchContextTestEnricher} are invoked.
     *
     * @param searchContext
     * @param target
     */
    protected static void enrichRecursively(SearchContext searchContext, Object target) {
        for (TestEnricher enricher : serviceLoader.get().all(TestEnricher.class)) {
            if (!enricher.getClass().equals(GrapheneEnricher.class)) {
                enricher.enrich(target);
            }
        }
        for (SearchContextTestEnricher enricher : getSortedSearchContextEnrichers(serviceLoader)) {
            enricher.enrich(searchContext, target);
        }
    }

    public static Collection<SearchContextTestEnricher> getSortedSearchContextEnrichers(Instance<ServiceLoader> serviceLoader) {
        List<SearchContextTestEnricher> allSearchContextErichers = new ArrayList<SearchContextTestEnricher>(serviceLoader.get()
            .all(SearchContextTestEnricher.class));
        Collections.sort(allSearchContextErichers, new SearchContextTestEnricherPrecedenceComparator());
        return allSearchContextErichers;
    }

    /**
     * It loads a real type of a field defined by parametric type. It searches in declaring class and super class. E. g. if a
     * field is declared as 'A fieldName', It tries to find type parameter called 'A' in super class declaration and its
     * evaluation in the class declaring the given field.
     *
     * @param field
     * @param testCase
     * @return type of the given field
     */
    protected final Class<?> getActualType(Field field, Object testCase) {

        // e.g. TestPage, HomePage
        Type[] superClassActualTypeArguments = getSuperClassActualTypeArguments(testCase);
        // e.g. T, E
        TypeVariable<?>[] superClassTypeParameters = getSuperClassTypeParameters(testCase);

        // the type parameter has the same index as the actual type
        String fieldParameterTypeName = field.getGenericType().toString();

        int index;
        for (index = 0; index < superClassTypeParameters.length; index++) {
            String superClassTypeParameterName = superClassTypeParameters[index].getName();
            if (fieldParameterTypeName.equals(superClassTypeParameterName)) {
                break;
            }
        }

        return (Class<?>) superClassActualTypeArguments[index];
    }

    /**
     * It loads the concrete type of list items. E.g. for List<String>, String is returned.
     *
     * @param listField
     * @return
     * @throws ClassNotFoundException
     */
    protected final Class<?> getListType(Field listField) throws ClassNotFoundException {
        return Class.forName(listField.getGenericType().toString().split("<")[1].split(">")[0].split("<")[0]);
    }

    /**
     * Initialize given class.
     *
     * @param clazz to be initialized
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    protected static <T> T instantiate(Class<T> type, Object... args) throws NoSuchMethodException, SecurityException,
        InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        Class<? extends T> clazz = TypeResolver.resolveType(type);
        Class<?> outerClass = clazz.getDeclaringClass();

        // load constructor and rea; arguments
        // check whether declared page object is not nested class
        Class<?>[] argTypes;
        Object[] realArgs;
        if (outerClass == null || Modifier.isStatic(clazz.getModifiers())) {
            argTypes = new Class<?>[args.length];
            realArgs = args;
            for (int i = 0; i < args.length; i++) {
                argTypes[i] = args[i].getClass();
            }
        } else {
            argTypes = new Class<?>[args.length + 1];
            realArgs = new Object[args.length + 1];
            argTypes[0] = outerClass;
            realArgs[0] = instantiate(outerClass);
            for (int i = 0; i < args.length; i++) {
                argTypes[i + 1] = args[i].getClass();
                realArgs[i + 1] = args[i];
            }
        }
        Constructor<? extends T> construtor;
        if (ReflectionHelper.hasConstructor(clazz, argTypes)) {
            construtor = clazz.getDeclaredConstructor(argTypes);
        } else {
            construtor = ReflectionHelper.getAssignableConstructor(clazz, argTypes);
        }
        // instantiate
        if (!construtor.isAccessible()) {
            construtor.setAccessible(true);
        }

        return construtor.newInstance(realArgs);
    }

    protected static void setValue(Field field, Object target, Object value) {

        boolean accessible = field.isAccessible();
        if (!accessible) {
            field.setAccessible(true);
        }
        try {
            field.set(target, value);
        } catch (Exception ex) {
            throw new GrapheneTestEnricherException("During enriching of " + NEW_LINE + target.getClass() + NEW_LINE
                + " the field " + NEW_LINE + field + " was not able to be set! Check the cause!", ex);
        }
        if (!accessible) {
            field.setAccessible(false);
        }
    }

    private Type[] getSuperClassActualTypeArguments(Object testCase) {
        Class<?> clazz = testCase.getClass();
        while(!(clazz.getGenericSuperclass() instanceof ParameterizedType)) {
            clazz = clazz.getSuperclass();
        }
        Type[] actualTypeArguments = ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments();
        return actualTypeArguments;
    }

    private TypeVariable<?>[] getSuperClassTypeParameters(Object testCase) {
        Class<?> clazz = testCase.getClass();
        while(!(clazz.getGenericSuperclass() instanceof ParameterizedType)) {
            clazz = clazz.getSuperclass();
        }
        return clazz.getTypeParameters();
    }
}
