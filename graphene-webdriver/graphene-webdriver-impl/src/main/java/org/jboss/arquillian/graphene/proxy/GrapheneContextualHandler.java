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
package org.jboss.arquillian.graphene.proxy;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.context.GrapheneContextImpl;
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
public class GrapheneContextualHandler extends GrapheneProxyHandler {

    private final GrapheneContext context;
    private Map<Class<?>, Interceptor> interceptors = new HashMap<Class<?>, Interceptor>();

    private GrapheneContextualHandler(GrapheneContext context, Object target) {
        super(target);
        this.context = context;
    }

    private GrapheneContextualHandler(GrapheneContext context, FutureTarget future) {
        super(future);
        this.context = context;
    }

    /**
     * Returns invocation handler which wraps the given target instance.
     *
     * @param target the target of invocation
     * @return invocation handler which wraps the given target instance
     */
    public static GrapheneContextualHandler forTarget(GrapheneContext context, Object target) {
        GrapheneContextualHandler handler = new GrapheneContextualHandler(context, target);
        return handler;
    }

    /**
     * Returns invocation handler which wraps the target for future computation.
     *
     * @param future the future target
     * @return invocation handler which wraps the target for future computation
     */
    public static GrapheneContextualHandler forFuture(GrapheneContext context, FutureTarget future) {
        GrapheneContextualHandler handler = new GrapheneContextualHandler(context, future);
        return handler;
    }

    /**
     * <p>
     * End point for handling invocations on proxy.
     * </p>
     *
     * <p>
     * Decides whenever the result of invocation is proxyable and if yes, it returns the proxy using new instance of
     * {@link GrapheneContextualHandler} wrapping the result of invocation.
     * </p>
     *
     * <p>
     * The method specifically handles {@link GrapheneProxyInstance#unwrap()} method which returns the real target for
     * invocation.
     */
    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        // handle finalizer
        if (method.getName().equals("finalize") && method.getParameterTypes().length == 0) {
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            Object target = getTarget();
            if (target instanceof GrapheneProxyInstance) {
                return ((GrapheneProxyInstance) target).unwrap();
            }
            method.invoke(target);
        }
        // handle the GrapheneProxyInstance's method unwrap
        if (method.equals(GrapheneProxyInstance.class.getMethod("unwrap"))) {
            Object target = getTarget();
            if (target instanceof GrapheneProxyInstance) {
                return ((GrapheneProxyInstance) target).unwrap();
            }
            return target;
        }
        // handle GrapheneProxyInstance's method registerInterceptor
        if (method.equals(GrapheneProxyInstance.class.getMethod("registerInterceptor", Interceptor.class))) {
            Interceptor interceptor = (Interceptor) args[0];
            if (interceptor == null) {
                throw new IllegalArgumentException("The parameter [interceptor] is null.");
            }
            interceptors.put(interceptor.getClass(), interceptor);
            return null;
        }
        // handle GrapheneProxyInstance's method unregisterInterceptor
        if (method.equals(GrapheneProxyInstance.class.getMethod("unregisterInterceptor", Interceptor.class))) {
            Interceptor interceptor = (Interceptor) args[0];
            if (interceptor == null) {
                throw new IllegalArgumentException("The parameter [interceptor] is null.");
            }
            return interceptors.remove(interceptor.getClass());
        }
        // handle GrapheneProxyInstance's method copy
        if (method.equals(GrapheneProxyInstance.class.getMethod("copy"))) {
            GrapheneProxyInstance clone;
            clone = (GrapheneProxyInstance) GrapheneProxy.getProxyForTarget(context, getTarget());
            for (Interceptor interceptor : interceptors.values()) {
                clone.registerInterceptor(interceptor);
            }
            return clone;
        }
        // handle GrapheneProxyInstance's method copy
        if (method.equals(GrapheneProxyInstance.class.getMethod("getHandler"))) {
            return this;
        }
        // handle GrapheneProxyInstance's method getContext
        if (method.equals(GrapheneProxyInstance.class.getMethod("getContext"))) {
            return context;
        }

        InvocationContext invocationContext = new InvocationContext() {

            @Override
            public Object invoke() throws Throwable {
                Object result = invokeReal(getTarget(), method, args);
                if (result == null) {
                    return null;
                }
                if (isProxyable(method, args) && !(result instanceof GrapheneProxyInstance)) {
                    Class<?>[] interfaces = GrapheneProxyUtil.getInterfaces(result.getClass());
                    Object newProxy = GrapheneProxy.getProxyForTargetWithInterfaces(context, result, interfaces);

//                    List<Interceptor> inheritingInterceptors = ((GrapheneProxyInstance)proxy).getInheritingInterceptors();

                    return newProxy;
                }
                return result;
            }

            @Override
            public Method getMethod() {
                return method;
            }

            @Override
            public Object[] getArguments() {
                return args;
            }

            @Override
            public Object getTarget() {
                return GrapheneContextualHandler.this.getTarget();
            }

            @Override
            public Object getProxy() {
                return proxy;
            }

            @Override
            public GrapheneContext getGrapheneContext() {
                return context;
            }

        };
        for (Interceptor interceptor : interceptors.values()) {
            invocationContext = new InvocationContextImpl(interceptor, invocationContext);
        }
        final InvocationContext finalInvocationContext = invocationContext;
        if (context != null) {
            return ((GrapheneContextImpl) context).getBrowserActions().performAction(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    try {
                        return finalInvocationContext.invoke();
                    } catch (Throwable e) {
                        if (e instanceof Exception) {
                            throw (Exception) e;
                        } else {
                            throw new IllegalStateException("Can't invoke method " + method.getName() + ".", e);
                        }
                    }
                }
            });
        } else {
            return finalInvocationContext.invoke();
        }
    }

    /**
     * Delegates to {@link #invoke(Object, Method, Object[])} to serve as {@link MethodInterceptor}.
     */
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        return invoke(obj, method, args);
    }

    /**
     * Resets the interceptors
     */
    public void resetInterceptors() {
        interceptors.clear();
    }

}