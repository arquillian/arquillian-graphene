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

/**
 * Represents a method on JavaScript interface
 *
 * @author Lukas Fryc
 */
public class JSMethod {

    private JSInterface target;
    private Method method;
    private String name;

    public JSMethod(JSInterface target, Method method) {
        this.target = target;
        this.method = method;
        this.name = resolveName(method);

    }

    /**
     * Returns a target JavaSript interface
     */
    public JSInterface getTarget() {
        return target;
    }

    /**
     * Returns a target {@link Method} of given JavaScript interface
     */
    public Method getMethod() {
        return method;
    }

    /**
     * Returns name of the method as diverged from name of the Java method or re-defined using {@link MethodName} annotation
     */
    public String getName() {
        return name;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "JSMethod [method=" + method.getName() + ", name=" + name + ", target=" + target + "]";
    }

    private String resolveName(Method method) {
        MethodName annotation = method.getAnnotation(MethodName.class);

        if (annotation != null && !"".equals(annotation.value())) {
            return annotation.value();
        }

        return method.getName();
    }
}
