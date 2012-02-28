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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

import org.jboss.arquillian.ajocado.framework.GrapheneConfiguration;
import org.jboss.arquillian.ajocado.framework.GrapheneConfiguration.TimeoutType;
import org.jboss.arquillian.ajocado.framework.GrapheneConfigurationContext;
import org.jboss.arquillian.ajocado.waiting.DefaultWaiting;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 *
 * @param <T>
 *            type of waiting
 */
public class WaitingProxy<T extends DefaultWaiting<T>> implements MethodHandler {

    GrapheneConfiguration configuration = GrapheneConfigurationContext.getProxy();

    T waiting;
    TimeoutType timeoutType;

    public WaitingProxy(T waiting, TimeoutType timeoutType) {
        this.waiting = waiting;
        this.timeoutType = timeoutType;
    }

    @Override
    public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
        try {
            Object target = waiting.timeout(configuration.getTimeout(timeoutType));
            return thisMethod.invoke(target, args);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }

    public static <T extends DefaultWaiting<T>> T create(T waiting, TimeoutType timeoutType) {
        ProxyFactory f = new ProxyFactory();
        f.setSuperclass(waiting.getClass());
        Class<T> c = f.createClass();

        T newInstance;
        try {
            newInstance = c.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        ((ProxyObject) newInstance).setHandler(new WaitingProxy<T>(waiting, timeoutType));
        return newInstance;
    }
}