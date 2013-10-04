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
package org.jboss.arquillian.graphene.spi;


/**
 * Instantiates given class while it reflects annotation {@link ImplementedBy} in order to determine final implementation of
 * given type.
 *
 * @author Lukas Fryc
 */
public class TypeResolver {

    /**
     * Resolves type based on given className, while it reflects annotation {@link ImplementedBy} in order to determine final
     * implementation of given type.
     *
     * @param typeName the name of the type
     * @return the implementation class for given type
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<? extends T> resolveType(String typeName) {
        try {
            Class<?> clazz = Class.forName(typeName);

            return (Class<? extends T>) resolveType(clazz);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(
                    String.format(
                            "Cannot find class %s. Make sure you have respective implementation (e.g. graphene-webdriver-impl.jar) included on the classpath.",
                            typeName), e);
        }
    }

    /**
     * Resolves implementation type based on given type, while it reflects annotation {@link ImplementedBy} in order to
     * determine final implementation of given type.
     *
     * @param type the type to be resolved
     * @return the implementation class for given type
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<? extends T> resolveType(Class<T> type) {
        ImplementedBy implementedBy = type.getAnnotation(ImplementedBy.class);
        if (implementedBy != null) {
            if (implementedBy.value() != ImplementedBy.class) {
                return (Class<? extends T>) resolveType(implementedBy.value());
            } else if (!"".equals(implementedBy.className())) {
                return (Class<? extends T>) resolveType(implementedBy.className());
            } else {
                throw new IllegalStateException(
                        String.format(
                                "Cannot instantiate an instance of '%s' as its %s is incomplete - it doesn't specify implementation class",
                                type.getName(), implementedBy));
            }
        } else {
            return type;
        }
    }

    /**
     * Instantiates a class by given implementation name
     *
     * @param className
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T instantiate(String className) {
        return (T) instantiate(resolveType(className));
    }

    /**
     * Instantiates class by given type, while it reflects annotation {@link ImplementedBy} in order to determine final
     * implementation of given type.
     *
     * @param type the type of the instantiated instance, which can be either final implementation type or type annotated by
     *        {@link ImplementedBy} in order to determine final implementation of given type.
     * @return instance of given type
     */
    public static <T> T instantiate(Class<T> type) {
        try {
            Class<? extends T> resolvedType = resolveType(type);

            return SecurityActions.newInstance(resolvedType.getName(), new Class<?>[] {}, new Object[] {}, type);
        } catch (Exception e) {
            throw new IllegalStateException(String.format("Cannot instantiate an instance of class '%s': %s", type.getName(),
                    e.getMessage()), e);
        }
    }

}
