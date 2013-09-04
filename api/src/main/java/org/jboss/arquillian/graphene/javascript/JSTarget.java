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

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

public class JSTarget {

    private Class<?> jsInterface;
    private JavaScript javascriptAnnotation;
    private Dependency dependecyAnnotation;
    private ExecutionResolver resolver;

    public JSTarget(Class<?> jsInterface) {
        this.jsInterface = getImplementationOfInterface(jsInterface);
        this.javascriptAnnotation = this.jsInterface.getAnnotation(JavaScript.class);
        this.dependecyAnnotation = this.jsInterface.getAnnotation(Dependency.class);
        this.resolver = createResolver(javascriptAnnotation);
    }

    public Class<?> getInterface() {
        return jsInterface;
    }

    public String getName() {
        if ("".equals(javascriptAnnotation.value())) {
            return getInterface().getSimpleName();
        }
        return javascriptAnnotation.value();
    }

    @SuppressWarnings("unchecked")
    public Collection<String> getSourceDependencies() {
        if (dependecyAnnotation == null) {
            return (Collection<String>) Collections.EMPTY_LIST;
        }
        return Arrays.asList(dependecyAnnotation.sources());
    }

    @SuppressWarnings("unchecked")
    public Collection<JSTarget> getJSInterfaceDependencies() {
        if (dependecyAnnotation == null) {
            return (Collection<JSTarget>) Collections.EMPTY_LIST;
        }
        return Collections2.transform(Arrays.asList(dependecyAnnotation.interfaces()), new Function<Class<?>, JSTarget>() {
            @Override
            public JSTarget apply(Class<?> input) {
                return new JSTarget(input);
            }
        });
    }

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

    public boolean isInstallable() {
        return InstallableJavaScript.class.isAssignableFrom(jsInterface);
    }

    public ExecutionResolver getResolver() {
        return resolver;
    }

    private ExecutionResolver createResolver(JavaScript annotation) {
        try {
            if (annotation.methodResolver().equals(JavaScript.DefaultExecutionResolver.class)) {
                return (ExecutionResolver) Class.forName(JavaScript.DefaultExecutionResolver.IMPLEMENTATION).newInstance();
            }
            return (ExecutionResolver) annotation.methodResolver().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("resolver " + annotation.methodResolver() + " can't be instantied", e);
        }
    }

    @Override
    public String toString() {
        return "JSTarget [jsInterface=" + jsInterface.getName() + "]";
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
                    + ", make sure you have arquillian-graphene-impl.jar included on the classpath.", e);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
