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
package org.jboss.test.selenium.interception;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.thoughtworks.selenium.CommandProcessor;

/**
 * <p>
 * This context holds the iterator over collection of interceptors, which will be triggered before the command will be
 * passed to commandProcessor to execute.
 * </p>
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class CommandContext {
    private String command;

    private String[] args;

    private CommandProcessor commandProcessor;
    private Iterator<CommandInterceptor> interceptors;
    private String result;
    private int invocations = 0;

    /**
     * Creates new context of command, using command name, its args, command processor used to execute command and
     * command interceptors, which will enfold the execution of command.
     * 
     * @param command
     *            the name of the command to trigger
     * @param args
     *            arguments of this command
     * @param commandProcessor
     *            the command processor used to execute command
     * @param interceptors
     *            enfolds the execution of command
     */
    CommandContext(String command, String[] args, CommandProcessor commandProcessor,
        Collection<CommandInterceptor> interceptors) {
        this.command = command;
        this.args = args;
        this.commandProcessor = commandProcessor;
        this.interceptors = interceptors.iterator();
    }

    /**
     * <p>
     * For each remaining interceptor in list call it's intercept method.
     * </p>
     * 
     * <p>
     * Watch if the following interceptor call's in it's {@link CommandInterceptor#intercept(CommandContext)} method
     * body method {@link CommandContext#doCommand()} at least once. If not, this interceptor will raise
     * {@link CommandInterceptionException}.
     * </p>
     * 
     * @return the return value of executing the command on given commandProcessor
     * @throws CommandInterceptionException
     *             if the subsequent interceptor doesn't call {@link CommandContext#doCommand()} in it's
     *             {@link CommandInterceptor#intercept(CommandContext)} method body.
     */
    public String doCommand() throws CommandInterceptionException {
        invocations += 1;
        final int currentInvocations = invocations;
        if (interceptors.hasNext()) {
            CommandInterceptor interceptor = interceptors.next();
            interceptor.intercept(this);
            if (currentInvocations == invocations) {
                throw new CommandInterceptionException();
            }
            return result;
        } else {
            result = commandProcessor.doCommand(command, args);
            return result;
        }
    }

    /**
     * Returns the command.
     * 
     * @return the command
     */
    public String getCommand() {
        return command;
    }

    /**
     * Returns the arguments of the command.
     * 
     * @return the arguments of the command
     */
    public String[] getArguments() {
        return args.clone();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("commandName", command).append("args", args).toString();
    }
}
