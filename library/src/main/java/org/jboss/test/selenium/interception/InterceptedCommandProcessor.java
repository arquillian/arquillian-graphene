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

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.jboss.test.selenium.framework.internal.ProxyCommandProcessor;

import com.thoughtworks.selenium.CommandProcessor;

/**
 * <p>
 * The command procesor which instead of direct executing of given command triggers logic of all associated
 * interceptors.
 * </p>
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class InterceptedCommandProcessor extends ProxyCommandProcessor {

    /**
     * The map of associated interceptors with keys of it's class
     */
    private Map<Class<? extends CommandInterceptor>, CommandInterceptor> interceptors =
        new LinkedHashMap<Class<? extends CommandInterceptor>, CommandInterceptor>();

    /**
     * Constructs new InterCeptedCommandProcessor with associated commandProcessor as executor for the commands.
     * 
     * @param commandProcessor
     */
    public InterceptedCommandProcessor(CommandProcessor commandProcessor) {
        super(commandProcessor);
    }

    /**
     * <p>
     * Overrides the doCommand of common {@link CommandProcessor}, which is end point for execution of command in known
     * implementations such as {@link com.thoughtworks.selenium.HttpCommandProcessor}.
     * </p>
     * 
     * <p>
     * Instead of direct execution of command all associated interceptors will be triggered around the call giving it
     * possibility to catch the exceptions, process the command name and it's result.
     * </p>
     */
    @Override
    public String doCommand(String command, String[] args) {
        CommandContext context = new CommandContext(command, args, commandProcessor, interceptors.values());
        try {
            return context.doCommand();
        } catch (CommandInterceptionException e) {
            throw new IllegalStateException("There was at least one interceptor which didn't call doCommand");
        }
    }

    /**
     * Registers the interceptor, only one interceptor can be registered for given class of interceptor.
     * 
     * @param interceptor
     *            the interceptor implementation
     */
    public void registerInterceptor(CommandInterceptor interceptor) {
        interceptors.put(interceptor.getClass(), interceptor);
    }

    /**
     * Removes and returns the interceptor instance, or null, if such instance isn't registered.
     * 
     * @param interceptor
     *            the instance of interceptor to remove
     * @return removed interceptor or null, if such interceptor ins't registered
     */
    public CommandInterceptor unregisterInterceptor(CommandInterceptor interceptor) {
        Class<? extends CommandInterceptor> typeToRemove = null;
        for (Entry<Class<? extends CommandInterceptor>, CommandInterceptor> entry : interceptors.entrySet()) {
            if (entry.getValue().equals(interceptor)) {
                typeToRemove = entry.getKey();
                break;
            }
        }
        return interceptors.remove(typeToRemove);
    }

    /**
     * Removes and returns all associated interceptors which of given type.
     * 
     * @param type
     *            of interceptors which we want to unregister from this command processor
     * @return the removed interceptors
     */
    public Set<CommandInterceptor> unregisterInterceptorType(Class<? extends CommandInterceptor> type) {
        Set<Class<? extends CommandInterceptor>> typesToRemove =
            new LinkedHashSet<Class<? extends CommandInterceptor>>();
        for (Class<? extends CommandInterceptor> entryType : interceptors.keySet()) {
            if (entryType.isInstance(type)) {
                typesToRemove.add(entryType);
            }
        }
        Set<CommandInterceptor> removedInterceptors = new LinkedHashSet<CommandInterceptor>();
        for (Class<? extends CommandInterceptor> typeToRemove : typesToRemove) {
            removedInterceptors.add(interceptors.remove(typeToRemove));
        }
        return removedInterceptors;
    }

    /**
     * Creates immutable copy of this command processor with all interceptors registered.
     * 
     * @return the immutable copy of this command processor
     */
    public InterceptedCommandProcessor immutableCopy() {
        InterceptedCommandProcessor copy = new InterceptedCommandProcessor(this.commandProcessor);
        copy.interceptors.putAll(this.interceptors);
        return copy;
    }
}
