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
package org.jboss.arquillian.graphene.context;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.openqa.selenium.WebDriver;

/**
 * GrapheneProxy provides methods for wrapping the target of invocation in the proxy.
 * 
 * @author Lukas Fryc
 */
class GrapheneProxy {

    /**
     * <p>
     * Wraps the given target instance in the proxy.
     * </p>
     * 
     * <p>
     * The list of interfaces which should be extended in the proxy is automatically computer from provided instance.
     * </p>
     * 
     * 
     * @param target the target instance to be wrapped
     * @return the proxy wrapping the target
     */
    @SuppressWarnings("unchecked")
    static <T> T getProxyForTarget(T target) {
        GrapheneProxyHandler handler = GrapheneProxyHandler.forTarget(target);
        return (T) Proxy.newProxyInstance(WebDriver.class.getClassLoader(), getInterfaces(target.getClass()), handler);
    }

    /**
     * <p>
     * Wraps the given future target instance in the proxy.
     * </p>
     * 
     * <p>
     * Future target can be computed dynamically for each invocation of proxy.
     * </p>
     * 
     * <p>
     * In this case interfaces which should the proxy implement needs to be provided.
     * </p>
     * 
     * <p>
     * The list of any classes can be provided, the list of interfaces will be automatically computed.
     * </p>
     * 
     * @param futureTarget the future target of invocation
     * @param targetClasses the list of classes from which should be determined what interfaces will returned proxy implement
     * @return the proxy wrapping the future target
     */
    @SuppressWarnings("unchecked")
    static <T> T getProxyForFutureTarget(FutureTarget futureTarget, Class<?>... targetClasses) {
        GrapheneProxyHandler handler = GrapheneProxyHandler.forFuture(futureTarget);
        return (T) Proxy.newProxyInstance(WebDriver.class.getClassLoader(), getInterfaces(targetClasses), handler);
    }

    /**
     * Transitively obtains the interfaces which are implemented by given classes.
     * 
     * @param targetClasses the list of classes from which should be determined the list of interfaces that these classes
     *        implement
     * @return the list of interfaces which are implemented by given classes
     */
    static Class<?>[] getInterfaces(Class<?>... targetClasses) {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        Queue<Class<?>> queue = new LinkedList<Class<?>>();

        queue.addAll(Arrays.asList(targetClasses));

        while (!queue.isEmpty()) {
            Class<?> clazz = queue.poll();

            if (classes.contains(clazz)) {
                continue;
            }

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
     * Interface for computation of future target of invocation by proxy.
     */
    interface FutureTarget {
        Object getTarget();
    }
}
