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
package org.jboss.test.selenium.utils;

/**
 * The static methods desired to simplify conversion of primitive types.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public final class PrimitiveUtils {

    private PrimitiveUtils() {
    }

    /**
     * Converts string to boolean
     * 
     * @param string
     *            to convert
     * @return the boolean value of string
     */
    public static boolean asBoolean(String string) {
        return Boolean.valueOf(string);
    }

    /**
     * Converts string to long
     * 
     * @param string
     *            to convert
     * @return the long value of string
     */
    public static long asLong(String string) {
        return Long.valueOf(string);
    }

    /**
     * Converts string to integer
     * 
     * @param string
     *            to convert
     * @return the integer value of string
     */
    public static int asInt(String string) {
        return Integer.valueOf(string);
    }

    /**
     * Converts string to double
     * 
     * @param string
     *            to convert
     * @return the double value of string
     */
    public static double asDouble(String string) {
        return Double.valueOf(string);
    }

    /**
     * Converts string to float
     * 
     * @param string
     *            to convert
     * @return the float value of string
     */
    public static float asFloat(String string) {
        return Float.valueOf(string);
    }
}
