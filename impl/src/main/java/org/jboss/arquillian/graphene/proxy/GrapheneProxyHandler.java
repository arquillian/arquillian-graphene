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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.jboss.arquillian.graphene.proxy.GrapheneProxy.FutureTarget;

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
public abstract class GrapheneProxyHandler implements MethodInterceptor, InvocationHandler {

    private Object target;
    private FutureTarget future;

    /**
     * Returns invocation handler which wraps the given target instance.
     *
     * @param target the target of invocation
     * @return invocation handler which wraps the given target instance
     */
    public GrapheneProxyHandler(Object target) {
        this.target = target;
    }

    /**
     * Returns invocation handler which wraps the target for future computation.
     *
     * @param future the future target
     * @return invocation handler which wraps the target for future computation
     */
    public GrapheneProxyHandler(FutureTarget future) {
        this.future = future;
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
     *
     * <p>
     * The method specifically handles {@link GrapheneProxyInstance#unwrap()} method which returns the real target for
     * invocation.
     */
    @Override
    public abstract Object invoke(Object proxy, Method method, Object[] args) throws Throwable;

    /**
     * Delegates to {@link #invoke(Object, Method, Object[])} to serve as {@link MethodInterceptor}.
     */
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        return invoke(obj, method, args);
    }

    /**
     * Determines whenever the given invocation can be proxied.
     *
     * @param method the method which is invoked
     * @param args the arguments used for invocation
     * @return true if the given invocation can be proxied; false otherwise
     */
    protected boolean isProxyable(Method method, Object[] args) {
        Class<?> returnType = method.getReturnType();
        return returnType.isInterface();
    }

    /**
     * <p>
     * Invokes the method on real target.
     * </p>
     *
     * @param target the target to be invoked
     * @param method the method to be invoked
     * @param args the arguments used for invocation
     * @return the result of invocation on real target
     */
    protected Object invokeReal(Object target, Method method, Object[] args) throws Throwable {
        Object result;
        try {
            if (target instanceof GrapheneProxyInstance) {
                target = ((GrapheneProxyInstance) target).unwrap();
            }
            if (!method.getDeclaringClass().isInstance(target)) {
                method = target.getClass().getDeclaredMethod(method.getName(), method.getParameterTypes());
            }
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            result = method.invoke(target, args);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        } catch (Exception e) {
            throw new RuntimeException("unexpected invocation exception during invocation of "
                    + method.getDeclaringClass().getName() + "#" + method.getName() + "(), on target '"
                    + target + "': " + e.getMessage(), e);
        }
        return result;
    }

    /**
     * Computes the target for invocation - if future target is provided, it will be returned, othewise the target instance will
     * be returned.
     *
     * @return the real target for invocation
     */
    protected Object getTarget() {
        return (future == null) ? target : future.getTarget();
    }

}