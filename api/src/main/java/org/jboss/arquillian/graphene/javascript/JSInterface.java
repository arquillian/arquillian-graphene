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
package org.jboss.arquillian.graphene.javascript;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.jboss.arquillian.graphene.spi.TypeResolver;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

/**
 * Represents the JavaScript interface instance
 *
 * @author Lukas Fryc
 */
public class JSInterface {

    private Class<?> jsInterface;
    private JavaScript javascriptAnnotation;
    private Dependency dependecyAnnotation;
    private ExecutionResolver resolver;

    public JSInterface(Class<?> jsInterface) {
        this.jsInterface = getImplementationOfInterface(jsInterface);
        this.javascriptAnnotation = this.jsInterface.getAnnotation(JavaScript.class);
        this.dependecyAnnotation = this.jsInterface.getAnnotation(Dependency.class);
        this.resolver = createResolver(javascriptAnnotation);
    }

    /**
     * Returns the class which defines this instance of JavaScript interface
     */
    public Class<?> getInterface() {
        return jsInterface;
    }

    /**
     * <p>
     * Returns the name of defined JavaScript interface as specified by {@link JavaScript} annotation.
     * </p>
     *
     * <p>
     * If no name is specified, the simple name of an implementation type is used instead.
     * </p>
     */
    public String getName() {
        if ("".equals(javascriptAnnotation.value())) {
            return getInterface().getSimpleName();
        }
        return javascriptAnnotation.value();
    }

    /**
     * Returns all resources which are dependencies of this interface as specified by {@link Dependency#sources()}.
     */
    @SuppressWarnings("unchecked")
    public Collection<String> getSourceDependencies() {
        if (dependecyAnnotation == null) {
            return (Collection<String>) Collections.EMPTY_LIST;
        }
        return Arrays.asList(dependecyAnnotation.sources());
    }

    /**
     * Returns all JavaScript interfaces which are dependencies of this interface as specified by {@link Dependency#interfaces()}.
     */
    @SuppressWarnings("unchecked")
    public Collection<JSInterface> getJSInterfaceDependencies() {
        if (dependecyAnnotation == null) {
            return (Collection<JSInterface>) Collections.EMPTY_LIST;
        }
        return Collections2.transform(Arrays.asList(dependecyAnnotation.interfaces()), new Function<Class<?>, JSInterface>() {
            @Override
            public JSInterface apply(Class<?> input) {
                return new JSInterface(input);
            }
        });
    }

    /**
     * Retrieves JavaScript interface method with given name and arguments.
     */
    public JSMethod getJSMethod(String methodName, Object... arguments) {
        Class<?>[] types = new Class[arguments.length];
        for (int i = 0; i < arguments.length; i++) {
            types[i] = arguments.getClass();
        }
        try {
            Method method = jsInterface.getMethod(methodName, types);
            return new JSMethod(this, method);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Returns true if this interface is {@link InstallableJavaScript}.
     */
    public boolean isInstallable() {
        return InstallableJavaScript.class.isAssignableFrom(jsInterface);
    }

    /**
     * Returns {@link ExecutionResolver} for this interface as specified by {@link JavaScript#executionResolver()}.
     */
    public ExecutionResolver getResolver() {
        return resolver;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "JSTarget [jsInterface=" + jsInterface.getName() + "]";
    }

    private ExecutionResolver createResolver(JavaScript annotation) {
        try {
            return TypeResolver.instantiate(annotation.executionResolver());
        } catch (Exception e) {
            throw new IllegalStateException("resolver " + annotation.executionResolver() + " can't be instantied", e);
        }
    }

    private Class<?> getImplementationOfInterface(Class<?> jsInterfaceClass) {
        JavaScript jsInterface = jsInterfaceClass.getAnnotation(JavaScript.class);
        if ("".equals(jsInterface.implementation())) {
            // the given class is also a final implementation
            return jsInterfaceClass;
        }

        try {
            // the interface which implements given JS Interface is specified
            Class<?> implementationClazz = (Class<?>) Class.forName(jsInterface.implementation());

            return implementationClazz;

        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Cannot find class " + jsInterface.implementation()
                    + ", make sure you have graphene-impl.jar included on the classpath.", e);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
