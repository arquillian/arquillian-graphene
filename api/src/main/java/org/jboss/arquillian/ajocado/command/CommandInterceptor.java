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
package org.jboss.arquillian.ajocado.command;

/**
 * <p>
 * Interface for implementers of command interception.
 * </p>
 * 
 * <p>
 * Each implementor must satisfy, that in the {@link #intercept(CommandContext)} method body will be called at least
 * once method from current context {@link CommandContext#invoke()}. This method also returns the return value of
 * executing given command on associated commandProcessor.
 * </p>
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public interface CommandInterceptor {

    /**
     * When processing custom logic must satisfy, that at least once will be called method
     * {@link CommandContext#invoke()}. It's entry point for passing the logic to next interceptor until the associated
     * interceptors aren't all triggered, then commandProcess will execute given command and it's return value will
     * bubble back to each interceptor as return value of {@link CommandContext#invoke()} method.
     * 
     * @param ctx
     *            the current command context
     * @throws CommandInterceptorException
     *             if the subsequent interceptor doesn't call {@link CommandContext#invoke()} in it's
     *             {@link CommandInterceptor#intercept(CommandContext)} method body.
     */
    void intercept(CommandContext ctx) throws CommandInterceptorException;
}
