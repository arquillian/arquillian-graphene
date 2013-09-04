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
package org.jboss.arquillian.graphene.proxy;

import java.lang.reflect.Method;

import org.jboss.arquillian.graphene.context.GrapheneContext;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class InvocationContextImpl implements InvocationContext {

    private final InvocationContext following;
    private final Interceptor interceptor;
    private final Object target;
    private final Object proxy;
    private final Method method;
    private final Object[] arguments;
    private final GrapheneContext context;

    public InvocationContextImpl(GrapheneContext context, Object target, Object proxy, Method method, Object[] arguments) {
        if (target == null) {
            throw new IllegalArgumentException("The parameter [target] is null.");
        }
        if (proxy == null) {
            throw new IllegalArgumentException("The parameter [proxy] is null.");
        }
        if (method == null) {
            throw new IllegalArgumentException("The parameter [method] is null.");
        }
        if (arguments == null) {
            throw new IllegalArgumentException("The parameter [arguments] is null.");
        }
        if (context == null) {
            throw new IllegalArgumentException("The parameter [context] is null.");
        }
        this.following = null;
        this.interceptor = null;
        this.arguments = arguments;
        this.method = method;
        this.target = target;
        this.context = context;
        this.proxy = proxy;
    }

    public InvocationContextImpl(Interceptor interceptor, InvocationContext following) {
        if (following == null) {
            throw new IllegalArgumentException("The parameter [following] is null.");
        }
        if (interceptor == null) {
            throw new IllegalArgumentException("The parameter [interceptor] is null.");
        }
        this.following = following;
        this.interceptor = interceptor;
        this.arguments = null;
        this.method = null;
        this.target = null;
        this.context = null;
        this.proxy = null;
    }

    @Override
    public Object invoke() throws Throwable {
        if (following == null) {
            return method.invoke(target, arguments);
        } else {
            return interceptor.intercept(following);
        }
    }

    @Override
    public Method getMethod() {
        return method == null ? following.getMethod() : method;
    }

    @Override
    public Object[] getArguments() {
        return arguments == null ? following.getArguments(): arguments;
    }

    @Override
    public Object getTarget() {
        return target == null ? following.getTarget() : target;
    }

    @Override
    public Object getProxy() {
        return proxy == null ? following.getProxy() : proxy;
    }

    @Override
    public GrapheneContext getGrapheneContext() {
        return context == null ? following.getGrapheneContext() : context;
    }

}
