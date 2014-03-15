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
package org.jboss.arquillian.graphene.location;

import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * SecurityActions
 *
 * A set of privileged actions that are not to leak out of this package
 *
 *
 * @author <a href="mailto:andrew.rubinger@jboss.org">ALR</a>
 *
 * @version $Revision: $
 */
final class SecurityActions {

    // -------------------------------------------------------------------------------||
    // Constructor
    // ------------------------------------------------------------------||
    // -------------------------------------------------------------------------------||

    /**
     * No instantiation
     */
    private SecurityActions() {
        throw new UnsupportedOperationException("No instantiation");
    }

    // -------------------------------------------------------------------------------||
    // Utility Methods
    // --------------------------------------------------------------||
    // -------------------------------------------------------------------------------||

    /**
     * Obtains the Thread Context ClassLoader
     */
    static ClassLoader getThreadContextClassLoader() {
        return AccessController.doPrivileged(GetTcclAction.INSTANCE);
    }

    /**
     * Create a new instance.
     *
     * @param className Full classname of class to create
     * @param argumentTypes The constructor argument types
     * @param arguments The constructor arguments
     * @return a new instance or null if it is unable to load
     * @throws IllegalArgumentException when {@code className} is null
     */
    static Class<?> newInstance(final String className) {
        if (className == null) {
            throw new IllegalArgumentException("Unable to instantiate class with null class name string.");
        }
        try {
            ClassLoader classLoader = getThreadContextClassLoader();
            return classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            return null;
        }

    }

    // -------------------------------------------------------------------------------||
    // Inner Classes
    // ----------------------------------------------------------------||
    // -------------------------------------------------------------------------------||

    /**
     * Single instance to get the TCCL
     */
    private enum GetTcclAction implements PrivilegedAction<ClassLoader> {
        INSTANCE;

        @Override
        public ClassLoader run() {
            return Thread.currentThread().getContextClassLoader();
        }

    }

}
