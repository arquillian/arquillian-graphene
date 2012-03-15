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
package org.jboss.arquillian.ajocado.framework.internal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.jboss.arquillian.ajocado.framework.GrapheneConfiguration;
import org.jboss.arquillian.ajocado.framework.GrapheneConfiguration.TimeoutType;
import org.jboss.arquillian.ajocado.framework.GrapheneConfigurationContext;
import org.jboss.arquillian.ajocado.waiting.Waiting;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 *
 * @param <T> type of waiting
 */
public class WaitingProxy<T extends Waiting<T>> implements InvocationHandler {

    GrapheneConfiguration configuration = GrapheneConfigurationContext.getProxy();

    T waiting;
    TimeoutType timeoutType;

    public WaitingProxy(T waiting, TimeoutType timeoutType) {
        this.waiting = waiting;
        this.timeoutType = timeoutType;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Waiting<T>> T create(T waiting, TimeoutType timeoutType) {
        InvocationHandler handler = new WaitingProxy<T>(waiting, timeoutType);
        T proxy = (T) Proxy.newProxyInstance(WaitingProxy.class.getClassLoader(), getInterfaces(waiting.getClass()), handler);
        return proxy;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            Object target = waiting.timeout(configuration.getTimeout(timeoutType));
            return method.invoke(target, args);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }

    private static Class<?>[] getInterfaces(Class<?> waitingClass) {
        Set<Class<?>> set = new HashSet<Class<?>>();
        Queue<Class<?>> queue = new LinkedList<Class<?>>();
        List<Class<?>> interfaces = new LinkedList<Class<?>>();

        queue.add(waitingClass);

        while (!queue.isEmpty()) {
            Class<?> clazz = queue.poll();

            if (set.contains(clazz)) {
                continue;
            }

            set.add(clazz);

            queue.addAll(Arrays.asList(clazz.getInterfaces()));

            if (clazz.getSuperclass() != null) {
                queue.add(clazz.getSuperclass());
            }
        }

        for (Class<?> clazz : set) {
            if (clazz.isInterface()) {
                interfaces.add(clazz);
            }
        }

        return interfaces.toArray(new Class<?>[interfaces.size()]);
    }
}