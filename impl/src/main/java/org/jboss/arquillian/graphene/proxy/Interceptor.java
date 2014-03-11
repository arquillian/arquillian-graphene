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

/**
 * <p>
 * Interface for implementers of interception.
 * </p>
 *
 * <p>
 * Each implementor must satisfy, that in the {@link #intercept(InvocationContext)} method body will be called at least
 * once method from current context {@link InvocationContext#invoke()}.
 * </p>
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public interface Interceptor {

    /**
     * When processing custom logic must satisfy, that at least once will be called method
     * {@link InvocationContext#invoke()}.
     *
     * @param context the current invocation context
     * @return the result of invocation
     * @throws Throwable
     */
    Object intercept(InvocationContext context) throws Throwable;

    int getPrecedence();

}
