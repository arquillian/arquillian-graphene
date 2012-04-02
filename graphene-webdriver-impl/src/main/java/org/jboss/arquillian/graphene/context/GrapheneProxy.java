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

import org.mockito.internal.creation.jmock.ClassImposterizer;

/**
 * GrapheneProxy provides methods for wrapping the target of invocation in the proxy.
 *
 * @author Lukas Fryc
 */
final class GrapheneProxy {

    /**
     * <p>
     * Wraps the given target instance in the proxy.
     * </p>
     *
     * <p>
     * The list of interfaces which should be implemented by the proxy is automatically computer from provided instance.
     * </p>
     *
     *
     * @param target the target instance to be wrapped
     * @return the proxy wrapping the target
     */
    @SuppressWarnings("unchecked")
    static <T> T getProxyForTarget(T target) {
        GrapheneProxyHandler handler = GrapheneProxyHandler.forTarget(target);
        return (T) createProxy(handler, target.getClass());
    }

    /**
     * <p>
     * Wraps the given target instance in the proxy.
     * </p>
     *
     * <p>
     * The list of interfaces which should be implemented by the proxy needs to be provided.
     * </p>
     *
     *
     * @param target the target instance to be wrapped
     * @param interfaces the list of interfaces which should be implemented by created proxy
     * @return the proxy wrapping the target
     */
    @SuppressWarnings("unchecked")
    static <T> T getProxyForTargetWithInterfaces(T target, Class<?>... interfaces) {
        GrapheneProxyHandler handler = GrapheneProxyHandler.forTarget(target);
        return (T) createProxy(handler, null, interfaces);
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
    static <T> T getProxyForFutureTarget(FutureTarget futureTarget, Class<?> implementationClass,
            Class<?>... additionalInterfaces) {
        GrapheneProxyHandler handler = GrapheneProxyHandler.forFuture(futureTarget);
        return (T) createProxy(handler, implementationClass, additionalInterfaces);
    }

    /**
     * <p>
     * Uses given proxy factory to create new proxy for given implementation class or interfaces with the given method handler.
     * </p>
     *
     * <p>
     * The returned proxy implements {@link GrapheneProxyInstance} by default.
     *
     * @param factory the {@link ProxyFactory} which will be used to create proxy
     * @param interceptor the {@link MethodHandler} for handling invocation
     * @param implementationClass the class which will be used as superclass or null if the created proxy should not have
     *        superclass
     * @param additionalInterfaces additional interfaces which should a created proxy implement
     * @return the proxy for given implementation class or interfaces with the given method handler.
     */
    @SuppressWarnings("unchecked")
    static <T> T createProxy(GrapheneProxyHandler interceptor, Class<?> implementationClass, Class<?>... additionalInterfaces) {

        Class<?>[] ancillaryTypes = concat(additionalInterfaces, GrapheneProxyInstance.class);

        if (implementationClass != null) {
            return (T) ClassImposterizer.INSTANCE.imposterise(interceptor, implementationClass, ancillaryTypes);
        } else {
            return (T) Proxy.newProxyInstance(GrapheneProxy.class.getClassLoader(), ancillaryTypes, interceptor);
        }
    }

    private static Class<?>[] concat(Class<?>[] interfaces, Class<?> clazz) {
        int length = interfaces.length;
        Class<?>[] out = new Class<?>[length + 1];
        System.arraycopy(interfaces, 0, out, 0, length);
        out[length] = clazz;
        return out;
    }

    /**
     * Interface for computation of future target of invocation by proxy.
     */
    interface FutureTarget {
        Object getTarget();
    }
}
