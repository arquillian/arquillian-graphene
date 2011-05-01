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

import java.util.Set;

/**
 * Allows to register command interceptors which wraps the invocation of commands giving Selenium commands new aspects.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public interface CommandInterceptorProxy {

    /**
     * Registers new interceptor
     * 
     * @param interceptor
     *            to register
     */
    void registerInterceptor(CommandInterceptor interceptor);

    /**
     * Unregisters the inceptor by reference
     * 
     * @param interceptor
     *            the interceptor by reference
     * @return the command interceptor which was unregistered, null if no such interceptor was registered on this proxy
     */
    CommandInterceptor unregisterInterceptor(CommandInterceptor interceptor);

    /**
     * Unregisters all of the interceptors of given type
     * 
     * @param type
     *            the type of interceptors to unregister
     * @return the references to all command interceptors which was unregistered
     */
    Set<CommandInterceptor> unregisterInterceptorType(Class<? extends CommandInterceptor> type);
}
