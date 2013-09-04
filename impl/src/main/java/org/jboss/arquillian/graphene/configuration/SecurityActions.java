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
package org.jboss.arquillian.graphene.configuration;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * SecurityActions
 *
 * A set of privileged actions that are not to leak out of this package
 *
 *
 * @author <a href="mailto:kpiwko@redhat.com">Karel Piwko</a>
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

    static Map<String, Field> getAccessableFields(final Class<?> source) {
        Map<String, Field> declaredAccessableFields = AccessController.doPrivileged(new PrivilegedAction<Map<String, Field>>() {
            public Map<String, Field> run() {
                Map<String, Field> foundFields = new LinkedHashMap<String, Field>();
                for (Field field : source.getDeclaredFields()) {
                    // omit final fields
                    if (Modifier.isFinal(field.getModifiers())) {
                        continue;
                    }

                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    foundFields.put(field.getName(), field);
                }
                return foundFields;
            }
        });
        return declaredAccessableFields;
    }

    static Map<String, String> getProperties(final String prefix) {
        try {
            Map<String, String> value = AccessController.doPrivileged(new PrivilegedExceptionAction<Map<String, String>>() {
                public Map<String, String> run() {
                    Properties props = System.getProperties();
                    Map<String, String> subset = new LinkedHashMap<String, String>();
                    for (Map.Entry<Object, Object> entry : props.entrySet()) {
                        String name = entry.getKey().toString();
                        if (name.startsWith(prefix)) {
                            subset.put(name, entry.getValue().toString());
                        }
                    }
                    return subset;
                }
            });
            return value;
        }
        // Unwrap
        catch (final PrivilegedActionException pae) {
            final Throwable t = pae.getCause();
            // Rethrow
            if (t instanceof SecurityException) {
                throw (SecurityException) t;
            }
            if (t instanceof NullPointerException) {
                throw (NullPointerException) t;
            } else if (t instanceof IllegalArgumentException) {
                throw (IllegalArgumentException) t;
            } else {
                // No other checked Exception thrown by System.getProperty
                try {
                    throw (RuntimeException) t;
                }
                // Just in case we've really messed up
                catch (final ClassCastException cce) {
                    throw new RuntimeException("Obtained unchecked Exception; this code should never be reached", t);
                }
            }
        }
    }

    static String getProperty(final String key) {
        try {
            String value = AccessController.doPrivileged(new PrivilegedExceptionAction<String>() {
                public String run() {
                    return System.getProperty(key);
                }
            });
            return value;
        }
        // Unwrap
        catch (final PrivilegedActionException pae) {
            final Throwable t = pae.getCause();
            // Rethrow
            if (t instanceof SecurityException) {
                throw (SecurityException) t;
            }
            if (t instanceof NullPointerException) {
                throw (NullPointerException) t;
            } else if (t instanceof IllegalArgumentException) {
                throw (IllegalArgumentException) t;
            } else {
                // No other checked Exception thrown by System.getProperty
                try {
                    throw (RuntimeException) t;
                }
                // Just in case we've really messed up
                catch (final ClassCastException cce) {
                    throw new RuntimeException("Obtained unchecked Exception; this code should never be reached", t);
                }
            }
        }
    }

}
