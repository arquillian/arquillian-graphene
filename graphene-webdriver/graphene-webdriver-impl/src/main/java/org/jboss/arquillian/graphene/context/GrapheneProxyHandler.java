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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.jboss.arquillian.graphene.context.GrapheneProxy.FutureTarget;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;

/**
 * <p>
 * Invocation handler which wraps the given target for invocation (as the target instance for future computation of target).
 * </p>
 * 
 * <p>
 * This handlers determines whenever the result of invocation can be proxied and if yes, it recursively wraps it with proxy
 * using this invocation handler.
 * </p>
 * 
 * @author Lukas Fryc
 */
class GrapheneProxyHandler implements MethodInterceptor, InvocationHandler {

    private Object target;
    private FutureTarget future;

    private GrapheneProxyHandler() {
    }

    /**
     * Returns invocation handler which wraps the given target instance.
     * 
     * @param target the target of invocation
     * @return invocation handler which wraps the given target instance
     */
    static GrapheneProxyHandler forTarget(Object target) {
        GrapheneProxyHandler handler = new GrapheneProxyHandler();
        handler.target = target;
        return handler;
    }

    /**
     * Returns invocation handler which wraps the target for future computation.
     * 
     * @param future the future target
     * @return invocation handler which wraps the target for future computation
     */
    static GrapheneProxyHandler forFuture(FutureTarget future) {
        GrapheneProxyHandler handler = new GrapheneProxyHandler();
        handler.future = future;
        return handler;
    }

    /**
     * <p>
     * End point for handling invocations on proxy.
     * </p>
     * 
     * <p>
     * Decides whenever the result of invocation is proxyable and if yes, it returns the proxy using new instance of
     * {@link GrapheneProxyHandler} wrapping the result of invocation.
     * </p>
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = invokeReal(method, args);

        if (isProxyable(method, args)) {
            Class<?>[] interfaces = GrapheneProxyUtil.getInterfaces(result.getClass());
            return GrapheneProxy.getProxyForTargetWithInterfaces(result, interfaces);
        }

        return result;
    }

    /**
     * Delegates to {@link #invoke(Object, Method, Object[])} to serve as {@link MethodInterceptor}.
     */
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        return invoke(proxy, method, args);
    }

    /**
     * Determines whenever the given invocation can be proxied.
     * 
     * @param method the method which is invoked
     * @param args the arguments used for invocation
     * @return true if the given invocation can be proxied; false otherwise
     */
    boolean isProxyable(Method method, Object[] args) {
        Class<?> returnType = method.getReturnType();
        return returnType.isInterface();
    }

    /**
     * <p>
     * Invokes the method on real target.
     * </p>
     * 
     * <p>
     * The target of invocation is computed, using the {@link #getTarget()} method.
     * </p>
     * 
     * @param method the method to be invoked
     * @param args the arguments used for invocation
     * @return the result of invocation on real target
     */
    Object invokeReal(Method method, Object[] args) throws Throwable {
        Object result;
        Object target = getTarget();
        try {
            result = method.invoke(target, args);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        } catch (Exception e) {
            throw new RuntimeException("unexpected invocation exception: " + e.getMessage(), e);
        }
        return result;
    }

    /**
     * Computes the target for invocation - if future target is provided, it will be returned, othewise the target instance will
     * be returned.
     * 
     * @return the real target for invocation
     */
    Object getTarget() {
        return (future == null) ? target : future.getTarget();
    }

}