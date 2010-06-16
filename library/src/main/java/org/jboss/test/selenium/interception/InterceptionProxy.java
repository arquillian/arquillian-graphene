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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.thoughtworks.selenium.CommandProcessor;

/**
 * <p>
 * The proxy for command processor which instead of direct executing of given command triggers logic of all associated
 * interceptors.
 * </p>
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public final class InterceptionProxy implements java.lang.reflect.InvocationHandler {

    CommandProcessor commandProcessor;

    /**
     * The map of associated interceptors with keys of it's class
     */
    private Map<Class<? extends CommandInterceptor>, CommandInterceptor> interceptors =
        new LinkedHashMap<Class<? extends CommandInterceptor>, CommandInterceptor>();

    /**
     * Constructs new proxy with associated command processor
     * 
     * @param commandProcessor
     *            to associate with this proxy
     */
    public InterceptionProxy(CommandProcessor commandProcessor) {
        this.commandProcessor = commandProcessor;
    }

    /**
     * Returns the command processor proxied with functionality of all associated interceptors.
     * 
     * @return the command processor proxied with functionality of all associated interceptors
     */
    public CommandProcessor getCommandProcessorProxy() {
        return (CommandProcessor) Proxy.newProxyInstance(commandProcessor.getClass().getClassLoader(), commandProcessor
            .getClass().getInterfaces(), this);
    }

    /**
     * <p>
     * Proxies all the request on associated command processor.
     * </p>
     * 
     * <p>
     * In case of {@link CommandProcessor#doCommand(String, String[])} method, it also executes all associated
     * interceptors before performing the actual doCommand method.
     * </p>
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result;
        try {
            if ("doCommand".equals(method.getName())) {
                String commandName = (String) args[0];
                String[] arguments = (String[]) args[1];
                CommandContext context =
                    new CommandContext(commandName, arguments, commandProcessor, interceptors.values());
                try {
                    result = context.doCommand();
                } catch (CommandInterceptionException e) {
                    throw new IllegalStateException("There was at least one interceptor which didn't call doCommand");
                } catch (Exception e) {
                    throw new InvocationTargetException(e);
                }
            } else {
                result = method.invoke(commandProcessor, args);
            }
        } catch (InvocationTargetException e) {
            throw e.getCause();
        } catch (Exception e) {
            throw new RuntimeException("unexpected invocation exception: " + e.getMessage());
        }
        return result;
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
    public InterceptionProxy immutableCopy() {
        InterceptionProxy copy = new InterceptionProxy(commandProcessor);
        copy.interceptors.putAll(this.interceptors);
        return copy;
    }
}
