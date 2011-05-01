/**
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
 * Context of the Selenium command including the name of command, its arguments and provides method for command
 * invocation
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public interface CommandContext {
    /**
     * Method for invoking the command
     * 
     * @return the result of command invocation
     * 
     * @throws CommandInterceptorException
     *             when one of the interceptors in chain didn't call this method
     */
    Object invoke() throws CommandInterceptorException;

    /**
     * Returns name of command
     * 
     * @return name of command
     */
    String getCommand();

    /**
     * Returns list of command arguments
     * 
     * @return list of command arguments
     */
    String[] getArguments();
}
