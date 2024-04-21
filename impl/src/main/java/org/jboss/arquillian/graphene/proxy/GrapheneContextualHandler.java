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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.FieldValue;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.bytebuddy.MethodInterceptor;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.context.GrapheneContextImpl;
import org.jboss.arquillian.graphene.enricher.WrapsElementInterceptor;
import org.jboss.arquillian.graphene.intercept.InterceptorBuilder;
import org.jboss.arquillian.graphene.intercept.InterceptorPrecedenceComparator;
import org.jboss.arquillian.graphene.proxy.GrapheneProxy.FutureTarget;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsElement;

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

    @RuntimeType
    public static Object intercept(@This Object self,
                                   @FieldValue("__interceptor") MethodInterceptor interceptor,
                                   @Origin Method method,
                                   @AllArguments Object[] args) throws Throwable {
        return GrapheneProxyHandler.intercept(self, interceptor, method, args);
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
            for (Interceptor interceptor : getSortedInterceptorsByPrecedence()) {
                clone.registerInterceptor(interceptor);
            }
            return clone;
        }
        // handle GrapheneProxyInstance's method copy
        if (method.equals(GrapheneProxyInstance.class.getMethod("getHandler"))) {
            return this;
        }
        // handle GrapheneProxyInstance's method getContext
        if (method.equals(GrapheneProxyInstance.class.getMethod("getGrapheneContext"))) {
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

                    // ARQGRA-484 hack: Selenium must be able to unwrap WebElement
                    // this is a hack in a sense that WebElement-specific logic shouldn't be in such a general place
                    // but it's not a hack in principle - proxying WebElement means wrapping it, so we do need to do this
                    // a proper fix is probably dependant on ARQGRA-317
                    if (result instanceof WebElement && !(result instanceof GrapheneElement)) {
                        int len = interfaces.length;
                        interfaces = Arrays.copyOf(interfaces, len + 1);
                        interfaces[len] = WrapsElement.class;
                    }

                    Object newProxy = GrapheneProxy.getProxyForTargetWithInterfaces(context, result, interfaces);

                    // ARQGRA-484 hack continues
                    if (result instanceof WebElement && !(result instanceof GrapheneElement)) {
                        final GrapheneProxyInstance elementProxy = (GrapheneProxyInstance) newProxy;

                        InterceptorBuilder b = new InterceptorBuilder();
                        b.interceptInvocation(WrapsElement.class, new WrapsElementInterceptor(elementProxy))
                            .getWrappedElement();

                        elementProxy.registerInterceptor(b.build());
                    }

                    // List<Interceptor> inheritingInterceptors = ((GrapheneProxyInstance)proxy).getInheritingInterceptors();

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
        for (Interceptor interceptor : getSortedInterceptorsByPrecedence()) {
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
                        } else if (e instanceof AssertionError) {
                            throw (AssertionError) e;
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
     * Resets the interceptors
     */
    public void resetInterceptors() {
        interceptors.clear();
    }

    private List<Interceptor> getSortedInterceptorsByPrecedence() {
        List<Interceptor> result = new ArrayList<Interceptor>(interceptors.values());
        Collections.sort(result, new InterceptorPrecedenceComparator());
        return result;
    }

}