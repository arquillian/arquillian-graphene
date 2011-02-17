/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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

import java.lang.reflect.Method;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

import org.jboss.arquillian.ajocado.framework.AjocadoConfiguration;
import org.jboss.arquillian.ajocado.framework.AjocadoConfigurationContext;
import org.jboss.arquillian.ajocado.framework.AjocadoConfiguration.TimeoutType;
import org.jboss.arquillian.ajocado.waiting.DefaultWaiting;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class WaitingProxy<T extends DefaultWaiting<T>> implements MethodHandler {

    AjocadoConfiguration configuration = AjocadoConfigurationContext.getProxy();

    T waiting;
    TimeoutType timeoutType;

    public WaitingProxy(T waiting, TimeoutType timeoutType) {
        this.waiting = waiting;
        this.timeoutType = timeoutType;
    }

    @Override
    public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
        Object target = waiting.timeout(configuration.getTimeout(timeoutType));
        return thisMethod.invoke(target, args);
    }

    public static <T extends DefaultWaiting<T>> T create(T waiting, TimeoutType timeoutType) {
        ProxyFactory f = new ProxyFactory();
        f.setSuperclass(waiting.getClass());
        Class<T> c = f.createClass();

        T newInstance;
        try {
            newInstance = (T) c.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        ((ProxyObject) newInstance).setHandler(new WaitingProxy<T>(waiting, timeoutType));
        return newInstance;
    }
}