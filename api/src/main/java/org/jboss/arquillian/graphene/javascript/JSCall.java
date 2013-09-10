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

import java.util.Arrays;

/**
 * Represents a call of the method on JavaScript interface.
 *
 * @author Lukas Fryc
 */
public class JSCall {

    private JSMethod method;
    private Object[] arguments;

    public JSCall(JSMethod method, Object[] arguments) {
        this.method = method;
        this.arguments = arguments;
    }

    /**
     * Returns a target JavaScript interface method
     */
    public JSMethod getMethod() {
        return method;
    }

    /**
     * Returns a target JavaSript interface
     */
    public JSInterface getTarget() {
        return method.getTarget();
    }

    /**
     * Returns arguments of the call
     */
    public Object[] getArguments() {
        return arguments;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "JSCall [arguments=" + Arrays.toString(arguments) + ", method=" + method + "]";
    }
}
