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
package org.jboss.arquillian.graphene.proxy;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * Utilities for handling with {@link GrapheneProxy} related tasks.
 *
 * @author Lukas Fryc
 */
public final class GrapheneProxyUtil {

    /**
     * Transitively obtains the interfaces which are implemented by given classes.
     *
     * @param targetClasses the list of classes from which should be determined the list of interfaces that these classes
     *        implement
     * @return the list of interfaces which are implemented by given classes
     */
    public static Class<?>[] getInterfaces(Class<?>... targetClasses) {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        Set<Class<?>> inspected = new HashSet<Class<?>>();
        Queue<Class<?>> queue = new LinkedList<Class<?>>();

        queue.addAll(Arrays.asList(targetClasses));

        while (!queue.isEmpty()) {
            Class<?> clazz = queue.poll();

            // if we already scanned the class, we can skip
            if (inspected.contains(clazz)) {
                continue;
            }

            // if this is interface, we don't need to include all interfaces it extends, we can skip instead
            if (clazz.isInterface() && classes.contains(clazz)) {
                continue;
            }

            inspected.add(clazz);
            classes.add(clazz);
            classes.addAll(Arrays.asList(clazz.getInterfaces()));
            queue.addAll(Arrays.asList(clazz.getInterfaces()));
            if (clazz.getSuperclass() != null) {
                classes.add(clazz.getSuperclass());
                queue.add(clazz.getSuperclass());
            }
        }

        List<Class<?>> interfaces = new LinkedList<Class<?>>();
        for (Class<?> clazz : classes) {
            if (clazz.isInterface()) {
                interfaces.add(clazz);
            }
        }
        return interfaces.toArray(new Class<?>[interfaces.size()]);
    }

    /**
     * Adds clazz to the array of classes
     *
     * @param classes to be appended
     * @param clazz to be added
     * @return the array of classes with added clazz
     */
    public static Class<?>[] concatClasses(Class<?>[] classes, Class<?> clazz) {
        for (Class<?> c: classes) {
            if (clazz.equals(c)) {
                return classes;
            }
        }
        int length = classes.length;
        Class<?>[] out = new Class<?>[length + 1];
        System.arraycopy(classes, 0, out, 0, length);
        out[length] = clazz;
        return out;
    }

    public static boolean isProxy(Class<?> clazz) {
        if (clazz.equals(Object.class)) {
            return false;
        }
        if (net.sf.cglib.proxy.Proxy.isProxyClass(clazz) || Proxy.isProxyClass(clazz)) {
            return true;
        } else {
            for (Class<?> interfaze: clazz.getInterfaces()) {
                if (interfaze.getName().endsWith(".cglib.proxy.Factory")) {
                    return true;
                }
            }
            return false;
        }
    }

    public static boolean isProxy(Object target) {
        return isProxy(target.getClass());

    }
}
